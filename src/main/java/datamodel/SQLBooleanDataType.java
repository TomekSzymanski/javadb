package datamodel;

/**
 * Created on 2014-11-01.
 */
public class SQLBooleanDataType extends SQLDataType {

    private SQLBooleanDataType() {}

    private static SQLBooleanDataType instance;

    static SQLBooleanDataType getInstance() {
        if (instance==null) {
            instance = new SQLBooleanDataType();
        }
        return instance;
    }

    @Override
    public int getFieldSizeSpecifier(int index) {
        throw new IllegalArgumentException("BOOLEAN data type cannot have any field size specifiers");
    }

    @Override
    public DataTypeValue valueOfNotNull(String stringValue) {
        if (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("Y")) {
            return BooleanValue.TRUE;
        }
        if (stringValue.equalsIgnoreCase("false") || stringValue.equalsIgnoreCase("N")) {
            return BooleanValue.FALSE;
        }
        throw new IllegalArgumentException("Cannot assign " + stringValue + " for BOOLEAN field");
    }
}
