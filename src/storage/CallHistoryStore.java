package storage;

import model.Call;
import model.CallStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý lịch sử cuộc gọi đã xử lý thành công.
 * 
 * Chức năng:
 * - Lưu cuộc gọi đã hoàn thành vào file CSV
 * - Đọc toàn bộ lịch sử cuộc gọi
 * - Tìm kiếm cuộc gọi theo từ khóa (tên, SĐT, mã KH)
 * 
 * Format CSV: customerId,customerName,phoneNumber,isVIP,repeatCalls,orderNumber,priorityScore,status
 */
public class CallHistoryStore {

    private FileHandler fileHandler;

    /**
     * Khởi tạo với đường dẫn file lịch sử mặc định.
     */
    public CallHistoryStore() {
        this("data/call_history.csv");
    }

    /**
     * Khởi tạo với đường dẫn file tùy chỉnh.
     */
    public CallHistoryStore(String filePath) {
        this.fileHandler = new FileHandler(filePath);
        initFileIfNeeded();
    }

    /**
     * Tạo header CSV nếu file chưa tồn tại.
     */
    private void initFileIfNeeded() {
        if (!fileHandler.exists()) {
            List<String> header = new ArrayList<>();
            header.add("customerId,customerName,phoneNumber,isVIP,repeatCalls,orderNumber,priorityScore,status");
            fileHandler.writeLines(header);
        }
    }

    /**
     * Lưu cuộc gọi đã xử lý vào file lịch sử.
     * Tự động đánh dấu trạng thái COMPLETED.
     * 
     * @param call cuộc gọi cần lưu
     */
    public void save(Call call) {
        call.setStatus(CallStatus.COMPLETED);
        fileHandler.appendLine(toCSV(call));
    }

    /**
     * Đọc toàn bộ lịch sử cuộc gọi từ file.
     * 
     * @return danh sách cuộc gọi đã xử lý
     */
    public List<Call> loadAll() {
        List<Call> calls = new ArrayList<>();
        List<String> lines = fileHandler.readLines();

        // Bỏ qua header
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                try {
                    Call call = fromCSV(line);
                    if (call != null) {
                        calls.add(call);
                    }
                } catch (Exception e) {
                    // Bỏ qua dòng lỗi
                }
            }
        }

        return calls;
    }

    /**
     * Tìm kiếm cuộc gọi theo từ khóa.
     * Tìm trong: tên khách hàng, số điện thoại, mã khách hàng.
     * 
     * @param keyword từ khóa tìm kiếm (không phân biệt hoa/thường)
     * @return danh sách cuộc gọi phù hợp
     */
    public List<Call> search(String keyword) {
        List<Call> all = loadAll();
        List<Call> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Call call : all) {
            if (call.getCustomerName().toLowerCase().contains(lowerKeyword)
                    || call.getPhoneNumber().contains(keyword)
                    || call.getCustomerId().toLowerCase().contains(lowerKeyword)) {
                results.add(call);
            }
        }

        return results;
    }

    /**
     * Chuyển đối tượng Call thành chuỗi CSV.
     */
    private String toCSV(Call call) {
        return String.join(",",
                call.getCustomerId(),
                call.getCustomerName(),
                call.getPhoneNumber(),
                String.valueOf(call.isVIP()),
                String.valueOf(call.getRepeatCalls()),
                String.valueOf(call.getOrderNumber()),
                String.valueOf(call.getPriorityScore()),
                call.getStatus().name()
        );
    }

    /**
     * Parse chuỗi CSV thành đối tượng Call.
     */
    private Call fromCSV(String line) {
        String[] parts = line.split(",");
        if (parts.length < 8) return null;

        Call call = new Call(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                Boolean.parseBoolean(parts[3].trim()),
                Integer.parseInt(parts[4].trim()),
                Integer.parseInt(parts[5].trim())
        );
        call.setPriorityScore(Integer.parseInt(parts[6].trim()));
        call.setStatus(CallStatus.valueOf(parts[7].trim()));

        return call;
    }
}
