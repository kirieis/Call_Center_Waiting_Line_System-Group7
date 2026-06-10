package main;

import ui.MainMenu;

/**
 * Main application entry class - Call Center Waiting Line System.
 * 
 * The system simulates the process of managing call queues,
 * prioritizing VIP customers or customers with high repeat calls.
 * 
 * Data structures used:
 * - Queue (StandardQueue interface)
 * - Priority Queue (PriorityCallQueue - Max Heap)
 * - Circular Queue (CircularCallQueue - Fixed Array)
 * 
 * Runs on Console only.
 * 
 * @author Group 7 - Nguyen Van An
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
        System.out.println("  │  WAITING LINE SYSTEM - Call Queue System            │");
        System.out.println("  │  Version: 1.0  |  Group 7 - Nguyen Van An           │");
        System.out.println("  └─────────────────────────────────────────────────────┘");
        System.out.println();

        MainMenu menu = new MainMenu();
        menu.run();
    }
}
