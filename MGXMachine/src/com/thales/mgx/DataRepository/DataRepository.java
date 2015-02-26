/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thales.mgx.DataRepository;

import com.mgx.shared.Configuration;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.sequences.SequenceInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class DataRepository {
    
    ActivityLogger l = new ActivityLogger(DataRepository.class.getName());
    private ConcurrentHashMap<String, String> data = new ConcurrentHashMap();
    Path path;
    public DataRepository() throws IOException {
        l.logD("Ctor called");
        init();
    }
    private void init() throws IOException {
        path = Paths.get("deprecated");
        if (!Files.exists(path)) {
            createNewStorage(path);
        }
        Files.lines(path).forEach((line) -> {
            String set[] = line.split("=");
            data.put(set[0], set[1]);
        });
    }
    
    
    public String fetch(String key){
        return data.get(key);
        
    }
    
    public void store(String key, String value) throws IOException{
        data.replace(key, value);
        StringBuilder updatedData = new StringBuilder();
        for (Entry<String, String> line :data.entrySet()) {
            updatedData.append(line.getKey());
            updatedData.append("=");
            updatedData.append(line.getValue());
            updatedData.append(System.lineSeparator());
        }
        Files.write(path, updatedData.toString().getBytes());
        
    }

    private void createNewStorage(Path path) throws IOException {
        path = Files.createFile(path);
        //add entries to storage
        StringBuilder structure = new StringBuilder();
        structure.append("SequanceIDSeed=1");
        structure.append(System.lineSeparator());
        
        //save initial data
        Files.write(path, structure.toString().getBytes() );
        
    }

    public Object loadObject(String fileName) {
        FileInputStream in = null;
        ObjectInputStream stream = null;
        Object objectFromFile = null;
        try {
            
            in = new FileInputStream(fileName);
            stream = new ObjectInputStream(in);
            objectFromFile = stream.readObject();
            
        } catch (FileNotFoundException ex) {
            l.logE("Couldn't create a file..");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                in.close();
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                l.logE("Failed to close streams");
                ex.printStackTrace();
            }
        }
        return objectFromFile;
    }
    public boolean storeObject(String fileName, Object data) {
        FileOutputStream out = null;
        ObjectOutputStream stream = null;
        boolean result = false;
        try {
            
            out = new FileOutputStream(fileName);
             stream = new ObjectOutputStream(out);
            stream.writeObject(data);
            result = true;
            
        } catch (FileNotFoundException ex) {
            l.logE("Couldn't create a file..");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (out != null) {
                out.close();
                }
                
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                l.logE("Failed to close streams");
                ex.printStackTrace();
                result = false;
            }
        }
        
        return result;
    }
}
