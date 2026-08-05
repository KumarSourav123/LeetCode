# LeetCode 7 - Reverse Integer

## Problem Statement

Given a signed 32-bit integer `x`, return `x` *with its digits reversed*. If reversing `x` causes the value to go outside the signed 32-bit integer range `[-2^31, 2^31 - 1]`, then return `0`.

**Assume the environment does not allow you to store 64-bit integers (signed or unsigned).**

---

## Example 1:
```
Input: x = 123
Output: 321
```

## Example 2:
```
Input: x = -123
Output: -321
```

## Example 3:
```
Input: x = 120
Output: 21
```

## Constraints:
- `-2^31 <= x <= 2^31 - 1`

---

## Approach

This solution reverses the digits of `x` using the **absolute value**, then reapplies the correct sign at the end — while checking for **overflow** at every step, since 64-bit integers aren't allowed.

### Idea

- Take `num = Math.abs(x)`, so the digit-reversal logic works uniformly regardless of the original sign.
- While `num > 0`:
  - Extract the last digit: `rem = num % 10`.
  - **Before** appending this digit to `val`, check whether doing so would cause a 32-bit overflow:
    - If `val > Integer.MAX_VALUE / 10`, then `val * 10` alone would already exceed `Integer.MAX_VALUE`, so return `0` immediately.
    - If `val == Integer.MAX_VALUE / 10` **and** `rem > 7` (since `Integer.MAX_VALUE` ends in `7`), appending this digit would push the result just past the maximum allowed value, so return `0`.
    - Similarly, check the symmetric conditions against `Integer.MIN_VALUE / 10` and whether `rem < -8` (since `Integer.MIN_VALUE` ends in `-8`), to guard against underflow.
  - If no overflow/underflow would occur, append the digit: `val = val * 10 + rem`.
  - Remove the last digit from `num`: `num = num / 10`.
- Once all digits have been processed, reapply the original sign:
  - If `x > 0`, return `val` as-is.
  - If `x < 0`, return `-val`.
  - If `x == 0`, the loop never executes and `val` remains `0`, so the final `return 0;` at the end handles that case.

---

## Code

```java
class Solution {
    public int reverse(int x) {
        int num = Math.abs(x);
        int rem = 0;
        int val = 0;

        while (num > 0) {
            rem = num % 10;
            if (val > Integer.MAX_VALUE / 10 ||
                (val == Integer.MAX_VALUE / 10 && rem > 7)) {
                return 0;
            }
            if (val < Integer.MIN_VALUE / 10 ||
                (val == Integer.MIN_VALUE / 10 && rem < -8)) {
                return 0;
            }
            val = (val * 10) + rem;
            num = num / 10;
        }
        if (x > 0) {
            return val;
        } if (x < 0) {
            return -val;
        }
        return 0;
    }
}
```

---

## Dry Run

### Input

```text
x = 123
```

**Initial:** `num = |123| = 123`, `rem = 0`, `val = 0`

| num (before) | rem = num % 10 | Overflow/Underflow check | val = val*10 + rem | num = num / 10 |
|----------------|-------------------|-------------------------------|------------------------|-------------------|
|123|3|Safe (val=0)|0*10+3 = 3|12|
|12|2|Safe (val=3)|3*10+2 = 32|1|
|1|1|Safe (val=32)|32*10+1 = 321|0|

Loop ends (`num = 0`).

**Sign reapplication:** `x = 123 > 0` → return `val = 321`.

Output:

```text
321
```

---

### Additional Trace (negative number)

```text
x = -123
```

`num = |-123| = 123` → identical digit-reversal process as above produces `val = 321`.

**Sign reapplication:** `x = -123 < 0` → return `-val = -321`.

Output:

```text
-321
```

This matches the expected output.

---

## Why This Approach Works

Working with the absolute value throughout the reversal loop means the digit-extraction and reconstruction logic (`% 10` and `* 10 + rem`) doesn't need to handle negative-number arithmetic edge cases — the sign is cleanly reapplied only once, at the very end.

The overflow/underflow checks are performed **before** each digit is appended (rather than after), which is essential: checking after the fact would already have caused undefined/wrapped behavior in a 32-bit `int`, silently producing an incorrect result instead of correctly detecting the overflow.

---

## Complexity Analysis

- **Time Complexity:** `O(log₁₀ x)` — proportional to the number of digits in `x`
- **Space Complexity:** `O(1)` — only a constant number of integer variables are used

---

## Key Insight

Since 64-bit integers aren't allowed for this problem, overflow must be detected **preemptively** — by comparing against `Integer.MAX_VALUE / 10` (and the equivalent boundary digit check) **before** performing the multiplication, rather than attempting the multiplication first and checking the result afterward, which would already have silently wrapped around to an incorrect value.

---

## Concepts Used

- Math
- Digit Manipulation
- Integer Overflow Detection
- Constant Space Algorithm