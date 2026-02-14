import java.util.*;

/**
 * LeetCode 149 - Max Points on a Line (Hard)
 * https://leetcode.com/problems/max-points-on-a-line/
 *
 * Approach: For each anchor point, compute normalized slopes to all other points.
 * Use HashMap to count points with same slope (collinear with anchor).
 *
 * Key tricks:
 * - Use GCD to reduce slope to canonical form (avoids floating-point issues)
 * - Normalize direction: if dx < 0, flip both signs
 * - Handle vertical (dx=0) and horizontal (dy=0) lines
 * - String key "dx/dy" as composite HashMap key
 *
 * Time:  O(n²) — compare all pairs, GCD is O(log(min(a,b)))
 * Space: O(n)  — HashMap per anchor point
 */
class Solution {
    public int maxPoints(int[][] points) {
        if (points.length <= 2) {
            return points.length;
        }

        int maxPoint = 0;
        for (int i = 0; i < points.length; i++) {
            Map<String, Integer> resultantSlope = new HashMap<>();
            int localMax = 0;
            int duplicate = 1; // anchor point itself

            for (int j = i + 1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];

                if (dx == 0 && dy == 0) {
                    duplicate++;
                    continue;
                }

                int gcd = gcd(dx, dy);
                dx = dx / gcd;
                dy = dy / gcd;

                // Normalize direction so (1,2) and (-1,-2) are the same slope
                if (dx < 0) {
                    dx = -dx;
                    dy = -dy;
                } else if (dx == 0) {
                    dy = 1; // vertical line
                } else if (dy == 0) {
                    dx = 1; // horizontal line
                }

                String key = dx + "/" + dy;
                resultantSlope.put(key, resultantSlope.getOrDefault(key, 0) + 1);
                localMax = Math.max(localMax, resultantSlope.get(key));
            }

            maxPoint = Math.max(maxPoint, localMax + duplicate);
        }

        return maxPoint;
    }

    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
