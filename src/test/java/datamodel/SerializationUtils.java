package datamodel;

import java.io.*;

/**
 * Created on 2014-11-20.
 */
public class SerializationUtils {
    public static <T> T serializeDeserialize(T beforeSerialization) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(beforeSerialization);

        byte[] bytes = bout.toByteArray();
        out.close();

        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bin);
        T deserialized = (T)in.readObject();
        in.close();
        return deserialized;
    }
}
