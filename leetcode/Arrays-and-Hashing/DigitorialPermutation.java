import java.util.Arrays;

/**
 * LeetCode 3848 - Check Digitorial Permutation
 *
 * A number is digitorial if sum of factorials of its digits equals the number.
 * Check if any permutation of n is digitorial.
 *
 * Key insight: sum of digit factorials is same regardless of digit order (addition is commutative).
 * So compute sum once, then check if sum's digits are a permutation of n's digits.
 *
 * Permutation check: sort both digit arrays, compare with Arrays.equals()
 * - char[] from String.valueOf() + Arrays.sort() — chars sort by ASCII (digits are in order)
 *
 * Time: O(d log d) where d = number of digits | Space: O(d)
 */
class Solution {
    public boolean isDigitorialPermutation(int n) {
        int z = n;
        int sumOfFactorial = 0;
        while (z > 0) {
            sumOfFactorial += factorial(z % 10);
            z /= 10;
        }
        return checkIfPermutation(sumOfFactorial, n);
    }

    private int factorial(int k) {
        if (k == 0 || k == 1) return 1;
        return k * factorial(k - 1);
    }

    private boolean checkIfPermutation(int a, int b) {
        char[] arr1 = String.valueOf(a).toCharArray();
        char[] arr2 = String.valueOf(b).toCharArray();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
    }
}
