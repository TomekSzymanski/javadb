package discstorage;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;

import java.io.File;

/**
 * Created on 2014-11-17.
 */
public class DiskLoaderTest extends PageLoaderTest {

    private final static File testDataFile = new File("someTestDataFile");
    private static final PageLoader loader = DiskLoader.createNewLoader(testDataFile);

    @Override
    protected PageLoader getLoaderInstance() {
        return loader;
    }

    @AfterClass
    public static void tearDown() {
        FileUtils.deleteQuietly(testDataFile);
    }
}
