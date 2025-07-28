import java.util.*;

public class AlgoStartRevenueMaximizer {

    static class Project {
        int investment;
        int revenue;

        Project(int investment, int revenue) {
            this.investment = investment;
            this.revenue = revenue;
        }
    }

    public static int maximizeCapital(int k, int c, int[] revenues, int[] investments) {
        int n = revenues.length;

        // Min-heap: Projects sorted by required investment
        PriorityQueue<Project> minInvestmentHeap = new PriorityQueue<>(Comparator.comparingInt(p -> p.investment));

        // Max-heap: Projects sorted by revenue (among affordable)
        PriorityQueue<Project> maxRevenueHeap = new PriorityQueue<>((a, b) -> b.revenue - a.revenue);

        // Step 1: Load all projects into minInvestmentHeap
        for (int i = 0; i < n; i++) {
            minInvestmentHeap.add(new Project(investments[i], revenues[i]));
        }

        // Step 2: Perform up to k project selections
        for (int i = 0; i < k; i++) {
            // Move all affordable projects to maxRevenueHeap
            while (!minInvestmentHeap.isEmpty() && minInvestmentHeap.peek().investment <= c) {
                maxRevenueHeap.add(minInvestmentHeap.poll());
            }

            // If no affordable projects, break
            if (maxRevenueHeap.isEmpty()) {
                break;
            }

            // Pick most profitable project
            c += maxRevenueHeap.poll().revenue;
        }

        return c;
    }

    public static void main(String[] args) {
        // Example 1
        int k1 = 2;
        int c1 = 0;
        int[] revenues1 = {2, 5, 8};
        int[] investments1 = {0, 2, 3};
        System.out.println("Output 1: " + maximizeCapital(k1, c1, revenues1, investments1));  // Output: 7

        // Example 2
        int k2 = 3;
        int c2 = 1;
        int[] revenues2 = {3, 6, 10};
        int[] investments2 = {1, 3, 5};
        System.out.println("Output 2: " + maximizeCapital(k2, c2, revenues2, investments2));  // Output: 19
    }
}

// Projects are represented as objects containing investment and revenue values.
// They are initially organized in a min-heap based on investment cost (ascending order).
// A max-heap is then used to select the most profitable project from those that are affordable
// with the current available capital.























// import java.util.*;

// class Solution {
//     public int maximizeCapital(int k, int c, int[] revenues, int[] investments) {
//         int n = revenues.length;
//         int[][] projects = new int[n][2];
//         for (int i = 0; i < n; i++) {
//             projects[i][0] = investments[i];
//             projects[i][1] = revenues[i];
//         }
//         Arrays.sort(projects, (a, b) -> a[0] - b[0]);
        
//         PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
//         int index = 0;
//         for (int i = 0; i < k; i++) {
//             while (index < n && projects[index][0] <= c) {
//                 pq.offer(projects[index][1]);
//                 index++;
//             }
//             if (pq.isEmpty()) {
//                 break;
//             }
//             c += pq.poll();
//         }
//         return c;
//     }
// }