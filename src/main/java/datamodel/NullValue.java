package datamodel;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
    public long longValue() {
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

    @Override
    public int byteLength() {
        return 0;
    }

    @Override
    public DataTypeValue valueOf(String s) {
        return NULL;
    }

    @Override
    public void writeToStream(ObjectOutputStream os) throws IOException {
        // intentionally blank: do not serialize anything for NULL values
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return !(o == null || getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
