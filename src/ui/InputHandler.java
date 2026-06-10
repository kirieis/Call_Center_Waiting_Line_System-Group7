package ui;

import java.util.Scanner;

/**
 * Handles input from keyboard with validation.
 * 
 * Provides safe input reading methods:
 * - readInt: reads an integer with a prompt
 * - readString: reads a string with a prompt
 * - readPhoneNumber: reads a 10-digit phone number
 * - readBoolean: reads yes/no (y/n)
 */
public class InputHandler {

    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads an integer from the keyboard.
     * Loops until the user enters a valid integer.
     * 
     * @param prompt display message
     * @return valid integer
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a string from the keyboard.
     * 
     * @param prompt display message
     * @return entered string (trimmed)
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads a 10-digit phone number.
     * Validation: starts with 0, contains only digits, exactly 10 characters.
     * 
     * @return valid phone number
     */
    public String readPhoneNumber() {
        while (true) {
            System.out.print("  Enter phone number (10 digits): ");
            String phone = scanner.nextLine().trim();
            if (phone.matches("^0\\d{9}$")) {
                return phone;
            }
            System.out.println("  [!] Phone number must have 10 digits and start with 0. E.g., 0901234567");
        }
    }

    /**
     * Reads a boolean value (y/n).
     * 
     * @param prompt display message
     * @return true if 'y' or 'yes', false if 'n' or 'no'
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
            System.out.println("  [!] Please enter 'y' or 'n'.");
        }
    }

    /**
     * Closes the Scanner.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
