package storageapi;

import clientapi.SQLException;

/**
 * Created on 2014-10-24.
 */
public class DataStoreException extends SQLException {
    public DataStoreException(String msg) {
        super(msg);
    }

    public DataStoreException(Throwable e) {
        super(e);
    }

    public DataStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
