package storageapi;

import systemdictionary.SystemDictionary;

/**
 * Created on 2014-11-15.
 */
public interface PersistentStorage extends Storage {
    //SystemDictionary loadSystemDictionary() throws DataStoreException;

    void writeSystemDictionary(SystemDictionary dictionary) throws DataStoreException;
}
