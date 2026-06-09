package core;

import java.util.List;

/**
 * Interface hàng đợi tiêu chuẩn (FIFO).
 * 
 * Cung cấp các phép toán cơ bản cho hàng đợi:
 * enqueue, dequeue, peek, isEmpty, size, toList.
 * 
 * Được implement bởi PriorityCallQueue và CircularCallQueue.
 *
 * @param <T> Kiểu dữ liệu phần tử trong hàng đợi
 */
public interface StandardQueue<T> {

    /**
     * Thêm phần tử vào hàng đợi.
     * @param item phần tử cần thêm
     */
    void enqueue(T item);

    /**
     * Lấy và xóa phần tử đầu hàng đợi.
     * @return phần tử đầu hàng đợi
     * @throws java.util.NoSuchElementException nếu hàng đợi rỗng
     */
    T dequeue();

    /**
     * Xem phần tử đầu hàng đợi mà không xóa.
     * @return phần tử đầu hàng đợi
     * @throws java.util.NoSuchElementException nếu hàng đợi rỗng
     */
    T peek();

    /**
     * Kiểm tra hàng đợi có rỗng không.
     * @return true nếu hàng đợi rỗng
     */
    boolean isEmpty();

    /**
     * Lấy số lượng phần tử trong hàng đợi.
     * @return số phần tử
     */
    int size();

    /**
     * Chuyển hàng đợi thành danh sách (không thay đổi hàng đợi).
     * @return danh sách các phần tử theo thứ tự ưu tiên
     */
    List<T> toList();
}
