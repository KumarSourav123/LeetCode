# LeetCode 240 - Search a 2D Matrix II

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

This solution uses a **straightforward brute-force scan**, checking every single cell in the matrix for a match.

### Idea

- Iterate through every row `i` from `0` to `matrix.length - 1`.
- For each row, iterate through every column `j` from `0` to `matrix[i].length - 1`.
- If `matrix[i][j] == target`, a match has been found — return `true` immediately.
- If the entire matrix is scanned without finding a match, return `false`.

This approach does **not** take advantage of the row-wise or column-wise sorted structure of the matrix at all — it simply checks every cell exhaustively, similar to searching an unsorted 2D grid.

---

## Code

```java
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (target == matrix[i][j]) {
                    return true;
                }
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

| i | j | matrix[i][j] | target == matrix[i][j]? |
|---|---|---------------|----------------------------|
|0|0|1|No|
|0|1|4|No|
|0|2|7|No|
|0|3|11|No|
|0|4|15|No|
|1|0|2|No|
|1|1|5|**Yes → return true**|

Output:

```text
true
```

This matches the expected output. (Cell `matrix[1][1] = 5` is highlighted in the problem's example diagram as well.)

---

## Why This Approach Works

Checking every single cell is guaranteed to find `target` if it exists anywhere in the matrix, since no cell is skipped. Correctness here is trivial — the only question is efficiency, since this approach ignores the sorted row/column structure entirely.

---

## Complexity Analysis

- **Time Complexity:** `O(m × n)` — every cell may need to be checked in the worst case
- **Space Complexity:** `O(1)` — no extra space is used beyond the loop variables

---

## Key Insight

While this brute-force approach is simple and always correct, it wastes the matrix's sorted properties entirely. A much more efficient `O(m + n)` solution exists using the **"staircase search"** technique: starting from the **top-right corner** (or bottom-left corner) of the matrix, move **left** when the current value is too large, or **down** when it's too small — since each row is sorted left-to-right and each column top-to-bottom, this eliminates an entire row or column with each comparison, avoiding the need to scan the full grid.

---

## Concepts Used

- Array
- Matrix
- Brute Force Search
- Linear Scan