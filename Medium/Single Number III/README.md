# LeetCode 260 - Single Number III

## Problem Statement

Given an integer array `nums`, in which exactly two elements appear only once and all the other elements appear exactly twice. Find the two elements that appear only once. You can return the answer in **any order**.

You must write an algorithm that runs in linear runtime complexity and uses only constant extra space.

---

## Example 1:
```
Input: nums = [1,2,1,3,2,5]
Output: [3,5]
Explanation: [5, 3] is also a valid answer.
```

## Example 2:
```
Input: nums = [-1,0]
Output: [-1,0]
```

## Example 3:
```
Input: nums = [0,1]
Output: [1,0]
```

## Constraints:
- `2 <= nums.length <= 3 * 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`
- Each integer in `nums` will appear twice, only two integers will appear once.

---

## Approach

This solution uses **XOR plus a partitioning bit**. XOR-ing the entire array cancels every pair and leaves `x ^ y`, where `x` and `y` are the two unique numbers. Since `x` and `y` are different, that result has at least one `1` bit, marking a position where they disagree. Splitting the array into two groups by that bit separates `x` from `y`, and XOR-ing each group on its own reveals each number.

### Idea

- **Step 1 — XOR everything:** `ans = ans ^ nums[i]` across the whole array. Every number that appears twice cancels itself out (`n ^ n = 0`), so `ans` ends up equal to `x ^ y`.
- **Step 2 — pick a bit where `x` and `y` differ:** `rightMost = (ans & (ans - 1)) ^ ans`. The expression `ans & (ans - 1)` clears the lowest set bit of `ans`; XOR-ing that with `ans` then leaves *only* that lowest set bit. Because `ans = x ^ y`, a set bit means exactly one of `x` or `y` has that bit set. (Note: in Java, `ans & ans-1` evaluates as `ans & (ans-1)`, since subtraction binds tighter than `&`.)
- **Step 3 — partition and XOR separately:** loop through `nums` again, keeping two accumulators `a` and `b`:
  - If `(nums[i] & rightMost) == 0` (the chosen bit is `0`), fold it into `a`: `a = a ^ nums[i]`.
  - Otherwise (the bit is `1`), fold it into `b`: `b = b ^ nums[i]`.
- Return `new int[]{a, b}`.

---

## Code

```java
class Solution {
    public int[] singleNumber(int[] nums) {
        int ans = 0;

        for(int i = 0 ; i < nums.length ; i++){
            ans = ans ^ nums[i];
        }

        int rightMost = (ans & ans-1) ^ ans;
        int a = 0;
        int b = 0;

        for(int i = 0 ; i < nums.length ; i++){
            if((nums[i] & rightMost) == 0){
                a = a ^ nums[i];
            }else{
                b = b ^ nums[i];
            }
        }

        return new int[]{a,b};
    }
}
```

---

## Dry Run

### Input (Example 1)

```text
nums = [1, 2, 1, 3, 2, 5]
```

Binary reference: `1 = 001`, `2 = 010`, `3 = 011`, `5 = 101`

### Phase 1 — XOR the whole array

| i | nums[i] | ans = ans ^ nums[i] |
|---|---------|------------------------|
|0|1 (`001`)|`000 ^ 001 = 001` (1)|
|1|2 (`010`)|`001 ^ 010 = 011` (3)|
|2|1 (`001`)|`011 ^ 001 = 010` (2)|
|3|3 (`011`)|`010 ^ 011 = 001` (1)|
|4|2 (`010`)|`001 ^ 010 = 011` (3)|
|5|5 (`101`)|`011 ^ 101 = 110` (6)|

`ans = 110 = 6`, which is exactly `3 ^ 5` (`011 ^ 101 = 110`).

### Phase 2 — Isolate the lowest set bit

| Step | Calculation | Result |
|------|-------------|--------|
|`ans - 1`|`110 - 1`|`101` (5)|
|`ans & (ans - 1)`|`110 & 101`|`100` (4)|
|`rightMost = (ans & (ans-1)) ^ ans`|`100 ^ 110`|`010` (2)|

`rightMost = 010 = 2` — the bit at position 1 is where `3` (`011`) and `5` (`101`) differ.

### Phase 3 — Partition by that bit and XOR each group

`a = 0`, `b = 0`

| i | nums[i] | nums[i] & `010` | Group | Update |
|---|---------|--------------------|-------|--------|
|0|1 (`001`)|`000` = 0|a|`a = 0 ^ 1 = 1`|
|1|2 (`010`)|`010` ≠ 0|b|`b = 0 ^ 2 = 2`|
|2|1 (`001`)|`000` = 0|a|`a = 1 ^ 1 = 0`|
|3|3 (`011`)|`010` ≠ 0|b|`b = 2 ^ 3 = 1`|
|4|2 (`010`)|`010` ≠ 0|b|`b = 1 ^ 2 = 3`|
|5|5 (`101`)|`000` = 0|a|`a = 0 ^ 5 = 5`|

Group `a` contained `{1, 1, 5}` → `5`. Group `b` contained `{2, 3, 2}` → `3`.

**Return:** `[5, 3]`

Output:

```text
[5,3]
```

This matches the expected output — the problem explicitly notes `[5, 3]` is also a valid answer, since order doesn't matter.

### Example 2 check — negative numbers

```text
nums = [-1, 0]
```

`ans = -1 ^ 0 = -1` (all bits set). `ans - 1 = -2`, `ans & (ans-1) = -1 & -2 = -2`, and `rightMost = -2 ^ -1 = 1`. Then `-1 & 1 = 1` (non-zero) → `b = -1`, and `0 & 1 = 0` → `a = 0`. Result `[0, -1]`, a valid ordering of `[-1, 0]`.

---

## Why This Approach Works

Pairs cancel under XOR, so after Step 1 the only thing left is `x ^ y`. If `x ^ y` has a `1` in some bit position, then one of `x`, `y` has a `1` there and the other has a `0` — so that bit **cleanly separates the two unique numbers**. And since `x ≠ y` is guaranteed, `x ^ y` can never be `0`, so such a bit always exists.

Splitting `nums` by that bit achieves two things simultaneously:

- `x` and `y` land in **different** groups (that's what "differ at this bit" means).
- Each duplicate pair lands **together** in the same group, because two equal numbers have identical bits everywhere, including this one.

So within each group, every duplicate pair still cancels out under XOR, leaving exactly one unique number per group — no further bookkeeping needed.

Extracting the lowest set bit with `(ans & (ans - 1)) ^ ans` is a standard bit trick: subtracting `1` flips the lowest `1` and everything below it, so ANDing with the original clears just that bit, and XOR-ing back isolates it. This also stays correct for negative values and for the edge case `ans = Integer.MIN_VALUE`, thanks to two's-complement wraparound (`MIN_VALUE - 1` wraps to `MAX_VALUE`, giving `rightMost = MIN_VALUE`, which is indeed its lowest set bit).

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — two linear passes over the array, with constant bitwise work per element
- **Space Complexity:** `O(1)` — only a handful of integer variables are used, no auxiliary arrays or hash sets, satisfying the constant-space requirement

---

## Key Insight

This builds directly on Single Number (136): when *one* element is unique, XOR-ing everything finds it. With *two* unique elements, a single XOR only gives their combination, so the trick is to use one differing bit to split the problem into **two independent copies of the single-unique-element problem**, each solvable with a plain XOR.

---

## Concepts Used

- Array
- Bit Manipulation (XOR)
- Lowest Set Bit Extraction
- Partitioning by a Differentiating Bit
- Constant Space Algorithm