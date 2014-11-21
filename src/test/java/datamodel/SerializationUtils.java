package datamodel;

import java.io.*;

/**
 * Created on 2014-11-20.
 */
public class SerializationUtils {
    public static <T> T serializeDeserialize(T beforeSerialization) throws IOException, ClassNotFoundException {
        byte[] bytes;
        try ( ByteArrayOutputStream bout = new ByteArrayOutputStream();  ObjectOutput out = new ObjectOutputStream(bout) ) {
            out.writeObject(beforeSerialization);
            bytes = bout.toByteArray();
        }

        try ( ByteArrayInputStream bin = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bin) ) {
            T deserialized = (T)in.readObject();
            return deserialized;
        }
    }
}
