# LeetCode 1901 - Find a Peak Element II

## Problem Statement

A **peak** element in a 2D grid is an element that is **strictly greater** than all of its **adjacent** neighbors to the left, right, top, and bottom.

Given a **0-indexed** `m x n` matrix `mat` where **no two adjacent cells are equal**, find **any** peak element `mat[i][j]` and return *the length 2 array* `[i,j]`.

You may assume that the entire matrix is surrounded by an **outer perimeter** with the value `-1` in each cell.

You must write an algorithm that runs in `O(m log(n))` or `O(n log(m))` time.

---

## Example 1:
```
Input: mat = [[1,4],[3,2]]
Output: [0,1]
Explanation: Both 3 and 4 are peak elements so [1,0] and [0,1] are both acceptable answers.
```

## Example 2:
```
Input: mat = [[10,20,15],[21,30,14],[7,16,32]]
Output: [1,1]
Explanation: Both 30 and 32 are peak elements so [1,1] and [2,2] are both acceptable answers.
```

## Constraints:
- `m == mat.length`
- `n == mat[i].length`
- `1 <= m, n <= 500`
- `1 <= mat[i][j] <= 10^5`
- No two adjacent cells are equal.

---

## Approach

This solution uses **binary search over the columns**. For the middle column, it finds the **largest element in that column**, then compares that element only against its left and right neighbors. Because it's already the biggest in its column, its top and bottom neighbors can't beat it — so only the horizontal comparison decides whether it's a peak, and if it isn't, which half of the matrix must contain one.

### Idea

