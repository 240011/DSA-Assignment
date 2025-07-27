package Traffic;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VehicleManager implements Runnable {
    private final PriorityBlockingQueue<Vehicle> vehicleQueue = new PriorityBlockingQueue<>();
    private volatile boolean running = true;

    public void addVehicle(Vehicle v) {
        vehicleQueue.offer(v);
    }

    public void stop() {
        running = false;
    }

    public void run() {
        while (running) {
            try {
                Vehicle v = vehicleQueue.take();
                System.out.println("Vehicle passed: " + v);
                Thread.sleep(2000); // Simulate vehicle passing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String[] getCurrentQueue() {
        return vehicleQueue.stream().map(Vehicle::toString).toArray(String[]::new);
    }
}
