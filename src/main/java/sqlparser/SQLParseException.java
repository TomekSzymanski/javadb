package sqlparser;

import clientapi.SQLException;

/**
 * Created on 2014-10-04.
 */
public class SQLParseException extends SQLException {
    public SQLParseException(String s) {
        super(s);
    }

    public SQLParseException(Throwable e) {
     super(e);
    }

    public SQLParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
