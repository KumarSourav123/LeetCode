# LeetCode 152 - Maximum Product Subarray

## Problem Statement

Given an integer array `nums`, find a subarray that has the largest product, and return *the product*.

The test cases are generated so that the answer will fit in a **32-bit** integer.

**Note** that the product of an array with a single element is the value of that element.

---

## Example 1:
```
Input: nums = [2,3,-2,4]
Output: 6
Explanation: [2,3] has the largest product 6.
```

## Example 2:
```
Input: nums = [-2,0,-1]
Output: 0
Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
```

## Constraints:
- `1 <= nums.length <= 2 * 10^4`
- `-10 <= nums[i] <= 10`
- The product of any subarray of `nums` is **guaranteed** to fit in a **32-bit** integer.

---

## Approach

This solution runs a **prefix product from the left and a suffix product from the right at the same time**, in a single loop. Both running products are compared against the best answer seen so far, and each one is reset to `1` the moment it hits `0`, so that a zero cleanly splits the array into independent segments.

### Idea

- Initialize `totalMax = Integer.MIN_VALUE` (so even an all-negative array is handled correctly), and `prefix = 1`, `suffix = 1`.
- **Loop** `for(int i = 0; i < nums.length; i++)`:
  - `prefix = prefix * nums[i]` — extend the running product walking **left to right**.
  - `suffix = suffix * nums[nums.length - 1 - i]` — extend the running product walking **right to left** (at step `i`, this reads the `i`th element from the end).
  - **Record the best so far:** if `prefix > totalMax`, update `totalMax = prefix`; likewise for `suffix`.
  - **Reset on zero:** if `prefix == 0`, set `prefix = 1`; if `suffix == 0`, set `suffix = 1`. This happens *after* the `totalMax` updates, so a `0` itself can still be recorded as the answer (when it's genuinely the best available) before the product restarts for the next segment.
- Return `totalMax`.

---

## Code

```java
class Solution {
    public int maxProduct(int[] nums) {
        int totalMax = Integer.MIN_VALUE;
        int prefix = 1;
        int suffix = 1;

        for(int i = 0 ; i < nums.length ; i++){
            prefix = prefix * nums[i];
            suffix = suffix * nums[nums.length-1-i];
            if(prefix > totalMax){
                totalMax = prefix;
            }
            if(suffix > totalMax){
                totalMax = suffix;
            }
            if(prefix == 0){
                prefix = 1;
            }
            if(suffix == 0){
                suffix = 1;
            }
        }

        return totalMax;
    }
}
```

---

## Dry Run

### Input (Example 1)

```text
nums = [2, 3, -2, 4]
index:  0  1   2  3
```

`totalMax = MIN`, `prefix = 1`, `suffix = 1`

| i | Element read for prefix | Element read for suffix | prefix after | suffix after | totalMax after | Reset? |
|---|---------------------------|----------------------------|--------------|--------------|-----------------|--------|
|0|`nums[0] = 2`|`nums[3] = 4`|`1 * 2 = 2`|`1 * 4 = 4`|2, then 4 → **4**|no|
|1|`nums[1] = 3`|`nums[2] = -2`|`2 * 3 = 6`|`4 * -2 = -8`|`6 > 4` → **6**; `-8 > 6`? no|no|
|2|`nums[2] = -2`|`nums[1] = 3`|`6 * -2 = -12`|`-8 * 3 = -24`|6 (neither beats it)|no|
|3|`nums[3] = 4`|`nums[0] = 2`|`-12 * 4 = -48`|`-24 * 2 = -48`|6 (neither beats it)|no|

**Loop check:** `i(4) < nums.length(4)`? false → loop exits.

**Return:** `totalMax = 6`

Output:

```text
6
```

This matches the expected output — the best product comes from the prefix `[2,3]`, which the left-to-right scan captured at `i = 1`.

### Input (Example 2 — exercises the zero reset)

```text
nums = [-2, 0, -1]
index:   0  1   2
```

`totalMax = MIN`, `prefix = 1`, `suffix = 1`

| i | Element read for prefix | Element read for suffix | prefix after multiply | suffix after multiply | totalMax after | Reset? |
|---|---------------------------|----------------------------|------------------------|------------------------|-----------------|--------|
|0|`nums[0] = -2`|`nums[2] = -1`|`-2`|`-1`|`-2 > MIN` → -2; `-1 > -2` → **-1**|no|
|1|`nums[1] = 0`|`nums[1] = 0`|`0`|`0`|`0 > -1` → **0**; `0 > 0`? no|prefix → 1, suffix → 1|
|2|`nums[2] = -1`|`nums[0] = -2`|`-1`|`-2`|0 (neither beats it)|no|

**Return:** `totalMax = 0`

Output:

```text
0
```

This matches the expected output. Notice the `0` was recorded into `totalMax` *before* the reset kicked in — that ordering is what lets a lone `0` be the correct answer when every other subarray product is negative.

---

## Why This Approach Works

Negative numbers are what make this problem tricky: a running product can flip from very negative to very positive when another negative arrives. The useful structural fact is about **zero-free segments**. Within a stretch of the array containing no zeros:

- If it has an **even** number of negatives, the product of the whole segment is positive, and that's the best possible for it.
- If it has an **odd** number of negatives, the whole segment's product is negative, so the best option is to drop **either** everything up to and including the *first* negative, **or** everything from the *last* negative onward. Either way, the optimal subarray is a **prefix or a suffix of the segment**.

So in every case, the best subarray inside a zero-free segment is either a prefix or a suffix of it. Scanning a running **prefix product from the left** captures every prefix, and scanning a running **suffix product from the right** captures every suffix — covering all candidates without needing to track a separate running minimum.

Resetting to `1` after a `0` is what handles segment boundaries: a zero would otherwise annihilate every product that follows it, so restarting at `1` begins a fresh, independent segment. Because the reset occurs **after** `totalMax` has been updated, the zero itself still counts as a candidate answer (a subarray consisting of just that `0`).

Initializing `totalMax` to `Integer.MIN_VALUE` rather than `0` ensures arrays where every possible product is negative (like `[-3]`) still return the right value.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single loop of `n` iterations, with constant work (two multiplications, a few comparisons) per iteration
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

Instead of the common approach of tracking both a running maximum *and* a running minimum product (to handle sign flips), this solution leans on a structural property: the optimal subarray is always a prefix or suffix of a zero-free segment. That reduces the problem to two independent running products, one from each end, plus a reset at zeros — a neat way to sidestep explicit sign-flip bookkeeping.

---

## Concepts Used

- Array
- Prefix Product / Suffix Product
- Single-Pass Traversal (two directions at once)
- Zero-Based Segment Reset
- Constant Space Algorithm