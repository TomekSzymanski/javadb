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

    SystemDictionary getSystemDictionary();
}
