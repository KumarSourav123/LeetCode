# LeetCode 118 - Pascal's Triangle

## Problem Statement

Given an integer `numRows`, return the first `numRows` of **Pascal's triangle**.

In Pascal's triangle, each number is the sum of the two numbers directly above it, as shown.

---

## Example 1:
```
Input: numRows = 5
Output: [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
```

## Example 2:
```
Input: numRows = 1
Output: [[1]]
```

## Constraints:
- `1 <= numRows <= 30`

---

## Approach

This solution builds each row using the **binomial coefficient formula** directly, rather than summing values from the previous row.

### Idea

- For each row number from `1` to `numRows`, generate that row using a helper method `printRow`.
- Within `printRow(row)`:
  - Start with `ans = 1` (the first element of every row is always `1`), and add it to the row's `list`.
  - For each subsequent position `i` (from `1` to `row - 1`), compute the next binomial coefficient using the incremental relation:
    ```
    ans = ans * (row - i) / i
    ```
    This formula updates the previous coefficient `C(row-1, i-1)` into the next one `C(row-1, i)` without recomputing factorials from scratch.
  - Add each computed `ans` to the row's `list`.
- Collect all generated rows into the final `result` list.

This approach directly computes `C(n, k)` values (where `n = row - 1`) using the standard identity `C(n, k) = C(n, k-1) * (n - k + 1) / k`.

---

## Code

```java
class Solution {
    public List<List<Integer>> generate(int numRows) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        for (int i = 1; i <= numRows; i++) {
            result.add(printRow(i));
        }
        return result;
    }

    private List<Integer> printRow(int row) {
        ArrayList<Integer> list = new ArrayList<>();
        int ans = 1;
        list.add(ans);

        for (int i = 1; i < row; i++) {
            ans = ans * (row - i);
            ans = ans / (i);
            list.add(ans);
        }

        return list;
    }
}
```

---

## Dry Run

### Input

```text
numRows = 5
```

**Row 1** (`printRow(1)`): `ans = 1`, list = `[1]`. Loop doesn't run (`i < 1` is false).
Result: `[1]`

**Row 2** (`printRow(2)`): `ans = 1`, list = `[1]`

| i | ans = ans*(row-i) | ans = ans/i | list |
|---|----------------------|----------------|------|
|1|1*(2-1)=1|1/1=1|[1,1]|

Result: `[1, 1]`

**Row 3** (`printRow(3)`): `ans = 1`, list = `[1]`

| i | ans = ans*(row-i) | ans = ans/i | list |
|---|----------------------|----------------|------|
|1|1*(3-1)=2|2/1=2|[1,2]|
|2|2*(3-2)=2|2/2=1|[1,2,1]|

Result: `[1, 2, 1]`

**Row 4** (`printRow(4)`): `ans = 1`, list = `[1]`

| i | ans = ans*(row-i) | ans = ans/i | list |
|---|----------------------|----------------|------|
|1|1*(4-1)=3|3/1=3|[1,3]|
|2|3*(4-2)=6|6/2=3|[1,3,3]|
|3|3*(4-3)=3|3/3=1|[1,3,3,1]|

Result: `[1, 3, 3, 1]`

**Row 5** (`printRow(5)`): `ans = 1`, list = `[1]`

| i | ans = ans*(row-i) | ans = ans/i | list |
|---|----------------------|----------------|------|
|1|1*(5-1)=4|4/1=4|[1,4]|
|2|4*(5-2)=12|12/2=6|[1,4,6]|
|3|6*(5-3)=12|12/3=4|[1,4,6,4]|
|4|4*(5-4)=4|4/4=1|[1,4,6,4,1]|

Result: `[1, 4, 6, 4, 1]`

**Final Output:**

```text
[[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
```

This matches the expected result.

---

## Why This Approach Works

Every entry in row `row` (0-indexed positions `0` to `row-1`) equals the binomial coefficient `C(row-1, i)`. Starting from `C(row-1, 0) = 1`, each subsequent coefficient can be derived from the previous one using the multiplicative relation `C(n, k) = C(n, k-1) * (n - k + 1) / k`.

Since this update only involves one multiplication and one division per step (rather than recomputing factorials, which grow extremely large very quickly), each row is generated efficiently and directly, without needing to reference the row above it in the `result` list at all.

---

## Complexity Analysis

- **Time Complexity:** `O(numRows^2)` — generating each of the `numRows` rows takes time proportional to the row's length
- **Space Complexity:** `O(numRows^2)` — to store the full triangle (excluding the output itself, only `O(1)` extra space is used per row)

---

## Key Insight

Unlike the traditional Pascal's Triangle approach (where each row is built by summing adjacent elements from the previous row), this solution computes each row's values directly through the binomial coefficient recurrence — trading a small amount of arithmetic per element for **not needing to store or reference the previous row at all** during generation.

---

## Concepts Used

- Array
- Combinatorics
- Binomial Coefficients
- Dynamic Row Construction
- Incremental Computation