- `m = mat.length - 1` and `n = mat[0].length - 1` are stored as the **last valid row and column indices** (not the sizes), which is why the loops below use `<=`.
- Binary search over columns: `low = 0`, `high = n`.
- **Loop** `while(low <= high)`:
  - `mid = (low + high) / 2` — the column to examine.
  - **Find the column maximum:** start with `max = mat[0][mid]` and `row = 0`, then scan rows `1` through `m`, updating `max` and `row` whenever a larger value appears in column `mid`.
  - **Read the horizontal neighbors of that maximum:**
    - `left = mat[row][mid-1]` if `mid - 1 >= 0`, otherwise `-1` (the outer perimeter).
    - `right = mat[row][mid+1]` if `mid + 1 <= n`, otherwise `-1`.
  - **If `mat[row][mid] > right` and `mat[row][mid] > left`**: this element beats both horizontal neighbors, and it beats its vertical neighbors automatically (it's the maximum of its column). It's a peak — return `[row, mid]`.
  - **Else if `max < right`**: the right neighbor is larger, so a peak must exist in the columns to the right — `low = mid + 1`.
  - **Else** (the left neighbor must be larger, since adjacent cells are never equal): a peak must exist in the columns to the left — `high = mid - 1`.
- The final `return new int[]{0,0};` is only a fallback to satisfy the compiler; the problem guarantees a peak exists, so the loop always returns before reaching it.

---

## Code

```java
class Solution {
    public int[] findPeakGrid(int[][] mat) {
        int m = mat.length-1;
        int n = mat[0].length-1;

        int low = 0;
        int high = n;

        while(low <= high){
            int mid = (low+high)/2;

            int max = mat[0][mid];
            int row = 0;
            for(int i = 1 ; i <= m ; i++){
                if(mat[i][mid] > max){
                    max = mat[i][mid];
                    row = i;
                }
            }

            int left = mid-1 >= 0 ? mat[row][mid-1] : -1;
            int right = mid + 1 <= n ? mat[row][mid+1] : -1;

            if((mat[row][mid] > right) && (mat[row][mid] > left)){
                return new int[]{row,mid};
            }else if(max < right){
                low = mid+1;
            }else{
                high = mid-1;
            }
        }
        return new int[]{0,0};
    }
}
```

---

## Dry Run

### Input

```text
mat = [[10, 20, 15],
       [21, 30, 14],
       [ 7, 16, 32]]
```

`m = 2` (last row index), `n = 2` (last column index), `low = 0`, `high = 2`

**Iteration 1:** `low(0) <= high(2)` → runs. `mid = (0+2)/2 = 1`

**Find the maximum of column 1** (values `20, 30, 16`):

| i | mat[i][1] | > max? | max | row |
|---|-----------|--------|-----|-----|
|0 (initial)|20|—|20|0|
|1|30|30 > 20 → yes|30|1|
|2|16|16 > 30 → no|30|1|

Column maximum is `30` at `row = 1`.

| Step | Calculation | Result |
|------|-------------|--------|
|left|`mid-1 = 0 >= 0` → `mat[1][0]`|21|
|right|`mid+1 = 2 <= n(2)` → `mat[1][2]`|14|
|Peak check|`30 > 14` and `30 > 21`|**true**|

**Return:** `[1, 1]`

Output:

```text
[1,1]
```

This matches the expected output — `30` is greater than its left (`21`), right (`14`), top (`20`), and bottom (`16`) neighbors.

### Additional trace — a case that moves the search window (not from the official examples)

The official examples both resolve on the first iteration, so here's a custom input that actually exercises the `low = mid + 1` branch:

```text
mat = [[1, 2, 3],
       [4, 5, 6],
       [7, 8, 9]]
```

`m = 2`, `n = 2`, `low = 0`, `high = 2`

**Iteration 1:** `mid = 1`. Column 1 is `2, 5, 8`, so `max = 8`, `row = 2`.

| Step | Calculation | Result |
|------|-------------|--------|
|left|`mat[2][0]`|7|
|right|`mid+1 = 2 <= 2` → `mat[2][2]`|9|
|Peak check|`8 > 9`?|false|
|Next branch|`max(8) < right(9)`|true → `low = mid + 1 = 2`|

**Iteration 2:** `low(2) <= high(2)` → runs. `mid = 2`. Column 2 is `3, 6, 9`, so `max = 9`, `row = 2`.

| Step | Calculation | Result |
|------|-------------|--------|
|left|`mid-1 = 1 >= 0` → `mat[2][1]`|8|
|right|`mid+1 = 3 <= 2`? no → perimeter|-1|
|Peak check|`9 > -1` and `9 > 8`|**true**|

**Return:** `[2, 2]` — the bottom-right `9`, which is indeed greater than all of its neighbors.

---

## Why This Approach Works

Choosing the **maximum of the entire column** is what makes the horizontal-only check sufficient. If that value beats its left and right neighbors, then its top and bottom neighbors are automatically smaller (they're in the same column, and it's the column's maximum), so it's a true 2D peak without ever checking vertical neighbors explicitly.

When it *isn't* a peak, one horizontal neighbor is bigger (adjacent cells are never equal, so exactly one side — or both — is strictly larger). The key claim is that a peak must exist in the half of the matrix on the **side of the larger neighbor**. Intuitively, if you keep walking toward larger values, you strictly increase each step and can never loop back; since the matrix is bounded by `-1` on every side, the walk must eventually stop at a cell larger than all its neighbors — and the walk can't cross back over the column you started from, because that column's maximum is already smaller than the neighbor you stepped onto. So discarding the other half, and the current column, loses nothing.

Each iteration therefore either finds a peak or **halves the number of candidate columns**, exactly like a standard binary search — except the per-step work is a scan down one column rather than a single comparison.

The `-1` sentinel for out-of-range neighbors mirrors the problem's own "surrounded by `-1`" assumption: since every real value is at least `1`, a missing neighbor never blocks an element from being a peak.

---

## Complexity Analysis

- **Time Complexity:** `O(m log n)` — the binary search runs over columns (`O(log n)` iterations), and each iteration scans one full column of `m` rows to find its maximum, matching the problem's required bound
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

This is a 2D extension of the classic 1D "find a peak element" binary search. The trick that makes it work in two dimensions is **collapsing one dimension**: by always examining the maximum of a whole column, the vertical comparison is satisfied for free, reducing the 2D problem to a 1D one over columns. The same idea works symmetrically by searching over rows and taking row maximums.

---

## Concepts Used

- Matrix / 2D Array
- Binary Search
- Peak Finding
- Dimension Reduction (column maximum)
- Constant Space Algorithm