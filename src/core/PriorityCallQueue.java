package core;

import model.Call;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Priority queue using a Max-Heap structure.
 * 
 * The call with the highest priority score (aged priority) will be 
 * dequeued first. Uses ArrayList + manual siftUp/siftDown 
 * to demonstrate the Priority Queue algorithm.
 * 
 * Implements StandardQueue<Call>.
 */
public class PriorityCallQueue implements StandardQueue<Call> {

    private List<Call> heap;

    public PriorityCallQueue() {
        this.heap = new ArrayList<>();
    }

    /**
     * Enqueues a call to the priority queue.
     * Performs siftUp after addition to maintain heap properties.
     */
    @Override
    public void enqueue(Call call) {
        heap.add(call);
        siftUp(heap.size() - 1);
    }

    /**
     * Dequeues and returns the call with the highest priority.
     * Swaps root with last element, removes last, then performs siftDown.
     */
    @Override
    public Call dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty!");
        }
        Call max = heap.get(0);
        Call last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return max;
    }

    /**
     * Peeks at the highest priority call without removing it.
     */
    @Override
    public Call peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty!");
        }
        return heap.get(0);
    }

    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Returns a copy of calls in descending priority order.
     * Does not affect the original heap structure.
     */
    @Override
    public List<Call> toList() {
        List<Call> sorted = new ArrayList<>(heap);
        sorted.sort((a, b) -> b.getAgedPriority() - a.getAgedPriority());
        return sorted;
    }

    /**
     * Moves the element at index up to maintain Max-Heap properties.
     * Compares with parent and swaps if greater.
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).getAgedPriority() > heap.get(parentIndex).getAgedPriority()) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Moves the element at index down to maintain Max-Heap properties.
     * Compares with children and swaps with the largest child if needed.
     */
    private void siftDown(int index) {
        int size = heap.size();
        while (true) {
            int largest = index;
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            if (left < size && heap.get(left).getAgedPriority() > heap.get(largest).getAgedPriority()) {
                largest = left;
            }
            if (right < size && heap.get(right).getAgedPriority() > heap.get(largest).getAgedPriority()) {
                largest = right;
            }

            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }

    /**
     * Swaps two elements in the heap.
     */
    private void swap(int i, int j) {
        Call temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Rebuilds the heap after priority updates (used for aging).
     */
    public void rebuildHeap() {
        for (int i = heap.size() / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * Gets direct reference list (used by AgingAlgorithm).
     */
    public List<Call> getInternalList() {
        return heap;
    }
}
