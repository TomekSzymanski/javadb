package datamodel;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created on 2014-11-01.
 */
public abstract class DataTypeValue {

    public abstract boolean booleanValue();

    public abstract int intValue();

    public abstract long longValue();

    public abstract float floatValue();

    public abstract String toString();

    /**
     * returns field length in bytes
     *
     * @return
     */
    public abstract int byteLength();

    public abstract DataTypeValue valueOf(String s);

    public abstract void writeToStream(ObjectOutputStream os) throws IOException;

}
