package sqlparser;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2014-10-23.
 */
public class SQLParser {

    private static Map<String, AbstractSQLParser> SQLCommandParsersMap = initializeParserInstancesMap(); //TODO map from String ... nice?

    private static Map<String, AbstractSQLParser> initializeParserInstancesMap() {
        Map<String, AbstractSQLParser> parserInstancesMap = new HashMap<>();
        parserInstancesMap.put(AbstractSQLParser.SELECT, new SelectSQLParser());
        parserInstancesMap.put(AbstractSQLParser.CREATE, new CreateTableStatementParser());
        parserInstancesMap.put(AbstractSQLParser.INSERT, new InsertSQLParser());
        parserInstancesMap.put(AbstractSQLParser.DELETE, new DeleteSQLParser());
        parserInstancesMap.put(AbstractSQLParser.DROP, new DropTableSQLParser());
        return parserInstancesMap;
    }


    public AbstractSQLCommand parse(String sql) throws SQLParseException {
        AbstractSQLParser parser = getParser(sql);
        return parser.parse(sql);
    }

    private static AbstractSQLParser getParser(String input) throws SQLParseException {
        SimpleTokenizer tokenizer = new SimpleTokenizer(input);
        String next = tokenizer.peek();
        AbstractSQLParser parser = SQLCommandParsersMap.get(next.toUpperCase());
        Validate.notNull(parser, "Unrecognized SQL command starting with: \"%s\"", next);
        return parser;
    }
}
