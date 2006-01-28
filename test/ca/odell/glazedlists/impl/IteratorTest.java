/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicobject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.impl;

// for being a JUnit test case
import junit.framework.*;
// the core Glazed Lists package
import ca.odell.glazedlists.*;
// standard collections
import java.util.*;

/**
 * This test verifies that the EventListIterator works.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
public class IteratorTest extends TestCase {

    /** for randomly choosing list indices */
    private Random random = new Random();

    /**
     * Tests to verify that the Iterator can iterate through the list both
     * forwards and backwards.
     */
    public void testIterateThrough() {
        // create a list of values
        BasicEventList originalList = new BasicEventList();
        for(int i = 0; i < 26; i++) {
            originalList.add(new Integer(i));
        }

        // iterate through that list forwards and add the results to a new list
        List forwardsControlList = new ArrayList();
        for(Iterator i = originalList.iterator(); i.hasNext(); ) {
            forwardsControlList.add(i.next());
        }

        // verify the lists are equal
        assertEquals(originalList, forwardsControlList);

        // iterate through that list backwards and add the results to a new list
        List backwardsControlList = new ArrayList();
        for(ListIterator i = originalList.listIterator(originalList.size()); i.hasPrevious(); ) {
            backwardsControlList.add(i.previous());
        }
        Collections.reverse(backwardsControlList);

        // verify the lists are equal
        assertEquals(originalList, backwardsControlList);
    }

    /**
     * Tests to verify that the ListIterator can iterate through the list
     * while removes are performed directly on the list.
     */
    public void testIterateWithExternalRemove() {
        // create a list of values
        BasicEventList deleteFromList = new BasicEventList();
        ArrayList originalList = new ArrayList();
        for(int i = 0; i < 100; i++) {
            Object value = new Integer(i);
            deleteFromList.add(value);
            originalList.add(value);
        }

        List iteratedElements = new ArrayList();
        Iterator iterator = deleteFromList.listIterator();

        // iterate through the list forwards for the first 50 values
        for(int a = 0; a < 50; a++) {
            iteratedElements.add(iterator.next());
        }
        // delete 50 elements from the beginning of the list
        for(int a = 50; a > 0; a--) {
            deleteFromList.remove(random.nextInt(a));
        }
        // continue iterating for the last 50 values
        for(int a = 0; a < 50; a++) {
            iteratedElements.add(iterator.next());
        }

        // verify the lists are equal and that we're out of elements
        assertEquals(originalList, iteratedElements);
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests to verify that the EventListIterator and the SimpleIterator
     * can iterate through the list and remove its elements as it goes without
     * incident.
     */
    public void testIterateWithInternalRemove() {
        // create a list of values
        BasicEventList iterateForwardList = new BasicEventList();
        BasicEventList iterateBackwardList = new BasicEventList();
        ArrayList originalList = new ArrayList();
        for(int i = 0; i < 20; i++) {
            Integer value = new Integer(random.nextInt(100));
            iterateForwardList.add(value);
            iterateBackwardList.add(value);
            originalList.add(value);
        }

        // walk through the forward lists, removing all values greater than 50
        for(ListIterator i = iterateForwardList.listIterator(); i.hasNext(); ) {
            Integer current = (Integer)i.next();
            if(current.intValue() > 50) i.remove();
        }

        // walk through the backward list, removing all values greater than 50
        for(ListIterator i = iterateBackwardList.listIterator(iterateBackwardList.size()); i.hasPrevious(); ) {
            Integer current = (Integer)i.previous();
            if(current.intValue() > 50) i.remove();
        }

        // verify the lists are equal and that we're out of elements
        for(int i = 0; i < originalList.size(); ) {
            Integer current = (Integer)originalList.get(i);
            if(current.intValue() > 50) originalList.remove(i);
            else i++;
        }
        assertEquals(originalList, iterateForwardList);
        assertEquals(originalList, iterateBackwardList);
    }

    /**
     * Tests the edge condition of the previous method
     */
    public void testPreviousEdgeCondition() {
        // create a list of values
        BasicEventList iterationList = new BasicEventList();
        for(int i = 0; i < 20; i++) {
            Integer value = new Integer(random.nextInt(100));
            iterationList.add(value);
        }

        ListIterator i = iterationList.listIterator();

        // Test before next is called
        assertEquals(false, i.hasPrevious());
        try {
            i.previous();
            fail("A call to previous() was allowed before next() was called");
        } catch(Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());

        }

        // Test when the iterator is at the first element
        i.next();
        assertEquals(true, i.hasPrevious());
    }

    /**
     * Tests that changing the underlying list externally to the ListIterator
     * doesn't break the expectation of the remove operation.
     */
    public void testRemoveAfterInsertAtCursor() {
        BasicEventList testList = new BasicEventList();
        String hello = "Hello, world.";
        String bye = "Goodbye, cruel world.";
        String end = "the end";
        testList.add(bye);
        testList.add(end);
        ListIterator iterator = testList.listIterator();

        // move iterator to bye
        iterator.next();
        testList.add(0, hello);
        iterator.remove();
        assertEquals(false, testList.contains(bye));
    }

    /**
     * Tests that changing the underlying list externally to the ListIterator
     * doesn't break the expectation of the remove operation.
     */
    public void testRemoveAfterInsertAtNext() {
        BasicEventList testList = new BasicEventList();
        String hello = "Hello, world.";
        String bye = "Goodbye, cruel world.";
        String end = "the end";
        testList.add(bye);
        testList.add(end);
        ListIterator iterator = testList.listIterator();

        // move iterator to bye
        iterator.next();
        testList.add(1, hello);
        iterator.remove();
        assertEquals(false, testList.contains(bye));
    }

    /**
     * Tests that changing the underlying list externally to the ListIterator
     * doesn't break the expectation of the remove operation.
     */
    public void testRemoveAfterInsertAtCursorReverse() {
        BasicEventList testList = new BasicEventList();
        String hello = "Hello, world.";
        String bye = "Goodbye, cruel world.";
        String end = "the end";
        testList.add(end);
        testList.add(bye);
        ListIterator iterator = testList.listIterator(testList.size());

        // move iterator to bye
        iterator.previous();
        testList.add(1, hello);
        iterator.remove();
        assertEquals(false, testList.contains(bye));
    }

    /**
     * Tests that changing the underlying list externally to the ListIterator
     * doesn't break the expectation of the remove operation.
     */
    public void testRemoveAfterInsertAtPrevious() {
        BasicEventList testList = new BasicEventList();
        String hello = "Hello, world.";
        String bye = "Goodbye, cruel world.";
        String end = "the end";
        testList.add(end);
        testList.add(bye);
        ListIterator iterator = testList.listIterator(testList.size());

        // move iterator to bye
        iterator.previous();
        testList.add(0, hello);
        iterator.remove();
        assertEquals(false, testList.contains(bye));
    }

    /**
     * Tests that adding at a particular element does the right thing.
     */
    public void testAdding() {
        // Create a control list to test against
        ArrayList controlList = new ArrayList();
        controlList.add("zero");
        controlList.add("one");
        controlList.add("two");
        controlList.add("three");
        controlList.add("four");

        // Create a list to be iterated on forwards
        BasicEventList forwardsList = new BasicEventList();
        forwardsList.add("one");
        forwardsList.add("three");

        // Iterate through the list forwards adding in places of interest
        ListIterator iterator = forwardsList.listIterator(0);
        iterator.add("zero");
        assertEquals("one", (String)iterator.next());
        iterator.add("two");
        assertEquals("three", (String)iterator.next());
        iterator.add("four");
        assertEquals(controlList, forwardsList);

        // Create a list to be iterated of backwards
        BasicEventList backwardsList = new BasicEventList();
        backwardsList.add("one");
        backwardsList.add("three");

        // Iterate through the list backwards adding in places of interest
        ListIterator backwardsIterator = backwardsList.listIterator(backwardsList.size());
        backwardsIterator.add("four");
        assertEquals("four", (String)backwardsIterator.previous());
        assertEquals("three", (String)backwardsIterator.previous());
        backwardsIterator.add("two");
        assertEquals("two", (String)backwardsIterator.previous());
        assertEquals("one", (String)backwardsIterator.previous());
        backwardsIterator.add("zero");
        assertEquals("zero", (String)backwardsIterator.previous());
        assertEquals(false, backwardsIterator.hasPrevious());
        assertEquals(controlList, backwardsList);
    }

    /**
     * Tests empty list adds
     */
    public void testEmptyListAdding() {
        List testList = new BasicEventList();
        ListIterator iterator = testList.listIterator();
        iterator.add("just one element");
        assertEquals(false, iterator.hasNext());
        assertEquals(true, iterator.hasPrevious());
    }

    /**
     * Tests that the EventListIterator responds correctly to adding on an
     * empty list from an external call to remove().
     */
    public void testExternalAddingOnEmptyList() {
        BasicEventList testList = new BasicEventList();
        ListIterator iterator = testList.listIterator();
        assertEquals(false, iterator.hasPrevious());
        assertEquals(false, iterator.hasNext());

        // add one element to validate the iterator responds accordingly
        String hello = "hello, world";
        testList.add(hello);
        assertEquals(true, iterator.hasPrevious());
        assertEquals(false, iterator.hasNext());
        assertEquals(hello, iterator.previous());
        assertEquals(false, iterator.hasPrevious());
        assertEquals(true, iterator.hasNext());
    }

    /**
     * This manually executed test runs forever creating iterators and
     * sublists of a source list, and modifying that list.
     */
    public static void main(String[] args) {
        List list = new BasicEventList();
        long memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024);
        int repetitions = 0;
        Random random = new Random();

        while(true) {
            // perform a random operation on this list
            int operation = random.nextInt(3);
            int index = list.isEmpty() ? 0 : random.nextInt(list.size());
            if(operation <= 1 || list.isEmpty()) {
                list.add(index, new Integer(random.nextInt()));
            } else if(operation == 2) {
                list.remove(index);
            } else if(operation == 3) {
                list.set(index, new Integer(random.nextInt()));
            }

            // test and output memory usage
            long newMemoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024);
            if(newMemoryUsage > memoryUsage) {
                memoryUsage = newMemoryUsage;
                System.out.println(repetitions + ": " + memoryUsage + "k, HIGHER");
            } else if(repetitions % 10000 == 0) {
                System.out.println(repetitions + ": " + newMemoryUsage + "k");
            }

            repetitions++;
        }
    }
}