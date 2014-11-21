package datamodel;

import org.apache.commons.lang3.Validate;
import sqlparser.AbstractSQLParser;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created on 2014-10-04.
 */
public class Identifier implements Serializable { //TODO: replace its serialization with int

    public static final int MAX_IDENTIFIER_LENGTH = 60;
    private static final String[] RESERVED_KEYWORDS = {"SELECT", "CREATE", "FROM", "TABLE", "WHERE", "INSERT", "INTO", "VALUES", "DROP", "DELETE",
            "NOT", "NULL",
            "NUMBER", "VARCHAR", "BOOLEAN", AbstractSQLParser.ASTERISK};

    private final String value;

    public Identifier(String value) {
        // TODO: is dependency from datamodel to sqlparser needed?
        Validate.isTrue(!isReservedKeyword(value), "Cannot use SQL reserved keyword %s as identifier name", value);
        Validate.isTrue(value.length() <= MAX_IDENTIFIER_LENGTH, "Cannot use %s as identifier. Exceeded maximum identifier length which is %s", value, MAX_IDENTIFIER_LENGTH);
        Validate.isTrue(!value.isEmpty(), "Cannot use empty string as identifier");
        Validate.matchesPattern(value, "[0-9a-zA-Z_]+", "Identifier name can be only alphanumeric or underscore");
        this.value = value;
    }

    public static boolean isReservedKeyword(String value) {
        return Arrays.stream(RESERVED_KEYWORDS).anyMatch(keyword -> keyword.equals(value));
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        return value.equalsIgnoreCase(that.value); // Identifier name is case insensitive

    }

    @Override
    public int hashCode() {
        return value.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
