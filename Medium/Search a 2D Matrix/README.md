# LeetCode 74 - Search a 2D Matrix

## Problem Statement

You are given an `m x n` integer matrix `matrix` with the following two properties:

- Each row is sorted in non-decreasing order.
- The first integer of each row is greater than the last integer of the previous row.

Given an integer `target`, return `true` if `target` is in `matrix`, or `false` otherwise.

You must write a solution in `O(log(m * n))` time complexity.

---

## Example 1:
```
Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
Output: true
```

## Example 2:
```
Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
Output: false
```

## Constraints:
- `m == matrix.length`
- `n == matrix[i].length`
- `1 <= m, n <= 100`
- `-10^4 <= matrix[i][j], target <= 10^4`

---

## Approach

This solution performs **two separate binary searches**: first to locate the correct **row**, then to locate the **column** within that row.

### Idea

- **Row search:** Since each row's first element is greater than the previous row's last element, the rows themselves form a sorted sequence based on their range of values. Binary search over the row indices (`start1` to `end1`) using `mid1`:
  - If `target` falls within the range `[matrix[mid1][0], matrix[mid1][m]]` (the first and last elements of that row), that row is the candidate — record it as `index` and stop searching.
  - If `target` is smaller than the row's first element, search the upper half (`end1 = mid1 - 1`).
  - Otherwise, search the lower half (`start1 = mid1 + 1`).
- If no valid row was found (`index` remains its sentinel value `Integer.MIN_VALUE`), the target cannot exist in the matrix, so return `false` immediately.
- **Column search:** Now that the correct row (`index`) is known, perform a standard binary search across that row's columns (`start2` to `end2`) to look for `target` directly, using the classic equal/less-than/greater-than binary search pattern.
- If the column search finds a match, return `true`; if the search space is exhausted without a match, return `false`.

---

## Code

```java
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int index = Integer.MIN_VALUE;
        int n = matrix.length - 1;
        int m = matrix[0].length - 1;

        int start1 = 0;
        int end1 = n;

        while (start1 <= end1) {
            int mid1 = (start1 + end1) / 2;
            if (target >= matrix[mid1][0] && target <= matrix[mid1][m]) {
                index = mid1;
                break;
            } else if (target < matrix[mid1][0]) {
                end1 = mid1 - 1;
            } else {
                start1 = mid1 + 1;
            }
        }

        if (index == Integer.MIN_VALUE) {
            return false;
        }

        int start2 = 0;
        int end2 = m;

        while (start2 <= end2) {
            int mid2 = (start2 + end2) / 2;
            if (target == matrix[index][mid2]) {
                return true;
            } else if (target > matrix[index][mid2]) {
                start2 = mid2 + 1;
            } else {
                end2 = mid2 - 1;
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
matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
```

`n = 2` (last row index), `m = 3` (last column index)

**Row search:** `start1 = 0`, `end1 = 2`

| mid1 | Row Range [matrix[mid1][0], matrix[mid1][m]] | target in range? | Action |
|------|--------------------------------------------------|----------------------|--------|
|1|[10, 20]|3 >= 10? No|target < 10 → end1 = 0|
|0|[1, 7]|3 >= 1 && 3 <= 7? **Yes**|index = 0, break|

**Column search:** `start2 = 0`, `end2 = 3` (searching row `index = 0`: `[1,3,5,7]`)

| mid2 | matrix[0][mid2] | Comparison | Action |
|------|-------------------|----------------|--------|
|1|3|target == 3|**Match found → return true**|

Output:

```text
true
```

This matches the expected output.

---

## Why This Approach Works

Because each row's values are entirely greater than the previous row's values, the matrix can be thought of as one long sorted sequence split into rows — this means binary search can first narrow down **which row** could possibly contain `target` by comparing against each row's first and last elements, exactly as if searching a sorted list of ranges.

Once the correct row is identified, that row itself is sorted independently, so a standard binary search within it correctly finds (or rules out) the target's exact position.

---

## Complexity Analysis

- **Time Complexity:** `O(log m + log n)`, which simplifies to `O(log(m × n))` — one binary search over the rows, one over the columns
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

Treating the two-dimensional search as **two independent one-dimensional binary searches** (row-level, then column-level) is what achieves the required `O(log(m*n))` time complexity — a single linear scan through rows and columns would only achieve `O(m + n)`, and even a naive flattened binary search would need to map a 1D index back to 2D coordinates, whereas this approach handles both dimensions directly and cleanly.

---

## Concepts Used

- Array
- Matrix
- Binary Search
- Two-Level Search (Row then Column)
- Sorted Search Space