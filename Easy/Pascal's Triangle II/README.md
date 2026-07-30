# LeetCode 119 - Pascal's Triangle II

## Problem Statement

Given an integer `rowIndex`, return the `rowIndex`th (**0-indexed**) row of the Pascal's triangle.

In Pascal's triangle, each number is the sum of the two numbers directly above it, as shown.

---

## Example 1:
```
Input: rowIndex = 3
Output: [1,3,3,1]
```

## Example 2:
```
Input: rowIndex = 0
Output: [1]
```

## Example 3:
```
Input: rowIndex = 1
Output: [1,1]
```

## Constraints:
- `0 <= rowIndex <= 33`

## Follow up:
Could you optimize your algorithm to use only `O(rowIndex)` extra space?

---

## Approach

This solution directly computes only the single requested row using the **binomial coefficient recurrence**, without generating any of the earlier rows.

### Idea

- Start with `ans = 1` (as a `long`, to safely handle intermediate multiplication results without overflow) — this is the first element of every row.
- Add `ans` to the result `list`.
- For each position `i` from `1` to `rowIndex`, update `ans` using the incremental relation:
  ```
  ans = ans * ((rowIndex + 1) - i) / i
  ```
  This transforms the previous binomial coefficient `C(rowIndex, i-1)` into the next one, `C(rowIndex, i)`.
- Add each computed `ans` (cast back to `int`) to the `list`.
- Since only one row is ever built, no earlier rows are stored or referenced, keeping the extra space limited to the output row itself.

---

## Code

```java
class Solution {
    public List<Integer> getRow(int rowIndex) {
        ArrayList<Integer> list = new ArrayList<>();
        long ans = 1;
        list.add((int) ans);

        for (int i = 1; i <= rowIndex; i++) {
            ans = ans * ((rowIndex + 1) - i);
            ans = ans / (i);
            list.add((int) ans);
        }

        return list;
    }
}
```

---

## Dry Run

### Input

```text
rowIndex = 3
```

**Initial:** `ans = 1`, `list = [1]`

| i | ans = ans * ((rowIndex+1)-i) | ans = ans / i | list |
|---|---------------------------------|------------------|------|
|1|1 * (4-1) = 3|3 / 1 = 3|[1, 3]|
|2|3 * (4-2) = 6|6 / 2 = 3|[1, 3, 3]|
|3|3 * (4-3) = 3|3 / 3 = 1|[1, 3, 3, 1]|

Final Output:

```text
[1, 3, 3, 1]
```

This matches the expected output for `rowIndex = 3`.

---

## Why This Approach Works

Each element at position `i` in row `rowIndex` corresponds to the binomial coefficient `C(rowIndex, i)`. Using the identity `C(n, k) = C(n, k-1) * (n - k + 1) / k`, every subsequent value in the row can be derived directly from the previous one with a single multiply and divide — without needing any information from rows above it.

Using `long` for intermediate calculations prevents overflow during the multiplication step, since `ans * ((rowIndex + 1) - i)` can temporarily exceed the range of an `int` before the division brings it back down to a valid coefficient value.

---

## Complexity Analysis

- **Time Complexity:** `O(rowIndex)` — a single pass computing each of the `rowIndex + 1` values in the row
- **Space Complexity:** `O(rowIndex)` — for the output list itself; no additional rows or auxiliary structures are stored, directly satisfying the `O(rowIndex)` follow-up requirement

---

## Key Insight

By computing the row directly through the binomial coefficient formula instead of building up from row `0`, this solution naturally achieves the follow-up's `O(rowIndex)` space requirement — there's no need to ever store or discard previous rows, since each entry only depends on its immediate predecessor within the same row.

---

## Concepts Used

- Array
- Combinatorics
- Binomial Coefficients
- Incremental Computation
- Overflow-Safe Arithmetic (using `long`)