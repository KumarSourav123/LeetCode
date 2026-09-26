# LeetCode 2220 - Minimum Bit Flips to Convert Number

## Problem Statement

A **bit flip** of a number `x` is choosing a bit in the binary representation of `x` and flipping it from either `0` to `1` or `1` to `0`.

- For example, for `x = 7`, the binary representation is `111` and we may choose any bit (including any leading zeros not shown) and flip it. We can flip the first bit from the right to get `110`, flip the second bit from the right to get `101`, flip the fifth bit from the right (a leading zero) to get `10111`, etc.

Given two integers `start` and `goal`, return *the minimum number of bit flips to convert* `start` *to* `goal`.

---

## Example 1:
```
Input: start = 10, goal = 7
Output: 3
Explanation: The binary representation of 10 and 7 are 1010 and 0111 respectively. We can convert 10 to 7 in 3 steps:
- Flip the first bit from the right: 1010 -> 1011.
- Flip the third bit from the right: 1011 -> 1111.
- Flip the fourth bit from the right: 1111 -> 0111.
It can be shown we cannot convert 10 to 7 in less than 3 steps. Hence, we return 3.
```

## Example 2:
```
Input: start = 3, goal = 4
Output: 3
Explanation: The binary representation of 3 and 4 are 011 and 100 respectively. We can convert 3 to 4 in 3 steps:
- Flip the first bit from the right: 011 -> 010.
- Flip the second bit from the right: 010 -> 000.
- Flip the third bit from the right: 000 -> 100.
It can be shown we cannot convert 3 to 4 in less than 3 steps. Hence, we return 3.
```

## Constraints:
- `0 <= start, goal <= 10^9`

**Note:** This question is the same as 461. Hamming Distance.

---

## Approach

This solution first computes `start XOR goal`, which produces a number whose bits are `1` in **exactly** the positions where `start` and `goal` differ — then counts how many `1` bits that XOR result has, using **Brian Kernighan's algorithm** to strip off one set bit per iteration rather than checking every individual bit position.

### Idea

- `ans = start ^ goal` — XOR-ing the two numbers produces a `1` in every bit position where `start` and `goal` disagree, and a `0` everywhere they agree. Every `1` bit in `ans` represents exactly one bit flip that's required to turn `start` into `goal`.
- `count = 0` — will accumulate the number of set bits found.
- **Loop** `while(ans != 0)`:
  - `count++` — one more set bit has been found (and is about to be removed).
  - `ans = ans & (ans - 1)` — this is the key trick: subtracting `1` from `ans` flips its lowest set bit to `0` and turns every bit below it into `1`; ANDing that with the original `ans` clears exactly that lowest set bit while leaving every other bit unchanged. Each iteration therefore removes exactly one `1` bit from `ans`.
  - The loop continues until `ans` becomes `0`, meaning every set bit has been counted and cleared.
- Return `count` — the total number of differing bit positions, which is exactly the minimum number of flips required (each flip can independently correct exactly one differing bit).

---

## Code

```java
class Solution {
    public int minBitFlips(int start, int goal) {
        int ans = start ^ goal;
        int count = 0;

        while(ans != 0){
            count++;
            ans = ans & (ans-1);
        }

        return count;
    }
}
```

---

## Dry Run

### Input

```text
start = 10 (binary 1010)
goal  = 7  (binary 0111)
```

**Compute the XOR:**

```text
  1010   (10)
^ 0111   (7)
------
  1101   (13)
```

`ans = 10 ^ 7 = 13` (binary `1101`), `count = 0`

| Iteration | ans (binary) | ans (decimal) | count after | ans - 1 (binary) | ans & (ans-1) | New ans |
|-----------|---------------|-----------------|-------------|----------------------|--------------------|---------|
|1|1101|13|1|1100 (12)|`1101 & 1100 = 1100`|12|
|2|1100|12|2|1011 (11)|`1100 & 1011 = 1000`|8|
|3|1000|8|3|0111 (7)|`1000 & 0111 = 0000`|0|

**Loop check:** `ans == 0` → loop exits.

**Return:** `count = 3`

Output:

```text
3
```

This matches the expected output — `10` (`1010`) and `7` (`0111`) differ in exactly 3 bit positions (comparing `1010` vs `0111`: bit 0 differs `0` vs `1`, bit 2 differs `0` vs `1`, bit 3 differs `1` vs `0`, while bit 1 matches `1` vs `1`), matching the 3 differing bits found by counting the set bits of the XOR result, `1101`.

---

## Why This Approach Works

XOR is the natural tool for finding "where do these two numbers differ, bit by bit" — a `1` in the result means the corresponding bits of `start` and `goal` disagree, a `0` means they agree. Since flipping a bit of `start` can only ever affect that single bit position independently of all others, the minimum number of flips needed is *exactly* the number of positions where they disagree — no flip can ever "fix" more than one differing bit at once, and every differing bit needs at least one flip, so the count of differing bits is both a lower bound and an achievable target.

`ans & (ans - 1)` is a classic bit-manipulation identity (Brian Kernighan's trick) for clearing the lowest set bit: subtracting `1` from a number turns its lowest `1` bit into a `0` and every bit below it into `1`s (e.g. `1100 - 1 = 1011`), while every bit above the lowest set bit is unaffected. ANDing this with the original number keeps all the unaffected higher bits, but the newly-flipped lower bits — being `0` in the original and `1` in `ans-1`, or vice versa for the target bit itself — combine to clear exactly that one lowest set bit and nothing else. Repeating this removes exactly one set bit per iteration, so the loop runs exactly as many times as there are set bits in `ans` — directly counting them.

---

## Complexity Analysis

- **Time Complexity:** `O(k)`, where `k` is the number of set bits in `start ^ goal` (at most the number of bits in the input, e.g. 30 for values up to `10^9`) — this is faster in practice than checking all 32 bit positions individually when the XOR result is sparse, since each iteration processes one actual set bit rather than one bit position
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

XOR is the standard operation for isolating "differing bits" between two numbers, converting a bit-comparison problem into a bit-counting problem. From there, Brian Kernighan's `n & (n-1)` trick is a well-known, more-efficient-than-naive way to count set bits — it iterates once per set bit present (rather than once per total bit width), which can be meaningfully faster when the number of set bits is small relative to the number of bits in the representation.

---

## Concepts Used

- Bit Manipulation
- XOR (Bitwise Difference Detection)
- Brian Kernighan's Algorithm (Set Bit Counting)
- Hamming Distance