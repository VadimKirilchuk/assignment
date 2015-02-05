/**
 * Create a class called SortedDirList with a constructor that takes a File object
 * and builds a sorted directory list from the files at that File.
 * Add to this class two overloaded list( ) methods: the first produces the whole list,
 * and the second produces the subset of the list that matches its argument
 */

package exercises;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class sortedDirList {
    private List<String> list;

    public static void main(String[] args) {
sortedDirList listDemo=new sortedDirList(new File("\\"));
        List<String> listOBject=listDemo.getList();
        System.out.println(listOBject.toString());



    }

    sortedDirList(File file) {
        this.list = list(file);
    }

    public List<String> list(File file) {
        String[] array = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.length()>5){
                    return false;
                }
                    return true;
            }
        });

        Arrays.sort(array);
        return Arrays.asList(array);
    }

    public List<String> getList(){
        return this.list;
    }
}
