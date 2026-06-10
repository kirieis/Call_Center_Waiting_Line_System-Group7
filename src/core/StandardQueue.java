package core;

import java.util.List;

/**
 * Standard Queue interface (FIFO).
 * 
 * Provides basic operations for a queue:
 * enqueue, dequeue, peek, isEmpty, size, toList.
 * 
 * Implemented by PriorityCallQueue and CircularCallQueue.
 *
 * @param <T> Data type of elements in the queue
 */
public interface StandardQueue<T> {

    /**
     * Enqueues an item into the queue.
     * @param item element to add
     */
    void enqueue(T item);

    /**
     * Dequeues and returns the first element of the queue.
     * @return front element of the queue
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    T dequeue();

    /**
     * Peeks at the first element of the queue without removing it.
     * @return front element of the queue
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    T peek();

    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty
     */
    boolean isEmpty();

    /**
     * Gets the number of elements in the queue.
     * @return number of elements
     */
    int size();

    /**
     * Converts the queue to a List (without modifying the queue).
     * @return list of elements in priority order
     */
    List<T> toList();
}
