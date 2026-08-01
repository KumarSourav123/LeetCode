# LeetCode 9 - Palindrome Number

## Problem Statement

Given an integer `x`, return `true` if `x` is a **palindrome**, and `false` otherwise.

---

## Example 1:
```
Input: x = 121
Output: true
Explanation: 121 reads as 121 from left to right and from right to left.
```

## Example 2:
```
Input: x = -121
Output: false
Explanation: From left to right, it reads -121. From right to left, it becomes 121-. Therefore it is not a palindrome.
```

## Example 3:
```
Input: x = 10
Output: false
Explanation: Reads 01 from right to left. Therefore it is not a palindrome.
```

## Constraints:
- `-2^31 <= x <= 2^31 - 1`

## Follow up:
Could you solve it without converting the integer to a string?

---

## Approach

This solution builds the **reversed version of the number mathematically**, digit by digit, and compares it to the original — satisfying the follow-up requirement of not converting to a string.

### Idea

- Copy `x` into a working variable `i`, and initialize `value = 0` to accumulate the reversed number.
- While `i > 0`:
  - Extract the last digit of `i` using `rem = i % 10`.
  - Append that digit to `value` by shifting it left one decimal place and adding the digit: `value = value * 10 + rem`.
  - Remove the last digit from `i` using integer division: `i = i / 10`.
- Once the loop finishes, `value` holds the digits of `x` in reverse order.
- Compare `value` to the original `x`: if they're equal, `x` is a palindrome; otherwise, it isn't.

Since the `while (i > 0)` condition is `false` immediately for any negative number, `value` stays at `0` for negative inputs — and since a negative `x` can never equal `0` (except when `x = 0`, which is non-negative), negative numbers are automatically correctly identified as non-palindromes without needing an explicit sign check.

---

## Code

```java
class Solution {
    public boolean isPalindrome(int x) {
        int i = x;
        int rem = 0;
        int value = 0;

        while (i > 0) {
            rem = i % 10;
            value = (value * 10) + rem;
            i = i / 10;
        }

        if (value == x) {
            return true;
        } else {
            return false;
        }
    }
}
```

---

## Dry Run

### Input

```text
x = 121
```

**Initial:** `i = 121`, `rem = 0`, `value = 0`

| i (before) | rem = i % 10 | value = value*10 + rem | i = i / 10 |
|-------------|----------------|---------------------------|--------------|
|121|1|0*10+1 = 1|12|
|12|2|1*10+2 = 12|1|
|1|1|12*10+1 = 121|0|

Loop ends (`i = 0`).

**Comparison:** `value (121) == x (121)`? Yes → return `true`.

---

### Additional Trace (negative number)

```text
x = -121
```

`i = -121` → the loop condition `i > 0` is `false` immediately, so the loop never executes.

`value` remains `0`.

**Comparison:** `value (0) == x (-121)`? No → return `false`, correctly identifying negative numbers as non-palindromes.

---

## Why This Approach Works

Extracting digits with `% 10` and rebuilding the number in reverse using `value * 10 + rem` is mathematically equivalent to reversing the digit sequence of `x`, without ever converting it to a string or array.

Since a true palindrome must read identically forwards and backwards, comparing the fully-reversed `value` against the original `x` is a direct and sufficient test — no partial comparison or intermediate string manipulation is needed.

---

## Complexity Analysis

- **Time Complexity:** `O(log₁₀ x)` — proportional to the number of digits in `x`
- **Space Complexity:** `O(1)` — only a constant number of integer variables are used

---

## Follow-up Considerations

The follow-up asks to solve this **without converting the integer to a string** — this solution already satisfies that constraint, since it works entirely through arithmetic (`%` and `/`) on the integer itself, never converting `x` to a `String`, `char[]`, or similar representation.

---

## Key Insight

Rather than reversing the number as text (which the follow-up explicitly discourages), reconstructing the reversed digits arithmetically using modulo and integer division achieves the same result while staying purely in the numeric domain — and elegantly handles negative numbers as a natural side effect of the loop condition, without any extra sign-checking logic.

---

## Concepts Used

- Math
- Digit Manipulation
- Integer Reversal
- Constant Space Algorithm