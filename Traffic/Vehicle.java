package Traffic;

public class Vehicle implements Comparable<Vehicle> {
    private String type; // "Car", "Bike", "Ambulance", etc.
    private boolean isEmergency;

    public Vehicle(String type, boolean isEmergency) {
        this.type = type;
        this.isEmergency = isEmergency;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(Vehicle o) {
        return Boolean.compare(!this.isEmergency, !o.isEmergency); // Emergency vehicles come first
    }

    @Override
    public String toString() {
        return (isEmergency ? "[EMG] " : "") + type;
    }
}
