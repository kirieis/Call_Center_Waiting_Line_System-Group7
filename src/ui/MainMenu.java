package ui;

import core.CallProcessor;
import core.CallRouter;
import model.Call;
import model.CallStatus;
import storage.CallHistoryStore;
import storage.DataGenerator;
import experiment.Exp1_PriorityQueue;
import experiment.Exp2_AgingAlgorithm;
import experiment.Exp3_HistoryLookup;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Vòng lặp menu chính của hệ thống Call Center.
 * 
 * Cung cấp giao diện Console cho người vận hành (Operator) với các chức năng:
 * 1. Generate Random Data (10,000 cuộc gọi)
 * 2. Auto Sort & Load to Queue
 * 3. Add New Call Manually
 * 4. View Waiting Queue (Priority)
 * 5. Process Next Call
 * 6. View Call History
 * 7. Search Call History
 * 8. Apply Aging Algorithm
 * 9. View Circular Queue Status
 * 10. Run Experiments
 * 0. Exit
 */
public class MainMenu {

    private CallRouter router;
    private CallHistoryStore historyStore;
    private InputHandler input;
    private ConsoleRenderer renderer;
    private CallProcessor processor;
    private DataGenerator dataGen;
    private Properties config;

    // Đường dẫn mặc định
    private String rawDataPath;
    private String historyPath;
    private int circularCapacity;
    private int generateCount;

    public MainMenu() {
        loadConfig();
        this.router = new CallRouter(circularCapacity);
        this.historyStore = new CallHistoryStore(historyPath);
        this.input = new InputHandler();
        this.renderer = new ConsoleRenderer();
        this.processor = new CallProcessor();
        this.dataGen = new DataGenerator();
    }

    /**
     * Đọc cấu hình từ file settings.properties.
     */
    private void loadConfig() {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/config/settings.properties")) {
            config.load(fis);
        } catch (IOException e) {
            System.out.println("  [!] Không tìm thấy settings.properties, dùng giá trị mặc định.");
        }

        rawDataPath = config.getProperty("data.raw.calls.path", "data/CustomerCalls.csv");
        historyPath = config.getProperty("data.call.history.path", "data/call_history.csv");
        circularCapacity = Integer.parseInt(config.getProperty("circular.queue.capacity", "100"));
        generateCount = Integer.parseInt(config.getProperty("generator.default.count", "10000"));

