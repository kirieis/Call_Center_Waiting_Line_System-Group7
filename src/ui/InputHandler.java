package ui;

import java.util.Scanner;

/**
 * Xử lý nhận dữ liệu từ bàn phím với kiểm tra hợp lệ.
 * 
 * Cung cấp các phương thức đọc input an toàn:
 * - readInt: đọc số nguyên với prompt
 * - readString: đọc chuỗi với prompt
 * - readPhoneNumber: đọc SĐT 10 chữ số
 * - readBoolean: đọc yes/no
 */
public class InputHandler {

    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Đọc số nguyên từ bàn phím.
     * Lặp lại cho đến khi người dùng nhập đúng.
     * 
     * @param prompt thông báo hiển thị
     * @return số nguyên hợp lệ
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    /**
     * Đọc chuỗi từ bàn phím.
     * 
     * @param prompt thông báo hiển thị
     * @return chuỗi đã nhập (đã trim)
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Đọc số điện thoại 10 chữ số.
     * Kiểm tra: bắt đầu bằng 0, chỉ chứa chữ số, đúng 10 ký tự.
     * 
     * @return số điện thoại hợp lệ
     */
    public String readPhoneNumber() {
        while (true) {
            System.out.print("  Nhập SĐT (10 chữ số): ");
            String phone = scanner.nextLine().trim();
            if (phone.matches("^0\\d{9}$")) {
                return phone;
            }
            System.out.println("  [!] SĐT phải có 10 chữ số và bắt đầu bằng 0. VD: 0901234567");
        }
    }

    /**
     * Đọc giá trị boolean (y/n).
     * 
     * @param prompt thông báo hiển thị
     * @return true nếu 'y' hoặc 'yes', false nếu 'n' hoặc 'no'
     */
    public boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("  [!] Vui lòng nhập 'y' hoặc 'n'.");
        }
    }

    /**
     * Đóng Scanner.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
