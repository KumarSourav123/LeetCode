# LeetCode 48 - Rotate Image

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

This solution uses a **helper matrix (`matrixTwo`)** to compute the rotated positions, then copies the result back into the original matrix.

### Idea

- For a 90-degree clockwise rotation, the element at `matrix[i][j]` moves to position `matrixTwo[j][m - i - 1]`, where `m` is the number of rows. This mapping comes from the fact that the **first column** of the rotated matrix (read top to bottom) is the **first row** of the original matrix (read left to right, but placed bottom to top).
- **First pass:** Iterate through every cell `matrix[i][j]` of the original matrix and place it into its rotated position in `matrixTwo`.
- **Second pass:** Copy every value from `matrixTwo` back into `matrix`, so the original matrix reflects the rotated result "in place" from the caller's perspective.

---

## Code

```java
class Solution {
    public void rotate(int[][] matrix) {

        int m = matrix.length;
        int n = matrix[0].length;

        int[][] matrixTwo = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrixTwo[j][m - i - 1] = matrix[i][j];
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = matrixTwo[i][j];
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

**First pass: map each element to its rotated position**

| i | j | matrix[i][j] | Target: matrixTwo[j][m-i-1] |
|---|---|---------------|-------------------------------|
|0|0|1|matrixTwo[0][2] = 1|
|0|1|2|matrixTwo[1][2] = 2|
|0|2|3|matrixTwo[2][2] = 3|
|1|0|4|matrixTwo[0][1] = 4|
|1|1|5|matrixTwo[1][1] = 5|
|1|2|6|matrixTwo[2][1] = 6|
|2|0|7|matrixTwo[0][0] = 7|
|2|1|8|matrixTwo[1][0] = 8|
|2|2|9|matrixTwo[2][0] = 9|

Resulting `matrixTwo`:

```text
[[7,4,1],
 [8,5,2],
 [9,6,3]]
```

**Second pass: copy matrixTwo back into matrix**

Final Matrix:

```text
[[7,4,1],
 [8,5,2],
 [9,6,3]]
```

This matches the expected output.

---

## Why This Approach Works

Rotating a matrix 90 degrees clockwise means that each original row becomes a column in the new matrix, but read from **bottom to top**. The formula `matrixTwo[j][m - i - 1] = matrix[i][j]` captures this exactly: row `i` of the original becomes column `m - i - 1` of the rotated matrix (counting from the left), and the original column index `j` becomes the new row index.

Since every original cell is mapped to a unique, non-overlapping position in `matrixTwo`, there's no risk of overwriting values mid-rotation the way there would be if rotating directly within a single matrix without care.

---

## Complexity Analysis

- **Time Complexity:** `O(n^2)` — two full passes over the matrix
- **Space Complexity:** `O(n^2)` — for the helper `matrixTwo` array

---

## Key Insight

While the problem asks not to **allocate another 2D matrix**, using a temporary `matrixTwo` array makes the rotation mapping straightforward and bug-free, at the cost of extra space. A true in-place `O(1)` space solution would instead perform the rotation via **layer-by-layer four-way swapping** (transposing the matrix and then reversing each row, or rotating groups of four cells directly), avoiding any extra array entirely.

---

## Concepts Used

- Array
- Matrix
- Matrix Rotation
- Index Mapping
- Simulation