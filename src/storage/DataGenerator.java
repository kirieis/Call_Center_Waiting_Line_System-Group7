package storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Random data generator for the Call Center system.
 * 
 * Generates a CSV file containing N random calls with:
 * - Random customer name (Vietnamese naming convention, no accents)
 * - Random phone number (10 digits)
 * - VIP percentage: ~15%
 * - Number of repeat calls: 0-10
 * 
 * Output format: customerId,customerName,phoneNumber,isVIP,repeatCalls
 */
public class DataGenerator {

    private Random random;

    // Common Vietnamese last names (no accents)
    private static final String[] HO = {
            "Nguyen", "Tran", "Le", "Pham", "Hoang", "Huynh",
            "Phan", "Vu", "Vo", "Dang", "Bui", "Do",
            "Ho", "Ngo", "Duong", "Ly"
    };

    // Middle names (no accents)
    private static final String[] DEM = {
            "Van", "Thi", "Duc", "Minh", "Hoang", "Thanh",
            "Quoc", "Ngoc", "Huu", "Phuong", "Tuan", "Bao",
            "Kim", "Anh", "Cong", "Trung"
    };

    // First names (no accents)
    private static final String[] TEN = {
            "An", "Binh", "Chi", "Dung", "Em", "Phuc",
            "Giang", "Hai", "Ich", "Khanh", "Linh", "Minh",
            "Nam", "Oanh", "Phong", "Quang", "Son", "Thao",
            "Uyen", "Vinh", "Xuan", "Yen", "Dat", "Hung",
            "Long", "Tam", "Tri", "Hoa", "Lan", "Mai"
    };

    public DataGenerator() {
        this.random = new Random();
    }

    /**
     * Generates N random calls and writes them to a CSV file.
     * 
     * @param count number of calls to generate (default 10,000)
     * @param outputPath output CSV file path
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
        System.out.println("  [✓] Generated " + count + " random calls -> " + outputPath);
    }

    /**
     * Generates random name in Vietnamese format (Last name + Middle name + First name).
     */
    private String randomName() {
        String ho = HO[random.nextInt(HO.length)];
        String dem = DEM[random.nextInt(DEM.length)];
        String ten = TEN[random.nextInt(TEN.length)];
        return ho + " " + dem + " " + ten;
    }

    /**
     * Generates random 10-digit phone number (starts with 0).
     */
    private String randomPhone() {
        StringBuilder sb = new StringBuilder("0");
        // Common prefixes
        String[] dauSo = {"90", "91", "93", "94", "96", "97", "98", "86", "83", "84", "85", "88", "89"};
        sb = new StringBuilder("0" + dauSo[random.nextInt(dauSo.length)]);
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Generates random VIP status (~15% VIP rate).
     */
    private boolean randomVIP() {
        return random.nextInt(100) < 15;
    }

    /**
     * Generates random number of repeat calls (0-10).
     * Distribution: 60% have 0, 25% have 1-3, 15% have 4-10.
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
