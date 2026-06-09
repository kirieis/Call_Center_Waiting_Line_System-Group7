package experiment;

import core.PriorityCallQueue;
import model.Call;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Thực nghiệm 1: Đo hiệu năng Priority Queue.
 * 
 * So sánh thời gian xử lý giữa:
 * - PriorityCallQueue (Max-Heap): sắp xếp ưu tiên tự động
 * - FIFO Queue (LinkedList): hàng đợi thông thường không ưu tiên
 * 
 * Metrics:
 * - Thời gian enqueue 10,000 cuộc gọi
 * - Thời gian dequeue 10,000 cuộc gọi
 * - Tổng thời gian xử lý
 */
public class Exp1_PriorityQueue {

    private static final int DATA_SIZE = 10000;

    public void run() {
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║  THỰC NGHIỆM 1: PRIORITY QUEUE PERFORMANCE          ║");
        System.out.println("  ╠═══════════════════════════════════════════════════════╣");
        System.out.println("  ║  Dữ liệu: " + DATA_SIZE + " cuộc gọi ngẫu nhiên              ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");

        // Sinh dữ liệu test
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
        System.out.println("      Tổng thời gian: " + (enqueueTimeMs + dequeueTimeMs) + " ms");
        System.out.println("      Sắp xếp đúng thứ tự ưu tiên: " + (sortedCorrectly ? "✓ Đúng" : "✗ Sai"));

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
        System.out.println("      Tổng thời gian: " + (fifoEnqMs + fifoDeqMs) + " ms");

        // --- Kết luận ---
        System.out.println("\n  ─── KẾT LUẬN ───");
        System.out.println("  PriorityQueue tổng: " + (enqueueTimeMs + dequeueTimeMs) + " ms");
        System.out.println("  FIFO Queue tổng   : " + (fifoEnqMs + fifoDeqMs) + " ms");

        if ((enqueueTimeMs + dequeueTimeMs) > (fifoEnqMs + fifoDeqMs)) {
            long diff = (enqueueTimeMs + dequeueTimeMs) - (fifoEnqMs + fifoDeqMs);
            System.out.println("  → PriorityQueue chậm hơn " + diff + " ms do chi phí sắp xếp heap.");
            System.out.println("  → Tuy nhiên, PriorityQueue đảm bảo thứ tự ưu tiên chính xác!");
        } else {
            System.out.println("  → PriorityQueue hoạt động hiệu quả tương đương FIFO.");
        }
        System.out.println("  ══════════════════════════════════════════════════════");
    }

    /**
     * Sinh dữ liệu test ngẫu nhiên.
     */
    private List<Call> generateTestData(int count) {
        List<Call> data = new ArrayList<>();
        Random random = new Random(42); // seed cố định để tái lập

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
