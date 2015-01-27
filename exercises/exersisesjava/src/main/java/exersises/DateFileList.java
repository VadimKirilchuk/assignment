package exersises;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Андрей on 21.01.2015.
 */
public class DateFileList {
    private List<File> list;

    public static void main(String[] args) {
        DateFileList listDemo=new DateFileList(new File("\\"));
        List<File> listOBject=listDemo.getList();
        System.out.println(listOBject.toString());



    }

    DateFileList(File file) {
        this.list = list(file);
    }

    public List<File> list(File file) {
        File [] array = file.listFiles(new FileFilter() {
            final long time = new GregorianCalendar(2015, 0, 1).getTimeInMillis();

            @Override
            public boolean accept(File file) {
                if (time > file.lastModified()) {
                    return false ;
                }
                return true;
            }
        });

        //Arrays.sort(array);
        return Arrays.asList(array);
    }

    public List<File> getList(){
        return this.list;
    }


}
