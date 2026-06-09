package model;

/**
 * Lớp đại diện cho một cuộc gọi trong hệ thống Call Center.
 * 
 * Mỗi cuộc gọi chứa thông tin khách hàng, trạng thái ưu tiên,
 * và các thuộc tính phục vụ tính điểm ưu tiên (Priority Score).
 * 
 * Công thức tính điểm ưu tiên cơ bản:
 *   priorityScore = (isVIP ? VIP_BONUS : 0) + (repeatCalls * REPEAT_MULTIPLIER) + waitTime
 */
public class Call {

    // --- Thuộc tính ---
    private String customerId;
    private String customerName;
    private String phoneNumber;
    private boolean isVIP;
    private int repeatCalls;
    private int orderNumber;
    private int waitTime;
    private int priorityScore;
    private CallStatus status;
    private long entryTime;

    // --- Hằng số tính điểm ưu tiên (mặc định, có thể override qua config) ---
    private static int VIP_BONUS = 50;
    private static int REPEAT_MULTIPLIER = 10;

    /**
     * Constructor đầy đủ.
     */
    public Call(String customerId, String customerName, String phoneNumber,
                boolean isVIP, int repeatCalls, int orderNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.isVIP = isVIP;
        this.repeatCalls = repeatCalls;
        this.orderNumber = orderNumber;
        this.waitTime = 0;
        this.status = CallStatus.WAITING;
        this.entryTime = System.currentTimeMillis();
        this.priorityScore = calculateBasePriority();
    }

    /**
     * Tính điểm ưu tiên cơ bản (không bao gồm aging).
     * VIP +50, mỗi lần gọi lại +10.
     */
    private int calculateBasePriority() {
        int score = 0;
        if (isVIP) {
            score += VIP_BONUS;
        }
        score += repeatCalls * REPEAT_MULTIPLIER;
        return score;
    }

    /**
     * Lấy điểm ưu tiên cơ bản (chưa tính waitTime).
     */
    public int getBasePriority() {
        return calculateBasePriority();
    }

    /**
     * Lấy điểm ưu tiên đã tính aging (bao gồm waitTime).
     */
    public int getAgedPriority() {
        return priorityScore + waitTime;
    }

    /**
     * Tăng thời gian chờ lên 1 đơn vị.
     */
    public void incrementWaitTime() {
        this.waitTime++;
    }

    /**
     * Tăng thời gian chờ theo giá trị tùy chỉnh.
     */
    public void incrementWaitTime(int amount) {
        this.waitTime += amount;
    }

    // --- Cấu hình tĩnh cho điểm ưu tiên ---
    public static void setVipBonus(int bonus) {
        VIP_BONUS = bonus;
    }

    public static void setRepeatMultiplier(int multiplier) {
        REPEAT_MULTIPLIER = multiplier;
    }

    // --- Getters & Setters ---

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public int getRepeatCalls() {
        return repeatCalls;
    }

    public void setRepeatCalls(int repeatCalls) {
        this.repeatCalls = repeatCalls;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(int priorityScore) {
        this.priorityScore = priorityScore;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | SĐT: %s | VIP: %s | Gọi lại: %d | Điểm: %d | TT: %s",
                customerId, customerName, phoneNumber,
                isVIP ? "Có" : "Không", repeatCalls,
                getAgedPriority(), status);
    }
}
