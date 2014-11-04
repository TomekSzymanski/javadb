package datamodel;

/**
 * Created on 2014-11-01.
 */
public class BooleanValue extends DataTypeValue {
    private final boolean value;

    private BooleanValue(boolean value) {
        this.value = value;
    }

    public static final BooleanValue TRUE = new BooleanValue(true);
    public static final BooleanValue FALSE = new BooleanValue(false);

    @Override
    public boolean booleanValue() {
        return value;
    }

    @Override
    public int intValue() {
        throw new IllegalArgumentException("cannot cast boolean to int");
    }

    @Override
    public float floatValue() {
        throw new IllegalArgumentException("cannot cast boolean to float");
    }

    @Override
    public String toString() {
        return Boolean.valueOf(value).toString();
    }
}
