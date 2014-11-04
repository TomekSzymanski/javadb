package discstorage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing actual values of data record.
 * Does not contain information on field names or data types, those information are kept in some directory (DB directory, extent directory, page directory - which - TBD)
 */
public class RecordValues implements Serializable {
    List<? extends Serializable> values = new LinkedList<>();

    /**
     * returns lenght of record values in bytes. It is sum of length of all fields of record
     */
    public int lenght() {
        return 0;
    }

    public RecordValues(RecordValues values, RecordFormat format) {}
}
