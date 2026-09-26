# LeetCode 485 - Max Consecutive Ones

## Problem Statement

Given a binary array `nums`, return *the maximum number of consecutive* `1`*'s in the array*.

---

## Example 1:
```
Input: nums = [1,1,0,1,1,1]
Output: 3
Explanation: The first two digits or the last three digits are consecutive 1s. The maximum number of consecutive 1s is 3.
```

## Example 2:
```
Input: nums = [1,0,1,1,0,1]
Output: 2
```

## Constraints:
- `1 <= nums.length <= 10^5`
- `nums[i]` is either `0` or `1`.

---

## Approach

This solution makes a **single pass** through the array, keeping a running `count` of the current streak of consecutive `1`s, resetting it to zero the instant a `0` is encountered, and tracking the best streak seen so far in `maxCount`.

### Idea

- Initialize `count = 0` (the current run length) and `maxCount = 0` (the best run length found so far).
- **Loop** `for(int i = 0; i < nums.length; i++)`:
  - **If `nums[i] == 1`:** the current streak continues, so `count++`. Immediately check whether this extended streak is a new best: `if(count > maxCount) maxCount = count;`.
  - **Otherwise (`nums[i] == 0`):** the streak is broken, so `count` resets back to `0`.
- After the loop, `maxCount` holds the length of the longest run of consecutive `1`s seen anywhere in the array. Return it.

---

## Code

```java
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int count = 0;
        int maxCount = 0;

        for(int i = 0 ; i <nums.length ; i++){
            if(nums[i] == 1){
                count++;
                if(count > maxCount){
                    maxCount = count;
                }
            }else{
                count = 0;
            }
        }
        return maxCount;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1, 1, 0, 1, 1, 1]
index:  0  1  2  3  4  5
```

`count = 0`, `maxCount = 0`

| i | nums[i] | Action | count after | count > maxCount? | maxCount after |
|---|---------|--------|--------------|-------------------|--------------------|
|0|1|`count++`|1|1 > 0? yes|**1**|
|1|1|`count++`|2|2 > 1? yes|**2**|
|2|0|`count = 0`|0|—|2|
|3|1|`count++`|1|1 > 2? no|2|
|4|1|`count++`|2|2 > 2? no|2|
|5|1|`count++`|3|3 > 2? yes|**3**|

**Loop check:** `i(6) < nums.length(6)`? false → loop exits.

**Return:** `maxCount = 3`

Output:

```text
3
```

This matches the expected output — the longest run of consecutive `1`s is the last three elements, `[1,1,1]`.

---

## Why This Approach Works

Since a "run of consecutive `1`s" can only be extended by seeing another `1` immediately next, and is unconditionally broken by any `0`, a single running counter is sufficient to track the *current* run's length at every position: increment on a `1`, reset on a `0`. Comparing this running count against the best-seen-so-far at every increment (rather than only at the end, or only when a run breaks) ensures that even a run that extends all the way to the final element of the array is correctly captured, since there's no "end of run" event still pending after the loop finishes.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array, with constant work done per element
- **Space Complexity:** `O(1)` — only two integer variables are used, no auxiliary arrays or data structures

---

## Key Insight

This is a straightforward instance of the **running counter with reset** pattern: whenever a problem asks for the longest/maximum contiguous run of some property, a single variable that increments while the property holds and resets the moment it doesn't — compared against a running best at each step — solves it in one linear pass, without needing to explicitly identify and measure each run's boundaries separately.

---

## Concepts Used

- Array
- Single-Pass Traversal
- Running Counter / Streak Tracking
- Constant Space Algorithm