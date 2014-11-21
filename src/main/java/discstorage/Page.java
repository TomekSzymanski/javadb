package discstorage;

import datamodel.*;
import org.apache.commons.lang3.Validate;
import storageapi.Record;
import systemdictionary.SystemDictionary;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created on 2014-11-13.
 */

class Page implements Serializable {

    //TODO for efficiency store also list of deleted records, ar pointer to first one, so that we do not have to iterate

    public static final int PAGE_SIZE = 8192; // TODO add it as configurable, from 1k up to 32k

    /**
     * database entity (object) that this page belongs to (table, index)
     * If this field is null then this page is not allocated yet to any object
     */
    private int pageId;

    private static final int INITIAL_FREE_SPACE_LEFT = PAGE_SIZE-2000; //TODO tune: what else is written in page?? what if serialized column list is extremely big (>1000?)

    private int freeSpaceLeft = INITIAL_FREE_SPACE_LEFT;

    private Identifier owningEntityId; // TODO reimplement it on int later, so that we do not have to store this string on disk

    private transient ReadWriteLock pageLock = new ReentrantReadWriteLock();

    private transient PageLoader pageLoader;

    private transient SystemDictionary systemDictionary;

    // this is not loaded upon page creation. This is loaded only when there is first call to the data.
    private List<Record> records; // page data in parsed List form

    // for unit testing
    List<Record> getRecords() {
        return records;
    }

    Page(int pageId, Identifier owningEntityId, PageLoader loader, SystemDictionary systemDictionary) {
        this(pageId, owningEntityId, new ArrayList<>(), loader, systemDictionary);
    }

    Page(int pageId, Identifier owningEntityId, SystemDictionary systemDictionary) {
        this(pageId, owningEntityId, null,DiskLoader.getInstance(), systemDictionary );
    }

    Page(int pageId, Identifier owningEntityId, List<Record> records, SystemDictionary systemDictionary) {
        this(pageId, owningEntityId, records, DiskLoader.getInstance(), systemDictionary);
    }

    // for unit tests
    Page(int pageId, Identifier owningEntityId, List<Record> records, PageLoader loader, SystemDictionary systemDictionary) {
        if (records!=null) {
            int recordSize = records.stream().map(Page::getRecordValuesAndHeaderSize).reduce(0, (x, y) -> x + y);
            Validate.isTrue(recordSize <= freeCapacity(), "size of records to be put on the page, which was %s was too big for page size, which was %s", recordSize, freeCapacity());
        } else {
            records = new ArrayList<>(); // TODO replace with factory call from Record
        }

        this.pageId = pageId;
        this.owningEntityId = owningEntityId;
        this.records = records;
        this.pageLoader = loader;
        this.systemDictionary = systemDictionary;
        pageLoader.writePageData(this); // initialize this page on disk
    }

    Identifier getOwningEntityId() {
        return owningEntityId;
    }

    public int getPageId() {
        return pageId;
    }

    void setUnallocated() {
        owningEntityId = null;
    }

    public void registerTable(Identifier tableName) {
        owningEntityId = tableName;
    }

    public void insertRecord(Record recordValues) {
        pageLock.writeLock().lock();
        loadPageData();
        records.add(recordValues);
        freeSpaceLeft -= getRecordValuesAndHeaderSize(recordValues);
        writePageData();
        pageLock.writeLock().unlock();
    }

    /**
     * Returns number of bytes that this record will take in this Page serialized form
     * @param record
     * @return
     */
    static int getRecordValuesAndHeaderSize(Record record) {
        int headerSize = record.size() * Integer.BYTES; // we write out field lengths as ints
        int valuesSize = record.byteLength();
        return headerSize + valuesSize;
    }

    public List<Record> getAllRecords() {
        pageLock.readLock().lock();
        loadPageData();
        pageLock.readLock().unlock();
        return records;
    }

    public Iterator<Record> iterator() {
        return getAllRecords().iterator();
    }


    public void deleteAllRecords() {
        pageLock.writeLock().lock();
        loadPageData();
        records.clear();
        freeSpaceLeft = INITIAL_FREE_SPACE_LEFT;
        writePageData();
        pageLock.writeLock().unlock();
    }

    /**
     * returns free space that is still left on this page in bytes
     * @return
     */
    public int freeCapacity() {
        return freeSpaceLeft;
    }

    void loadPageData() {
        // TODO: change comment we load data from page loader (for example buffer cache) on purpose, we do not store them in records on purpose, so that they can be evicted (TODO: try weak references here??)
        records = pageLoader.getPageData(pageId).records;
    }

    void writePageData() {
        pageLoader.writePageData(this);
    }

    // NULL_NON_EXISTENT to avoid having to check if it is not null in other functions, as it will be changed later anyway


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (pageId != page.pageId) return false;
        if (owningEntityId != null ? !owningEntityId.equals(page.owningEntityId) : page.owningEntityId != null)
            return false;
        if (records != null ? !records.equals(page.records) : page.records != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageId;
        result = 31 * result + (owningEntityId != null ? owningEntityId.hashCode() : 0);
        result = 31 * result + (records != null ? records.hashCode() : 0);
        return result;
    }

    private void writeObject(ObjectOutputStream os ) throws IOException {
        os.writeInt(pageId);
        os.writeInt(freeSpaceLeft);
        os.writeObject(owningEntityId);

        if (owningEntityId == null){
            return; // no need to write anything more, empty page
        }
        // write record field types
        List<Column> columnList = systemDictionary.getTableColumnsAsList(owningEntityId);
        os.writeObject(columnList);

        int totalRecordNumber = records.size();
        os.writeInt(totalRecordNumber);

        for (Record record : records) {
            if (record.isDeleted()) {
                continue;
            }
            // write out list of record field lengths first
            for(DataTypeValue value : record) {
                os.writeInt(value.byteLength());
            }
            // and then actual record data
            for (DataTypeValue value : record) {
                value.writeToStream(os); // we do not use default serialization here as we do not need DataTypeValue object headers to be written to the stream
            }
        }
    }

    private void readObject(ObjectInputStream is ) throws IOException, ClassNotFoundException {
        pageId = is.readInt();
        freeSpaceLeft = is.readInt();
        owningEntityId = (Identifier)is.readObject();

        if (owningEntityId == null){
            records = new ArrayList<>();
            return; // no need to write anything more, empty page
        }

        // read record field types
        List<Column> columns = (List<Column>) is.readObject();
        int numberOfFields = columns.size();
        int[] recordLengths = new int[numberOfFields];

        int totalRecordNumber = is.readInt();

        List<Record> allRecords = new ArrayList<>(numberOfFields);
        for (int i = 0; i < totalRecordNumber; i++) {
            // read record lengths first
            for (int j = 0; j < numberOfFields; j++) {
                recordLengths[j] = is.readInt();
            }
            // read actual data
            Record record = new Record();
            for (int j = 0; j < numberOfFields; j++) {
                DataTypeValue value;
                if (recordLengths[j] == 0) { // null value
                    value = NullValue.NULL;
                } else {
                    Column column = columns.get(j);
                    SQLDataType dataValueFactory = column.dataType;
                    value = dataValueFactory.readValue(is, recordLengths[j]); // TODO recordLengths[j] represent, but for varchar, the serializied lenght. nothing to do with
                }
                record.add(value);
            }
            allRecords.add(record);
        }
        records = allRecords;
    }

}
