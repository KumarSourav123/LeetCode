# LeetCode 137 - Single Number II

## Problem Statement

Given an integer array `nums` where every element appears **three times** except for one, which appears **exactly once**. *Find the single element and return it*.

You must implement a solution with a linear runtime complexity and use only constant extra space.

---

## Example 1:
```
Input: nums = [2,2,3,2]
Output: 3
```

## Example 2:
```
Input: nums = [0,1,0,1,0,1,99]
Output: 99
```

## Constraints:
- `1 <= nums.length <= 3 * 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`
- Each element in `nums` appears **exactly three times** except for one element which appears **once**.

---

## Approach

This solution uses two integers, `ones` and `twos`, as a **bitwise counter modulo 3**. For every bit position at once, the pair of bits `(twos, ones)` stores how many times that bit has been seen so far, counted modulo 3. Values that appear three times cancel back to a count of `0`, and the only bits left over are the ones belonging to the single element.

### Idea

- Initialize `ones = 0` and `twos = 0`.
- Think of each bit position independently. The two corresponding bits `(twos, ones)` encode a count mod 3:
  - `(0, 0)` → seen `0` times (mod 3)
  - `(0, 1)` → seen `1` time
  - `(1, 0)` → seen `2` times
- **Loop** over every `nums[i]` and update both variables:
  - `ones = (ones ^ nums[i]) & ~twos` — XOR toggles the "seen once" bit for every `1` bit in `nums[i]`, and `& ~twos` forces it back to `0` if that bit has already been seen twice (since that means this is the third occurrence, which resets the count).
  - `twos = (twos ^ nums[i]) & ~ones` — XOR toggles the "seen twice" bit, and `& ~ones` forces it to `0` whenever the freshly updated `ones` bit is set (a bit can't be in the "once" and "twice" states at the same time). This line deliberately uses the **already-updated** `ones`.
- Each `1` bit in a number moves that bit position's state one step along `0 → 1 → 2 → 0`, while `0` bits leave the state unchanged.
- After processing everything, every bit that belongs to a triple-occurring number has been seen a multiple of 3 times, so its state is back at `(0, 0)`. The bits of the single element have been seen exactly once (mod 3), so they sit in the `ones` state. Therefore `ones` is exactly the single element. Return `ones`.

---

## Code

```java
class Solution {
    public int singleNumber(int[] nums) {
        int ones = 0;
        int twos = 0;

        for(int i = 0 ; i < nums.length ; i++){
            ones = (ones^nums[i]) & ~twos;
            twos = (twos^nums[i]) & ~ones;
        }

        return ones;
    }
}
```

---

## Dry Run

### Input

```text
nums = [2, 2, 3, 2]
index:  0  1  2  3
```

Binary values: `2 = 10`, `3 = 11`. All values here fit in 2 bits, and every higher bit stays `0` throughout, so the trace below shows only the low 2 bits.

`ones = 00`, `twos = 00`

| i | nums[i] | ones = (ones ^ x) & ~twos | twos = (twos ^ x) & ~ones | ones (dec) | twos (dec) |
|---|---------|------------------------------|-------------------------------|------------|------------|
|0|`10`|`(00^10) & ~00 = 10 & 11 = 10`|`(00^10) & ~10 = 10 & 01 = 00`|2|0|
|1|`10`|`(10^10) & ~00 = 00 & 11 = 00`|`(00^10) & ~00 = 10 & 11 = 10`|0|2|
|2|`11`|`(00^11) & ~10 = 11 & 01 = 01`|`(10^11) & ~01 = 01 & 10 = 00`|1|0|
|3|`10`|`(01^10) & ~00 = 11 & 11 = 11`|`(00^10) & ~11 = 10 & 00 = 00`|3|0|

**Loop check:** `i(4) < nums.length(4)`? false → loop exits.

**Return:** `ones = 11` (binary) `= 3`

Output:

```text
3
```

This matches the expected output. Watching the high bit of the trace: the three `2`s each toggle it through `once → twice → gone`-style states (bit 1 sees four `1`s in total because `3 = 11` also has it set, which is `1 mod 3`, so it correctly ends in `ones`), while bit 0 is only set by the lone `3`, so it also ends in `ones`. Together they spell out `11 = 3`.

---

## Why This Approach Works

Counting how many times each bit appears across the whole array, **modulo 3**, isolates the single element: every number that appears three times contributes `3` (≡ `0` mod 3) to each of its set bits, so those contributions vanish, while the single element contributes exactly `1` to each of its set bits. The bits whose count is `1 (mod 3)` are therefore precisely the bits of the answer.

A count mod 3 needs three distinct states, which takes two bits per position. `ones` and `twos` supply those two bits for all 32 positions at once, using bitwise operations to update every position in parallel:

- When the incoming bit is `0`, both update formulas leave the state unchanged.
- When the incoming bit is `1`, the state steps `(twos, ones)`: `(0,0) → (0,1) → (1,0) → (0,0)`, which is exactly counting `0 → 1 → 2 → 0`.

The `& ~twos` and `& ~ones` masks are what enforce the rule that the two bits never both end up set, and what produce the wrap-around back to zero on the third occurrence.

Because the arithmetic is purely bitwise on the two's-complement representation, negative numbers (including the sign bit) work without any special handling.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array, with a constant number of bitwise operations per element
- **Space Complexity:** `O(1)` — only two integer variables are used, no auxiliary arrays, maps, or sorting, satisfying the problem's constant-space requirement

---

## Key Insight

Single Number (136) works because XOR cancels pairs, i.e. it is a counter modulo **2**. Single Number II needs a counter modulo **3**, and since three states don't fit in one bit, the solution uses two bits per position (`ones` and `twos`) and wires their update rules to form a tiny 3-state machine that runs on all bit positions in parallel. The same idea generalizes to "every element appears `k` times except one" by using enough bit-counters to represent counts modulo `k`.

---

## Concepts Used

- Array
- Bit Manipulation
- Bitwise State Machine (counting modulo 3)
- Single-Pass Traversal
- Constant Space Algorithm