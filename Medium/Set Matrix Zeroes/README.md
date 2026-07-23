# LeetCode 73 - Set Matrix Zeroes

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
- Could you devise a constant space solution?

---

## Approach

This solution uses two **HashSets** to record which rows and columns need to be zeroed out, then applies the changes in a second pass.

### Idea

- **First pass:** Scan the entire matrix. Whenever `matrix[i][j] == 0` is found, record its row index `i` in the `row` set and its column index `j` in the `column` set.
- **Second pass:** Scan the entire matrix again. For every cell `matrix[i][j]`, if its row index exists in `row` **or** its column index exists in `column`, set that cell to `0`.

Recording the positions first (instead of zeroing immediately) prevents newly-created zeros from being mistaken for original zeros and incorrectly cascading further zeros.

---

## Code

```java
class Solution {
    public void setZeroes(int[][] matrix) {
        HashSet<Integer> row = new HashSet<>();
        HashSet<Integer> column = new HashSet<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    row.add(i);
                    column.add(j);
                }
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (row.contains(i) || column.contains(j)) {
                    matrix[i][j] = 0;
                }
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

**First pass: locate zeros**

| i | j | matrix[i][j] | Action |
|---|---|---------------|--------|
|0|0,1,2|1,1,1|no zero found|
|1|0|1|no zero|
|1|1|0|row.add(1), column.add(1)|
|1|2|1|no zero|
|2|0,1,2|1,1,1|no zero found|

Result: `row = {1}`, `column = {1}`

**Second pass: apply zeroing**

| i | j | row.contains(i)? | column.contains(j)? | New Value |
|---|---|--------------------|------------------------|-----------|
|0|0|No|No|1|
|0|1|No|Yes|0|
|0|2|No|No|1|
|1|0|Yes|-|0|
|1|1|Yes|-|0|
|1|2|Yes|-|0|
|2|0|No|No|1|
|2|1|No|Yes|0|
|2|2|No|No|1|

Final Matrix:

```text
[[1,0,1],
 [0,0,0],
 [1,0,1]]
```

This matches the expected output.

---

## Why This Approach Works

Separating the "detection" phase from the "modification" phase ensures that the original positions of zeros are fully known **before** any cell is actually changed, so the zeroing of one row or column never gets misinterpreted as a fresh zero that would incorrectly trigger further row/column wipes.

Using sets for `row` and `column` allows O(1) membership checks during the second pass, so every cell only needs a constant amount of extra work to decide whether it should become zero.

---

## Complexity Analysis

- **Time Complexity:** `O(m × n)` — two full passes over the matrix
- **Space Complexity:** `O(m + n)` — for storing the marked row and column indices

---

## Follow-up Considerations

- **O(mn) space (naive):** Copy the entire matrix first, then use the copy to check original values while modifying the original — wasteful since only row/column indices actually need to be remembered.
- **O(m + n) space (this solution):** Track marked rows and columns using two sets, as shown above.
- **O(1) constant space:** Instead of separate sets, use the **first row and first column of the matrix itself** as markers for which rows/columns contain a zero, along with two boolean flags to track whether the first row and first column originally contained a zero themselves. This avoids any extra data structure entirely.

---

## Key Insight

By decoupling "finding zeros" from "applying zeros," this approach avoids the classic bug of cascading zeros overwriting the matrix mid-scan, while still achieving efficient row/column lookups through hashing — trading a bit of extra space (`O(m + n)`) for a simple and safe two-pass solution.

---

## Concepts Used

- Array
- Matrix
- HashSet
- Hashing
- In-Place Modification
- Marking Technique