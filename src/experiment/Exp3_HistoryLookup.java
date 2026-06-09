package experiment;

import model.Call;
import model.CallStatus;
import storage.CallHistoryStore;
import storage.FileHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Thực nghiệm 3: Benchmark tìm kiếm trong lịch sử cuộc gọi.
 * 
 * Mục tiêu:
 * - Đo thời gian tìm kiếm với dữ liệu lịch sử lớn
 * - So sánh tìm kiếm theo tên, SĐT, mã KH
 * - Đánh giá hiệu năng linear search trên file CSV
 * 
 * Kịch bản:
 * 1. Sinh 5,000 cuộc gọi lịch sử vào file tạm
 * 2. Tìm kiếm 100 lần với các từ khóa khác nhau
 * 3. Đo thời gian trung bình mỗi lần tìm kiếm
 */
public class Exp3_HistoryLookup {

    private static final int HISTORY_SIZE = 5000;
    private static final int SEARCH_ITERATIONS = 100;
    private static final String TEMP_FILE = "data/exp3_history_temp.csv";

    public void run() {
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");
        System.out.println("  ║  THỰC NGHIỆM 3: HISTORY LOOKUP BENCHMARK            ║");
        System.out.println("  ╠═══════════════════════════════════════════════════════╣");
        System.out.println("  ║  Dữ liệu: " + HISTORY_SIZE + " cuộc gọi lịch sử                 ║");
        System.out.println("  ║  Tìm kiếm: " + SEARCH_ITERATIONS + " lần lặp                              ║");
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");

        // Tạo dữ liệu lịch sử test
        System.out.println("\n  Đang sinh dữ liệu lịch sử test...");
        generateTestHistory();

        // Khởi tạo CallHistoryStore với file test
        CallHistoryStore store = new CallHistoryStore(TEMP_FILE);

        // --- Test 1: Load all ---
        System.out.println("\n  [1] Load toàn bộ lịch sử:");
        long startLoad = System.nanoTime();
        List<Call> all = store.loadAll();
        long endLoad = System.nanoTime();
        long loadMs = (endLoad - startLoad) / 1_000_000;
        System.out.println("      Loaded " + all.size() + " records trong " + loadMs + " ms");

        // --- Test 2: Tìm theo tên ---
        System.out.println("\n  [2] Tìm kiếm theo tên (\"Nguyễn\"):");
        long totalNameSearch = 0;
        int totalNameResults = 0;
        for (int i = 0; i < SEARCH_ITERATIONS; i++) {
            long start = System.nanoTime();
            List<Call> results = store.search("Nguyễn");
            long end = System.nanoTime();
            totalNameSearch += (end - start);
            totalNameResults = results.size();
        }
        long avgNameMs = totalNameSearch / SEARCH_ITERATIONS / 1_000_000;
        System.out.println("      Kết quả: " + totalNameResults + " records");
        System.out.println("      Thời gian TB: " + avgNameMs + " ms/lần");
        System.out.println("      Tổng " + SEARCH_ITERATIONS + " lần: " + (totalNameSearch / 1_000_000) + " ms");

        // --- Test 3: Tìm theo SĐT ---
        System.out.println("\n  [3] Tìm kiếm theo SĐT (\"090\"):");
        long totalPhoneSearch = 0;
        int totalPhoneResults = 0;
        for (int i = 0; i < SEARCH_ITERATIONS; i++) {
            long start = System.nanoTime();
            List<Call> results = store.search("090");
            long end = System.nanoTime();
            totalPhoneSearch += (end - start);
            totalPhoneResults = results.size();
        }
        long avgPhoneMs = totalPhoneSearch / SEARCH_ITERATIONS / 1_000_000;
        System.out.println("      Kết quả: " + totalPhoneResults + " records");
        System.out.println("      Thời gian TB: " + avgPhoneMs + " ms/lần");
        System.out.println("      Tổng " + SEARCH_ITERATIONS + " lần: " + (totalPhoneSearch / 1_000_000) + " ms");

        // --- Test 4: Tìm theo mã KH (exact match) ---
        System.out.println("\n  [4] Tìm kiếm theo mã KH (\"HS02500\"):");
        long totalIdSearch = 0;
        int totalIdResults = 0;
        for (int i = 0; i < SEARCH_ITERATIONS; i++) {
            long start = System.nanoTime();
            List<Call> results = store.search("HS02500");
            long end = System.nanoTime();
            totalIdSearch += (end - start);
            totalIdResults = results.size();
        }
        long avgIdMs = totalIdSearch / SEARCH_ITERATIONS / 1_000_000;
        System.out.println("      Kết quả: " + totalIdResults + " records");
        System.out.println("      Thời gian TB: " + avgIdMs + " ms/lần");
        System.out.println("      Tổng " + SEARCH_ITERATIONS + " lần: " + (totalIdSearch / 1_000_000) + " ms");

        // --- Kết luận ---
        System.out.println("\n  ─── KẾT LUẬN ───");
        System.out.println("  ┌─────────────────────┬──────────────┬────────────┐");
        System.out.println("  │ Loại tìm kiếm       │ TB (ms/lần)  │ Kết quả    │");
        System.out.println("  ├─────────────────────┼──────────────┼────────────┤");
        System.out.printf("  │ Theo tên            │ %-12d │ %-10d │%n", avgNameMs, totalNameResults);
        System.out.printf("  │ Theo SĐT            │ %-12d │ %-10d │%n", avgPhoneMs, totalPhoneResults);
        System.out.printf("  │ Theo mã KH          │ %-12d │ %-10d │%n", avgIdMs, totalIdResults);
        System.out.println("  └─────────────────────┴──────────────┴────────────┘");
        System.out.println("  → Linear search O(n) phù hợp cho dữ liệu vừa phải (<100K)");
        System.out.println("  → Với dữ liệu lớn hơn, cần index hoặc HashMap để tối ưu.");

        // Dọn dẹp
        cleanup();
        System.out.println("  ══════════════════════════════════════════════════════");
    }

    /**
     * Sinh dữ liệu lịch sử test vào file tạm.
     */
    private void generateTestHistory() {
        FileHandler fh = new FileHandler(TEMP_FILE);
        List<String> lines = new ArrayList<>();
        lines.add("customerId,customerName,phoneNumber,isVIP,repeatCalls,orderNumber,priorityScore,status");

        Random random = new Random(999);
        String[] hoList = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng"};
        String[] tenList = {"An", "Bình", "Chi", "Dũng", "Em", "Phúc", "Giang", "Hải"};

        for (int i = 0; i < HISTORY_SIZE; i++) {
            String id = "HS" + String.format("%05d", i);
            String name = hoList[random.nextInt(hoList.length)] + " Văn " + tenList[random.nextInt(tenList.length)];
            String phone = "090" + String.format("%07d", random.nextInt(10000000));
            boolean vip = random.nextInt(100) < 15;
            int repeat = random.nextInt(11);
            int priority = (vip ? 50 : 0) + repeat * 10;

            lines.add(String.join(",", id, name, phone, String.valueOf(vip),
                    String.valueOf(repeat), String.valueOf(i + 1),
                    String.valueOf(priority), "COMPLETED"));
        }

        fh.writeLines(lines);
        System.out.println("  Đã sinh " + HISTORY_SIZE + " records vào " + TEMP_FILE);
    }

    /**
     * Xóa file tạm sau khi test.
     */
    private void cleanup() {
        java.io.File tempFile = new java.io.File(TEMP_FILE);
        if (tempFile.exists()) {
            tempFile.delete();
            System.out.println("  [✓] Đã dọn dẹp file tạm: " + TEMP_FILE);
        }
    }
}
