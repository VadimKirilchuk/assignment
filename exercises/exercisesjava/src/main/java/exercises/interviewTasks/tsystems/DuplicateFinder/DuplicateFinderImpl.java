package exercises.interviewTasks.tsystems.DuplicateFinder;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class provides methods to find duplicate strings at file and store
 * strings at target file with indication of occurrences.
 */
public class DuplicateFinderImpl implements DuplicateFinder {
    public static void main(String[] args) {
        DuplicateFinderImpl testObject = new DuplicateFinderImpl();
        System.out.println(testObject.process(new File("e:\\FIRST.txt"), new File("e:\\SECOND.txt")));
    }

    public DuplicateFinderImpl() {
    }

    /**
     * Processes the specified file and puts into another sorted and unique
     * lines each followed by number of occurrences.
     *
     * @param sourceFile file to be processed
     * @param targetFile output file; append if file exist, create if not.
     * @return {@code false} if there were any errors, otherwise {@code true}.
     */
    public boolean process(File sourceFile, File targetFile) {
        Map<String, Integer> map = new TreeMap<>();
        try {
            putDataToMap(map, sourceFile);
            writeDataToFile(map, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Stores strings at map and increment count for each string
     * in case if string repeats.
     *
     * @param map        to store unique strings
     * @param sourceFile specific file, where data stored
     * @throws IOException if any errors during reading data occurs
     */
    private void putDataToMap(Map<String, Integer> map, File sourceFile)
            throws IOException {
        //Integer countObject;
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        try {
            String string;
            while ((string = reader.readLine()) != null) {
             /*   countObject = map.put(string,1);
                if (countObject != null) {
                    map.put(string, ++countObject);
                }*/
                if (!map.containsKey(string)) {
                    map.put(string, 1);
                } else {
                    map.put(string, map.get(string) + 1);
                }
            }
        } finally {
            reader.close();
        }
    }

    /**
     * Writes data to target file.
     *
     * @param map
     * @param targetFile
     * @throws IOException if any errors during writing data occurs
     */
    private void writeDataToFile(Map<String, Integer> map, File targetFile)
            throws IOException {
        //  String lineSeparator = System.getProperty("line.separator");
        // we use bufferedwriter rather printwriter because we must handle exceptions
        BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true));
        try {
            for (Map.Entry<String, Integer> entity : map.entrySet()) {
                writer.write(entity.getKey() + "[" + entity.getValue() + "]");
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }
}