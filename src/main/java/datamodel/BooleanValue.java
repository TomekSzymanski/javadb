package datamodel;

import java.io.IOException;
import java.io.ObjectOutputStream;

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

    public static final BooleanValue valueOf(boolean arg) {
        return arg? TRUE : FALSE;
    }

    @Override
    public boolean equals(Object o) { // although we create objects only by factory methods which can return either of the two records, after deserialization we cannot rely on it to be the same object
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooleanValue that = (BooleanValue) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override

    public boolean booleanValue() {
        return value;
    }

    @Override
    public int intValue() {
        throw new IllegalArgumentException("cannot cast boolean to int");
    }

    @Override
    public long longValue() {
        throw new IllegalArgumentException("cannot cast boolean to long");
    }

    @Override
    public float floatValue() {
        throw new IllegalArgumentException("cannot cast boolean to float");
    }

    @Override
    public String toString() {
        return Boolean.valueOf(value).toString();
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public DataTypeValue valueOf(String s) {
        return Boolean.parseBoolean(s) ? TRUE : FALSE;
    }

    @Override
    public void writeToStream(ObjectOutputStream os) throws IOException {
        os.writeBoolean(value);
    }

}
