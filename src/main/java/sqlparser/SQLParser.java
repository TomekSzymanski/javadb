package sqlparser;

/**
 * Created by wojtek on 08.11.14.
 */
public interface SQLParser<T extends AbstractSQLCommand> {

    T parse(String sql) throws SQLParseException;
}
