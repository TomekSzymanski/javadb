package discstorage;

import datamodel.Identifier;
import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.lang3.Validate;
import storageapi.DataStoreException;
import storageapi.PersistentStorage;
import storageapi.Record;
import storageapi.Storage;
import systemdictionary.SystemDictionary;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2014-11-13.
 */
public class PageBasedStorage implements PersistentStorage {

    private static final String systemTablespaceFileName = "system.dat";
    private static final String pageRegistryFileName = "page_registry.dat";
    private static final String dataFileName = "data.dat";

    private File systemTablespaceFile;
    static File dataFile;

    private PageRegistry pageRegistry;

    private SystemDictionary systemDictionary;

    private static PageBasedStorage INSTANCE;

    private PageBasedStorage(String databaseDirectoryPath){
        // initialize tablespaces
        systemTablespaceFile = new File(databaseDirectoryPath, systemTablespaceFileName);
        if (!systemTablespaceFile.exists()) { // new database
            try {
                systemTablespaceFile.createNewFile();
            } catch (IOException e) {
                throw new DataStoreException(e);
            }
        } else {
            validateFilePrivileges(systemTablespaceFile);
        }
        systemDictionary = SystemDictionary.loadSystemDictionary(systemTablespaceFile); //TODO move this initialization to some main bootstram class, that will initialize whole database (will initialize storage, system dictionary, pools for parsers and executors, listeners and so on)

        // we initialize pageLoader before we initialize pageRegistry, as loader is needed for Pages (which are needed by Registry)
        dataFile = new File(databaseDirectoryPath, dataFileName);
        DiskLoader.createNewLoader(dataFile);

        File pageRegistryFile = new File(databaseDirectoryPath, pageRegistryFileName);
        pageRegistry = PageRegistry.loadFromDisk(pageRegistryFile, SystemDictionary.getInstance());

    }

    public static void loadDatabaseFromDisk(String databaseDirectoryPath) {
        File databaseDirectory = new File(databaseDirectoryPath);

        Validate.isTrue(databaseDirectory.exists(), "Trying ot initialize database from non existent directory %s" + databaseDirectoryPath);

        Validate.isTrue(databaseDirectory.isDirectory(), "You have to provide directory to initialize database. %s is not directory", databaseDirectoryPath);
        validateDirectoryPrivilleges(databaseDirectory);

        if (INSTANCE!= null) {
            throw new IllegalStateException("Trying to initialize database from disk, while it has been already initialized");
        }
        INSTANCE = new PageBasedStorage(databaseDirectoryPath);
    }

    /**
     * Provided directory on disk must either not exist (then it will be created) or exist and be empty
     * @param databaseDirectoryPath
     */
    public static void createDatabaseOnDisk(String databaseDirectoryPath) { //TODO add support for max tablespace and directory size
        File databaseDirectory = new File(databaseDirectoryPath);
        if (!databaseDirectory.exists()) {
            databaseDirectory.mkdir();
        } else {
            validateDirectoryPrivilleges(databaseDirectory);
            // TODO Validate.isTrue(databaseDirectory.list().length == 0, "Provided directory %s to create database is not empty. You have to either provide path to directory to be created (not existing yet), or provide path to empty directory", databaseDirectoryPath);
        }
        if (INSTANCE!= null) {
            throw new IllegalStateException("Trying to initialize new database while database already exists");
        }
        INSTANCE = new PageBasedStorage(databaseDirectoryPath);
    }

    private static void validateDirectoryPrivilleges(File databaseDirectory) {
        Validate.isTrue(databaseDirectory.canRead(), "Missing privileges to read the directory %s", databaseDirectory);
        Validate.isTrue(databaseDirectory.canWrite(), "Missing privileges to write the directory %s", databaseDirectory);
        //TODO test on Windows and unix : Validate.isTrue(databaseDirectory.canExecute(), "Missing privileges to write the directory %s" , databaseDirectoryPath);

    }

    private static void validateFilePrivileges(File file) {
        Validate.isTrue(file.canRead(), "Missing privileges to read the file %s", file);
        Validate.isTrue(file.canWrite(), "Missing privileges to write the file %s", file);

    }


    public static Storage getInstance() {
        if (INSTANCE==null) {
            throw new IllegalStateException("Cannot get instance of not initialized storage object");
        }
        return INSTANCE;
    }

    @Override
    public void createTable(Identifier tableName) {
        Page newPage = pageRegistry.getUnallocatedPage();
        //Validate.isTrue(!pageRegistry.isAtLeastOnePageAllocated(tableName), "Trying to create table again. Table " + tableName + " already exists");
        newPage.registerTable(tableName);
    }

    @Override
    public void dropTable(Identifier tableName) {
        //Validate.isTrue(pageRegistry.isAtLeastOnePageAllocated(tableName), "Trying to drop non existent table "+ tableName);
        pageRegistry.deallocateAllPages(tableName);
    }

    @Override
    public void insertRecord(Identifier tableName, Record recordValues) throws DataStoreException {
        int recordLength = Page.getRecordValuesAndHeaderSize(recordValues);
        Page freePage = pageRegistry.findPageWithFreeSpace(tableName, recordLength);
        freePage.registerTable(tableName);
        freePage.insertRecord(recordValues);
    }

    @Override
    public Iterator<Record> tableIterator(Identifier tableName) throws DataStoreException {
        List<Iterator<Record>> iteratorsList = pageRegistry.getPageList(tableName).stream()
                .map(Page::iterator).collect(Collectors.toList());
        return (Iterator<Record>)new IteratorChain(iteratorsList);
    }

    @Override
    public void deleteAll(Identifier tableName) {
        for (Page page : pageRegistry.getPageList(tableName)) {
            page.deleteAllRecords();
        }
    }

    @Override
    public SystemDictionary getSystemDictionary() {
        return systemDictionary;
    }

    @Override
    public void writeSystemDictionary(SystemDictionary dictionary) throws DataStoreException {
        try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(systemTablespaceFile))) {
            output.writeObject(dictionary);
        } catch (IOException e) {
            throw new DataStoreException("Error writing system dictiionary to disk file", e);
        }
    }
}
