package datamodel;

import clientapi.SQLException;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2014-11-01.
 */
public class SQLVarcharDataType extends SQLDataType {
    private final int length;

    // object creation possible tru factory method only
    private SQLVarcharDataType(int length) {
        this.length = length;
    }

    private static Map<Integer, SQLVarcharDataType> instances = new HashMap<>();

    int getLength() {
        return length;
    }

    static SQLVarcharDataType getInstance(int length) {
        if (!instances.containsKey(length)) {
            instances.put(length, new SQLVarcharDataType(length));
        }

        return instances.get(length);
    }

    @Override
    public int getFieldSizeSpecifier(int index) {
        Validate.isTrue(index==0, "There can be only one field length specifier for VARCHAR");
        return length;
    }

    @Override
    public DataTypeValue valueOfNotNull(String stringValue) {
        // we work as Oracle: if string length exceeds VARCHAR maz size then we throw exception
        if (stringValue.length() > length) {
            throw new SQLException("Cannot use string \"" + stringValue + "\" of length " + stringValue.length()
                                    + " for VARCHAR data type of maximum length of " + length);
        }
        if (stringValue.length()<= length) {
            return new VarcharValue(stringValue);
        } else {
            return new VarcharValue(stringValue.substring(0, length -1));
        }
    }

    @Override
    public String getTypeName() {
        return SQLDataType.VARCHAR;
    }
}
