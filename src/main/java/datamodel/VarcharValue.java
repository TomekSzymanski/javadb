package datamodel;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
    public long longValue() {
        return Long.parseLong(value);
    }

    @Override
    public float floatValue() {
        return Float.parseFloat(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int length() {
        return value.length() * 2; // char is represented on two bytes (in serialization)

    }

    @Override
    public DataTypeValue valueOf(String s) {
        return new VarcharValue(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VarcharValue that = (VarcharValue) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public void writeToStream(ObjectOutputStream os) throws IOException {
        os.writeChars(value); // TODO: encoding always the same??
    }

}
