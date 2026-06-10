package core;

import model.Call;
import model.CallStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Circular Queue using a fixed-size array.
 * 
 * Caps the maximum number of waiting calls in the system.
 * When the queue is full, new calls will be marked as MISSED.
 * 
 * Uses front/rear pointers and modulo arithmetic to manage the queue.
 * 
 * Implements StandardQueue<Call>.
 */
public class CircularCallQueue implements StandardQueue<Call> {

    private Call[] elements;
    private int front;
    private int rear;
    private int capacity;
    private int count;

    /**
     * Initializes circular queue with default capacity of 100.
     */
    public CircularCallQueue() {
        this(100);
    }

    /**
     * Initializes circular queue with custom capacity.
     * @param capacity maximum number of elements
     */
    public CircularCallQueue(int capacity) {
        this.capacity = capacity;
        this.elements = new Call[capacity];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    /**
     * Enqueues a call to the circular queue.
     * If full, the call is marked as MISSED and not added.
     */
    @Override
    public void enqueue(Call call) {
        if (isFull()) {
            call.setStatus(CallStatus.MISSED);
            System.out.println("  [!] Circular queue is full! Call from " 
                    + call.getCustomerName() + " was MISSED.");
            return;
        }
        rear = (rear + 1) % capacity;
        elements[rear] = call;
        count++;
    }

    /**
     * Dequeues and returns the first call (FIFO).
     */
    @Override
    public Call dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Circular queue is empty!");
        }
        Call call = elements[front];
        elements[front] = null;
        front = (front + 1) % capacity;
        count--;
        return call;
    }

    /**
     * Peeks at the first call without removing it.
     */
    @Override
    public Call peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Circular queue is empty!");
        }
        return elements[front];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    /**
     * Checks if the circular queue is full.
     * @return true if full
     */
    public boolean isFull() {
        return count == capacity;
    }

    /**
     * Gets maximum capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Converts circular queue elements to a List (in FIFO order).
     */
    @Override
    public List<Call> toList() {
        List<Call> list = new ArrayList<>();
        if (isEmpty()) {
            return list;
        }
        int index = front;
        for (int i = 0; i < count; i++) {
            list.add(elements[index]);
            index = (index + 1) % capacity;
        }
        return list;
    }
}
