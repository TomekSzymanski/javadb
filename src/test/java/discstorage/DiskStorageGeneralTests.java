package discstorage;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import storageapi.Storage;
import storageapi.StorageGeneralTest;

import java.io.File;

/**
 * Created on 2014-11-13.
 */
public class DiskStorageGeneralTests extends StorageGeneralTest {

    private static String testDiskDBDirectoryString = "tmp_database_for_unit_tests";

    static {
        PageBasedStorage.createDatabaseOnDisk(testDiskDBDirectoryString); // TODO: remove dependency on hard drive
    }

    private static Storage pageBasedStorage = PageBasedStorage.getInstance(); // TODO: do we need another factory method (3rd one?)

    @Override
    protected Storage initializeStorage() {
        return pageBasedStorage;
    }


    @AfterClass
    public static void tearDown() {
        File testDiskDBDirectory = new File(testDiskDBDirectoryString);
        FileUtils.deleteQuietly(testDiskDBDirectory);
    }
}
