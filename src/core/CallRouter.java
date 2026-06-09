package core;

import model.Call;
import model.CallStatus;
import java.util.List;

/**
 * Bộ điều phối cuộc gọi - Trung tâm quản lý luồng cuộc gọi.
 * 
 * Chức năng:
 * - Nhận cuộc gọi mới → thêm vào cả PriorityQueue và CircularQueue
 * - Xử lý cuộc gọi tiếp theo (dequeue từ PriorityQueue)
 * - Áp dụng thuật toán Aging để tăng ưu tiên cho cuộc gọi chờ lâu
 * - Cung cấp snapshot trạng thái hàng đợi
 */
public class CallRouter {

    private PriorityCallQueue priorityQueue;
    private CircularCallQueue circularQueue;
    private AgingAlgorithm aging;

    /**
     * Khởi tạo CallRouter với các queue mặc định.
     */
    public CallRouter() {
        this.priorityQueue = new PriorityCallQueue();
        this.circularQueue = new CircularCallQueue();
        this.aging = new AgingAlgorithm();
    }

    /**
     * Khởi tạo CallRouter với CircularQueue capacity tùy chỉnh.
     */
    public CallRouter(int circularCapacity) {
        this.priorityQueue = new PriorityCallQueue();
        this.circularQueue = new CircularCallQueue(circularCapacity);
        this.aging = new AgingAlgorithm();
    }

    /**
     * Khởi tạo CallRouter với PriorityQueue có sẵn (từ CallProcessor).
     */
    public CallRouter(PriorityCallQueue existingQueue, int circularCapacity) {
        this.priorityQueue = existingQueue;
        this.circularQueue = new CircularCallQueue(circularCapacity);
        this.aging = new AgingAlgorithm();
    }

    /**
     * Thêm cuộc gọi mới vào hệ thống.
     * Cuộc gọi được thêm vào cả PriorityQueue (để xử lý ưu tiên)
     * và CircularQueue (để giới hạn tổng số cuộc gọi chờ).
     * 
     * @param call cuộc gọi cần thêm
     */
    public void addCall(Call call) {
        call.setStatus(CallStatus.WAITING);
        priorityQueue.enqueue(call);
        circularQueue.enqueue(call);
    }

    /**
     * Xử lý cuộc gọi có ưu tiên cao nhất.
     * Dequeue từ PriorityQueue, chuyển trạng thái sang PROCESSING.
     * 
     * @return cuộc gọi đã được xử lý, hoặc null nếu hàng đợi rỗng
     */
    public Call processNext() {
        if (priorityQueue.isEmpty()) {
            return null;
        }
        Call call = priorityQueue.dequeue();
        call.setStatus(CallStatus.PROCESSING);
        return call;
    }

    /**
     * Áp dụng thuật toán Aging lên tất cả cuộc gọi đang chờ.
     * Sau khi tăng waitTime, rebuild heap để cập nhật thứ tự.
     */
    public void applyAging() {
        List<Call> waitingCalls = priorityQueue.getInternalList();
        aging.applyAging(waitingCalls);
        priorityQueue.rebuildHeap();
    }

    /**
     * Lấy snapshot hàng đợi ưu tiên (sắp xếp theo priority giảm dần).
     */
    public List<Call> getQueueSnapshot() {
        return priorityQueue.toList();
    }

    /**
     * Lấy snapshot hàng đợi vòng (thứ tự FIFO).
     */
    public List<Call> getCircularQueueSnapshot() {
        return circularQueue.toList();
    }

    /**
     * Lấy PriorityCallQueue hiện tại.
     */
    public PriorityCallQueue getPriorityQueue() {
        return priorityQueue;
    }

    /**
     * Gán PriorityCallQueue mới (dùng khi load từ CallProcessor).
     */
    public void setPriorityQueue(PriorityCallQueue queue) {
        this.priorityQueue = queue;
    }

    /**
     * Lấy CircularCallQueue hiện tại.
     */
    public CircularCallQueue getCircularQueue() {
        return circularQueue;
    }

    /**
     * Lấy AgingAlgorithm hiện tại.
     */
    public AgingAlgorithm getAgingAlgorithm() {
        return aging;
    }

    /**
     * Kiểm tra tổng thể hàng đợi có rỗng không.
     */
    public boolean isQueueEmpty() {
        return priorityQueue.isEmpty();
    }

    /**
     * Lấy tổng số cuộc gọi đang chờ trong PriorityQueue.
     */
    public int getQueueSize() {
        return priorityQueue.size();
    }
}
