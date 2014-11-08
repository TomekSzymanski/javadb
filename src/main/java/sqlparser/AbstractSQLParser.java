package sqlparser;

import datamodel.Identifier;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2014-10-22.
 */
public abstract class AbstractSQLParser {

    static final String COMMA = ",";
    static final String LEFT_PAREN = "(";
    static final String RIGHT_PAREN = ")";
    static final String TABLE = "TABLE";
    static final String CREATE = "CREATE";
    static final String WHERE = "WHERE";
    static final String FROM = "FROM";
    static final String SELECT = "SELECT";
    static final String DROP = "DROP";
    static final String DELETE = "DELETE";
    static final String INSERT = "INSERT";
    static final String VALUES = "VALUES";
    static final String INTO = "INTO";
    static final String NOT = "NOT";
    static final String NULL = "NULL";
    public static final String ASTERISK = "*";

    private static final String[] RESERVED_KEYWORDS = {"SELECT", "CREATE", "FROM", "TABLE", "WHERE", "INSERT", "INTO", "VALUES", "DROP", "DELETE",
            "NOT", "NULL",
            "NUMBER", "VARCHAR", "BOOLEAN", ASTERISK};

    public static boolean isReservedKeyword(String value) {
        for (String keyword : RESERVED_KEYWORDS) {
            if (value.equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    List<Identifier> parseIdentifierList(List<String> list) throws SQLParseException {

        List<String> elements;
        try {
            elements = parseCommaSeparatedList(list);
        } catch (IllegalArgumentException e) {
            throw new SQLParseException("No identifiers specified OR Missing comma in identifiers list");
             // TODO how to differentiate exceptions
        }

        List<Identifier> identifiers = new ArrayList<>();
        for (String element : elements) {
            identifiers.add(new Identifier(element));
        }

        return identifiers;
    }

    List<String> createListFromAllTokensBefore(Tokenizer tokenizer, String endToken) {
        List<String> tokensBefore = new LinkedList<>();
        while (tokenizer.hasNext() && !tokenizer.peek().equals(endToken)) {
            tokensBefore.add(tokenizer.next());
        }
        return tokensBefore;
    }

    List<String> parseCommaSeparatedList(List<String> list) throws SQLParseException {
        List<String> elements = new ArrayList<>();
        Validate.notEmpty(list, "Comma separated list is empty");
        // add first identifier
        elements.add(list.get(0));
        // continue for more elements, if there are more
        for (int idx = 1; idx < list.size();) {
            // first we have to meet comma
            if (!list.get(idx++).equals(COMMA)) {
                throw new SQLParseException("Missing comma in list that should be comma separated");
            }
            // and then column name
            elements.add(list.get(idx++));
        }
        return elements;
    }

    /**
     * Checks if next token from provided tokenizer exists and is equal to provided expected string. If it is true then consumes this token
     * If there is no more tokens or string does not match then SQLParseException is thrown, with the provided message
     * @param tokenizer
     * @param expected
     * @param exceptionMessage
     */
    void expect(Tokenizer tokenizer, String expected, String exceptionMessage) throws SQLParseException {
        if (!tokenizer.hasNext() || !tokenizer.next().equalsIgnoreCase(expected)) {
            throw new SQLParseException(exceptionMessage);
        }
    }

    Tokenizer getTokenizer(String sql) {
        return new SimpleTokenizer(sql);
    }

    public AbstractSQLCommand parse(String sql) throws SQLParseException {
        return parse(getTokenizer(sql));
    }

    abstract AbstractSQLCommand parse(Tokenizer tokenizer) throws SQLParseException;
}
