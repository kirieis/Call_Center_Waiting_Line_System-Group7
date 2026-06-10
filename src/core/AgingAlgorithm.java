package core;

import model.Call;
import java.util.List;

/**
 * Aging Algorithm - Increases priority score based on waiting time.
 * 
 * Purpose: Prevent starvation of low-priority calls
 * by gradually increasing their priority score when they wait too long.
 * 
 * Mechanism:
 * - Each time applyAging() is called, loop through all waiting calls.
 * - If wait duration exceeds the threshold (agingThresholdMs), increase waitTime.
 * - Total priority score = basePriority + waitTime (automatically computed via getAgedPriority()).
 */
public class AgingAlgorithm {

    private long agingThresholdMs;
    private int agingBoost;

    /**
     * Initializes with default values.
     * Threshold: 30 seconds, Boost: +5 points.
     */
    public AgingAlgorithm() {
        this(30000, 5);
    }

    /**
     * Initializes with custom values.
     * @param agingThresholdMs time threshold (ms) before boost
     * @param agingBoost points to add each time
     */
    public AgingAlgorithm(long agingThresholdMs, int agingBoost) {
        this.agingThresholdMs = agingThresholdMs;
        this.agingBoost = agingBoost;
    }

    /**
     * Applies aging to a list of calls.
     * Loops through calls; if wait duration is over threshold, increase waitTime.
     * 
     * @param calls list of waiting calls
     */
    public void applyAging(List<Call> calls) {
        long currentTime = System.currentTimeMillis();
        for (Call call : calls) {
            long waitDuration = currentTime - call.getEntryTime();
            if (waitDuration >= agingThresholdMs) {
                int boost = calculateBoost(call);
                call.incrementWaitTime(boost);
            }
        }
    }

    /**
     * Calculates boost score for a specific call.
     * Can be extended based on business logic.
     * 
     * @param call call to calculate boost for
     * @return boost points to add
     */
    public int calculateBoost(Call call) {
        // Base boost + 1 additional point for each repeat call (encourages loyal customers)
        return agingBoost + (call.getRepeatCalls() > 0 ? 1 : 0);
    }

    // --- Getters & Setters ---

    public long getAgingThresholdMs() {
        return agingThresholdMs;
    }

    public void setAgingThresholdMs(long agingThresholdMs) {
        this.agingThresholdMs = agingThresholdMs;
    }

    public int getAgingBoost() {
        return agingBoost;
    }

    public void setAgingBoost(int agingBoost) {
        this.agingBoost = agingBoost;
    }
}
