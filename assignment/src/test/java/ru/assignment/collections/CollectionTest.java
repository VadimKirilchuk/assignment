package ru.assignment.collections;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class CollectionTest {
    ArrayList collection;

    @Before
    public void initialization() {
        collection = new ArrayList();
    }

    @Test
    @Parameters
    public void addElementTest(List list, int i) {
        collection.addAll(list);

        assertTrue(collection.contains(i));
    }

    private Object[] parametersForAddElementTest() {
        return $($(Arrays.asList(3, 2, 1), 3),
                $(Arrays.asList(3, 2, 1), 2),
                $(Arrays.asList(3, 2, 1), 1));
    }

    @Test
    @Parameters
    public void shuffleElementTest(List list) {
        collection.addAll(list);
        Collections.shuffle(collection);

        assertNotEquals(collection, list);
    }

    private Object[] parametersForShuffleElementTest() {
        return $(Arrays.asList(7, 6, 5, 4, 3, 2, 1));
    }

    @Test
    @Parameters
    public void searchElementTest(List list, int element) {
        collection.addAll(list);
        Collections.sort(collection);
        int index = Collections.binarySearch(collection, element);

        assertEquals(collection.get(index), element);
    }

    private Object[] parametersForSearchElementTest() {
        return $(Arrays.asList(1, 3, 2), 1);
    }

    @Test
    @Parameters
    public void swapElementTest(List list, int indexFrom, int indexTo) {
        collection.addAll(list);
        Object objectFirst = collection.get(indexFrom);
        Object objectSecond = collection.get(indexTo);
        Collections.swap(collection, indexFrom, indexTo);

        assertEquals(collection.get(indexFrom), objectSecond);
        assertEquals(collection.get(indexTo), objectFirst);
    }

    private Object[] parametersForSwapElementTest() {
        return $(Arrays.asList(1, 3, 2), 0, 2);
    }

    @Test
    @Parameters
    public void minElementTest(List list, int minElement) {
        collection.addAll(list);

        assertEquals(minElement, Collections.min(collection));
    }

    private Object[] parametersForMinElementTest() {
        return $(Arrays.asList(1, 3, 2), 1);
    }

    @Test
    @Parameters
    public void maxElementTest(List list, int maxElement) {
        collection.addAll(list);

        assertEquals(maxElement, Collections.max(collection));
    }

    private Object[] parametersForMaxElementTest() {
        return $(Arrays.asList(1, 3, 12), 12);
    }

    @Test
    @Parameters
    public void fewMaxElementTest(List list, int maxValuesAmount, int expectedMaxElement2,
                                  int expectedMaxElement1) {
        collection.addAll(list);
        Collections.shuffle(collection);
        Collections.sort(collection);

        assertEquals(expectedMaxElement1, collection.get(collection.size() - 1));
        assertEquals(expectedMaxElement2, collection.get(collection.size() - 2));
    }

    private Object[] parametersForFewMaxElementTest() {
        return $(Arrays.asList(1, 3, 12, 15, 7, 31), 2, 15, 31);
    }

    @Test
    @Parameters
    public void findSequenceTest(List list, int maxSequence) {
        collection.addAll(list);

        Iterator iterator = collection.iterator();
        int maxElementsCount = 1;
        int count = 1;
        Object firstObject = iterator.next();

        while (iterator.hasNext()) {
            Object secondObject = iterator.next();
            if (firstObject.equals(secondObject)) {
                count++;
            } else {
                System.out.println("count " + count);
                if (count > maxElementsCount) {
                    maxElementsCount = count;
                }
                firstObject = secondObject;
                count = 1;
            }
        }
        if (count > maxElementsCount) {
            maxElementsCount = count;
        }

        assertEquals(maxSequence, maxElementsCount);
    }

    private Object[] parametersForFindSequenceTest() {
        return $(Arrays.asList("1", "3", "3", "4", "5", "5", "5"), 3);
    }

    @Test
    @Parameters
    public void WordsTest(List list, int countOfUniqueWords, int firstWordCount,
                          String firstWord, int secondWordCount, String secondWord,
                          int thirdWordCount, String thirdWord) {
        collection.addAll(list);
        Map<String, Integer> map = new HashMap<>(collection.size());
        for (Object object : collection) {
            Integer value = map.put((String) object, 1);
            if (value != null) {
                map.put((String) object, ++value);
            }
        }
        assertEquals(countOfUniqueWords, map.size());
        assertEquals(firstWordCount, (int) map.get(firstWord));    // is it beautiful solution, i don't think so
        assertEquals(secondWordCount, (int) map.get(secondWord));
        assertEquals(thirdWordCount, (int) map.get(thirdWord));
    }

    private Object[] parametersForWordsTest() {
        return $(Arrays.asList("a", "b", "c", "a", "a", "b", "b", "c", "b"),
                3, 3, "a", 4, "b", 2, "c");
    }
}
