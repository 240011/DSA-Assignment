package Ticket;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BookingProcessor implements Runnable {
    private final BookingRequest request;
    private final SeatManager manager;
    private final String mode; // "optimistic" or "pessimistic"
    private final BookingGUI gui;

    public BookingProcessor(BookingRequest request, SeatManager manager, String mode, BookingGUI gui) {
        this.request = request;
        this.manager = manager;
        this.mode = mode;
        this.gui = gui;
    }

    @Override
    public void run() {
        try {
            String seat = request.seatId;
            if (mode.equals("pessimistic")) {
                ReentrantLock lock = manager.getLock(seat);
                if (lock.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        if (manager.isAvailable(seat)) {
                            Thread.sleep(500); // simulate processing
                            manager.bookSeat(seat);
                            gui.updateSeat(seat, true);
                            gui.log("Seat " + seat + " booked by " + request.userId);
                        } else {
                            gui.log("Seat " + seat + " already booked.");
                        }
                    } finally {
                        lock.unlock();
                    }
                } else {
                    gui.log("Lock timeout for seat " + seat);
                }
            } else { // optimistic
                if (manager.isAvailable(seat)) {
                    Thread.sleep(500); // simulate delay
                    if (manager.isAvailable(seat)) {
                        manager.bookSeat(seat);
                        gui.updateSeat(seat, true);
                        gui.log("Seat " + seat + " booked by " + request.userId);
                    } else {
                        gui.log("Conflict: Seat " + seat + " already booked.");
                    }
                } else {
                    gui.log("Seat " + seat + " already booked.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
