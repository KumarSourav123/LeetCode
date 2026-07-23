# LeetCode 48 - Rotate Image (In-Place Transpose + Reverse Approach)

## Problem Statement

You are given an `n x n` 2D `matrix` representing an image, rotate the image by **90 degrees (clockwise)**.

You have to rotate the image **in-place**, which means you have to modify the input 2D matrix directly. **DO NOT** allocate another 2D matrix and do the rotation.

---

## Example 1:
```
Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [[7,4,1],[8,5,2],[9,6,3]]
```

## Example 2:
```
Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
```

## Constraints:
- `n == matrix.length == matrix[i].length`
- `1 <= n <= 20`
- `-1000 <= matrix[i][j] <= 1000`

---

## Approach

This solution performs a true **in-place, constant-space rotation** by first **transposing** the matrix, then **reversing each row**.

### Idea

A 90-degree clockwise rotation can be broken down into two simpler, well-known operations:

- **Step 1 — Transpose the matrix:** Swap `matrix[i][j]` with `matrix[j][i]` for every cell above (or on) the main diagonal (`j <= i`). This flips rows into columns, turning the matrix into its transpose.
- **Step 2 — Reverse each row:** For every row `i`, swap elements from the two ends inward (`j` from `0` to `n/2 - 1`, swapping `matrix[i][j]` with `matrix[i][n-j-1]`). Reversing each row after transposing completes the clockwise rotation.

Both steps modify the matrix directly with only a single temporary variable, requiring no auxiliary matrix at all.

---

## Code

```java
class Solution {
    public void rotate(int[][] matrix) {

        int m = matrix.length;
        int n = matrix[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= i; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n - j - 1] = temp;
            }
        }
    }
}
```

---

## Dry Run

### Input

```text
matrix = [[1,2,3],
          [4,5,6],
          [7,8,9]]
```

`m = 3`, `n = 3`

**Step 1: Transpose (swap matrix[i][j] with matrix[j][i], for j <= i)**

| i | j | Swap | Matrix After |
|---|---|------|--------------|
|0|0|matrix[0][0] ↔ matrix[0][0] (no change)|[[1,2,3],[4,5,6],[7,8,9]]|
|1|0|matrix[1][0] ↔ matrix[0][1]|[[1,4,3],[2,5,6],[7,8,9]]|
|1|1|matrix[1][1] ↔ matrix[1][1] (no change)|[[1,4,3],[2,5,6],[7,8,9]]|
|2|0|matrix[2][0] ↔ matrix[0][2]|[[1,4,7],[2,5,6],[3,8,9]]|
|2|1|matrix[2][1] ↔ matrix[1][2]|[[1,4,7],[2,5,8],[3,6,9]]|
|2|2|matrix[2][2] ↔ matrix[2][2] (no change)|[[1,4,7],[2,5,8],[3,6,9]]|

Matrix after transpose:

```text
[[1,4,7],
 [2,5,8],
 [3,6,9]]
```

**Step 2: Reverse each row (n/2 = 1, so j = 0 only)**

| i | Swap | Row After |
|---|------|-----------|
|0|matrix[0][0] ↔ matrix[0][2] → 1 ↔ 7|[7,4,1]|
|1|matrix[1][0] ↔ matrix[1][2] → 2 ↔ 8|[8,5,2]|
|2|matrix[2][0] ↔ matrix[2][2] → 3 ↔ 9|[9,6,3]|

Final Matrix:

```text
[[7,4,1],
 [8,5,2],
 [9,6,3]]
```

This matches the expected output.

---

## Why This Approach Works

Transposing the matrix converts every row into a column while keeping elements in the same relative order top-to-bottom. Reversing each row afterward flips that order left-to-right, which together produces exactly the effect of rotating the whole matrix 90 degrees clockwise.

Because both the transpose and the row-reversal swap elements **within the same matrix**, using only a single `temp` variable per swap, no auxiliary matrix or extra array is ever needed — satisfying the strict in-place requirement of the problem.

---

## Complexity Analysis

- **Time Complexity:** `O(n^2)` — each cell is visited a constant number of times across both passes
- **Space Complexity:** `O(1)` — only a single temporary variable is used

---

## Key Insight

Unlike the earlier helper-matrix approach (which uses `O(n^2)` extra space to directly compute each rotated position), decomposing the rotation into **transpose + reverse rows** achieves the same result using only `O(1)` extra space, making it the optimal solution that fully satisfies the "do not allocate another 2D matrix" constraint.

---

## Concepts Used

- Array
- Matrix
- Matrix Transpose
- In-Place Swapping
- Simulation
- Constant Space Algorithm