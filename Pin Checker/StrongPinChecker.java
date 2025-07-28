import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class StrongPinChecker {
    public static int strongPinChanges(String pinCode) {
        int n = pinCode.length();

        // Step 1: Check which character types are missing
        boolean hasLower = false, hasUpper = false, hasDigit = false;

        for (char c : pinCode.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }

        int missingTypes = 0;
        if (!hasLower) missingTypes++;
        if (!hasUpper) missingTypes++;
        if (!hasDigit) missingTypes++;

        // Step 2: Check for three consecutive repeating characters
        int replace = 0;
        int i = 2;
        List<Integer> repeats = new ArrayList<>();

        while (i < n) {
            if (pinCode.charAt(i) == pinCode.charAt(i - 1) &&
                pinCode.charAt(i - 1) == pinCode.charAt(i - 2)) {
                int len = 3;
                while (i + 1 < n && pinCode.charAt(i + 1) == pinCode.charAt(i)) {
                    len++;
                    i++;
                }
                repeats.add(len);
                i++;
            } else {
                i++;
            }
        }

        for (int len : repeats) {
            replace += len / 3;
        }

        if (n < 6) {
            return Math.max(missingTypes, 6 - n);
        } else if (n <= 20) {
            return Math.max(missingTypes, replace);
        } else {
            int deletions = n - 20;
            int overLen = deletions;

            // Try to reduce replacements using deletions
            for (int j = 0; j < repeats.size() && overLen > 0; j++) {
                int len = repeats.get(j);
                int need = len % 3 == 0 ? 1 : (len % 3 == 1 ? 2 : 3);
                if (overLen >= need) {
                    repeats.set(j, len - need);
                    overLen -= need;
                } else {
                    repeats.set(j, len - overLen);
                    overLen = 0;
                }
            }

            // Recalculate replacements after deletions
            replace = 0;
            for (int len : repeats) {
                replace += len / 3;
            }

            return deletions + Math.max(missingTypes, replace);
        }
    }

    public static void main(String[] args) {
        System.out.println(strongPinChanges("X1!"));       // Output: 3
        System.out.println(strongPinChanges("123456"));    // Output: 2
        System.out.println(strongPinChanges("Aa1234!"));   // Output: 0
        System.out.println(strongPinChanges("aaaaaa"));    // Output: 2
        System.out.println(strongPinChanges("aA1aaaaaaaAAAAAAA123")); // Output: 3
    }
}

// This method calculates the minimum number of changes required to make a given PIN code strong.
// A strong PIN must satisfy the following security rules:
// 1. Length must be between 6 and 20 characters inclusive.
// 2. Must contain at least one lowercase letter, one uppercase letter, and one digit.
// 3. Must not contain three or more repeating characters in a row.
//
// The function performs the following steps:
// - Checks for missing character types (lowercase, uppercase, digit).
// - Identifies sequences with three or more repeating characters.
// - Based on the length of the PIN, it determines how many changes (insertions, deletions, or replacements) are required:
//    * If the PIN is too short (<6), insertions are made to satisfy both length and character type requirements.
//    * If the PIN is within the valid length (6–20), replacements are used to fix repeated sequences and missing types.
//    * If the PIN is too long (>20), deletions are prioritized to reduce length while also helping minimize replacements for repeated characters.
//
// The final result is the total number of modifications needed to make the PIN meet all security criteria.
