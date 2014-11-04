package datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2014-11-01.
 */
public class SQLVarcharDataType extends SQLDataType {
    private final int lenght;

    // cannot instantiate VARCHAR without specifying max lenght
    private SQLVarcharDataType(int lenght) {
        this.lenght = lenght;
    }

    private static Map<Integer, SQLVarcharDataType> instances = new HashMap<>();

    int getLenght() {
        return lenght;
    }

    static SQLVarcharDataType getInstance(int lenght) {
        if (!instances.containsKey(lenght)) {
            instances.put(lenght, new SQLVarcharDataType(lenght));
        }

        return instances.get(lenght);
    }

    @Override
    public int getFieldSizeSpecifier(int index) {
        if (index==0) {
            return lenght;
        }
        throw new IllegalArgumentException("There can be only one field length specifier for VARCHAR");
    }

    @Override
    public DataTypeValue valueOfNotNull(String stringValue) {
        // TODO check: we trim to column size, no exception
        if (stringValue.length()<=lenght) {
            return new VarcharValue(stringValue);
        } else {
            return new VarcharValue(stringValue.substring(0, lenght-1));
        }
    }
}
