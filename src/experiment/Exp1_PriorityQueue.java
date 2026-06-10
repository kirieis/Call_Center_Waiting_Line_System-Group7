package experiment;

import core.PriorityCallQueue;
import model.Call;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Experiment 1: Measure Priority Queue Performance.
 * 
 * Compares processing times between:
 * - PriorityCallQueue (Max-Heap): automatic priority sorting
 * - FIFO Queue (LinkedList): regular non-priority queue
 * 
 * Metrics:
 * - Time to enqueue 10,000 calls
 * - Time to dequeue 10,000 calls
 * - Total processing time
 */
public class Exp1_PriorityQueue {

    private static final int DATA_SIZE = 10000;

    public void run() {
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║  EXPERIMENT 1: PRIORITY QUEUE PERFORMANCE             ║");
        System.out.println("  ╠═══════════════════════════════════════════════════════╣");
        System.out.println("  ║  Data: " + DATA_SIZE + " random calls                      ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");

        // Generate test data
        List<Call> testData = generateTestData(DATA_SIZE);

        // --- Test PriorityCallQueue ---
        System.out.println("\n  [1] PriorityCallQueue (Max-Heap):");
        PriorityCallQueue pq = new PriorityCallQueue();

        long startEnqueue = System.nanoTime();
        for (Call call : testData) {
            pq.enqueue(call);
        }
        long endEnqueue = System.nanoTime();
        long enqueueTimeMs = (endEnqueue - startEnqueue) / 1_000_000;

        long startDequeue = System.nanoTime();
        int prevPriority = Integer.MAX_VALUE;
        boolean sortedCorrectly = true;
        while (!pq.isEmpty()) {
            Call call = pq.dequeue();
            if (call.getAgedPriority() > prevPriority) {
                sortedCorrectly = false;
            }
            prevPriority = call.getAgedPriority();
        }
        long endDequeue = System.nanoTime();
        long dequeueTimeMs = (endDequeue - startDequeue) / 1_000_000;

        System.out.println("      Enqueue " + DATA_SIZE + " items: " + enqueueTimeMs + " ms");
        System.out.println("      Dequeue " + DATA_SIZE + " items: " + dequeueTimeMs + " ms");
        System.out.println("      Total time: " + (enqueueTimeMs + dequeueTimeMs) + " ms");
        System.out.println("      Sorted correctly by priority: " + (sortedCorrectly ? "✓ Correct" : "✗ Incorrect"));

        // --- Test FIFO Queue ---
        System.out.println("\n  [2] FIFO Queue (LinkedList):");
        Queue<Call> fifo = new LinkedList<>();

        long startFifoEnq = System.nanoTime();
        for (Call call : testData) {
            fifo.add(call);
        }
        long endFifoEnq = System.nanoTime();
        long fifoEnqMs = (endFifoEnq - startFifoEnq) / 1_000_000;

        long startFifoDeq = System.nanoTime();
        while (!fifo.isEmpty()) {
            fifo.poll();
        }
        long endFifoDeq = System.nanoTime();
        long fifoDeqMs = (endFifoDeq - startFifoDeq) / 1_000_000;

        System.out.println("      Enqueue " + DATA_SIZE + " items: " + fifoEnqMs + " ms");
        System.out.println("      Dequeue " + DATA_SIZE + " items: " + fifoDeqMs + " ms");
        System.out.println("      Total time: " + (fifoEnqMs + fifoDeqMs) + " ms");

        // --- Conclusion ---
        System.out.println("\n  ─── CONCLUSION ───");
        System.out.println("  PriorityQueue Total: " + (enqueueTimeMs + dequeueTimeMs) + " ms");
        System.out.println("  FIFO Queue Total   : " + (fifoEnqMs + fifoDeqMs) + " ms");

        if ((enqueueTimeMs + dequeueTimeMs) > (fifoEnqMs + fifoDeqMs)) {
            long diff = (enqueueTimeMs + dequeueTimeMs) - (fifoEnqMs + fifoDeqMs);
            System.out.println("  → PriorityQueue is slower by " + diff + " ms due to heap sorting overhead.");
            System.out.println("  → However, PriorityQueue guarantees correct priority ordering!");
        } else {
            System.out.println("  → PriorityQueue performs comparably to FIFO.");
        }
        System.out.println("  ══════════════════════════════════════════════════════");
    }

    /**
     * Generates random test data.
     */
    private List<Call> generateTestData(int count) {
        List<Call> data = new ArrayList<>();
        Random random = new Random(42); // fixed seed for reproducibility

        for (int i = 0; i < count; i++) {
            boolean isVIP = random.nextInt(100) < 15;
            int repeatCalls = random.nextInt(11);
            Call call = new Call(
                    "TEST" + String.format("%05d", i),
                    "TestUser" + i,
                    "090" + String.format("%07d", random.nextInt(10000000)),
                    isVIP,
                    repeatCalls,
                    i + 1
            );
            data.add(call);
        }
        return data;
    }
}
