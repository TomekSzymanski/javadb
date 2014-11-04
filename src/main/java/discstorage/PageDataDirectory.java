package discstorage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created on 2014-10-08.
 */
public class PageDataDirectory {
    Map<Integer, RecordInfo> records = new HashMap<>();
    List<Integer> deletedRecords = new LinkedList<Integer>();
    private int nextRecordId = 0; // next inserted record will get this ID

    private int endOfDataArea;

    /**
     * register record in data directory.
     * @return id for new record registered in the page
     */
    public RecordId addRecord(int recordLenght) {
        // find next available area that is big enough to store the record
        int offsetForNewRecord = findAvailableArea(recordLenght);
        records.put(nextRecordId, new RecordInfo(offsetForNewRecord, recordLenght));
        if (offsetForNewRecord > endOfDataArea) {
            endOfDataArea =+ recordLenght;
        }
        return new RecordId(nextRecordId++);
    }

    private int findAvailableArea(int recordLength) {
        // record fits into available free space
        if (endOfDataArea + recordLength <= Page.DATA_AREA_SIZE) {
            return endOfDataArea;
        } else {
            // if remaining free space is not enough then do page defragmentation (reuse space from deleted records)
            defragmentPage();
            return findAvailableArea(recordLength);
        }
    }

    public RecordInfo getRecordInfo(RecordId recordId) {
        return records.get(recordId.id);
    }

    private void defragmentPage() {}


}
