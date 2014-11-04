package discstorage;

/**
 */
public class RecordInfo {

    // record offset in the page, starting counting from the beginning of data area of page
    int offset;

    // record length, in bytes
    int length;

    // record removed, its space can be reused
    boolean deleted;

    public RecordInfo(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }
}
