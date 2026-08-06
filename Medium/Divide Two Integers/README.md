# LeetCode 29 - Divide Two Integers

## Problem Statement

Given two integers `dividend` and `divisor`, divide two integers **without** using multiplication, division, and mod operator.

The integer division should truncate toward zero, which means losing its fractional part. For example, `8.345` would be truncated to `8`, and `-2.7335` would be truncated to `-2`.

Return *the quotient* after dividing `dividend` by `divisor`.

**Note:** Assume we are dealing with an environment that could only store integers within the 32-bit signed integer range: `[-2^31, 2^31 - 1]`. For this problem, if the quotient is **strictly greater than** `2^31 - 1`, then return `2^31 - 1`, and if the quotient is **strictly less than** `-2^31`, then return `-2^31`.

---

## Example 1:
```
Input: dividend = 10, divisor = 3
Output: 3
Explanation: 10/3 = 3.33333.. which is truncated to 3.
```

## Example 2:
```
Input: dividend = 7, divisor = -3
Output: -2
Explanation: 7/-3 = -2.33333.. which is truncated to -2.
```

## Constraints:
- `-2^31 <= dividend, divisor <= 2^31 - 1`
- `divisor != 0`

---

## Approach

This solution uses **bit-shift-based repeated subtraction** (also known as the "exponential/doubling" technique), avoiding the forbidden `*`, `/`, and `%` operators entirely.

### Idea

- **Handle the overflow edge case first:** if `dividend == Integer.MIN_VALUE` and `divisor == -1`, the mathematically correct result (`2^31`) would overflow a 32-bit `int`, so directly return `Integer.MAX_VALUE`.
- **Handle the trivial equal case:** if `dividend == divisor`, the answer is simply `1`.
- **Determine the sign of the result** using boolean logic: the result is negative if exactly one of `dividend`/`divisor` is negative (and the other non-negative); otherwise the result is positive. Track this in a `sign` flag.
- **Work with absolute values as `long`:** convert both `dividend` and `divisor` to their absolute values (`n` and `d`), using `long` to safely handle `Math.abs(Integer.MIN_VALUE)`, which would otherwise overflow as an `int`.
- **Repeated doubling subtraction:** while `n >= d`, find the largest power-of-two multiple of `d` that still fits within `n`, using a bit-shift loop (`d << count`) to grow the multiple exponentially instead of checking one multiple at a time (which would be too slow for large inputs):
  - Increase `count` while `n >= (d << (count + 1))`, doubling the candidate multiple of `d` each time.
  - Once the largest valid `count` is found, add `(1 << count)` to `ans` (this represents how many times `d` fits into the portion of `n` just subtracted), and subtract `(d << count)` from `n`.
  - Repeat until `n < d`, at which point `ans` holds the full quotient.
- **Reapply the sign:** return `ans` if the result should be positive, or `-ans` if it should be negative.

---

## Code

```java
class Solution {
    public int divide(int dividend, int divisor) {

        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        if (dividend == divisor) {
            return 1;
        }

        boolean sign = true;
        if (dividend >= 0 && divisor < 0) {
            sign = false;
        }
        if (dividend < 0 && divisor > 0) {
            sign = false;
        }

        long n = Math.abs((long) dividend);
        long d = Math.abs((long) divisor);

        int ans = 0;

        while (n >= d) {
            int count = 0;
            while (n >= (d << (count + 1))) {
                count++;
            }

            ans += (1 << count);
            n -= (d << count);
        }

        return sign ? (int) ans : (int) (-ans);
    }
}
```

---

## Dry Run

### Input

```text
dividend = 10, divisor = 3
```

Not the overflow edge case, and `10 != 3`, so proceed normally.

**Sign determination:** `dividend(10) >= 0 && divisor(3) < 0`? No. `dividend < 0 && divisor > 0`? No. → `sign = true` (positive result).

**Setup:** `n = |10| = 10`, `d = |3| = 3`, `ans = 0`

**Outer loop iteration 1** (`n = 10 >= d = 3`):

| count | n >= (d << (count+1))? | Action |
|-------|-----------------------------|--------|
|0|10 >= (3<<1)=6? Yes|count++ → count=1|
|1|10 >= (3<<2)=12? No|stop inner loop|

`ans += (1 << 1) = 2` → `ans = 2`
`n -= (3 << 1) = 6` → `n = 10 - 6 = 4`

**Outer loop iteration 2** (`n = 4 >= d = 3`):

| count | n >= (d << (count+1))? | Action |
|-------|-----------------------------|--------|
|0|4 >= (3<<1)=6? No|stop inner loop|

`ans += (1 << 0) = 1` → `ans = 3`
`n -= (3 << 0) = 3` → `n = 4 - 3 = 1`

**Outer loop check:** `n = 1 >= d = 3`? No → loop ends.

**Final result:** `sign = true` → return `ans = 3`

Output:

```text
3
```

This matches the expected output (`10 / 3 = 3.333...` truncated to `3`).

---

## Why This Approach Works

Since multiplication, division, and mod are forbidden, repeatedly subtracting the divisor one at a time would technically work but be far too slow (`O(dividend / divisor)` iterations). Instead, this solution finds the **largest power-of-two multiple** of the divisor that still fits into the remaining dividend at each step, using left-shifts (`<<`) to double the candidate multiple — this is mathematically equivalent to binary long division, converging in logarithmic time.

Each subtraction removes a "chunk" equal to `d << count` (i.e., `d * 2^count`) from `n`, and correspondingly adds `2^count` to the running quotient `ans`, since that many copies of `d` were just accounted for.

---

## Complexity Analysis

- **Time Complexity:** `O((log n)^2)` — the outer loop runs `O(log n)` times (each iteration removes at least half of the remaining value), and each inner loop also takes `O(log n)` to find the correct shift amount
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

Using bit-shifts to simulate multiplication and division by powers of two is the standard trick for solving "divide without division" problems: since any positive integer can be represented as a sum of powers of two, exponentially growing and then subtracting the largest fitting multiple of the divisor at each step achieves fast division using only addition, subtraction, and bit-shifting.

---

## Concepts Used

- Math
- Bit Manipulation
- Bit Shifting
- Binary Long Division
- Overflow Handling