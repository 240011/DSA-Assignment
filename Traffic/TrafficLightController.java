package Traffic;

/**
 * Simulates a traffic light system cycling through Red, Green, and Yellow signals.
 * Runs in a separate thread, changing the signal every 5 seconds.
 */
public class TrafficLightController implements Runnable {
    private final String[] signals = {"Red", "Green", "Yellow"}; // Signal list
    private int currentSignalIndex = 0; // Tracks current signal
    private volatile boolean running = true; // Controls the loop

    // Returns the current signal
    public String getCurrentSignal() {
        return signals[currentSignalIndex];
    }

    // Stops the signal cycle
    public void stop() {
        running = false;
    }

    // Cycles through signals every 5 seconds while running
    public void run() {
        while (running) {
            try {
                currentSignalIndex = (currentSignalIndex + 1) % signals.length;
                System.out.println("Signal: " + getCurrentSignal());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
