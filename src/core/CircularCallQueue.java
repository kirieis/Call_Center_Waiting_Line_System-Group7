package core;

import model.Call;
import model.CallStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Hàng đợi vòng (Circular Queue) sử dụng mảng cố định.
 * 
 * Giới hạn số cuộc gọi chờ tối đa trong hệ thống.
 * Khi hàng đợi đầy, cuộc gọi mới sẽ bị đánh dấu MISSED.
 * 
 * Sử dụng con trỏ front/rear và phép modulo để quản lý vòng tròn.
 * 
 * Implements StandardQueue<Call>.
 */
public class CircularCallQueue implements StandardQueue<Call> {

    private Call[] elements;
    private int front;
    private int rear;
    private int capacity;
    private int count;

    /**
     * Khởi tạo hàng đợi vòng với dung lượng mặc định 100.
     */
    public CircularCallQueue() {
        this(100);
    }

    /**
     * Khởi tạo hàng đợi vòng với dung lượng tùy chỉnh.
     * @param capacity số phần tử tối đa
     */
    public CircularCallQueue(int capacity) {
        this.capacity = capacity;
        this.elements = new Call[capacity];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    /**
     * Thêm cuộc gọi vào hàng đợi vòng.
     * Nếu đầy, cuộc gọi bị đánh dấu MISSED và không được thêm.
     */
    @Override
    public void enqueue(Call call) {
        if (isFull()) {
            call.setStatus(CallStatus.MISSED);
            System.out.println("  [!] Hàng đợi vòng đầy! Cuộc gọi của " 
                    + call.getCustomerName() + " bị MISSED.");
            return;
        }
        rear = (rear + 1) % capacity;
        elements[rear] = call;
        count++;
    }

    /**
     * Lấy và xóa cuộc gọi đầu hàng đợi (FIFO).
     */
    @Override
    public Call dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Hàng đợi vòng rỗng!");
        }
        Call call = elements[front];
        elements[front] = null;
        front = (front + 1) % capacity;
        count--;
        return call;
    }

    /**
     * Xem cuộc gọi đầu hàng đợi mà không xóa.
     */
    @Override
    public Call peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Hàng đợi vòng rỗng!");
        }
        return elements[front];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    /**
     * Kiểm tra hàng đợi vòng có đầy không.
     * @return true nếu đầy
     */
    public boolean isFull() {
        return count == capacity;
    }

    /**
     * Lấy dung lượng tối đa.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Chuyển hàng đợi vòng thành danh sách (theo thứ tự FIFO).
     */
    @Override
    public List<Call> toList() {
        List<Call> list = new ArrayList<>();
        if (isEmpty()) {
            return list;
        }
        int index = front;
        for (int i = 0; i < count; i++) {
            list.add(elements[index]);
            index = (index + 1) % capacity;
        }
        return list;
    }
}
