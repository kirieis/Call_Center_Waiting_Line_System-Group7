package storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Bộ sinh dữ liệu ngẫu nhiên cho hệ thống Call Center.
 * 
 * Sinh ra file CSV chứa N cuộc gọi ngẫu nhiên với:
 * - Tên khách hàng ngẫu nhiên (Việt Nam)
 * - Số điện thoại ngẫu nhiên (10 chữ số)
 * - Tỷ lệ VIP: ~15%
 * - Số lần gọi lại: 0-10
 * 
 * Output format: customerId,customerName,phoneNumber,isVIP,repeatCalls
 */
public class DataGenerator {

    private Random random;

    // Mảng tên họ Việt Nam phổ biến
    private static final String[] HO = {
            "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh",
            "Phan", "Vũ", "Võ", "Đặng", "Bùi", "Đỗ",
            "Hồ", "Ngô", "Dương", "Lý"
    };

    // Mảng tên đệm
    private static final String[] DEM = {
            "Văn", "Thị", "Đức", "Minh", "Hoàng", "Thanh",
            "Quốc", "Ngọc", "Hữu", "Phương", "Tuấn", "Bảo",
            "Kim", "Anh", "Công", "Trung"
    };

    // Mảng tên chính
    private static final String[] TEN = {
            "An", "Bình", "Chi", "Dũng", "Em", "Phúc",
            "Giang", "Hải", "Ích", "Khanh", "Linh", "Minh",
            "Nam", "Oanh", "Phong", "Quang", "Sơn", "Thảo",
            "Uyên", "Vinh", "Xuân", "Yến", "Đạt", "Hùng",
            "Long", "Tâm", "Trí", "Hoa", "Lan", "Mai"
    };

    public DataGenerator() {
        this.random = new Random();
    }

    /**
     * Sinh N cuộc gọi ngẫu nhiên và ghi vào file CSV.
     * 
     * @param count số cuộc gọi cần sinh (mặc định 10,000)
     * @param outputPath đường dẫn file CSV đầu ra
     */
    public void generate(int count, String outputPath) {
        FileHandler fileHandler = new FileHandler(outputPath);
        List<String> lines = new ArrayList<>();

        // Header
        lines.add("customerId,customerName,phoneNumber,isVIP,repeatCalls");

        for (int i = 1; i <= count; i++) {
            String customerId = String.format("KH%05d", i);
            String customerName = randomName();
            String phoneNumber = randomPhone();
            boolean isVIP = randomVIP();
            int repeatCalls = randomRepeatCalls();

            String line = String.join(",",
                    customerId,
                    customerName,
                    phoneNumber,
                    String.valueOf(isVIP),
                    String.valueOf(repeatCalls)
            );
            lines.add(line);
        }

        fileHandler.writeLines(lines);
        System.out.println("  [✓] Đã sinh " + count + " cuộc gọi ngẫu nhiên → " + outputPath);
    }

    /**
     * Sinh tên ngẫu nhiên theo kiểu Việt Nam (Họ + Đệm + Tên).
     */
    private String randomName() {
        String ho = HO[random.nextInt(HO.length)];
        String dem = DEM[random.nextInt(DEM.length)];
        String ten = TEN[random.nextInt(TEN.length)];
        return ho + " " + dem + " " + ten;
    }

    /**
     * Sinh số điện thoại ngẫu nhiên 10 chữ số (bắt đầu bằng 0).
     */
    private String randomPhone() {
        StringBuilder sb = new StringBuilder("0");
        // Đầu số phổ biến
        String[] dauSo = {"90", "91", "93", "94", "96", "97", "98", "86", "83", "84", "85", "88", "89"};
        sb = new StringBuilder("0" + dauSo[random.nextInt(dauSo.length)]);
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Sinh trạng thái VIP ngẫu nhiên (~15% tỷ lệ VIP).
     */
    private boolean randomVIP() {
        return random.nextInt(100) < 15;
    }

    /**
     * Sinh số lần gọi lại ngẫu nhiên (0-10).
     * Phân bố: 60% có 0 lần, 25% có 1-3 lần, 15% có 4-10 lần.
     */
    private int randomRepeatCalls() {
        int rand = random.nextInt(100);
        if (rand < 60) {
            return 0;
        } else if (rand < 85) {
            return 1 + random.nextInt(3);
        } else {
            return 4 + random.nextInt(7);
        }
    }
}
