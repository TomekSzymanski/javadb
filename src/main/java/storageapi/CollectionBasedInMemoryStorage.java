package storageapi;

import datamodel.Identifier;
import org.apache.commons.lang3.Validate;
import systemdictionary.SystemDictionary;

import java.util.*;

/**
 * Created on 2014-10-23.
 */
public class CollectionBasedInMemoryStorage implements Storage {

    private final Map<Identifier, List<Record>> tablesRecords = new HashMap<>();

    private static CollectionBasedInMemoryStorage INSTANCE;

    private CollectionBasedInMemoryStorage(){}

    public static Storage getInstance() {
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
        Validate.isTrue(tablesRecords.containsKey(tableName), "Trying to remove non existing table %s", tableName.getValue());
        tablesRecords.remove(tableName);
    }

    @Override
    public void insertRecord(Identifier tableName, Record recordValues) throws DataStoreException {
        if (!tablesRecords.containsKey(tableName)) {
            throw new DataStoreException("Table " + tableName + " does not exist");
        }
        tablesRecords.get(tableName).add(recordValues);
    }

    @Override
    public Iterator<Record> tableIterator(Identifier tableName) throws DataStoreException {
        return tablesRecords.get(tableName).iterator();
    }

    @Override
    public void deleteAll(Identifier tableName) {
        tablesRecords.get(tableName).clear();
    }

    @Override
    public SystemDictionary getSystemDictionary() {
        return null; // TODO: implement returning instance of NON persistent dictionary
    }
}
