# LeetCode 66 - Plus One

## Problem Statement

You are given a large integer represented as an integer array `digits`, where each `digits[i]` is the `ith` digit of the integer. The digits are ordered from most significant to least significant in left-to-right order. The large integer does not contain any leading `0`'s.

Increment the large integer by one and return *the resulting array of digits*.

---

## Example 1:
```
Input: digits = [1,2,3]
Output: [1,2,4]
Explanation: The array represents the integer 123.
Incrementing by one gives 123 + 1 = 124.
Thus, the result should be [1,2,4].
```

## Example 2:
```
Input: digits = [4,3,2,1]
Output: [4,3,2,2]
Explanation: The array represents the integer 4321.
Incrementing by one gives 4321 + 1 = 4322.
Thus, the result should be [4,3,2,2].
```

## Example 3:
```
Input: digits = [9]
Output: [1,0]
Explanation: The array represents the integer 9.
Incrementing by one gives 9 + 1 = 10.
Thus, the result should be [1,0].
```

## Constraints:
- `1 <= digits.length <= 100`
- `0 <= digits[i] <= 9`
- `digits` does not contain any leading `0`'s.

---

## Approach

This solution handles the increment in stages of increasing complexity, from the simplest case to the trickiest: a fast path for when no carrying is needed at all, then a counting step to detect whether **every** digit is a `9` (the one case requiring the array to grow), and otherwise a right-to-left carry-propagation loop for everything in between.

### Idea

- **Fast path — no carry needed:** if the last digit (`last = digits[digits.length-1]`) isn't `9`, incrementing it can't cause any carrying at all — simply `last = last + 1`, write it back to `digits[digits.length-1]`, and return `digits` immediately.
- **Count how many digits are `9`:** if the fast path didn't apply (the last digit *is* `9`), loop through the whole array counting how many digits equal `9` into `count`. This distinguishes "some trailing 9s that will carry into a non-9 digit" from "every digit is a 9" (which needs an extra digit in the result, e.g. `999 -> 1000`).
- **Case A — not every digit is a `9` (`count != digits.length`):** walk from the **last** digit backward:
  - If the current digit is `9`, it rolls over to `0` (`digits[i] = 0`), and the carry continues to the next digit to the left.
  - The **first** digit encountered (scanning right to left) that *isn't* `9` absorbs the carry: increment it by `1` and return `digits` immediately — no further digits need to change, since the carry has been fully resolved.
- **Case B — every digit is a `9` (`count == digits.length`)**: incrementing would produce a number with one more digit (e.g. `999 -> 1000`), so a new array `newDigits` of length `digits.length + 1` is used instead: set `newDigits[0] = 1` and every remaining position to `0`, then return `newDigits`.
- A final `return digits;` at the very end is a fallback that's never actually reached given the two branches above, since both `if` and `else` paths inside `if(count != digits.length)` always return.

---

## Code

```java
class Solution {
    public int[] plusOne(int[] digits) {
        int count = 0;
        int last = digits[digits.length-1];
        int[] newDigits = new int[digits.length+1];

        if(last != 9){
            last = last +1;
            digits[digits.length-1] = last;
            return digits;
        }

        for(int i = 0; i<digits.length ; i++){
            if(digits[i] == 9){
                count++;
            }
        }

        if(count != digits.length){
            for(int i = digits.length-1 ; i >= 0 ; i--){
                if(digits[i] == 9){
                    digits[i] = 0;
                }else{
                    digits[i] = digits[i] + 1;
                    return digits;
                }
            }
        }else{
            newDigits[0] = 1;
            for(int i = 1; i< newDigits.length ; i++){
                newDigits[i] = 0;
            }
            return newDigits;
        }
        return digits;
    }
}
```

---

## Dry Run

### Input (Example 3 — every digit is 9)

```text
digits = [9]
```

