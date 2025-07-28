package Ticket;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

public class SeatManager {
    public final Map<String, Boolean> seats = new ConcurrentHashMap<>();
    public final Map<String, ReentrantLock> seatLocks = new HashMap<>();

    public SeatManager(int rows, int cols) {
        for (char row = 'A'; row < 'A' + rows; row++) {
            for (int col = 1; col <= cols; col++) {
                String seatId = row + String.valueOf(col);
                seats.put(seatId, false); // false = available
                seatLocks.put(seatId, new ReentrantLock());
            }
        }
    }

    public boolean isAvailable(String seatId) {
        return !seats.get(seatId);
    }

    public void bookSeat(String seatId) {
        seats.put(seatId, true);
    }

    public ReentrantLock getLock(String seatId) {
        return seatLocks.get(seatId);
    }
}
