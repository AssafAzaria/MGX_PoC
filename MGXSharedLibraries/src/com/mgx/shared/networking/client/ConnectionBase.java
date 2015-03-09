/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking.client;

import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.events.Event;
import com.mgx.shared.networking.Transmitable;
import com.mgx.shared.networking.InboundParcelHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Asaf
 * @param <Inbound>
 * @param <Outbound>
 */
public abstract class ConnectionBase<
        Inbound extends Transmitable, Outbound extends Transmitable, Handler extends InboundParcelHandler> {

    private ActivityLogger l = new ActivityLogger(getClientName());
    private Socket socket;
    private ObjectInputStream reciever = null;
    private ObjectOutputStream transmiter = null;
    private ArrayBlockingQueue<Inbound> dispatchingQueue = new ArrayBlockingQueue<>(Configuration.QueueSize);
    private ArrayBlockingQueue<Outbound> transmitingQueue = new ArrayBlockingQueue<>(Configuration.QueueSize);
    private ConcurrentHashMap<Integer, Handler> handlers = new ConcurrentHashMap<>(Configuration.QueueSize);
    private Thread transmiterThread;
    private Thread dispatcherThread;

    private volatile boolean continueProcessing = true;

    private Lock lock = new ReentrantLock();
    private Condition noHandlers = lock.newCondition();
    private CountDownLatch waitThreads = new CountDownLatch(3);

    private Lock connectionState = new ReentrantLock();
    private Condition connectionReady = connectionState.newCondition();

    protected volatile boolean isReady = false;
    private Thread recieverThread;

    private String ownerName = "";

    private boolean isShuttingDown = false;

    public ConnectionBase(Socket socket) throws IOException {
        this.socket = socket;
        initChanels();
    }

    public ConnectionBase(int port, InetAddress serverAddress) throws IOException {

        try {

            this.socket = new Socket(serverAddress, port);
            initChanels();

            l.logI("initialized");
        } catch (IOException ex) {
            l.logE("Client ctor couldn't initiate streems " + ex.getMessage());
            throw ex;
        }

    }

    public void setOwner(String owner) {
        this.ownerName = owner;
        l = new ActivityLogger(getClientName());
    }

    private void initChanels() throws IOException {
        transmiter = new ObjectOutputStream(socket.getOutputStream());
        reciever = new ObjectInputStream(socket.getInputStream());
    }

    public boolean isConnected() {
        if (socket != null && socket.isConnected()) {
            return true;
        }
        return false;
    }

    public synchronized void restart() throws IOException {
        String fName = header() + "restart: ";
        l.logI(fName + " trying to reconnect to: " + socket.getInetAddress() + " port:" + socket.getPort());
        socket = new Socket(socket.getInetAddress(), socket.getPort());
        initChanels();
        this.start();
        l.logI(fName + " reconnected");

    }

    public synchronized void dropConnection(Thread caller) {
        if (!isShuttingDown) {
            try {
                l.logW(header() + " dropConnection called");
                isShuttingDown = true;
                transmiter.close();
                reciever.close();
                socket.close();
                continueProcessing = false;

                if (!caller.equals(transmiterThread)) {
                    transmiterThread.interrupt();
                    transmiterThread.join();
                }

                if (!caller.equals(recieverThread)) {
                    recieverThread.interrupt();
                    dispatcherThread.join();
                }
                if (!caller.equals(dispatcherThread)) {
                    dispatcherThread.interrupt();
                    recieverThread.join();
                }

                l.logW(header() + "connection is closed");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                l.logW(header() + "dropConnection interrupted...");
                ex.printStackTrace();
            }
        }
    }

    public void start() {
        if (this.handlers.isEmpty()) {
            l.logW(this.getInstanceClassName() + " owner:" + ownerName + " no handlers registered....thread will sleep until one is registered");
        }
        startDispatcherThread();
        startTransmiterThread();
        startRecieverThread();

        try {
            l.logD("waiting for all threads to initialize");
            waitThreads.await();
            l.logD("All threads running");
            l.logD("waiting for connection");
            if (!isReady) {

                try {
                    connectionState.lock();
                    connectionReady.await();
                } finally {
                    connectionState.unlock();
                }
            }
            l.logI("!!!!!connection established");
        } catch (InterruptedException ex) {
            l.logD("start:countDownLatch interupted");
            ex.printStackTrace();
        }

    }

    protected abstract String getInstanceClassName();

    public String getOwnerName() {
        return ownerName;
    }

    private void startRecieverThread() {
        String fName = header() + "recieverThread(client) >";
        recieverThread = new Thread() {
            @Override
            public void run() {
                waitThreads.countDown();
                while (continueProcessing) {
                    try {

                        Inbound packet = (Inbound) reciever.readObject();
                        
                        l.logD(fName + "new inbound packet:" + packet.getName());
                        dispatch(packet);

                    } catch (IOException ex) {
                        if (isShuttingDown) {
                            l.logD(fName + "shutting down...");
                        } else {
                            if (ex instanceof java.net.SocketException) {
                                l.logE(fName + "Got Socket exception >" + ex.getMessage());
                                l.logE(fName + "Dropping connection");
                                dropConnection(this);
                            } else {
                                ex.printStackTrace();
                            }
                        }
                    } catch (ClassNotFoundException ex) {
                        if (isShuttingDown) {
                            l.logD(fName + "shutting down...");
                        } else {
                            ex.printStackTrace();
                        }
                    } catch (InterruptedException ex) {
                        if (isShuttingDown) {
                            l.logD(fName + "shutting down...");
                        } else {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        };
        recieverThread.setName(this.getOwnerName()+"::"+getInstanceClassName()+":recieverThread(client) ");
        recieverThread.start();
    }

    private void startDispatcherThread() {
        String fName = header() + ":dispatcherThread(client) - ";
        dispatcherThread = new Thread(this.getOwnerName()+"::"+getInstanceClassName()+":dispatcherThread(client) " ) {
            @Override
            public void run() {
                waitThreads.countDown();
                while (continueProcessing) {
                    try {

                        if (handlers.isEmpty()) {
                            try {
                                l.logD(fName + "No responseHandlers are registerd... waiting");
                                lock.lock();
                                noHandlers.await();
                                l.logD(fName + "First responseHandlers is register, we can start processing");
                            } finally {
                                lock.unlock();
                            }
                        }

                        l.logD(fName + "waiting for new  items in the queue....");
                        Transmitable inboundPacket = dispatchingQueue.take();
                        l.logD(fName + "got dispatching item: " + inboundPacket.getName());
                        if (!isReady) {
                            //if response was consumed, continue
                            if (checkIfReady(inboundPacket)) {
                                continue;
                            }
                            //TODO: server side shouldn't send events before connectin ready
                            if (!isReady) {
                            
                                continue;
                            }
                        }
                        l.logI(fName + "Got inbound packet " + inboundPacket.getName());

                        handlers.forEachValue(MAX_PRIORITY, (InboundParcelHandler responseHandler) -> {
                            responseHandler.handleInboundParcel(inboundPacket, getInstance());
                        });

                    } catch (InterruptedException ex) {
                        if (isShuttingDown) {
                            l.logD(fName + "interrupted (shutting down)");
                        } else {
                            l.logE(getInstanceClassName() + "::" + ownerName + ":dispatcher - dispatch failed (interrupted) >" + ex.getMessage());
                            ex.printStackTrace();;
                        }
                    }
                }

            }

        };
        dispatcherThread.start();
    }
    private ConnectionBase getInstance() {
        return this;
    }
    private boolean checkIfReady(Transmitable response) {
        boolean consumed = false;
        l.logD("cheking server response");
        
        if (response instanceof Event) {
            Event event = (Event) response;
            if ("ConnectionActiveResponse".equals(event.getName())) {
                l.logD("got ConnectionActiveResponse - connection is stable");
                isReady = true;
                consumed = true;

            }
        } else if (response instanceof Command) {
            isReady = true;
            consumed = false;
            l.logD("Got response (" + response.getName() + ") from server - connection is stable");
        }
        if (isReady) {

            try {
                connectionState.lock();
                connectionReady.signal();

            } finally {
                connectionState.unlock();
            }
        }
        return consumed;
    }

    private String header() {
        return ownerName+ "::" +getInstanceClassName()   + ":";
    }

    private void startTransmiterThread() {
        String funcName = header() + "TransmiterThread(client) - ";
        transmiterThread = new Thread(funcName ) {
            @Override
            public void run() {
                waitThreads.countDown();
                while (continueProcessing) {
                    try {
                        l.logD(funcName + "waiting for an outbound item...");
                        Transmitable outgoing = transmitingQueue.take();
                        l.logI(funcName + "Sending Transmitable " + outgoing.getName());

                        transmiter.writeObject(outgoing);
                        transmiter.reset();
                        l.logD(funcName + "Transmitable sent");
                    } catch (InterruptedException ex) {
                        if (isShuttingDown) {
                            l.logD(funcName + "interrupted (shutting down");
                        } else {
                            l.logE(funcName + "Failed to read  from queue ");
                            ex.printStackTrace();
                        }
                    } catch (IOException ex) {
                        l.logE(funcName + "Failed to write object to stream ");
                        ex.printStackTrace();
                    }
                }
            }
        };
        transmiterThread.start();
    }

    /**
     * Called when an incoming parcel has arrived and need to be dispatched to 
     * listeners
     * @param parcel the incoming Transimitable
     * @throws InterruptedException dispatchingQueue is interrupted 
     */
    public synchronized void dispatch(Inbound parcel) throws InterruptedException {
        l.logD("adding Transmitable " + parcel.getName() + " to the inboundQueue");
        dispatchingQueue.put(parcel);

    }

    /**
     * send parcel on this connection
     * @param parcel The Transmitable to send
     * @throws InterruptedException transmitingQueue is interrupted
     */
    public synchronized void transmit(Outbound parcel) throws InterruptedException {
        if (parcel == null) {
            l.logE("can't transmit null parcel");
            return;
        }
        l.logD("adding Transmitable " + parcel.getName() + " to the outboundQueue");
        transmitingQueue.put(parcel);
    }

    
    /**
     * Register a handler class and provide a UID for it. Each time that a
     * command is posted it should include the UID provided by this method
     *
     * @param handler the object that will handle inbound packets
     * @return UID a unique identifier for this object
     */
    public synchronized int addHandler(Handler handler) {
        int UID = handler.hashCode();
        l.logD("Adding Handler " + handler.getName() + "with UID " + UID);
        handlers.putIfAbsent(UID, handler);
        return UID;
    }
    public Handler getHandler(int handlerUID) {
        return handlers.get(handlerUID);
    }

    public String getClientName() {
        return getInstanceClassName() + " owner:" + ownerName;
    }


}
