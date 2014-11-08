package storageapi;

import datamodel.Identifier;
import datamodel.DataTypeValue;

import java.util.Iterator;
import java.util.List;

/**
 * Created on 2014-10-23.
 */
public interface Storage {

    void createTable(Identifier tableName) throws DataStoreException;

    void dropTable(Identifier tableName) throws DataStoreException;

    void insertRecord(Identifier tableName, List<DataTypeValue> recordValues) throws DataStoreException;

    List<List<DataTypeValue>> getAllRecords(Identifier tableName) throws DataStoreException;

    Iterator<List<DataTypeValue>> tableIterator(Identifier tableName) throws DataStoreException;

    void deleteAll(Identifier tableName);
}
