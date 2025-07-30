// Online Ticket Booking System – Concurrency Control
// Functionality:
//  Locks (Mutex / Semaphore): Ensure multiple users don’t book the same seat simultaneously.
//  Queue (Booking Requests): Manage pending seat reservation requests.
//  Database (Shared Resource): Store and update seat availability status concurrently.
// GUI:
//  A seating chart displaying available and booked seats.
//  A queue showing pending booking requests.
//  Buttons to:
// o Book a Seat (Simulate multiple users trying to book seats).
// o Enable Concurrency Control (Optimistic or Pessimistic Locking).
// o Process Bookings (Execute transactions concurrently).
// Implementation:
// Initialization:
// 1. Generate a seating layout for a theater/train/flight with available seats.
// 2. Create a queue of booking requests from multiple users.
// 3. Allow the user to choose a concurrency control mechanism (optimistic or pessimistic locking).
// 4. Display the seat availability in the GUI.
// Booking Process:
// 1. Choose a Concurrency Control Mechanism:
// o Optimistic Locking:
//  Read seat availability → Attempt to book → Check if status changed →
// Commit or retry.
// o Pessimistic Locking:
//  Lock the seat → Process booking → Unlock after completion.
// 2. Process Booking Requests:
// o Fetch a request from the queue.
// o Apply the chosen concurrency mechanism.
// o Update the seat status safely.
// 3. Real-time GUI Updates:
// o Show updated seat availability.
// o Handle failures if a seat is already booked.
// Booking Completion:
//  If a seat is successfully booked, confirm the booking.
//  If a conflict arises, retry or notify the user.
// Data Structures:
//  Queue: Store pending booking requests before processing.
//  HashMap / Dictionary: Maintain seat availability status.
//  Mutex / Semaphore: Prevent race conditions during seat selection.
//  Thread Pool: Simulate multiple users booking seats concurrently.
// Additional Considerations:
//  Deadlock Prevention: Handle timeout or avoid circular waits.
//  Transaction Logging: Keep a record of successful and failed bookings.
//  Performance Monitoring: Display success rate, conflicts, and retries.
//  Refund and Cancellation Handling: Allow users to cancel bookings and free up seats.


package Ticket;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;

// Booking Request Object to hold user ID and desired seat number
class BookingRequest {
    String userId;
    int seatNumber;

    public BookingRequest(String userId, int seatNumber) {
        this.userId = userId;
        this.seatNumber = seatNumber;
    }
}

// Seat Manager handles booking logic with both pessimistic and optimistic locking
class SeatManager {
    Map<Integer, Boolean> seats = new ConcurrentHashMap<>(); // Map of seat number to booking status
    final Object lock = new Object(); // Shared lock object for synchronization

    public SeatManager(int totalSeats) {
        // Initialize all seats as unbooked
        for (int i = 1; i <= totalSeats; i++) seats.put(i, false);
    }

    // Pessimistic locking: Lock is acquired before checking and booking
    public boolean bookSeatPessimistic(int seatNumber) {
        synchronized (lock) {
            if (!seats.get(seatNumber)) {
                seats.put(seatNumber, true); // Book seat
                return true;
            }
            return false; // Seat already booked
        }
    }

    // Optimistic locking: Try without locking, and retry under lock if necessary
    public boolean bookSeatOptimistic(int seatNumber) {
        if (!seats.get(seatNumber)) {
            if (Math.random() > 0.2) { // Simulate 80% chance of no conflict
                synchronized (lock) {
                    if (!seats.get(seatNumber)) {
                        seats.put(seatNumber, true);
                        return true;
                    }
                }
            }
        }
        return false; // Booking failed due to conflict or seat already booked
    }

    // Returns current seat booking status
    public Map<Integer, Boolean> getSeats() {
        return seats;
    }
}

