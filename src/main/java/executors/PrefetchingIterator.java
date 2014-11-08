package executors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created on 2014-11-05.
 */
public class PrefetchingIterator<E> implements Iterator<E>  {

    private Queue<E> buffer = new LinkedList<>();
    private Iterator<E> iterator;
    private int nextFetchSize;
    private int initialFetchSize;
    private boolean isFirstFetch = true;

    public PrefetchingIterator(Iterator<E> iterator, int initialFetchSize, int nextFetchSize) {
        this.iterator = iterator;
        this.initialFetchSize = initialFetchSize;
        this.nextFetchSize = nextFetchSize;
    }

    @Override
    public boolean hasNext() {
        return (!buffer.isEmpty() || iterator.hasNext()); // there is something in the buffer or in the underlying iterator
    }

    @Override
    public E next() {
        if (buffer.isEmpty()) { // buffer empty, fill it
            int recordsToLoad = (isFirstFetch)? initialFetchSize : nextFetchSize;
            isFirstFetch = false;
            while (iterator.hasNext() && (recordsToLoad-- > 0)) {
                buffer.offer(iterator.next());
            }
        }
        // there are elements in the buffer
        return buffer.poll();
    }

}
