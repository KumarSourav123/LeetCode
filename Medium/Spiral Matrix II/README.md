# LeetCode 59 - Spiral Matrix II

## Problem Statement

Given a positive integer `n`, generate an `n x n` `matrix` filled with elements from `1` to `n^2` in spiral order.

---

## Example 1:
```
Input: n = 3
Output: [[1,2,3],[8,9,4],[7,6,5]]
```

## Example 2:
```
Input: n = 1
Output: [[1]]
```

## Constraints:
- `1 <= n <= 20`

---

## Approach

This solution is the **reverse** of the Spiral Matrix traversal problem: instead of *reading* elements out of an existing matrix in spiral order, it *writes* increasing numbers (`1, 2, 3, ...`) into an empty matrix following the same spiral pattern, using four shrinking boundary pointers (`left`, `right`, `top`, `bottom`).

### Idea

- Create an empty `n x n` matrix `arr`, and initialize the four boundaries: `top = 0`, `left = 0`, `right = n-1`, `bottom = n-1`, along with a `count` starting at `1` (the first value to place).
- While the boundaries still describe a valid region:
  1. **Fill left to right** along the `top` row with increasing `count` values, then move `top` down.
  2. **Fill top to bottom** along the `right` column, then move `right` left.
  3. **Fill right to left** along the `bottom` row, then move `bottom` up.
  4. **Fill bottom to top** along the `left` column, then move `left` right.
  - Each cell filled increments `count`, so the values placed always increase in the correct spiral order.
- Once the boundaries cross (no valid region remains), the matrix is fully filled and returned.

**Note on the loop condition:** the code uses `left <= right || top <= bottom` (an **OR**) rather than the more typical `&&` (AND) seen in the read-based Spiral Matrix problem. This still produces correct results here because once one of the two conditions becomes false, the corresponding inner `for` loops naturally become no-ops (their start/end indices are already invalid), so no extra cells get overwritten — but it is a slightly looser condition than strictly necessary.

---

## Code

```java
class Solution {
    public int[][] generateMatrix(int n) {

        int[][] arr = new int[n][n];
        int top = 0;
        int left = 0;
        int right = n - 1;
        int bottom = n - 1;
        int count = 1;

        while (left <= right || top <= bottom) {
            for (int i = left; i <= right; i++) {
                arr[top][i] = count;
                count++;
            }
            top++;

            for (int i = top; i <= bottom; i++) {
                arr[i][right] = count;
                count++;
            }
            right--;

            for (int i = right; i >= left; i--) {
                arr[bottom][i] = count;
                count++;
            }
            bottom--;

            for (int i = bottom; i >= top; i--) {
                arr[i][left] = count;
                count++;
            }
            left++;
        }

        return arr;
    }
}
```

---

## Dry Run

### Input

```text
n = 3
```

**Initial:** `top=0`, `left=0`, `right=2`, `bottom=2`, `count=1`

**Ring 1** (`left=0, right=2, top=0, bottom=2`):

| Step | Traversal | Cells Filled | count after |
|------|-----------|----------------|--------------|
|1|Top row, left→right|arr[0][0]=1, arr[0][1]=2, arr[0][2]=3|4|
|2|Right col, top→bottom|arr[1][2]=4, arr[2][2]=5|6|
|3|Bottom row, right→left|arr[2][1]=6, arr[2][0]=7|8|
|4|Left col, bottom→top|arr[1][0]=8|9|

After ring 1: `top=1, left=1, right=1, bottom=1`

**Ring 2** (`left=1, right=1, top=1, bottom=1`):

| Step | Traversal | Cells Filled | count after |
|------|-----------|----------------|--------------|
|1|Top row, left→right|arr[1][1]=9|10|
|2|Right col, top→bottom (top=2 > bottom=1, no cells)|none|10|
|3|Bottom row, right→left (right=0 < left=1, no cells)|none|10|
|4|Left col, bottom→top (bottom=0 < top=2, no cells)|none|10|

After ring 2: `top=2, left=2, right=0, bottom=0` → loop condition `left<=right (2<=0)` false and `top<=bottom (2<=0)` false → loop exits.

**Final Matrix:**

```text
[[1,2,3],
 [8,9,4],
 [7,6,5]]
```

This matches the expected output.

---

## Why This Approach Works

Since the four boundaries shrink inward after each full pass around the current ring, every cell in the matrix gets visited and assigned exactly once, in the exact spiral order required, with `count` naturally tracking the next value to place.

Even though the loop condition uses `||` instead of `&&`, correctness is preserved because each of the four inner `for` loops has its own independent bounds check (`i <= bound` or `i >= bound`). Once a ring has been fully consumed, these checks fail immediately for any "extra" pass attempted, so no cell is ever double-written or skipped.

---

## Complexity Analysis

- **Time Complexity:** `O(n^2)` — every cell in the `n x n` matrix is filled exactly once
- **Space Complexity:** `O(1)` extra space (excluding the output matrix, which is required to hold `n^2` values)

---

## Key Insight

This problem is the natural counterpart to "Spiral Matrix" (reading values out in spiral order): instead of collecting existing values, it assigns increasing values while following the identical four-boundary spiral traversal pattern — demonstrating that the same directional logic can be reused for both reading and writing spiral structures.

---

## Concepts Used

- Array
- Matrix
- Boundary Pointers
- Simulation
- Two-Dimensional Traversal