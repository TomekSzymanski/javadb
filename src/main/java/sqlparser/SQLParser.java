package sqlparser;

/**
 * Created on 2014-10-23.
 */
public class SQLParser {

    public AbstractSQLCommand parse(String sql) throws SQLParseException {
        SQLStatementParser parser = getParser(sql);
        return parser.parse(sql);
    }

    private static SQLStatementParser getParser(String input) throws SQLParseException {
        SimpleTokenizer tokenizer = new SimpleTokenizer(input);
        String next = tokenizer.peek();
        SQLStatementParser statementParser;
        switch (next.toUpperCase()) {
            case AbstractSQLParser.SELECT: statementParser = new SelectSQLParser(); break;
            case AbstractSQLParser.CREATE: statementParser = new CreateTableStatementParser(); break;
            case AbstractSQLParser.INSERT: statementParser = new InsertSQLParser(); break;
            case AbstractSQLParser.DELETE: statementParser = new DeleteSQLParser(); break;
            case AbstractSQLParser.DROP: statementParser = new DropTableSQLParser(); break;
            default: throw new SQLParseException("Unrecognized command: " + next);
        }
        return statementParser;
    }
}
