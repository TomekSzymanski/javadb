package sqlparser;

public interface SQLParser<T extends AbstractSQLCommand> {

    T parse(String sql) throws SQLParseException;
}
