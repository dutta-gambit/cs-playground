/**
 * LeetCode 67 - Add Binary
 * https://leetcode.com/problems/add-binary/
 *
 * Add two binary strings and return result as binary string.
 *
 * Approach 1: Pad + explicit if-else cases (verbose but clear)
 * Approach 2: sum % 2 / sum / 2 (elegant, same as Plus One pattern)
 *
 * Key: Binary addition = right-to-left with carry, same pattern as decimal addition
 * sum % 2 gives digit, sum / 2 gives carry — works for all combinations
 *
 * Time: O(max(m,n)) | Space: O(max(m,n))
 */

// --- Approach 1: Pad + explicit cases ---
// class Solution {
//     public String addBinary(String a, String b) {
//         int lengthOfA = a.length();
//         int lengthOfB = b.length();
//
//         if (lengthOfA > lengthOfB) {
//             for (int i = 0; i < (lengthOfA - lengthOfB); i++) b = "0" + b;
//         }
//         if (lengthOfB > lengthOfA) {
//             for (int i = 0; i < (lengthOfB - lengthOfA); i++) a = "0" + a;
//         }
//
//         char[] arr1 = a.toCharArray();
//         char[] arr2 = b.toCharArray();
//         int carry = 0;
//         char[] res = new char[arr1.length];
//
//         for (int i = arr1.length - 1; i >= 0; i--) {
//             if (arr1[i] == '1' && arr2[i] == '1' && carry == 1) { res[i] = '1'; carry = 1; }
//             else if ((arr1[i] == '1' || arr2[i] == '1') && carry == 1) { res[i] = '0'; carry = 1; }
//             else if (arr1[i] == '1' && arr2[i] == '1') { res[i] = '0'; carry = 1; }
//             else if (arr1[i] == '0' && arr2[i] == '0' && carry == 1) { res[i] = '1'; carry = 0; }
//             else if (arr1[i] == '1' || arr2[i] == '1') { res[i] = '1'; carry = 0; }
//             else { res[i] = '0'; carry = 0; }
//         }
//
//         if (carry == 1) return "1" + new String(res);
//         return new String(res);
//     }
// }

// --- Approach 2: sum % 2 / sum / 2 (optimal) ---
class Solution {
    public String addBinary(String a, String b) {
        StringBuilder result = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;

        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += a.charAt(i--) - '0';
            if (j >= 0) sum += b.charAt(j--) - '0';

            result.append(sum % 2);   // digit (0 or 1)
            carry = sum / 2;          // carry (0 or 1)
        }

        return result.reverse().toString();
    }
}
