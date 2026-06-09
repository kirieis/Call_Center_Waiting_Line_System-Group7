package storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp xử lý đọc/ghi file cơ bản.
 * 
 * Hỗ trợ:
 * - Đọc tất cả dòng từ file
 * - Ghi đè toàn bộ file
 * - Thêm dòng vào cuối file
 * 
 * Sử dụng BufferedReader/BufferedWriter để tối ưu hiệu năng I/O.
 */
public class FileHandler {

    private String filePath;

    /**
     * Khởi tạo FileHandler với đường dẫn file.
     * @param filePath đường dẫn file (tương đối hoặc tuyệt đối)
     */
    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Đọc tất cả dòng từ file.
     * Tự động tạo file nếu chưa tồn tại.
     * 
     * @return danh sách các dòng, rỗng nếu file không tồn tại
     */
    public List<String> readLines() {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("  [!] Lỗi đọc file: " + e.getMessage());
        }

        return lines;
    }

    /**
     * Ghi đè toàn bộ nội dung file.
     * Tự động tạo thư mục cha nếu chưa tồn tại.
     * 
     * @param lines danh sách dòng cần ghi
     */
    public void writeLines(List<String> lines) {
        ensureParentDirectory();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("  [!] Lỗi ghi file: " + e.getMessage());
        }
    }

    /**
     * Thêm một dòng vào cuối file.
     * Tự động tạo file và thư mục nếu chưa tồn tại.
     * 
     * @param line dòng cần thêm
     */
    public void appendLine(String line) {
        ensureParentDirectory();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8"))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("  [!] Lỗi append file: " + e.getMessage());
        }
    }

    /**
     * Đảm bảo thư mục cha tồn tại.
     */
    private void ensureParentDirectory() {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    /**
     * Lấy đường dẫn file.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Kiểm tra file có tồn tại không.
     */
    public boolean exists() {
        return new File(filePath).exists();
    }
}
