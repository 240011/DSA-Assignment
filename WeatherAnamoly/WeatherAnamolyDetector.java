package WeatherAnamoly;

public class WeatherAnamolyDetector {

    public static int countValidAnomalies(int[] temperatureChanges, int low, int high) {
        int count = 0;

        for (int start = 0; start < temperatureChanges.length; start++) {
            int sum = 0;

            for (int end = start; end < temperatureChanges.length; end++) {
                sum += temperatureChanges[end];

                if (sum >= low && sum <= high) {
                    count++;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        // Example 1
        int[] changes1 = {3, -1, -4, 6, 2};
        int low1 = 2, high1 = 5;
        System.out.println("Output 1: " + countValidAnomalies(changes1, low1, high1));  // Output: 3

        // Example 2
        int[] changes2 = {-2, 3, 1, -5, 4};
        int low2 = -1, high2 = 2;
        System.out.println("Output 2: " + countValidAnomalies(changes2, low2, high2));  // Output: 5
    }
}

// This Java program checks every possible sequence of consecutive days in the temperatureChanges array. 
// For each sequence, it calculates the sum of temperature changes. If the sum falls within the given range 
// (between the low and high thresholds), it counts that sequence as an anomaly. 
// The process continues until all possible subarrays have been checked. 
// Finally, it returns the total number of valid anomalous periods detected.
