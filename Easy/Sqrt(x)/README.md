# LeetCode 69 - Sqrt(x)

## Problem Statement

Given a non-negative integer `x`, return *the square root of `x` rounded down to the nearest integer*. The returned integer should be **non-negative** as well.

You **must not use** any built-in exponent function or operator. For example, do not use `pow(x, 0.5)` in c++ or `x ** 0.5` in python.

---

## Example 1:
```
Input: x = 4
Output: 2
Explanation: The square root of 4 is 2, so we return 2.
```

## Example 2:
```
Input: x = 8
Output: 2
Explanation: The square root of 8 is 2.82842..., and since we round it down to the nearest integer, 2 is returned.
```

## Constraints:
- `0 <= x <= 2^31 - 1`

---

## Approach

Binary search over the answer space `[1, x]`, looking for the largest integer whose square doesn't exceed `x`.

### Idea

- Handle the edge case upfront: if `x == 0`, return `0` immediately (avoids running the search on a degenerate range).
- Set up the search range: `start = 1`, `end = x`, and keep a running answer `ans = 1` (the best valid candidate found so far).
- Standard binary search loop while `start <= end`:
  - Compute `mid = start + (end - start) / 2`.
  - Compute `val = (long) mid * mid` — cast to `long` to avoid integer overflow, since `mid` can be as large as `~2^31`, making `mid * mid` overflow a 32-bit `int`.
  - If `val > x`, `mid` is too big to be (or exceed) the square root — search the left half: `end = mid - 1`.
  - Otherwise (`val <= x`), `mid` is a valid candidate — record it (`ans = mid`) and search the right half for a possibly larger valid value: `start = mid + 1`.
- Once the loop ends (`start > end`), `ans` holds the largest `mid` whose square did not exceed `x` — return it.

---

## Code

```java
class Solution {
    public int mySqrt(int x) {

        if (x == 0) {
            return 0;
        }

        int start = 1;
        int end = x;
        int ans = 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;
            long val = (long) mid * mid;

            if (val > x) {
                end = mid - 1;
            } else {
                ans = mid;
                start = mid + 1;
            }
        }

        return ans;
    }
}
```

---

## Dry Run

### Input

```text
x = 8
```

`start = 1`, `end = 8`, `ans = 1`

| start | end | mid | mid*mid | val > x? | Action |
|-------|-----|-----|---------|----------|--------|
|1|8|4|16|16 > 8? Yes|end = mid-1 → 3|
|1|3|2|4|4 > 8? No|ans = 2, start = mid+1 → 3|
|3|3|3|9|9 > 8? Yes|end = mid-1 → 2|

Loop ends (`start=3 > end=2`).

**Final Result:**

```text
2
```

This matches the expected output — `⌊√8⌋ = 2`.

---

## Why This Approach Works

The set of integers `{1, 2, ..., x}` has a monotonic property with respect to the condition `mid * mid <= x`: it's true for every integer up to `⌊√x⌋` and false for every integer beyond it. That monotonicity is exactly what makes binary search valid here — instead of searching for a specific value, the search is over "the largest integer satisfying a property," and each comparison of `mid*mid` against `x` safely eliminates half of the remaining candidates.

Tracking `ans` separately (rather than trying to return `mid` or `end` directly at the end) avoids off-by-one mistakes: whenever a `mid` is confirmed valid (`val <= x`), it's saved immediately, and the search keeps pushing right to see if an even larger valid value exists, so the last saved `ans` is guaranteed to be the true floor of the square root.

---

## Complexity Analysis

- **Time Complexity:** `O(log x)` — binary search halves the range every iteration
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

Squaring `mid` can overflow a 32-bit `int` well before `mid` itself gets large (since `x` can be up to `2^31 - 1`, `mid` can approach `~46340`, and `mid * mid` can approach `~2^31`, right at the edge of overflow, and beyond it for values further along the search). Casting to `long` before multiplying (`(long) mid * mid`) sidesteps this overflow entirely, which is a small but easy-to-miss detail that would otherwise cause incorrect comparisons and a wrong answer for large inputs.

---

## Concepts Used

- Binary Search
- Binary Search on Answer Space
- Integer Overflow Handling