/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.servicesProvider;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.sequences.SequenceInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author Asaf
 */
public class FileSystemDataProvider implements DataProvider {

    ActivityLogger l = new ActivityLogger(FileSystemDataProvider.class.getName());
    private HashMap<String, SequenceInfo> dataByName = new HashMap();
    private HashMap<Integer, SequenceInfo> dataByUID = new HashMap();
    private int nextSequenceID = -1;
    private static final String SEQUENCE_PREFIX = "sequnce";
    private static final String NEXT_ID_STORAGE_FILENAME = "nextSequenceID.txt";
    private static final String NEXT_ID_KEY = "NextSequenceID";
    private Path NextIdStoragePath;

    private static final String SETTINGS_FILE_PREFIX = "settings";
    private static final String CLIENT_DATA_PREFIX = "client";

    private void createNewStorage(Path path) throws IOException {
        path = Files.createFile(path);
        //add entries to storage
        StringBuilder structure = new StringBuilder();
        structure.append(NEXT_ID_KEY + "=1");
        structure.append(System.lineSeparator());

        //save initial data
        Files.write(path, structure.toString().getBytes());

    }

    private Object loadObject(String filename) throws DataRepositoryErrorException {
        FileInputStream in = null;
        ObjectInputStream stream = null;
        Object objectFromFile = null;
        try {

            in = new FileInputStream(filename);
            stream = new ObjectInputStream(in);
            objectFromFile = stream.readObject();

        } catch (FileNotFoundException ex) {
            l.logE("Couldn't open a file..");
            ex.printStackTrace();
            throw new DataRepositoryErrorException("Couldn't open a file " + filename, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new DataRepositoryErrorException("Couldn't read file", ex);
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

    private SequenceInfo loadSequence(Path filePath) throws DataRepositoryErrorException {
        return (SequenceInfo) loadObject(filePath.toString());
    }

    private boolean storeObject(String fileName, Object data) throws DataRepositoryErrorException {
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
            throw new DataRepositoryErrorException("Couldn't create a file", ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new DataRepositoryErrorException("Couldn't write to file", ex);
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
                throw new DataRepositoryErrorException("Failed to close streams", ex);
            }
        }

        return result;
    }

    @Override
    public void init() throws DataRepositoryErrorException {
        NextIdStoragePath = Paths.get(NEXT_ID_STORAGE_FILENAME);

        //create if not exsists the file that store the next ID param
        if (!Files.exists(NextIdStoragePath)) {
            try {
                createNewStorage(NextIdStoragePath);
            } catch (IOException ex) {
                throw new DataRepositoryErrorException("Failed to create new storage", ex);
            }
        }

        //load and set the next ID
        try {
            Files.lines(NextIdStoragePath).forEach((line) -> {
                String set[] = line.split("=");
                if (NEXT_ID_KEY.equals(set[0])) {
                    nextSequenceID = Integer.parseInt(set[1]);
                } else {
                    throw new RuntimeException("Data is broken - Couldn't find " + NEXT_ID_KEY);
                }

            });
        } catch (IOException ex) {
            throw new DataRepositoryErrorException("Failed to read lines from file", ex);
        } catch (RuntimeException ex) {
            throw new DataRepositoryErrorException("Failed to load key", ex);
        }

        if (nextSequenceID == -1) {
            throw new DataRepositoryErrorException("Failed to read SequanceIDSeed");
        }

        //load all stored sequences
        Path cwd = new File(".").toPath();
        try {
            Files.list(cwd)
                    .filter(p -> p.getFileName().toString().startsWith(getSequenceFilenamePrefix()))
                    .forEach(file -> {
                        try {
                            SequenceInfo seq = loadSequence(file);
                            if ((seq.sequenceName != null && dataByName.containsKey(seq.sequenceName)) || dataByUID.containsKey(seq.sequenceId)) {
                                throw new RuntimeException("data storage is not consistent (same key exsist more then once");
                            }
                            dataByName.put(seq.sequenceName, seq);
                            dataByUID.put(seq.sequenceId, seq);
                        } catch (DataRepositoryErrorException ex) {
                            l.logE("failed to load sequence");
                            ex.printStackTrace();
                            throw new RuntimeException("Failed to load sequence", ex);
                        }

                    });
        } catch (IOException | NullPointerException ex) {
            throw new DataRepositoryErrorException("Failed to read storage", ex);
        }

    }

    private int getNextSequenceID() throws IOException {
        int uid = this.nextSequenceID++;
        String set = NEXT_ID_KEY + "=" + nextSequenceID;
        Files.write(NextIdStoragePath, set.getBytes());
        return uid;
    }

    private String getSequenceFilenamePrefix() {
        return CLIENT_DATA_PREFIX + "_" + SEQUENCE_PREFIX;
    }

    private String getSequenceFilename(SequenceInfo seq) {
        return getSequenceFilenamePrefix() + "_" + seq.sequenceId;
    }

    @Override
    public int storeSequence(SequenceInfo sequence) throws DataRepositoryErrorException {

        try {

            if (dataByUID.containsKey(sequence.sequenceId) && dataByName.containsKey(sequence.sequenceName)) {
                //update
                String sequenceStorageName = getSequenceFilename(sequence);
                storeObject(sequenceStorageName, sequence);
                dataByUID.replace(sequence.sequenceId, sequence);
                dataByName.replace(sequence.sequenceName, sequence);
            } else {
                //store new
                sequence.sequenceId = getNextSequenceID();
                String sequenceStorageName = getSequenceFilename(sequence);
                storeObject(sequenceStorageName, sequence);
                dataByUID.put(sequence.sequenceId, sequence);
                dataByName.put(sequence.sequenceName, sequence);
            }
        } catch (IOException ex) {
            throw new DataRepositoryErrorException("Failed to get next sequence ID", ex);
        }

        return sequence.sequenceId;
    }

    @Override
    public SequenceInfo getSequenceByName(String sequenceName) {
        return dataByName.get(sequenceName);
    }

    @Override
    public SequenceInfo getSequenceByUID(int sequenceUID) {
        return dataByUID.get(sequenceUID);
    }

    @Override
    public void deleteSequence(int sequenceUID) throws DataRepositoryErrorException {
        SequenceInfo seq = dataByUID.get(sequenceUID);
        if (seq == null) {
            throw new DataRepositoryErrorException("Can't delete sequence  " + sequenceUID + " - sequence doesn't exist");
        }

        String filename = getSequenceFilename(seq);
        try {
            Files.delete(Paths.get(filename));
            dataByName.remove(seq.sequenceName);
            dataByUID.remove(seq.sequenceId);
        } catch (IOException ex) {
            throw new DataRepositoryErrorException("Failed to delete sequence entry", ex);
        }
    }

    @Override
    public HashMap<Integer, String> getStoredSequences() throws DataRepositoryErrorException {
        HashMap<Integer, String> sequences = new HashMap(this.dataByUID.size());
        dataByUID.values().stream().forEach((seq) -> {
            sequences.put(seq.sequenceId, seq.sequenceName);
        });
        return sequences;
    }

    private String getSettingsFilename(int UID) {
        return SETTINGS_FILE_PREFIX + "_" + UID;
    }

    @Override
    public void storeCFPGASettings(CFPGADescriptor cFPGA) throws DataRepositoryErrorException {
        storeObject(getSettingsFilename(cFPGA.getUID()), cFPGA);
    }

    @Override
    public CFPGADescriptor loadCFPGASettings(int cFPGAUID) throws DataRepositoryErrorException {
        return (CFPGADescriptor) loadObject(getSettingsFilename(cFPGAUID));
    }

    private String getClientSettingsFilename(String settingsName) {
        return CLIENT_DATA_PREFIX + "_" + SETTINGS_FILE_PREFIX + "_" + settingsName;
    }

    @Override
    public void storeClientSettings(String settingName, CFPGADescriptor cFPGA) throws DataRepositoryErrorException {
        storeObject(getClientSettingsFilename(settingName), cFPGA);
    }

    @Override
    public CFPGADescriptor loadClientSettings(String settingsName) throws DataRepositoryErrorException {
        return (CFPGADescriptor) loadObject(getClientSettingsFilename(settingsName));
    }

}