`count = 0`, `last = digits[0] = 9`, `newDigits = new int[2]` (initialized to `[0, 0]` by Java's default)

**Fast path check:** `last != 9`? `9 != 9` → false → skip the fast path.

**Count the 9s:**

| i | digits[i] | == 9? | count after |
|---|-----------|-------|-------------|
|0|9|yes|1|

`count = 1`

**Branch check:** `count != digits.length`? `1 != 1` → false → **Case B** (every digit is a 9)

| Step | Action | Result |
|------|--------|--------|
|`newDigits[0] = 1`|set first digit|`newDigits = [1, 0]`|
|loop `i=1`: `newDigits[1] = 0`|explicitly set (already 0 by default)|`newDigits = [1, 0]`|

**Return:** `newDigits = [1, 0]`

Output:

```text
[1,0]
```

This matches the expected output.

### Additional trace — a partial-carry case (not from the official examples)

To illustrate the carry-propagation loop (Case A), which none of the three official examples happen to exercise cleanly, consider:

```text
digits = [1, 2, 9]
```

`last = digits[2] = 9`. Fast path check: `9 != 9` → false → skip.

**Count the 9s:**

| i | digits[i] | == 9? | count after |
|---|-----------|-------|-------------|
|0|1|no|0|
|1|2|no|0|
|2|9|yes|1|

`count = 1`

**Branch check:** `count != digits.length`? `1 != 3` → true → **Case A** (carry-propagation loop, right to left)

| i | digits[i] | == 9? | Action | Result |
|---|-----------|-------|--------|--------|
|2|9|yes|`digits[2] = 0`, continue|`[1, 2, 0]`|
|1|2|no|`digits[1] = 2+1 = 3`, `return digits`|`[1, 3, 0]`|

**Return:** `digits = [1, 3, 0]`

This correctly represents `129 + 1 = 130`.

---

## Why This Approach Works

Incrementing a decimal number by one only ever affects digits from the **right** end inward, and only continues past a digit if that digit was a `9` (which rolls over to `0` and carries into the next digit to the left) — the moment a non-`9` digit is reached, incrementing it absorbs the carry completely, and every digit to its left is guaranteed unaffected. This is exactly what the right-to-left loop in Case A implements: roll `9`s to `0` while carrying, stop and increment (then immediately return) at the first non-`9`.

The only scenario the "roll and stop" logic can't handle in place is when the carry reaches all the way past the **first** digit — i.e. every digit was a `9` (like `999 -> 1000`), which requires one more digit than the original array has room for. Precomputing `count` (how many digits equal `9`) up front is what distinguishes this all-`9`s case from every other case in a single pass, before committing to either the in-place carry loop or allocating the larger `newDigits` array.

The fast path at the very top is a shortcut for the overwhelmingly common case (the last digit isn't a `9`) — algebraically it's just the same logic as Case A's first iteration would produce, but it avoids the overhead of counting `9`s and setting up the loop when no carry is going to happen at all.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — in the worst case (all `9`s), the digit array is scanned once to count `9`s and then walked again to build `newDigits`, both linear in `n`; in the best/average case (no carry, or a short carry chain), the work is `O(1)` or proportional to just the carry chain's length
- **Space Complexity:** `O(1)` in the common cases where the increment happens in place on `digits`; `O(n)` only in the all-`9`s case, where a new array one element longer is allocated to hold the result (this is unavoidable, since the output genuinely has one more digit than the input in that case)

---

## Key Insight

Simulating manual "carry the one" arithmetic — walking from the least significant digit and propagating a carry leftward until it's absorbed by a non-`9` digit — is the standard technique for digit-array increment/addition problems. The one edge case that breaks the simple "walk and stop" pattern is when the carry propagates all the way past the most significant digit, which is precisely when the result needs an extra digit; detecting that case up front (by checking whether *every* digit is a `9`) is what allows the rest of the logic to safely assume it can always find a non-`9` digit to stop at.

---

## Concepts Used

- Array
- Digit-by-Digit Arithmetic Simulation
- Carry Propagation
- Edge Case Handling (Array Growth)