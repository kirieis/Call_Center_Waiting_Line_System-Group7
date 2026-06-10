package core;

import model.Call;
import model.CallStatus;
import storage.FileHandler;
import java.util.List;

/**
 * Call Processor - Reads raw CSV, calculates priority scores, and
 * automatically loads them into PriorityCallQueue.
 * 
 * Execution flow:
 * 1. Reads CSV file (CustomerCalls.csv) via FileHandler
 * 2. Parses each line into a Call object
 * 3. Computes the priority score for each Call
 * 4. Enqueues into PriorityCallQueue (automatic sorting)
 */
public class CallProcessor {

    private FileHandler fileHandler;
    private PriorityCallQueue queue;

    public CallProcessor() {
        this.queue = new PriorityCallQueue();
    }

    /**
     * Reads CSV file and loads calls into the priority queue.
     * 
     * CSV Format: customerId,customerName,phoneNumber,isVIP,repeatCalls
     * 
     * @param path path to the CSV file
     */
    public void loadFromCSV(String path) {
        this.fileHandler = new FileHandler(path);
        List<String> lines = fileHandler.readLines();

        if (lines.isEmpty()) {
            System.out.println("  [!] CSV file is empty or does not exist.");
            return;
        }

        // Skip header row if present
        int startIndex = 0;
        if (lines.get(0).toLowerCase().contains("customerid") 
                || lines.get(0).toLowerCase().contains("customer_id")) {
            startIndex = 1;
        }

        int orderCounter = 1;
        int loadedCount = 0;

        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String customerId = parts[0].trim();
                String customerName = parts[1].trim();
                String phoneNumber = parts[2].trim();
                boolean isVIP = Boolean.parseBoolean(parts[3].trim());
                int repeatCalls = Integer.parseInt(parts[4].trim());

                Call call = new Call(customerId, customerName, phoneNumber,
                        isVIP, repeatCalls, orderCounter++);

                // Calculate and assign priority score
                int priority = calculatePriority(call);
                call.setPriorityScore(priority);

                queue.enqueue(call);
                loadedCount++;
            } catch (Exception e) {
                System.out.println("  [!] Parse error at line " + (i + 1) + ": " + e.getMessage());
            }
        }

        System.out.println("  [✓] Loaded " + loadedCount + " calls into priority queue.");
    }

    /**
     * Computes priority score for a call.
     * Formula: (VIP ? 50 : 0) + (repeatCalls * 10)
     * 
     * @param call call to calculate
     * @return priority score
     */
    public int calculatePriority(Call call) {
        return call.getBasePriority();
    }

    /**
     * Gets the loaded priority queue.
     */
    public PriorityCallQueue getQueue() {
        return queue;
    }

    /**
     * Resets the queue (used when reloading new data).
     */
    public void reset() {
        this.queue = new PriorityCallQueue();
    }
}
