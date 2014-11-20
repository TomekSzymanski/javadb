package datamodel;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created on 2014-11-02.
 */
public class IntegerValue extends DataTypeValue {
    private int value;

    public IntegerValue(String stringValue) {
        value = Integer.valueOf(stringValue);
    }

    public IntegerValue(int intValue) {
        this.value = intValue;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerValue that = (IntegerValue) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override

    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int length() {
        return 4;
    }

    @Override
    public DataTypeValue valueOf(String s) {
        return new IntegerValue(Integer.parseInt(s));
    }

    @Override
    public void writeToStream(ObjectOutputStream os) throws IOException {
        os.writeInt(value);
    }

}
