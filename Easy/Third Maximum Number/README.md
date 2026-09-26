# LeetCode 414 - Third Maximum Number

## Problem Statement

Given an integer array `nums`, return *the third distinct maximum number in this array*. If the third maximum does not exist, return *the maximum number*.

---

## Example 1:
```
Input: nums = [3,2,1]
Output: 1
Explanation:
The first distinct maximum is 3.
The second distinct maximum is 2.
The third distinct maximum is 1.
```

## Example 2:
```
Input: nums = [1,2]
Output: 2
Explanation:
The first distinct maximum is 2.
The second distinct maximum is 1.
The third distinct maximum does not exist, so the maximum (2) is returned instead.
```

## Example 3:
```
Input: nums = [2,2,3,1]
Output: 1
Explanation:
The first distinct maximum is 3.
The second distinct maximum is 2 (both 2's are counted together since they have the same value).
The third distinct maximum is 1.
```

## Constraints:
- `1 <= nums.length <= 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`

**Follow up:** Can you find an `O(n)` solution?

---

## Approach

This solution tracks the top three **distinct** values seen so far (`max`, `secondMax`, `thirdMax`) in a single pass, using `long` for all three so that `Long.MIN_VALUE` can safely serve as a genuine "nothing found yet" sentinel — since the array can itself contain `Integer.MIN_VALUE`, using `int` with `Integer.MIN_VALUE` as the sentinel could otherwise be ambiguous with a real value in the array.

### Idea

- Initialize `max`, `secondMax`, and `thirdMax` all to `Long.MIN_VALUE` (as `long`s, safely below any possible `int` value in `nums`), and `count = 0` to track how many distinct top values have actually been found.
- **Loop** through each `nums[i]`, checking three mutually exclusive cases in order:
  - **New overall maximum:** if `nums[i] > max`, everything shifts down a rank — `thirdMax = secondMax`, `secondMax = max`, `max = nums[i]` — and `count++`.
  - **New second-highest distinct value:** `else if (nums[i] > secondMax) && (nums[i] != max)` — the `!= max` check is essential to ensure a **duplicate** of the current maximum doesn't get mistakenly treated as a new distinct second value. If it qualifies, `thirdMax = secondMax`, `secondMax = nums[i]`, and `count++`.
  - **New third-highest distinct value:** `else if (nums[i] > thirdMax) && (nums[i] != secondMax) && (nums[i] != max)` — again guarding against duplicates of either higher rank. If it qualifies, `thirdMax = nums[i]` and `count++`.
  - If none of the three conditions hold (the value is a duplicate of an existing top-3 value, or too small to place), nothing happens for this element.
- After the loop, `count` reflects how many **distinct** values have been successfully placed into the top-3 tracking. **If `count >= 3`**, a genuine third-distinct-maximum was found — return `thirdMax` (cast back to `int`). **Otherwise**, fewer than 3 distinct values exist in the whole array, so per the problem's fallback rule, return `max` instead.

---

## Code

```java
class Solution {
    public int thirdMax(int[] nums) {
        long max = Long.MIN_VALUE;
        long secondMax = Long.MIN_VALUE;
        long thirdMax = Long.MIN_VALUE;
        int count = 0;

        for(int i = 0; i<nums.length ; i++){
            if(nums[i] > max){
                thirdMax = secondMax;
                secondMax = max;
                max = nums[i];
                count++;
            }
            else if((nums[i] > secondMax) && (nums[i] != max)){
                thirdMax = secondMax;
                secondMax = nums[i];
                count++;
            }
            else if((nums[i] > thirdMax) && (nums[i] != secondMax) && (nums[i] != max)){
                thirdMax = nums[i];
                count++;
            }
        }
        if(count >= 3){
            return (int)thirdMax;
        }else{
            return (int)max;
        }
    }
}
```

---

## Dry Run

### Input

```text
nums = [2, 2, 3, 1]
index:  0  1  2  3
```

