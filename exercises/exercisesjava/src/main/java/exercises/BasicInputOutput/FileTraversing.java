package exercises.BasicInputOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class FileTraversing implements Comparator<File> {
    public static void main(String[] args) {
        //System.out.println(traverse(new File("e:\\art")));
        showFileInformation(new File("e:\\LR"));
    }

    //traversing directory to get general size of directory
    public static long traverse(File file) {
        long sumSize = 0;
        if (!file.exists()) {
            return 0;
        } else {
            if (!file.isDirectory()) {
                return file.length();
            }
        }
        File[] fileMembers = file.listFiles();
        for (File member : fileMembers) {
            if (member.isFile()) {
                sumSize += member.length();
            } else {
                sumSize += traverse(member);
            }
        }
        return sumSize;
    }

    //getting List of files, sort them and demonstrate file name,size and modif date
    public static void showFileInformation(File file) {
        List<File> list = traverseArrayList(file);
        Collections.sort(list, new FileTraversing());
        for (File member : list) {
            System.out.println(member.getName() + " size " + member.length() + " modif date " + member.lastModified());
        }
    }

    //recursively traversing directory to get ArrayList filled file instances
    public static List<File> traverseArrayList(File file) {
        List<File> list = new ArrayList<>();
        if (!file.exists()) {
            return null;
        } else {
            if (!file.isDirectory()) {
                list.add(file);
                return list;
            }
        }
        File[] fileMembers = file.listFiles();
        for (File member : fileMembers) {
            if (member.isFile()) {
                list.add(member);
            } else {
                list.addAll(traverseArrayList(member));
            }
        }
        return list;
    }

    @Override
    public int compare(File f1, File f2) {
        String f1Name = f1.getName();
        String f2Name = f2.getName();
        return f1Name.compareTo(f2Name);
    }
}
