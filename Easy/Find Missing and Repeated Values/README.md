# LeetCode 2965 - Find Missing and Repeated Values

## Problem Statement

You are given a **0-indexed** 2D integer matrix `grid` of size `n * n` with values in the range `[1, n^2]`. Each integer appears exactly once except `a` which appears twice and `b` which is missing. The task is to find the repeating and missing numbers `a` and `b`.

Return a **0-indexed** integer array `ans` of size `2` where `ans[0]` equals to `a` and `ans[1]` equals to `b`.

---

## Example 1:
```
Input: grid = [[1,3],[2,2]]
Output: [2,4]
Explanation: Number 2 is repeated and number 4 is missing so the answer is [2,4].
```

## Example 2:
```
Input: grid = [[9,1,7],[8,9,2],[3,4,6]]
Output: [9,5]
Explanation: Number 9 is repeated and number 5 is missing so the answer is [9,5].
```

## Constraints:
- `2 <= n == grid.length == grid[i].length <= 50`
- `1 <= grid[i][j] <= n * n`
- For all `x` that `1 <= x <= n * n` there is exactly one `x` that is not equal to any of the grid members.
- For all `x` that `1 <= x <= n * n` there is exactly one `x` that is equal to exactly two of the grid members.
- For all `x` that `1 <= x <= n * n` except two of them there is exactly one pair of `i, j` that `0 <= i, j <= n` and `grid[i][j] == x`.

---

## Approach

This solution uses a **mathematical technique based on sum and sum-of-squares differences**, avoiding any need for a HashSet or frequency array.

### Idea

Let `a` be the repeated number and `b` be the missing number. If every number from `1` to `n²` appeared exactly once, the sum and sum-of-squares of the grid would exactly match the sum and sum-of-squares of `1, 2, ..., n²`. Since `a` appears twice and `b` doesn't appear at all, the **actual** sum differs from the **expected** sum by exactly `a - b`, and similarly for sum-of-squares.

- Compute `s`, the actual sum of all grid elements, and `sn`, the expected sum of `1` through `n²` (built up incrementally using a `count` variable).
- Compute `s2`, the actual sum of squares of all grid elements, and `s2n`, the expected sum of squares of `1` through `n²`.
- **Derive `val1 = s - sn = a - b`:** since the actual sum has one extra `a` and is missing one `b`.
- **Derive `val2 = s2 - s2n = a² - b² = (a-b)(a+b)`:** the same reasoning applied to squares.
- Dividing `val2` by `val1` cancels the `(a - b)` factor, leaving `val2 = a + b`.
- Now with two simple equations — `val1 = a - b` and `val2 = a + b` — solve for both:
  - `repeated (a) = (val1 + val2) / 2`
  - `missing (b) = repeated - val1`

---

## Code

```java
class Solution {
    public int[] findMissingAndRepeatedValues(int[][] grid) {
        int n = grid.length;
        long repeated = 0;
        long missing = 0;
        int count = 1;
        long s = 1;
        long sn = 1;
        long s2 = 1;
        long s2n = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s = s + grid[i][j];
                s2 = s2 + (grid[i][j] * grid[i][j]);
                sn = sn + count;
                s2n = s2n + (count * count);
                count++;
            }
        }

        long val1 = s - sn;
        long val2 = s2 - s2n;
        val2 = val2 / val1;

        repeated = (val1 + val2) / 2;
        missing = repeated - val1;

        return new int[]{(int) repeated, (int) missing};
    }
}
```

---

## Dry Run

### Input

```text
grid = [[1,3],[2,2]]
```

`n = 2`, so values should span `1` to `n² = 4`.

**Accumulation pass** (note: `s`, `sn`, `s2`, `s2n` all start at `1` — see note below):

| i,j | grid[i][j] | s | s2 | count (before) | sn | s2n | count (after) |
|-----|--------------|---|----|--------------------|----|----|--------------------|
|0,0|1|1+1=2|1+1=2|1|1+1=2|1+1=2|2|
|0,1|3|2+3=5|2+9=11|2|2+2=4|2+4=6|3|
|1,0|2|5+2=7|11+4=15|3|4+3=7|6+9=15|4|
|1,1|2|7+2=9|15+4=19|4|7+4=11|15+16=31|5|

Final: `s = 9`, `s2 = 19`, `sn = 11`, `s2n = 31`

**Derive results:**

`val1 = s - sn = 9 - 11 = -2` (this is `a - b`)

`val2 = s2 - s2n = 19 - 31 = -12`, then `val2 = val2 / val1 = -12 / -2 = 6` (this is `a + b`)

`repeated = (val1 + val2) / 2 = (-2 + 6) / 2 = 4 / 2 = 2`

`missing = repeated - val1 = 2 - (-2) = 4`

Output:

```text
[2, 4]
```

This matches the expected output.

---

## Why This Approach Works

Since the grid contains every number from `1` to `n²` exactly once, **except** one duplicate (`a`) and one omission (`b`), the difference between the actual and expected sums isolates exactly `a - b`, and the difference between actual and expected sum-of-squares isolates `a² - b² = (a-b)(a+b)`.

Dividing the second difference by the first algebraically cancels the shared `(a - b)` factor, revealing `a + b` directly — from there, solving the resulting 2-variable linear system (`a - b` and `a + b`) gives both `a` and `b` with simple arithmetic, no searching required.

---

## Complexity Analysis

- **Time Complexity:** `O(n^2)` — a single pass over every cell in the `n x n` grid
- **Space Complexity:** `O(1)` — only a constant number of `long` variables are used

---

## Key Insight

This is a clever application of **Newton's identities**-style reasoning (relating sums of powers to symmetric differences): rather than tracking frequencies with a HashSet or array (which would also work but use `O(n²)` extra space), comparing actual vs. expected sums and sum-of-squares reduces the problem to solving a simple system of two linear equations, achieving the same result in constant extra space.

---

## Concepts Used

- Array
- Matrix
- Math
- Sum and Sum of Squares
- Algebraic Derivation
- Constant Space Algorithm