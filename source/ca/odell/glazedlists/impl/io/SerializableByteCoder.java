/**
 * Glazed Lists
 * http://glazedlists.dev.java.net/
 *
 * COPYRIGHT 2003 O'DELL ENGINEERING LTD.
 */
package ca.odell.glazedlists.impl.io;

import java.util.List;
import java.io.*;
// Glazed Lists' pluggable object to bytes interface
import ca.odell.glazedlists.io.ByteCoder;

/**
 * A {@link ByteCoder} that uses {@link Serializable}.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
public class SerializableByteCoder implements ByteCoder {

    /** {@inheritDoc} */
    public void encode(Object source, OutputStream target) throws IOException {
        ObjectOutputStream objectOut = new ObjectOutputStream(target);
        objectOut.writeObject(source);
        objectOut.close();
    }
    
    /** {@inheritDoc} */
    public Object decode(InputStream source) throws IOException {
        try {
            ObjectInputStream objectIn = new ObjectInputStream(source);
            return objectIn.readObject();
        } catch(ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}