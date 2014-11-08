package discstorage;

import datamodel.DataTypeValue;
import datamodel.Identifier;
import storageapi.DataStoreException;
import storageapi.Storage;

import java.util.*;

/**
 * Created on 2014-10-23.
 */
public class CollectionBasedInMemoryStorage implements Storage {

    private final Map<Identifier, List<List<DataTypeValue>>> tablesRecords = new HashMap<>();

    private static CollectionBasedInMemoryStorage INSTANCE;

    private CollectionBasedInMemoryStorage(){}

    public static CollectionBasedInMemoryStorage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CollectionBasedInMemoryStorage();
        }
        return INSTANCE;
    }

    @Override
    public void createTable(Identifier tableName) throws DataStoreException {
        if (tablesRecords.containsKey(tableName)) {
            throw new DataStoreException("Table " + tableName + " already exists");
        } else {
            tablesRecords.put(tableName, new ArrayList<>());
        }
    }

    @Override
    public void dropTable(Identifier tableName) throws DataStoreException {
        tablesRecords.remove(tableName);
    }

    @Override
    public void insertRecord(Identifier tableName, List<DataTypeValue> recordValues) throws DataStoreException {
        if (!tablesRecords.containsKey(tableName)) {
            throw new DataStoreException("Table " + tableName + " does not exist");
        }
        tablesRecords.get(tableName).add(recordValues);
    }

    @Override
    public List<List<DataTypeValue>> getAllRecords(Identifier tableName) throws DataStoreException {
        List<List<DataTypeValue>> records = new ArrayList<>();
        if (tablesRecords.containsKey(tableName)) {
            records = tablesRecords.get(tableName);
        }
        return records;
    }

    @Override
    public Iterator<List<DataTypeValue>> tableIterator(Identifier tableName) throws DataStoreException {
        return tablesRecords.get(tableName).iterator();
    }

    @Override
    public void deleteAll(Identifier tableName) {
        tablesRecords.get(tableName).clear();
    }
}
