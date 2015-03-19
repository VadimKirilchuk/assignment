package exercises.interviewTasks.tsystems.Subsequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class SubsequenceImpl implements Subsequence {
    public static void main(String[] args) {
        List<Integer> firstList = new ArrayList<>();
        firstList.add(1);
        firstList.add(2);
        firstList.add(3);
        List<Integer> secondList = new ArrayList<>();
        secondList.add(55);
        secondList.add(1);
        secondList.add(46);
        secondList.add(2);
        secondList.add(29);
        secondList.add(3);
        System.out.println(new SubsequenceImpl().find(firstList, secondList));
    }

    public SubsequenceImpl() {

    }

    @Override
    public boolean find(List x, List y) {
        // check if Lis x and List y are the same link
        if (x == y) {
            return true;
        } else {
            //if List x is null link or List y is null or size of y < size of x, then return false
            if ((x == null || y == null) || (y.size() < x.size())) {
                return false;
            } else {
                //if count of identical elements==size of x, then return true? otherwise return false
                return x.size() == checkIdenticalElements(x, y);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private int checkIdenticalElements(List x, List y) {
        int identicalElementsCount = 0;
        int xListSize = x.size();
        Iterator xListIterator = x.iterator();
        Iterator yListIterator = y.iterator();
        Object xEntity = xListIterator.next();

        // loop roll until we search all elements from x in y or until y has elements
        while ((identicalElementsCount != xListSize) && (yListIterator.hasNext())) {
            Object yEntity = yListIterator.next();

            //if x element is null, we can't use equals
            if (xEntity == null) {
                if (xEntity == yEntity) {
                    identicalElementsCount++;
                    if (xListIterator.hasNext()) {
                        xEntity = xListIterator.next();
                    }
                }
            } else {
                if (xEntity.equals(yEntity)) {
                    identicalElementsCount++;
                    if (xListIterator.hasNext()) {
                        xEntity = xListIterator.next();
                    }
                }
            }
        }
        return identicalElementsCount;
    }
}
