package core;

import model.Call;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Hàng đợi ưu tiên sử dụng cấu trúc Max-Heap.
 * 
 * Cuộc gọi có điểm ưu tiên (aged priority) cao nhất sẽ được 
 * dequeue trước. Sử dụng ArrayList + thủ công siftUp/siftDown
 * để minh họa thuật toán Priority Queue.
 * 
 * Implements StandardQueue<Call>.
 */
public class PriorityCallQueue implements StandardQueue<Call> {

    private List<Call> heap;

    public PriorityCallQueue() {
        this.heap = new ArrayList<>();
    }

    /**
     * Thêm cuộc gọi vào hàng đợi ưu tiên.
     * Sau khi thêm, thực hiện siftUp để duy trì tính chất heap.
     */
    @Override
    public void enqueue(Call call) {
        heap.add(call);
        siftUp(heap.size() - 1);
    }

    /**
     * Lấy và xóa cuộc gọi có điểm ưu tiên cao nhất.
     * Swap root với phần tử cuối, xóa cuối, rồi siftDown.
     */
    @Override
    public Call dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Hàng đợi ưu tiên rỗng!");
        }
        Call max = heap.get(0);
        Call last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return max;
    }

    /**
     * Xem cuộc gọi có điểm ưu tiên cao nhất mà không xóa.
     */
    @Override
    public Call peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Hàng đợi ưu tiên rỗng!");
        }
        return heap.get(0);
    }

    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Trả về danh sách cuộc gọi theo thứ tự ưu tiên giảm dần.
     * Tạo bản sao và sắp xếp, không ảnh hưởng heap gốc.
     */
    @Override
    public List<Call> toList() {
        List<Call> sorted = new ArrayList<>(heap);
        sorted.sort((a, b) -> b.getAgedPriority() - a.getAgedPriority());
        return sorted;
    }

    /**
     * Đẩy phần tử tại index lên trên để duy trì Max-Heap.
     * So sánh với parent, nếu lớn hơn thì swap.
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).getAgedPriority() > heap.get(parentIndex).getAgedPriority()) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Đẩy phần tử tại index xuống dưới để duy trì Max-Heap.
     * So sánh với 2 children, swap với child lớn nhất nếu cần.
     */
    private void siftDown(int index) {
        int size = heap.size();
        while (true) {
            int largest = index;
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            if (left < size && heap.get(left).getAgedPriority() > heap.get(largest).getAgedPriority()) {
                largest = left;
            }
            if (right < size && heap.get(right).getAgedPriority() > heap.get(largest).getAgedPriority()) {
                largest = right;
            }

            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }

    /**
     * Hoán đổi 2 phần tử trong heap.
     */
    private void swap(int i, int j) {
        Call temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Xây dựng lại heap sau khi thay đổi priority (dùng cho aging).
     */
    public void rebuildHeap() {
        for (int i = heap.size() / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * Lấy danh sách tham chiếu trực tiếp (dùng cho AgingAlgorithm).
     */
    public List<Call> getInternalList() {
        return heap;
    }
}
