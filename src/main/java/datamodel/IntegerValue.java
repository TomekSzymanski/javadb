package datamodel;

/**
 * Created on 2014-11-02.
 */
public class IntegerValue extends DataTypeValue {
    private int value;

    public IntegerValue(String stringValue) {
        value = Integer.valueOf(stringValue);
    }

    @Override
    public boolean booleanValue() {
        return false;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
