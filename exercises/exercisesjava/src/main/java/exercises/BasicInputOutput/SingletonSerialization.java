package exercises.BasicInputOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 04.03.2015.
 */
public class SingletonSerialization {
    public static void main(String[] args) {
        List<Double> b = new ArrayList();

        try {
            FileOutputStream fileout = new FileOutputStream("e:\\singleton.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileout);
            out.writeObject(Singleton.singleA);
            out.flush();
            out.close();

            FileInputStream inputStream = new FileInputStream("e:\\singleton.dat");
            ObjectInputStream input = new ObjectInputStream(inputStream);
            Singleton result = (Singleton) input.readObject();
            input.close();
            System.out.println(result == Singleton.singleA);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Singleton implements Serializable {
    public final static Singleton singleA = new Singleton(1);
    public final static Singleton singleB = new Singleton(2);
    int val;

    private Singleton(int number) {

        val = number;
    }

    public Object readResolve() throws ObjectStreamException {
        if (val == 1)
            return singleA;
        else {
            return singleB;
        }
    }
}