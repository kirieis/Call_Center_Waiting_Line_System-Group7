package core;

import model.Call;
import java.util.List;

/**
 * Thuật toán Aging - Tăng điểm ưu tiên theo thời gian chờ.
 * 
 * Mục đích: Chống starvation (đói tài nguyên) cho các cuộc gọi 
 * có điểm ưu tiên thấp bằng cách tăng dần điểm khi chờ lâu.
 * 
 * Cơ chế:
 * - Mỗi lần gọi applyAging(), duyệt tất cả cuộc gọi đang chờ
 * - Nếu thời gian chờ vượt ngưỡng (agingThresholdMs), tăng waitTime
 * - Điểm ưu tiên tổng = basePriority + waitTime (tự động tính qua getAgedPriority())
 */
public class AgingAlgorithm {

    private long agingThresholdMs;
    private int agingBoost;

    /**
     * Khởi tạo với giá trị mặc định.
     * Threshold: 30 giây, Boost: +5 điểm.
     */
    public AgingAlgorithm() {
        this(30000, 5);
    }

    /**
     * Khởi tạo với giá trị tùy chỉnh.
     * @param agingThresholdMs ngưỡng thời gian (ms) trước khi boost
     * @param agingBoost số điểm tăng thêm mỗi lần
     */
    public AgingAlgorithm(long agingThresholdMs, int agingBoost) {
        this.agingThresholdMs = agingThresholdMs;
        this.agingBoost = agingBoost;
    }

    /**
     * Áp dụng aging cho danh sách cuộc gọi.
     * Duyệt từng cuộc gọi, nếu đã chờ quá ngưỡng thì tăng waitTime.
     * 
     * @param calls danh sách cuộc gọi đang chờ
     */
    public void applyAging(List<Call> calls) {
        long currentTime = System.currentTimeMillis();
        for (Call call : calls) {
            long waitDuration = currentTime - call.getEntryTime();
            if (waitDuration >= agingThresholdMs) {
                int boost = calculateBoost(call);
                call.incrementWaitTime(boost);
            }
        }
    }

    /**
     * Tính số điểm boost cho một cuộc gọi cụ thể.
     * Có thể mở rộng logic tùy theo yêu cầu nghiệp vụ.
     * 
     * @param call cuộc gọi cần tính boost
     * @return số điểm tăng thêm
     */
    public int calculateBoost(Call call) {
        // Boost cơ bản + thêm 1 điểm cho mỗi lần gọi lại (khuyến khích khách trung thành)
        return agingBoost + (call.getRepeatCalls() > 0 ? 1 : 0);
    }

    // --- Getters & Setters ---

    public long getAgingThresholdMs() {
        return agingThresholdMs;
    }

    public void setAgingThresholdMs(long agingThresholdMs) {
        this.agingThresholdMs = agingThresholdMs;
    }

    public int getAgingBoost() {
        return agingBoost;
    }

    public void setAgingBoost(int agingBoost) {
        this.agingBoost = agingBoost;
    }
}
