package model;

/**
 * Enum đại diện cho các trạng thái của một cuộc gọi trong hệ thống.
 * 
 * - WAITING:    Cuộc gọi đang chờ trong hàng đợi
 * - PROCESSING: Cuộc gọi đang được xử lý bởi điện thoại viên
 * - COMPLETED:  Cuộc gọi đã được phục vụ xong
 * - MISSED:     Cuộc gọi bị lỡ (hàng đợi vòng đầy)
 */
public enum CallStatus {
    WAITING,
    PROCESSING,
    COMPLETED,
    MISSED
}
