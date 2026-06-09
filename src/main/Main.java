package main;

import ui.MainMenu;

/**
 * Lớp khởi chạy ứng dụng chính - Call Center Waiting Line System.
 * 
 * Hệ thống giả lập quá trình quản lý cuộc gọi chờ,
 * ưu tiên khách hàng VIP hoặc khách hàng gọi lại nhiều lần.
 * 
 * Cấu trúc dữ liệu sử dụng:
 * - Queue (StandardQueue interface)
 * - Priority Queue (PriorityCallQueue - Max Heap)
 * - Circular Queue (CircularCallQueue - Fixed Array)
 * 
 * Chạy trên Console only.
 * 
 * @author Group 7 - Nguyễn Văn An
 */
public class Main {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("  ██████╗ █████╗ ██╗     ██╗          ██████╗███████╗███╗   ██╗████████╗███████╗██████╗ ");
        System.out.println("  ██╔════╝██╔══██╗██║     ██║         ██╔════╝██╔════╝████╗  ██║╚══██╔══╝██╔════╝██╔══██╗");
        System.out.println("  ██║     ███████║██║     ██║         ██║     █████╗  ██╔██╗ ██║   ██║   █████╗  ██████╔╝");
        System.out.println("  ██║     ██╔══██║██║     ██║         ██║     ██╔══╝  ██║╚██╗██║   ██║   ██╔══╝  ██╔══██╗");
        System.out.println("  ╚██████╗██║  ██║███████╗███████╗    ╚██████╗███████╗██║ ╚████║   ██║   ███████╗██║  ██║");
        System.out.println("   ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝     ╚═════╝╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚══════╝╚═╝  ╚═╝");
        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────────────────┐");
        System.out.println("  │  WAITING LINE SYSTEM - Hệ thống hàng đợi cuộc gọi  │");
        System.out.println("  │  Phiên bản: 1.0  |  Group 7 - Nguyễn Văn An        │");
        System.out.println("  └─────────────────────────────────────────────────────┘");
        System.out.println();

        MainMenu menu = new MainMenu();
        menu.run();
    }
}
