package clientapi;

import discstorage.PageBasedStorage;
import storageapi.CollectionBasedInMemoryStorage;
import storageapi.Storage;

/**
 * Created on 2014-12-06.
 */
public class DatabaseBuilder {

    private JavaDB constructedInstance;

    public DatabaseBuilder newInMemoryDatabase() {
        Storage memoryStorage = CollectionBasedInMemoryStorage.createNewStorage();
        constructedInstance = new JavaDB(memoryStorage);
        return this;
    }

    public DatabaseBuilder newDiskDatabase(String databaseDirectoryPath) {
        Storage diskStorage = PageBasedStorage.createDatabaseOnDisk(databaseDirectoryPath);
        constructedInstance = new JavaDB(diskStorage);
        return this;
    }

    public DatabaseBuilder openDatabaseFromDisk(String databaseDirectoryPath) {
        Storage diskStorage = PageBasedStorage.loadDatabaseFromDisk(databaseDirectoryPath);
        constructedInstance = new JavaDB(diskStorage);
        return this;
    }

    public DatabaseBuilder createOrOpenDatabase(Storage thirdPartyStorage) {
        constructedInstance = new JavaDB(thirdPartyStorage);
        return this;
    }

    public JavaDB build() {
        if (constructedInstance==null) {
            throw new IllegalStateException("Trying to use JavaDB without storage configured. Use newDiskDatabase() or openDatabaseFromDisk() or createOrOpenDatabase() first");
        }
        return constructedInstance;
    }
}
