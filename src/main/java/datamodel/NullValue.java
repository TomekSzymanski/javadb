package datamodel;

/**
 * Created on 2014-11-02.
 */
public class NullValue extends DataTypeValue {
    private NullValue() {}

    public static final NullValue NULL = new NullValue();

    @Override
    public boolean booleanValue() {
        return false;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
