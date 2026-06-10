package model;

/**
 * Enum representing call statuses in the system.
 * 
 * - WAITING:    Call is waiting in the queue
 * - PROCESSING: Call is being handled by an operator
 * - COMPLETED:  Call has been successfully served
 * - MISSED:     Call was missed (circular queue full)
 */
public enum CallStatus {
    WAITING,
    PROCESSING,
    COMPLETED,
    MISSED
}
