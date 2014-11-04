package datamodel;

/**
 * Created on 2014-11-01.
 */
public class FloatValue extends DataTypeValue {
    private float value; // TODO all issues with BigDecimal, maybe save on datatypes, consider lenght

    public FloatValue(String value) {
        this.value = Float.valueOf(value);
    }

    @Override
    public boolean booleanValue() {
        return false;
    }

    @Override
    public int intValue() {
        return (int)value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
