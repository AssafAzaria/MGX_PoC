/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.servicesProvider;

import com.mgx.shared.sequences.SequenceInfo;

/**
 *
 * @author Asaf
 */
public interface DataProvider {
    
    
    /**
     * Init the service
     * @throws com.mgx.servicesProvider.DataRepositoryErrorException on initialization error
     */
    public void init() throws DataRepositoryErrorException;
    /**
     * Store a sequence data structure
     * If sequence already exists (Existing sequence means a sequence  in storage with same 
     * SequenceInfo.sequenceId and SequenceInfo.sequenceName) it will get updated
     * @param sequence the sequence to store
     * @return the UID of the sequence for future reference
     * @throws com.mgx.servicesProvider.DataRepositoryErrorException on error
     */
    public int storeSequence(SequenceInfo sequence) throws DataRepositoryErrorException;

    /**
     * Get sequence by its name
     * @param sequenceName the name of the sequence to load
     * @return  the sequence info data structure, null if not exist
     */
    public SequenceInfo getSequenceByName(String sequenceName);
    /**
     * Get a stored sequence by its UID (fastest loading)
     * @param sequenceUID the UID of the desired sequence to load
     * @return the sequence info data structure, null if not exist
     */
    public SequenceInfo getSequenceByUID(int sequenceUID);

    /**
     * Delete a stored sequence
     * @param sequenceUID the UID of the sequence to delete
     * @throws DataRepositoryErrorException if sequence not exists
     */
    public void deleteSequence(int sequenceUID) throws DataRepositoryErrorException;
}
