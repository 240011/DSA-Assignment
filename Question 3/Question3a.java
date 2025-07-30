/*
 * This program determines how many times a string `p2` can be formed as a subsequence
 * from the repeated concatenation of another string `p1`, up to `t1` times.
 * 
 * Key points:
 * - The method `maxRepetitions` simulates matching characters of `p2` within repeated sequences of `p1`.
 * - `count1` tracks how many times `p1` has been repeated.
 * - `count2` tracks how many full matches of `p2` are found.
 * - It returns the minimum of `count2` and `t2` to respect the repetition limit of `p2`.
 * 
 * The `main` method demonstrates this by testing two different values of `t2` and printing the results.
 */



public class Question3a {

    public static int maxRepetitions(String p1, int t1, String p2, int t2) {
        int len1 = p1.length();
        int len2 = p2.length();

        int i = 0, j = 0;      // i for traversing p1, j for traversing p2
        int count1 = 0;        // count of how many times p1 is fully traversed
        int count2 = 0;        // count of how many times p2 is fully matched

        // Loop until all repetitions of p1 are used
        while (count1 < t1) {
            // If characters match, move to next character in p2
            if (p1.charAt(i) == p2.charAt(j)) {
                j++;
                // If we reached end of p2, one full match is done
                if (j == len2) {
                    j = 0;     // reset pointer for p2
                    count2++;  // one full match of p2 done
                }
            }

            i++;  // move to next character in p1
            // If end of one p1 repetition is reached
            if (i == len1) {
                i = 0;        // reset pointer for p1
                count1++;     // one full repetition of p1 done
            }
        }

        // The maximum number of times p2 can be used is limited by t2
        int result = Math.min(count2, t2);

        // Debugging output
        System.out.println("Matched repetitions: " + count2 + ", Requested: " + t2 + ", Returning: " + result);

        return result;
    }

    public static void main(String[] args) {
        String p1 = "bca";
        int t1 = 6;
        String p2 = "ba";

        int t2_1 = 3;
        int result1 = maxRepetitions(p1, t1, p2, t2_1);
        System.out.println("Result for t2 = " + t2_1 + ": " + result1);  // Expected: 3

        int t2_2 = 5;
        int result2 = maxRepetitions(p1, t1, p2, t2_2);
        System.out.println("Result for t2 = " + t2_2 + ": " + result2);  // Expected: 3
    }
}