// Runnable processor that handles booking requests from a queue
class BookingProcessor implements Runnable {
    BlockingQueue<BookingRequest> queue;
    SeatManager manager;
    boolean useOptimistic;
    JTextArea logArea;
    Runnable updateDisplay;

    public BookingProcessor(
        BlockingQueue<BookingRequest> queue,
        SeatManager manager,
        boolean useOptimistic,
        JTextArea logArea,
        Runnable updateDisplay
    ) {
        this.queue = queue;
        this.manager = manager;
        this.useOptimistic = useOptimistic;
        this.logArea = logArea;
        this.updateDisplay = updateDisplay;
    }

    public void run() {
        while (!queue.isEmpty()) {
            try {
                BookingRequest request = queue.take(); // Take request from queue

                // Book seat using selected strategy
                boolean success = useOptimistic
                    ? manager.bookSeatOptimistic(request.seatNumber)
                    : manager.bookSeatPessimistic(request.seatNumber);

                // Generate result message
                String msg = "User " + request.userId + " tried Seat " + request.seatNumber +
                    " → " + (success ? "✅ Booked" : "❌ Failed") + "\n";

                // Update log and seat display in UI thread
                SwingUtilities.invokeLater(() -> {
                    logArea.append(msg);
                    updateDisplay.run();
                });

                Thread.sleep(200); // Simulate processing delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Main GUI class for the Online Ticket Booking System
public class OnlineTicketing extends JFrame {
    SeatManager seatManager = new SeatManager(40); // 40 total seats
    BlockingQueue<BookingRequest> bookingQueue = new LinkedBlockingQueue<>();
    boolean useOptimisticLocking = true; // Default locking strategy

    JTextArea statusArea = new JTextArea(20, 30); // Displays seat status
    JTextArea logArea = new JTextArea(10, 30);    // Displays booking log

    public OnlineTicketing() {
        super("🎟️ Online Ticket Booking System");

        statusArea.setEditable(false);
        logArea.setEditable(false);

        // Buttons and label for controls
        JButton simulateBtn = new JButton("Simulate Bookings");
        JButton processBtn = new JButton("Process Bookings");
        JButton toggleBtn = new JButton("Toggle Locking");
        JLabel lockLabel = new JLabel("🔒 Mode: Optimistic");

        // Simulate 10 random booking requests
        simulateBtn.addActionListener(_ -> {
            for (int i = 1; i <= 10; i++) {
                int seat = (int)(Math.random() * 40) + 1;
                bookingQueue.add(new BookingRequest("User" + i, seat));
            }
        });

        // Start booking processor thread
        processBtn.addActionListener(_ -> {
            new Thread(new BookingProcessor(
                bookingQueue,
                seatManager,
                useOptimisticLocking,
                logArea,
                this::refreshSeatDisplay
            )).start();
        });

        // Toggle locking mode between Optimistic and Pessimistic
        toggleBtn.addActionListener(_ -> {
            useOptimisticLocking = !useOptimisticLocking;
            lockLabel.setText("🔒 Mode: " +
                (useOptimisticLocking ? "Optimistic" : "Pessimistic"));
        });

        // Organize GUI layout
        JPanel controlPanel = new JPanel();
        controlPanel.add(simulateBtn);
        controlPanel.add(processBtn);
        controlPanel.add(toggleBtn);
        controlPanel.add(lockLabel);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(new JScrollPane(statusArea));
        textPanel.add(new JScrollPane(logArea));

        add(controlPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        refreshSeatDisplay(); // Initial seat status display

        // Window setup
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Updates seat status area in the UI
    void refreshSeatDisplay() {
        StringBuilder sb = new StringBuilder();
        seatManager.getSeats().forEach((k, v) ->
            sb.append("Seat ").append(k).append(": ")
              .append(v ? "Booked ✅" : "Available 🟢").append("\n"));
        statusArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        // Launch the GUI on the Swing UI thread
        SwingUtilities.invokeLater(OnlineTicketing::new);
    }
}
