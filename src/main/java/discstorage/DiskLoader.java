package discstorage;

import storageapi.DataStoreException;

import java.io.*;

/**
 * Created on 2014-11-15.
 */
public class DiskLoader implements PageLoader {

    private static DiskLoader INSTANCE;

    private final RandomAccessFile dataFile;

    public static PageLoader createNewLoader(File pageDataFile) {
        if (INSTANCE != null) {
            throw new IllegalStateException("Trying to initialize already initialized loader");
        }
        INSTANCE = new DiskLoader(pageDataFile);
        return INSTANCE;
    }

    public static PageLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Trying to use not initialized loader");
        }
        return INSTANCE;
    }

    @Override
    public Page getPageData(int pageId) {
        Page page;
        try {
            // 1. calculate file offset to write page data
            long pageDataStartOffset = calculatePageOffset(pageId);

            // 2. read page data at that offset
            dataFile.seek(pageDataStartOffset);
            byte [] bytes = new byte[Page.PAGE_SIZE];
            dataFile.read(bytes); // TODO consider replacing with new i/o, measure performance in both cases
            // TODO dataFile not closed?

            // 3. transform byte array into Page object (
            ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
            ObjectInput objectIn = new ObjectInputStream(byteIn); // TODO Objectin not closed. Try-with-resourceS?
            page = (Page)objectIn.readObject();
            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new DataStoreException("Unable to read page from data file. Page id = " + pageId, e);
        }
        return page;
    }

    @Override
    public void writePageData(Page page) {
        try {
            // 1. have page put its data in byte array
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutput objectOut = new ObjectOutputStream(bout);
            objectOut.writeObject(page);
            byte [] bytes = bout.toByteArray();
            objectOut.close();

            // page data cannot be bigger than page size
            assert bytes.length <= Page.PAGE_SIZE; // this assertion is needed for time of code writing and testing, can be disabled later in production code. It is kind of electric fence alarm, we do not rely on it

            // 2. calculate file offset to write page data
            int pageId = page.getPageId();
            long pageDataStartOffset = calculatePageOffset(pageId);

            // 3. write page data at that offset
            dataFile.seek(pageDataStartOffset);
            dataFile.write(bytes); // TODO consider replacing with new i/o, measure performance in both cases
        } catch (IOException e) {
            throw new DataStoreException(e);
        }
    }

    private int calculatePageOffset(int pageId) {
        return 1000 + pageId * Page.PAGE_SIZE + 1;
    }

    private DiskLoader(File pageDataFile) {
        try {
            dataFile = new RandomAccessFile(pageDataFile, "rw"); // TODO: later consider rws/rwd mode (guarantee of synchronous write to disk)
        } catch (IOException e) {
            throw new DataStoreException(e);
        }
    }

}
