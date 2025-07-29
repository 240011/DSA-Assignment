// A pattern sequence is formed by taking a base pattern and repeating it a given number of times.
// For example, if pattern = "xyz" and times = 5, the resulting sequence is: xyzxyzxyzxyzxyz
// We define that one sequence can be derived from another if we can remove some characters (without
// reordering) to form the target sequence.
// You are given two base patterns p1 and p2, and two integers’ t1 and t2.
//  seqA = [p1, t1] is the sequence formed by repeating p1 exactly t1 times.
//  seqB = [p2, t2] is the sequence formed by repeating p2 exactly t2 times.
// Fi Input:
// p1 = "bca"
// t1 = 6
// p2 = "ba"
// t2 = 3
// Output: 3.
// Explanation
// We are given:
// p1 = "bca", t1 = 6
// This means we repeat "bca" 6 times to form seqA.
// So, seqA = "bcabcabcabcabcabcabc"
// p2 = "ba", t2 = 3
// This means we need to check how many times "ba" can be extracted from seqA.
// t2 = 3 means that in the original problem, we want to form seqB = "ba" × 3 = "babababa".
// We need to check whether this is possible by removing some characters (while keeping order) from
// seqA.
// We need to extract "ba" as many times as possible while maintaining order.
// Let's check where "ba" appears:
// 1. First "ba" → "bcabcabcabcabcabcabc" → Use 'b' at index 0, 'a' at index 2
// o Remaining sequence: "bcabcabcabcabcabc"
// 2. Second "ba" → "bcabcabcabcabcabc" → Use 'b' at index 3, 'a' at index 5
// o Remaining sequence: "bcabcabcabcabc"
// 3. Third "ba" → "bcabcabcabcabc" → Use 'b' at index 6, 'a' at index 8
// o Remaining sequence: "bcabcabcabc"
// At this point, there are not enough 'b' and 'a' pairs left to form another "ba."
// Thus, we cannot form more than 3 instances of "ba", so the maximum x = 3.
// The output represents the maximum number of full p2 = "ba" sequences that can be extracted from
// seqA.
// Even though t2 = 3, which means we ideally want 3 copies of "ba", we solve the problem to find out if
// that is possible. In this case, the answer is yes, so the output is 3.
// If t2 = 5, we would try to extract "ba" 5 times, but we would fail because we can only extract it 3
// times. In that case, the output would be 3, since it's the maximum possible




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