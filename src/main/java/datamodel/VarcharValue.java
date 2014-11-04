package datamodel;

/**
 * Created on 2014-11-01.
 */
public class VarcharValue extends DataTypeValue {
    private final String value;

    public VarcharValue(String value) {
        this.value = value;
    }

    @Override
    public boolean booleanValue() {
        return Boolean.parseBoolean(value);
    }

    @Override
    public int intValue() {
        return Integer.parseInt(value);
    }

    @Override
    public float floatValue() {
        return Float.parseFloat(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
