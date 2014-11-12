package executors;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created on 2014-11-05.
 */
public class PrefetchingIteratorTest {

    private static int initialFetchSize = 10;
    private static int nextFetchSize = 5;


    @Test
    public <T> void testInitialFetchDoneOnFirstCallToNext() {
        Iterator<T> mock = (Iterator<T>)Mockito.mock(Iterator.class);
        Iterator prefetchingIterator = new PrefetchingIterator<>(mock, initialFetchSize, nextFetchSize);
        Mockito.when(mock.hasNext()).thenReturn(true);
        // when next() called at least once
        prefetchingIterator.next();
        // then all initial prefetching is done. Exactly initialPrefetchSize number of records is fetched
        Mockito.verify(mock, Mockito.times(initialFetchSize)).next();
    }

    @Test
    public void afterInitialPrefetchNoCallsToNextWithinBufferSize() {
        Iterator<Integer> mock = (Iterator<Integer>)Mockito.mock(Iterator.class);
        Iterator prefetchingIterator = new PrefetchingIterator<>(mock, initialFetchSize, nextFetchSize);
        // given
        Mockito.when(mock.hasNext()).thenReturn(true);
        prefetchingIterator.next(); // load buffer

        // now block more calls to mock.next()
        Mockito.when(mock.next()).thenThrow(new IllegalStateException());

        // verify:
        // 1. we should ne able to call iterator.next() for the initialFetchSize number of times minus one (the already executed first next())
        try {
            for (int i=0;i<initialFetchSize-1;i++) {
                prefetchingIterator.next();
            }
        } catch (IllegalStateException e) {
            fail("Next should not be called on mock object yet, we should use the buffer");
        }

        // 2. any next call to mock.next() is prohibited
        try {
            prefetchingIterator.next();
            fail("next() on mock should be called"); // exception thrown to signal it
        } catch (IllegalStateException e) {}
    }

    @Test
    public void testActualContentForwarderCorrectly() {
        Iterator<Integer> mock = (Iterator<Integer>)Mockito.mock(Iterator.class);
        Iterator prefetchingIterator = new PrefetchingIterator<Integer>(mock, initialFetchSize, nextFetchSize);
        Mockito.when(mock.hasNext()).thenReturn(true);
        Mockito.when(mock.next()).thenReturn(new Integer(1)).thenReturn(new Integer(2));

        assertEquals(1, prefetchingIterator.next());
        assertEquals(2, prefetchingIterator.next());
    }

}
