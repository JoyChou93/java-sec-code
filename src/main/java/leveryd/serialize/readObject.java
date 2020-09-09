package leveryd.serialize;

import org.junit.Test;

import java.io.*;


class Evil implements Serializable {
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
     System.out.println("readObject");
    }
}

public class readObject {
    @Test
    public void write() throws Exception {
        Evil evil = new Evil();
        OutputStream os = new FileOutputStream("/tmp/evil");
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(evil);
    }

    @Test
    public void read() throws Exception {
        InputStream is = new FileInputStream("/tmp/evil");
        ObjectInputStream ois = new ObjectInputStream(is);
        ois.readObject();
    }
}
