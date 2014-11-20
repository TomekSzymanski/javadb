package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created on 2014-11-01.
 */
public abstract class SQLDataType implements Serializable {

    public static final String NUMBER = "NUMBER";
    public static final String VARCHAR = "VARCHAR";
    public static final String BOOLEAN = "BOOLEAN";

    public abstract int getFieldSizeSpecifier(int index);

    abstract DataTypeValue valueOfNotNull(String stringValue);

    public DataTypeValue valueOf(String stringValue) {
        if (stringValue.equalsIgnoreCase("NULL")) { // TODO: NULL also defined in parser. Move all keyword definitions into datamodel?
            return NullValue.NULL;
        } else {
            return valueOfNotNull(stringValue);
        }
    }

    public abstract String getTypeName();

    public abstract DataTypeValue readValue(ObjectInputStream is, int numberOfBytesToRead) throws IOException;

}