        // Cấu hình priority scoring
        int vipBonus = Integer.parseInt(config.getProperty("priority.vip.bonus", "50"));
        int repeatMul = Integer.parseInt(config.getProperty("priority.repeat.multiplier", "10"));
        Call.setVipBonus(vipBonus);
        Call.setRepeatMultiplier(repeatMul);
    }

    /**
     * Chạy vòng lặp menu chính.
     */
    public void run() {
        boolean running = true;

        while (running) {
            display();
            int choice = input.readInt("  Nhập lựa chọn: ");
            running = handleChoice(choice);
        }

        input.close();
        System.out.println("\n  ★ Cảm ơn bạn đã sử dụng hệ thống! Tạm biệt. ★\n");
    }

    /**
     * Hiển thị menu chính.
     */
    public void display() {
        String[] options = {
                "1.  Sinh dữ liệu ngẫu nhiên (" + generateCount + " cuộc gọi)",
                "2.  Tự động sắp xếp & nạp vào hàng đợi",
                "3.  Thêm cuộc gọi mới (thủ công)",
                "4.  Xem hàng đợi chờ (Priority Queue)",
                "5.  Xử lý cuộc gọi tiếp theo",
                "6.  Xem lịch sử cuộc gọi",
                "7.  Tìm kiếm lịch sử cuộc gọi",
                "8.  Áp dụng thuật toán Aging",
                "9.  Xem trạng thái Circular Queue",
                "10. Chạy thực nghiệm (Experiments)",
                "",
                "0.  Thoát"
        };
        renderer.renderMenu(options);
    }

    /**
     * Xử lý lựa chọn từ menu.
     * @return false nếu người dùng chọn thoát
     */
    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                generateData();
                break;
            case 2:
                autoSortAndLoad();
                break;
            case 3:
                addNewCall();
                break;
            case 4:
                viewWaitingQueue();
                break;
            case 5:
                processNextCall();
                break;
            case 6:
                viewCallHistory();
                break;
            case 7:
                searchCallHistory();
                break;
            case 8:
                applyAging();
                break;
            case 9:
                viewCircularQueue();
                break;
            case 10:
                runExperiments();
                break;
            case 0:
                return false;
            default:
                renderer.renderMessage("Lựa chọn không hợp lệ. Vui lòng chọn 0-10.");
        }
        return true;
    }

    // ===== Chức năng 1: Sinh dữ liệu ngẫu nhiên =====
    private void generateData() {
        System.out.println("\n  ─── SINH DỮ LIỆU NGẪU NHIÊN ───");
        dataGen.generate(generateCount, rawDataPath);
    }

    // ===== Chức năng 2: Auto Sort & Load =====
    private void autoSortAndLoad() {
        System.out.println("\n  ─── TỰ ĐỘNG SẮP XẾP & NẠP HÀNG ĐỢI ───");
        processor.reset();
        processor.loadFromCSV(rawDataPath);

        // Gán queue đã sắp xếp cho router
        router.setPriorityQueue(processor.getQueue());
        renderer.renderMessage("Hàng đợi ưu tiên đã sẵn sàng. Tổng: " 
                + router.getQueueSize() + " cuộc gọi.");
    }

    // ===== Chức năng 3: Thêm cuộc gọi mới =====
    private void addNewCall() {
        System.out.println("\n  ─── THÊM CUỘC GỌI MỚI ───");

        String customerId = input.readString("  Nhập mã KH: ");
        String customerName = input.readString("  Nhập tên KH: ");
        String phoneNumber = input.readPhoneNumber();
        boolean isVIP = input.readBoolean("  Khách VIP?");
        int repeatCalls = input.readInt("  Số lần gọi lại: ");

        Call call = new Call(customerId, customerName, phoneNumber,
                isVIP, repeatCalls, router.getQueueSize() + 1);

        router.addCall(call);
        renderer.renderMessage("Đã thêm cuộc gọi thành công!");
        renderer.renderCall(call);
    }

    // ===== Chức năng 4: Xem hàng đợi =====
    private void viewWaitingQueue() {
        System.out.println("\n  ─── HÀNG ĐỢI CHỜ (PRIORITY QUEUE) ───");
        List<Call> snapshot = router.getQueueSnapshot();
        renderer.renderQueue(snapshot);
    }

    // ===== Chức năng 5: Xử lý cuộc gọi tiếp theo =====
    private void processNextCall() {
        System.out.println("\n  ─── XỬ LÝ CUỘC GỌI TIẾP THEO ───");
        Call call = router.processNext();

        if (call == null) {
            renderer.renderMessage("Không có cuộc gọi nào trong hàng đợi.");
            return;
        }

        renderer.renderMessage("Đang xử lý cuộc gọi:");
        renderer.renderCall(call);

        // Đánh dấu hoàn thành và lưu lịch sử
        call.setStatus(CallStatus.COMPLETED);
        historyStore.save(call);
        renderer.renderMessage("Cuộc gọi đã hoàn thành và lưu vào lịch sử.");
        renderer.renderMessage("Còn lại trong hàng đợi: " + router.getQueueSize());
    }

    // ===== Chức năng 6: Xem lịch sử =====
    private void viewCallHistory() {
        System.out.println("\n  ─── LỊCH SỬ CUỘC GỌI ───");
        List<Call> history = historyStore.loadAll();
        renderer.renderHistory(history);
    }

    // ===== Chức năng 7: Tìm kiếm lịch sử =====
    private void searchCallHistory() {
        System.out.println("\n  ─── TÌM KIẾM LỊCH SỬ CUỘC GỌI ───");
        String keyword = input.readString("  Nhập từ khóa (tên, SĐT, mã KH): ");
        List<Call> results = historyStore.search(keyword);

        if (results.isEmpty()) {
            renderer.renderMessage("Không tìm thấy cuộc gọi nào phù hợp với: \"" + keyword + "\"");
        } else {
            renderer.renderMessage("Tìm thấy " + results.size() + " kết quả:");
            renderer.renderQueue(results);
        }
    }

    // ===== Chức năng 8: Áp dụng Aging =====
    private void applyAging() {
        System.out.println("\n  ─── ÁP DỤNG THUẬT TOÁN AGING ───");
        if (router.isQueueEmpty()) {
            renderer.renderMessage("Hàng đợi rỗng. Không có gì để áp dụng aging.");
            return;
        }

        int sizeBefore = router.getQueueSize();
        router.applyAging();
        renderer.renderMessage("Đã áp dụng Aging Algorithm cho " + sizeBefore + " cuộc gọi.");
        renderer.renderMessage("Hàng đợi đã được cập nhật thứ tự ưu tiên mới.");
    }

    // ===== Chức năng 9: Xem Circular Queue =====
    private void viewCircularQueue() {
        System.out.println("\n  ─── TRẠNG THÁI CIRCULAR QUEUE ───");
        List<Call> circularCalls = router.getCircularQueueSnapshot();
        int capacity = router.getCircularQueue().getCapacity();
        renderer.renderCircularQueueStatus(circularCalls, capacity);
    }

    // ===== Chức năng 10: Chạy thực nghiệm =====
    private void runExperiments() {
        System.out.println("\n  ─── CHẠY THỰC NGHIỆM ───");
        String[] expOptions = {
                "1. Exp1 - Priority Queue Performance",
                "2. Exp2 - Aging Algorithm Test",
                "3. Exp3 - History Lookup Benchmark",
                "4. Chạy tất cả",
                "0. Quay lại"
        };

        for (String opt : expOptions) {
            System.out.println("  " + opt);
        }

        int expChoice = input.readInt("  Chọn thực nghiệm: ");

        switch (expChoice) {
            case 1:
                new Exp1_PriorityQueue().run();
                break;
            case 2:
                new Exp2_AgingAlgorithm().run();
                break;
            case 3:
                new Exp3_HistoryLookup().run();
                break;
            case 4:
                System.out.println("\n  === Chạy tất cả thực nghiệm ===\n");
                new Exp1_PriorityQueue().run();
                System.out.println();
                new Exp2_AgingAlgorithm().run();
                System.out.println();
                new Exp3_HistoryLookup().run();
                break;
            case 0:
                break;
            default:
                renderer.renderMessage("Lựa chọn không hợp lệ.");
        }
    }
}
