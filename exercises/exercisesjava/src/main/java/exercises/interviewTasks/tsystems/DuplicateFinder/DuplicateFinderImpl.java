package exercises.interviewTasks.tsystems.DuplicateFinder;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;
/**
 *Description of this task is quite massive so i have loaded it to googl drive
 *
 */

public class DuplicateFinderImpl implements DuplicateFinder {
    public static void main(String [] args) {
        DuplicateFinderImpl testObject = new DuplicateFinderImpl();
        System.out.println(testObject.process(new File("e:\\firstfile.txt"), new File("e:\\secondfile.txt")));
    }
    public DuplicateFinderImpl() {
    }

    public boolean process(File sourceFile, File targetFile) {

        Map<String, Integer> map = new TreeMap<String, Integer>();
        try {
            putDataToMap(map, sourceFile);
            writeDataToFile(map, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void putDataToMap(Map<String, Integer> map, File sourceFile)
            throws IOException {
        BufferedReader reader = null;
        Integer countObject;
        String string;
        try {
            reader = new BufferedReader(new FileReader(sourceFile));
            while ((string = reader.readLine()) != null) {
                countObject = map.put(string, new Integer(1));
                if (countObject != null) {
                    map.put(string, ++countObject);
                }
            }
        } finally {
            reader.close();
        }
    }

    private void writeDataToFile(Map<String, Integer> map, File targetFile)
            throws FileNotFoundException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(targetFile, true));
            for (Map.Entry<String, Integer> entity : map.entrySet()) {
                writer.println(entity.getKey() + "[" + entity.getValue() + "]");
            }
        } finally {
            writer.close();
        }
    }
}