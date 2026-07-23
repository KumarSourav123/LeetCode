# LeetCode 73 - Set Matrix Zeroes (O(1) Space Approach)

## Problem Statement

Given an `m x n` integer matrix `matrix`, if an element is `0`, set its entire row and column to `0`'s.

You must do it **in place**.

---

## Example 1:
```
Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
Output: [[1,0,1],[0,0,0],[1,0,1]]
```

## Example 2:
```
Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
```

## Constraints:
- `m == matrix.length`
- `n == matrix[0].length`
- `1 <= m, n <= 200`
- `-2^31 <= matrix[i][j] <= 2^31 - 1`

## Follow up:
- A straightforward solution using `O(mn)` space is probably a bad idea.
- A simple improvement uses `O(m + n)` space, but still not the best solution.
- Could you devise a **constant space** solution?

---

## Approach

This solution achieves **O(1) extra space** by using the **first row and first column of the matrix itself** as markers, instead of separate HashSets.

### Idea

- Since `matrix[0][j]` and `matrix[i][0]` are being repurposed as markers, we first need a separate variable `col` to remember whether the **first column itself** originally needs to be zeroed (because `matrix[i][0]` is about to be overwritten with marker values, its original "is it zero" state would otherwise be lost).
- **First pass:** Scan the whole matrix. Whenever `matrix[i][j] == 0` is found:
  - Mark its row by setting `matrix[i][0] = 0`.
  - Mark its column by setting `matrix[0][j] = 0` — unless `j == 0`, in which case the first column itself needs zeroing, so set the separate flag `col = 0` instead (to avoid immediately corrupting `matrix[i][0]`, which is also being used as a row marker).
- **Second pass:** For the *inner* submatrix (`i` and `j` both starting from `1`, skipping the first row/column since they're now marker cells), if `matrix[i][j] != 0`, check whether its row marker (`matrix[i][0]`) or column marker (`matrix[0][j]`) is `0`. If so, zero out `matrix[i][j]`.
- **Finally, handle the first row and first column themselves:**
  - If `matrix[0][0] == 0`, the first row originally contained a zero, so zero out the entire first row.
  - If `col == 0`, the first column originally contained a zero, so zero out the entire first column.

---

## Code

```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int col = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    if (j != 0) {
                        matrix[0][j] = 0;
                    } else {
                        col = 0;
                    }
                }
            }
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] != 0) {
                    if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                        matrix[i][j] = 0;
                    }
                }
            }
        }

        if (matrix[0][0] == 0) {
            for (int i = 0; i < n; i++) {
                matrix[0][i] = 0;
            }
        }

        if (col == 0) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }
        }
    }
}
```

---

## Dry Run

### Input

```text
matrix = [[1,1,1],
          [1,0,1],
          [1,1,1]]
```

`m = 3`, `n = 3`, `col = 1` (initially)

**First pass: mark row/column zeros using the matrix itself**

| i | j | matrix[i][j] | Action | Matrix After |
|---|---|---------------|--------|--------------|
|1|1|0|j≠0 → matrix[1][0]=0, matrix[0][1]=0|[[1,0,1],[0,0,1],[1,1,1]]|

All other cells are non-zero, so no other markers are set. `col` remains `1`.

State after first pass: `matrix = [[1,0,1],[0,0,1],[1,1,1]]`, `col = 1`

**Second pass: zero out inner cells based on markers (i, j starting from 1)**

| i | j | matrix[i][j] | matrix[i][0]==0? | matrix[0][j]==0? | Action |
|---|---|---------------|--------------------|------------------------|--------|
|1|1|0|-|-|already 0, skipped (condition `!= 0` is false)|
|1|2|1|Yes (matrix[1][0]=0)|-|matrix[1][2] = 0|
|2|1|1|No|Yes (matrix[0][1]=0)|matrix[2][1] = 0|
|2|2|1|No|No|stays 1|

State after second pass: `matrix = [[1,0,1],[0,0,0],[1,0,1]]`

**Final cleanup:**

- `matrix[0][0] == 0`? No (`1`) → first row is **not** zeroed further.
- `col == 0`? No (`1`) → first column is **not** zeroed further.

Final Matrix:

```text
[[1,0,1],
 [0,0,0],
 [1,0,1]]
```

This matches the expected output.

---

## Why This Approach Works

By reusing `matrix[i][0]` and `matrix[0][j]` as row/column markers instead of allocating separate sets, no extra data structure of size proportional to `m` or `n` is needed.

The `col` variable is essential because `matrix[i][0]` is being overloaded as a row marker — without a separate flag, information about whether the **first column** originally needed zeroing would be lost the moment any row marker gets set. Processing the first row and first column **last** (after the inner submatrix is fully resolved) ensures the marker values aren't consumed prematurely before they're used to zero out the inner cells.

---

## Complexity Analysis

- **Time Complexity:** `O(m × n)` — a constant number of passes over the matrix
- **Space Complexity:** `O(1)` — only a single extra integer (`col`) is used, satisfying the constant-space follow-up

---

## Key Insight

The key trick is recognizing that the matrix's own first row and first column can double as the marker storage that would otherwise require a `HashSet` or boolean array — with just one extra variable (`col`) needed to resolve the ambiguity of the first column overlapping with the row markers.

---

## Concepts Used

- Array
- Matrix
- In-Place Modification
- Marking Technique
- Constant Space Algorithm