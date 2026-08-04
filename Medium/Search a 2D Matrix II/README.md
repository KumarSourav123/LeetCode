# LeetCode 240 - Search a 2D Matrix II (Staircase Search Approach)

## Problem Statement

Write an efficient algorithm that searches for a value `target` in an `m x n` integer matrix `matrix`. This matrix has the following properties:

- Integers in each row are sorted in ascending order from left to right.
- Integers in each column are sorted in ascending order from top to bottom.

---

## Example 1:
```
Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
Output: true
```

## Example 2:
```
Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 20
Output: false
```

## Constraints:
- `m == matrix.length`
- `n == matrix[i].length`
- `1 <= n, m <= 300`
- `-10^9 <= matrix[i][j] <= 10^9`
- All the integers in each row are sorted in ascending order.
- All the integers in each column are sorted in ascending order.
- `-10^9 <= target <= 10^9`

---

## Approach

This solution uses the classic **"staircase search"** technique, starting from the **top-right corner** of the matrix and eliminating an entire row or column with each comparison.

### Idea

- Start at position `row = 0` (the first row) and `col = n - 1` (the last column) — the top-right corner of the matrix.
- While the current position stays within bounds (`row <= m && col >= 0`):
  - Let `current = matrix[row][col]`.
  - If `current == target`, the value has been found — return `true`.
  - If `current < target`, every value in the current **row to the left** of this position is even smaller (since rows are sorted ascending left-to-right), so none of them can equal `target` either — move **down** to the next row (`row++`) to search larger values.
  - If `current > target`, every value **below** this position in the current column is even larger (since columns are sorted ascending top-to-bottom), so none of them can equal `target` — move **left** to the previous column (`col--`) to search smaller values.
- If the search runs off the matrix (`row` exceeds the last row or `col` goes below `0`) without finding a match, return `false`.

The key property that makes this work is starting from a corner where moving in **one direction increases values** and moving in the **other direction decreases values** — the top-right (and bottom-left) corners are the only positions with this property in a matrix sorted this way.

---

## Code

```java
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {

        int m = matrix.length - 1;
        int n = matrix[0].length - 1;

        int row = 0;
        int col = matrix[0].length - 1;

        while (row <= m && col >= 0) {
            int current = matrix[row][col];

            if (target == current) {
                return true;
            } else if (target > current) {
                row++;
            } else {
                col--;
            }
        }

        return false;
    }
}
```

---

## Dry Run

### Input

```text
matrix = [[1,4,7,11,15],
          [2,5,8,12,19],
          [3,6,9,16,22],
          [10,13,14,17,24],
          [18,21,23,26,30]], target = 5
```

**Starting position:** `row = 0`, `col = 4` (top-right corner, value `15`)

| row | col | current = matrix[row][col] | Comparison | Action |
|-----|-----|-------------------------------|----------------|--------|
|0|4|15|5 > 15? No, so 5 < 15|col-- → col=3|
|0|3|11|5 > 11? No, so 5 < 11|col-- → col=2|
|0|2|7|5 > 7? No, so 5 < 7|col-- → col=1|
|0|1|4|5 > 4? Yes|row++ → row=1|
|1|1|5|5 == 5|**Match found → return true**|

Output:

```text
true
```

This matches the expected output, correctly locating `5` at `matrix[1][1]`.

---

## Why This Approach Works

Starting from the top-right corner means the current row (moving leftward) contains progressively **smaller** values, while the current column (moving downward) contains progressively **larger** values. This gives a clean binary decision at every step: if the target is smaller than the current value, it can only be somewhere to the left in this row (since everything below is even larger) — so move left. If the target is larger, it can only be somewhere below in this column (since everything to the left is even smaller) — so move down.

Each step eliminates either an entire row or an entire column from consideration, and since there are only `m` rows and `n` columns total, the search terminates in at most `m + n` steps.

---

## Complexity Analysis

- **Time Complexity:** `O(m + n)` — at most `m` downward moves and `n` leftward moves before the search space is exhausted
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

Unlike a brute-force scan of every cell (`O(m × n)`), starting the search from a corner where one direction increases values and the other decreases them allows each comparison to definitively rule out an entire row or column at once — a much more efficient use of the matrix's sorted structure than checking cells individually.

---

## Concepts Used

- Array
- Matrix
- Staircase Search
- Two Pointers (row/column)
- Sorted Search Space