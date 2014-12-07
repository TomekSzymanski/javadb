package storageapi;

import datamodel.Identifier;
import systemdictionary.SystemDictionary;

import java.util.Iterator;

/**
 * Created on 2014-10-23.
 */
public interface Storage {

    void createTable(Identifier tableName);

    void dropTable(Identifier tableName);

    void insertRecord(Identifier tableName, Record record) throws DataStoreException;

    Iterator<Record> tableIterator(Identifier tableName) throws DataStoreException;

    void deleteAll(Identifier tableName);




    /**
     * Returns system dictionary instance that is collaborating with this storgae instance.
     * If storage is persistent then it is storage resposibility to load dictionary from persistence.
     * JavaDB assumes the dictionary instance will be one and the same instance.
     * @return // TODO maybe InputStream getSystemDictionary() and writeSystemDictionary(OutputStream) would also do?
     */
    SystemDictionary getSystemDictionary();

    /**
     * If storage is not persistent than this one does not need to be implemented. If Storage is persistent then it must be implemented
     * @param dictionary
     * @throws DataStoreException
     */
    default void writeSystemDictionary(SystemDictionary dictionary) throws DataStoreException {
        // intentionally blank.
    }

    void close();
}
