package discstorage;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Page for storing fixed-length and variable-length records
 */
public class Page implements Serializable {
    private static final int DATA_AREA_START_OFFSET = 500; //

    private static final int PAGE_SIZE = 2048; //2kB

    static final int DATA_AREA_SIZE = 1024; //2kB

    PageID pageID;

    PageDataDirectory directory;

    // byte[] dataArea = new byte[DATA_AREA_SIZE];
    Map<RecordId, RecordValues> dataArea = new HashMap<>();

    //private ByteArrayInputStream dataInput = new ByteArrayInputStream(dataArea);
    //private ByteArrayOutputStream dataOutput = new ByteArrayOutputStream();


    /**
     * adds record values to this page.
     * @param recordValues
     */
    public RecordId addRecord(RecordValues recordValues) {
       /*
    1. registers records in page data directory
    2. actually adds the record values to the page
     */
        RecordId recordId = directory.addRecord(recordValues.lenght());
        dataArea.put(recordId, recordValues);
        return recordId;
    }


    public void removeRecord(RecordId recordId) {

    }

    public RecordValues getRecord(RecordId recordId) {
        int offset = directory.getRecordInfo(recordId).offset;
        int length = directory.getRecordInfo(recordId).length;
        // byte[] recordData = new byte[length];
        // dataInput.read(recordData, offset, length);
        return new RecordValues(dataArea.get(recordId), new RecordFormat()); // TODO: get record format from DB data dictionary
    }

    public void updateRecord(RecordId recordId){} // record may grow out of current allocated slot
}
