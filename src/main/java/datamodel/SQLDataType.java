package datamodel;

/**
 * Created on 2014-11-01.
 */
public abstract class SQLDataType {

    static final String NUMBER = "NUMBER";
    static final String VARCHAR = "VARCHAR";
    static final String BOOLEAN = "BOOLEAN";

    public abstract int getFieldSizeSpecifier(int index);

    abstract DataTypeValue valueOfNotNull(String stringValue);

    public DataTypeValue valueOf(String stringValue) {
        if (stringValue.equalsIgnoreCase("NULL")) { // TODO: NULL also defined in parser. Move all keyword definitions into datamodel?
            return NullValue.NULL;
        } else {
            return valueOfNotNull(stringValue);
        }
    }

}
