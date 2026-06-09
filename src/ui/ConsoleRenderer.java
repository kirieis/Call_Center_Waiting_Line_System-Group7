package ui;

import model.Call;
import java.util.List;

/**
 * Lớp hiển thị giao diện Console.
 * 
 * Chịu trách nhiệm render:
 * - Menu chính
 * - Bảng danh sách cuộc gọi (có phân trang)
 * - Chi tiết cuộc gọi
 * - Thông báo hệ thống
 */
public class ConsoleRenderer {

    private static final int PAGE_SIZE = 20;
    private static final String LINE_SEPARATOR = "═══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final String THIN_SEPARATOR = "───────────────────────────────────────────────────────────────────────────────────────────────────";

    /**
     * Hiển thị danh sách cuộc gọi trong bảng, có phân trang.
     * 
     * @param calls danh sách cuộc gọi
     */
    public void renderQueue(List<Call> calls) {
        if (calls == null || calls.isEmpty()) {
            renderMessage("Không có cuộc gọi nào trong hàng đợi.");
            return;
        }

        System.out.println();
        System.out.println("  " + LINE_SEPARATOR);
        System.out.printf("  ║ %-5s │ %-8s │ %-22s │ %-12s │ %-4s │ %-7s │ %-7s │ %-10s ║%n",
                "STT", "Mã KH", "Tên KH", "SĐT", "VIP", "Gọi lại", "Điểm", "Trạng thái");
        System.out.println("  " + LINE_SEPARATOR);

        for (int i = 0; i < calls.size(); i++) {
            Call call = calls.get(i);
            System.out.printf("  ║ %-5d │ %-8s │ %-22s │ %-12s │ %-4s │ %-7d │ %-7d │ %-10s ║%n",
                    (i + 1),
                    truncate(call.getCustomerId(), 8),
                    truncate(call.getCustomerName(), 22),
                    call.getPhoneNumber(),
                    call.isVIP() ? " ★" : " -",
                    call.getRepeatCalls(),
                    call.getAgedPriority(),
                    call.getStatus());

            // Phân trang mỗi PAGE_SIZE dòng
            if ((i + 1) % PAGE_SIZE == 0 && i < calls.size() - 1) {
                System.out.println("  " + THIN_SEPARATOR);
                System.out.printf("  ║ --- Trang %d/%d --- Hiển thị %d/%d cuộc gọi ---%n",
                        (i + 1) / PAGE_SIZE, (int) Math.ceil((double) calls.size() / PAGE_SIZE),
                        (i + 1), calls.size());
                System.out.println("  " + THIN_SEPARATOR);
            }
        }

        System.out.println("  " + LINE_SEPARATOR);
        System.out.println("  Tổng: " + calls.size() + " cuộc gọi");
        System.out.println();
    }

    /**
     * Hiển thị chi tiết một cuộc gọi.
     */
    public void renderCall(Call call) {
        if (call == null) {
            renderMessage("Không có thông tin cuộc gọi.");
            return;
        }

        System.out.println();
        System.out.println("  ╔═══════════════════════════════════╗");
        System.out.println("  ║       CHI TIẾT CUỘC GỌI          ║");
        System.out.println("  ╠═══════════════════════════════════╣");
        System.out.printf("  ║  Mã KH    : %-20s  ║%n", call.getCustomerId());
        System.out.printf("  ║  Tên KH   : %-20s  ║%n", truncate(call.getCustomerName(), 20));
        System.out.printf("  ║  SĐT      : %-20s  ║%n", call.getPhoneNumber());
        System.out.printf("  ║  VIP      : %-20s  ║%n", call.isVIP() ? "★ Có" : "- Không");
        System.out.printf("  ║  Gọi lại  : %-20d  ║%n", call.getRepeatCalls());
        System.out.printf("  ║  Điểm ƯT  : %-20d  ║%n", call.getAgedPriority());
        System.out.printf("  ║  Trạng thái: %-19s  ║%n", call.getStatus());
        System.out.println("  ╚═══════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Hiển thị lịch sử cuộc gọi (dùng lại renderQueue).
     */
    public void renderHistory(List<Call> calls) {
        System.out.println("\n  ╔═════════════════════════════════════════╗");
        System.out.println("  ║         LỊCH SỬ CUỘC GỌI               ║");
        System.out.println("  ╚═════════════════════════════════════════╝");
        renderQueue(calls);
    }

    /**
     * Hiển thị thông báo hệ thống.
     */
    public void renderMessage(String msg) {
        System.out.println("\n  >> " + msg);
    }

    /**
     * Hiển thị menu từ danh sách tùy chọn.
     */
    public void renderMenu(String[] options) {
        System.out.println();
        System.out.println("  ╔═════════════════════════════════════════════════════════╗");
        System.out.println("  ║       CALL CENTER WAITING LINE SYSTEM                  ║");
        System.out.println("  ║       Hệ thống quản lý hàng đợi cuộc gọi              ║");
        System.out.println("  ╠═════════════════════════════════════════════════════════╣");

        for (String option : options) {
            System.out.printf("  ║   %-52s  ║%n", option);
        }

        System.out.println("  ╚═════════════════════════════════════════════════════════╝");
    }

    /**
     * Hiển thị trạng thái hàng đợi vòng (Circular Queue).
     */
    public void renderCircularQueueStatus(List<Call> calls, int capacity) {
        System.out.println("\n  ╔═════════════════════════════════════════╗");
        System.out.println("  ║      TRẠNG THÁI HÀNG ĐỢI VÒNG         ║");
        System.out.println("  ╠═════════════════════════════════════════╣");
        System.out.printf("  ║  Dung lượng : %d / %d                  ║%n", calls.size(), capacity);
        System.out.printf("  ║  Còn trống  : %d                       ║%n", capacity - calls.size());
        System.out.printf("  ║  Tỷ lệ lấp  : %.1f%%                   ║%n",
                (calls.size() * 100.0 / capacity));
        System.out.println("  ╚═════════════════════════════════════════╝");

        if (!calls.isEmpty()) {
            renderQueue(calls);
        }
    }

    /**
     * Xóa màn hình console.
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Cắt chuỗi nếu quá dài.
     */
    private String truncate(String str, int maxLen) {
        if (str == null) return "";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 2) + "..";
    }
}
