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

    void insertRecord(Identifier tableName, List<? extends DataTypeValue> recordValues) throws DataStoreException;

    List<List<? extends DataTypeValue>> getAllRecords(Identifier tableName) throws DataStoreException;

    Iterator<List<? extends DataTypeValue>> tableIterator(Identifier tableName) throws DataStoreException;

    void deleteAll(Identifier tableName);
}
