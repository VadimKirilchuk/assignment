package exercises.BasicInputOutput;

import java.io.*;

/**
 * Created by Андрей on 26.02.2015.
 */
public class SimpleSerialization {
    public static void main(String[] args) {
        try {
/*
        Second second = new Second(15);
        System.out.println("sec " + second.getZ());

            ObjectOutputStream objectOutput = new ObjectOutputStream(
                    new FileOutputStream("e:\\outputStre.txt"));
            objectOutput.writeObject(second);
            second.a = 125;
            objectOutput.writeObject(second);
            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream("e:\\outputStre.txt"));

            Second newSecond = (Second) inputStream.readObject();

            System.out.println("r" + newSecond.getR());
            System.out.println("z " + newSecond.getZ());
            System.out.println("a " + newSecond.getFieldValue());
            System.out.println("hh " + newSecond.hh + "  " + newSecond.b);
            Second newSecond2 = (Second) inputStream.readObject();
            System.out.println("a 2 object " + newSecond2.getFieldValue()+" obje" +newSecond2.ag);
            // System.out.println(newSecond.getFirst().getR());
   */
Ag g=new Ag(111);
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream("e:\\outputS.txt"));
            output.writeObject(g);
            g.i=155;
            System.out.println(" obj  " +g.i);
            output.writeObject(g);
            ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream("e:\\outputS.txt"));
            Ag g1=(Ag)input.readObject();
            Ag g2=(Ag)input.readObject();
            System.out.println(" obj 1 " +g1.i);
            System.out.println(" obj 2 " +g2.i);

            S obj=new S(1,2,3);
            System.out.println("f "+obj.f+" h" +obj.h+" i" +obj.i);
            ObjectOutputStream outputd = new ObjectOutputStream(
                    new FileOutputStream("e:\\outputSSS.txt"));
            output.writeObject(obj);
            ObjectInputStream inputd = new ObjectInputStream(
                    new FileInputStream("e:\\outputSSS.txt"));
           S obj1=(S)input.readObject();
            System.out.println("f "+obj1.f+" h " +obj1.h+" i" +obj1.i);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class test implements Serializable {
    int y = 1;
}

class First implements Serializable {
     private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("r", int.class)};
    public int b = 5;
    int r = 12;
    int hh = 7;

    protected First() {
        // r = 25;
    }

    public int getR() {
        return r;
    }

    /*
            private void writeObject(ObjectOutputStream out) throws IOException {
                out.defaultWriteObject();
                out.writeInt(second.getFieldValue());
            }

            private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
                input.defaultReadObject();
                int value = input.readInt();
                second = new Second(value);
            }
    */
    //+second.toString()
    public String toString() {
        return b + "";
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // out.writeInt(218);

       // ObjectOutputStream.PutField putField = out.putFields();
       // putField.put("r", 118);
        //  putField.put("r", 25);
      // out.writeFields();

    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        // r = input.readInt();
       // ObjectInputStream.GetField getField = input.readFields();
      //   r = getField.get("r", 17);
    }
}

class Second extends First implements Serializable {
   transient  int a = 5;
    private int z = 7;
    public int g=1;
    Ag ag=new Ag(125);
   //private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("z", int.class)
//   ,new ObjectStreamField("a", int.class)   };

    // private First first =new First(3);
    private void writeObject(ObjectOutputStream out) throws IOException {
       //out.writeInt(87);
     //   out.defaultWriteObject();
//out.writeInt(a);
        //out.writeObject(ag);
     //out.defaultWriteObject();
out.writeObject(ag);
        ag.i=21;
        out.writeObject(ag);
       //ObjectOutputStream.PutField putField = out.putFields();
      // putField.put("ag", ag);
         //putField.put("a", 1121);
        //putField.put("r", 29);
    // out.writeFields();
       // out.write(a);
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
      // a = input.readInt();
        //input.defaultReadObject();
        input.readObject();
        ag=(Ag)input.readObject();
       // input.readObject();
//a=input.readInt();
     //   ff=(Integer)input.readObject();
     //  ObjectInputStream.GetField getField = input.readFields();
    //    ag = (Ag)getField.get("ag", new Ag(1));
        //  a=getField.get("a", 17);
        // r=getField.get("r",27);

    }

    public Second(int value) {
        // super(value);
       // a = 1;
        r = value;
        // first = new First();
    }

    public int getZ() {
        return z;
    }

    //public First getFirst(){
    //  return first;
//}
    public String toString() {
        return a + "" + z;
    }

    public int getFieldValue() {
        return a;
    }
}

class Ag implements Serializable{
    int i=87;
    Ag(){
        i=5;
    }
    public Ag(int d){
        i=d;
    }
    public String toString(){
        return "test"+ i;
    }
}

class Z extends Ag {
    int h=1;
    Z(){
        h=95;
    }
}
class S extends Z {
    int f=5;
///    int i=7;

    S(int z, int d,int g){
        h=71;
       i=88;
        f=115;
    }

}

