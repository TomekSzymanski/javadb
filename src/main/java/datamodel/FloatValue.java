package datamodel;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return value;
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
        return new FloatValue(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatValue that = (FloatValue) o;

        if (Float.compare(that.value, value) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (value != +0.0f ? Float.floatToIntBits(value) : 0);
    }

    @Override
    public void writeToStream(ObjectOutputStream os) throws IOException {
        os.writeFloat(value);
    }

}