`max = MIN`, `secondMax = MIN`, `thirdMax = MIN`, `count = 0` (using `MIN` to denote `Long.MIN_VALUE`)

| i | nums[i] | nums[i] > max? | 2nd condition | 3rd condition | Action | max / secondMax / thirdMax | count |
|---|---------|-----------------|-----------------------------------|--------------------------------------------------|--------|-------------------------------------|-------|
|0|2|`2 > MIN` → yes|—|—|shift down; `max=2`|thirdMax=MIN, secondMax=MIN, max=2|1|
|1|2|`2 > 2` → no|`2 > MIN` true, but `2 != max(2)` → **false**|not reached|no branch taken|thirdMax=MIN, secondMax=MIN, max=2|1|
|2|3|`3 > 2` → yes|—|—|shift down; `max=3`|thirdMax=MIN, secondMax=2, max=3|2|
|3|1|`1 > 3` → no|`1 > secondMax(2)` → false|`1 > thirdMax(MIN)` true, `1 != secondMax(2)` true, `1 != max(3)` true → **all true**|`thirdMax = 1`|thirdMax=1, secondMax=2, max=3|3|

**Loop check:** `i(4) < nums.length(4)`? false → loop exits.

**Final check:** `count(3) >= 3`? true → **return `(int) thirdMax = 1`**

Output:

```text
1
```

This matches the expected output — the three distinct values in the array, from highest to lowest, are `3`, `2`, `1`, and the third of those is `1`. The duplicate `2` at index 1 correctly contributed nothing new (it failed the `!= max` guard), confirming duplicates don't inflate the distinct-value count.

---

## Why This Approach Works

The three ranked variables (`max`, `secondMax`, `thirdMax`) together maintain the invariant "the top 3 **distinct** values seen so far, in order." Each of the three `if`/`else if` branches is checked in strictly descending priority, and each one's guard conditions (`!= max`, `!= secondMax`) are what prevent a duplicate value from being miscounted as a "new" distinct entry — without those checks, an array like `[2,2,3,1]` might incorrectly treat the second `2` as filling the third-max slot, corrupting the ranking.

Using `long` with `Long.MIN_VALUE` as the "not found yet" sentinel (rather than `int` with `Integer.MIN_VALUE`) is a deliberate correctness safeguard: since the problem allows `nums[i]` to be as low as `Integer.MIN_VALUE`, using that same value as a sentinel in `int` arithmetic could cause a real array value to be indistinguishable from "no value found," breaking the comparisons. Widening to `long` sidesteps this ambiguity entirely, since `Long.MIN_VALUE` is far below any possible `int` value.

Tracking `count` alongside the three ranked values is what implements the problem's fallback rule cleanly: rather than checking whether `thirdMax` is still equal to its sentinel value (which risks the same ambiguity issue the `long` widening was meant to avoid), counting how many times a value was successfully placed gives an unambiguous signal for whether a genuine third distinct maximum exists.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array, with constant work (a few comparisons and assignments) per element, satisfying the follow-up's requirement
- **Space Complexity:** `O(1)` — only a fixed set of variables (`max`, `secondMax`, `thirdMax`, `count`) is used, no auxiliary arrays or sorting

---

## Key Insight

This is a generalization of the common "track the top-1 (or top-2) value in a single pass" pattern extended to three ranks, with the added wrinkle of needing to treat **duplicates** as non-distinct. The combination of explicit inequality guards (`!= max`, `!= secondMax`) plus a `count` of successful placements — rather than relying on sentinel-value comparisons alone — is a robust way to correctly track "top-k distinct values" without needing to sort the array or use a set-based deduplication structure.

---

## Concepts Used

- Array
- Single-Pass Traversal
- Top-K Tracking with Ranked Variables
- Duplicate Handling via Explicit Guards
- Sentinel Values (with type-widening to avoid ambiguity)