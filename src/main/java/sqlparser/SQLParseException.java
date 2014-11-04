package sqlparser;

/**
 * Created on 2014-10-04.
 */
public class SQLParseException extends RuntimeException {
    public SQLParseException(String s) {
        super(s);
    }

    public SQLParseException(Throwable e) {
     super(e);
    }
}
