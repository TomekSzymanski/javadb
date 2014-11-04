package sqlparser;

/**
 * Created on 2014-10-04.
 */
interface SQLStatementParser {
    AbstractSQLCommand parse(String sql) throws SQLParseException;
}
