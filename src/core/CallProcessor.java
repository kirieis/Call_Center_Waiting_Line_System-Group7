package core;

import model.Call;
import model.CallStatus;
import storage.FileHandler;
import java.util.List;

/**
 * Bộ xử lý cuộc gọi - Đọc CSV thô, tính điểm ưu tiên và 
 * tự động nạp vào PriorityCallQueue.
 * 
 * Luồng xử lý:
 * 1. Đọc file CSV (CustomerCalls.csv) qua FileHandler
 * 2. Parse từng dòng thành đối tượng Call
 * 3. Tính điểm ưu tiên cho mỗi Call
 * 4. Enqueue vào PriorityCallQueue (tự động sắp xếp)
 */
public class CallProcessor {

    private FileHandler fileHandler;
    private PriorityCallQueue queue;

    public CallProcessor() {
        this.queue = new PriorityCallQueue();
    }

    /**
     * Đọc file CSV và nạp cuộc gọi vào hàng đợi ưu tiên.
     * 
     * Format CSV: customerId,customerName,phoneNumber,isVIP,repeatCalls
     * 
     * @param path đường dẫn file CSV
     */
    public void loadFromCSV(String path) {
        this.fileHandler = new FileHandler(path);
        List<String> lines = fileHandler.readLines();

        if (lines.isEmpty()) {
            System.out.println("  [!] File CSV rỗng hoặc không tồn tại.");
            return;
        }

        // Bỏ qua dòng header (nếu có)
        int startIndex = 0;
        if (lines.get(0).toLowerCase().contains("customerid") 
                || lines.get(0).toLowerCase().contains("customer_id")) {
            startIndex = 1;
        }

        int orderCounter = 1;
        int loadedCount = 0;

        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String customerId = parts[0].trim();
                String customerName = parts[1].trim();
                String phoneNumber = parts[2].trim();
                boolean isVIP = Boolean.parseBoolean(parts[3].trim());
                int repeatCalls = Integer.parseInt(parts[4].trim());

                Call call = new Call(customerId, customerName, phoneNumber,
                        isVIP, repeatCalls, orderCounter++);

                // Tính và gán điểm ưu tiên
                int priority = calculatePriority(call);
                call.setPriorityScore(priority);

                queue.enqueue(call);
                loadedCount++;
            } catch (Exception e) {
                System.out.println("  [!] Lỗi parse dòng " + (i + 1) + ": " + e.getMessage());
            }
        }

        System.out.println("  [✓] Đã nạp " + loadedCount + " cuộc gọi vào hàng đợi ưu tiên.");
    }

    /**
     * Tính điểm ưu tiên cho một cuộc gọi.
     * Công thức: (VIP ? 50 : 0) + (repeatCalls * 10)
     * 
     * @param call cuộc gọi cần tính
     * @return điểm ưu tiên
     */
    public int calculatePriority(Call call) {
        return call.getBasePriority();
    }

    /**
     * Lấy hàng đợi ưu tiên đã được nạp dữ liệu.
     */
    public PriorityCallQueue getQueue() {
        return queue;
    }

    /**
     * Reset hàng đợi (dùng khi load lại dữ liệu mới).
     */
    public void reset() {
        this.queue = new PriorityCallQueue();
    }
}
