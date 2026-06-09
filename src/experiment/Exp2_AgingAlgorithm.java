package experiment;

import core.AgingAlgorithm;
import core.PriorityCallQueue;
import model.Call;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Thực nghiệm 2: Kiểm tra thuật toán Aging.
 * 
 * Mục tiêu:
 * - Chứng minh Aging Algorithm chống starvation hiệu quả
 * - Kiểm tra cuộc gọi thường (non-VIP, 0 repeat) có được 
 *   tăng ưu tiên sau nhiều vòng aging không
 * - Đo thời gian xử lý aging với số lượng data lớn
 * 
 * Kịch bản:
 * 1. Tạo 10,000 cuộc gọi: mix VIP và thường
 * 2. Chạy 5 vòng aging
 * 3. So sánh thứ tự ưu tiên trước/sau aging
 */
public class Exp2_AgingAlgorithm {

    private static final int DATA_SIZE = 10000;
    private static final int AGING_ROUNDS = 5;

    public void run() {
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║  THỰC NGHIỆM 2: AGING ALGORITHM                     ║");
        System.out.println("  ╠═══════════════════════════════════════════════════════╣");
        System.out.println("  ║  Dữ liệu: " + DATA_SIZE + " cuộc gọi | " + AGING_ROUNDS + " vòng aging             ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");

        // Sinh dữ liệu test
        List<Call> testData = generateTestData(DATA_SIZE);

        // Nạp vào PriorityQueue
        PriorityCallQueue pq = new PriorityCallQueue();
        for (Call call : testData) {
            pq.enqueue(call);
        }

        // Lưu top 5 trước aging
        System.out.println("\n  [Trước Aging] Top 5 cuộc gọi ưu tiên cao nhất:");
        printTop5(pq);

        // Tìm cuộc gọi có ưu tiên thấp nhất (thường, 0 repeat)
        Call lowestCall = findLowestPriorityCall(pq.getInternalList());
        System.out.println("\n  Cuộc gọi ưu tiên thấp nhất:");
        System.out.println("    → " + lowestCall);
        int initialPriority = lowestCall.getAgedPriority();

        // Chạy aging nhiều vòng
        AgingAlgorithm aging = new AgingAlgorithm(0, 5); // threshold=0 để test ngay

        System.out.println("\n  ─── Bắt đầu Aging (" + AGING_ROUNDS + " vòng) ───");
        long startTime = System.nanoTime();

        for (int round = 1; round <= AGING_ROUNDS; round++) {
            aging.applyAging(pq.getInternalList());
            pq.rebuildHeap();

            System.out.println("  Vòng " + round + ": Điểm thấp nhất trước đó = " 
                    + lowestCall.getAgedPriority());
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Kết quả sau aging
        System.out.println("\n  [Sau Aging] Top 5 cuộc gọi ưu tiên cao nhất:");
        printTop5(pq);

        System.out.println("\n  Cuộc gọi ban đầu thấp nhất:");
        System.out.println("    → Điểm ban đầu: " + initialPriority);
        System.out.println("    → Điểm sau aging: " + lowestCall.getAgedPriority());
        System.out.println("    → Tăng: +" + (lowestCall.getAgedPriority() - initialPriority) + " điểm");

        // --- Kết luận ---
        System.out.println("\n  ─── KẾT LUẬN ───");
        System.out.println("  Thời gian " + AGING_ROUNDS + " vòng aging cho " + DATA_SIZE + " cuộc gọi: " + durationMs + " ms");
        System.out.println("  Aging Algorithm đã tăng điểm ưu tiên cho cuộc gọi chờ lâu.");
        System.out.println("  → Chống starvation: cuộc gọi thường sẽ dần được phục vụ!");
        System.out.println("  ══════════════════════════════════════════════════════");
    }

    /**
     * In top 5 cuộc gọi có ưu tiên cao nhất.
     */
    private void printTop5(PriorityCallQueue pq) {
        List<Call> sorted = pq.toList();
        for (int i = 0; i < Math.min(5, sorted.size()); i++) {
            Call c = sorted.get(i);
            System.out.printf("    %d. [%s] %s | VIP: %s | Repeat: %d | Điểm: %d%n",
                    i + 1, c.getCustomerId(), c.getCustomerName(),
                    c.isVIP() ? "★" : "-", c.getRepeatCalls(), c.getAgedPriority());
        }
    }

    /**
     * Tìm cuộc gọi có ưu tiên thấp nhất.
     */
    private Call findLowestPriorityCall(List<Call> calls) {
        Call lowest = calls.get(0);
        for (Call call : calls) {
            if (call.getAgedPriority() < lowest.getAgedPriority()) {
                lowest = call;
            }
        }
        return lowest;
    }

    /**
     * Sinh dữ liệu test ngẫu nhiên.
     */
    private List<Call> generateTestData(int count) {
        List<Call> data = new ArrayList<>();
        Random random = new Random(123);

        for (int i = 0; i < count; i++) {
            boolean isVIP = random.nextInt(100) < 15;
            int repeatCalls = random.nextInt(11);
            Call call = new Call(
                    "AG" + String.format("%05d", i),
                    "AgingTest" + i,
                    "091" + String.format("%07d", random.nextInt(10000000)),
                    isVIP, repeatCalls, i + 1
            );
            // Đặt entryTime về quá khứ để aging có tác dụng
            call.setEntryTime(System.currentTimeMillis() - (long)(random.nextInt(120000)));
            data.add(call);
        }
        return data;
    }
}
