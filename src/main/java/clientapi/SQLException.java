package clientapi;

/**
 * Created on 2014-10-23.
 */
public class SQLException extends RuntimeException {

    public SQLException(Throwable cause) {
        super(cause);
    }

    public SQLException(String s) {
        super(s);
    }

    public SQLException(String message, Throwable cause) {
        super(message, cause);
    }

}
