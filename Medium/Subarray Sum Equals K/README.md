# LeetCode 560 - Subarray Sum Equals K (Refined Version)

## Problem Statement

Given an array of integers `nums` and an integer `k`, return *the total number of subarrays whose sum equals to* `k`.

A subarray is a **contiguous non-empty** sequence of elements within an array.

---

## Example 1:
```
Input: nums = [1,1,1], k = 2
Output: 2
```

## Example 2:
```
Input: nums = [1,2,3], k = 3
Output: 2
```

## Constraints:
- `1 <= nums.length <= 2 * 10^4`
- `-1000 <= nums[i] <= 1000`
- `-10^7 <= k <= 10^7`

---

## Approach

This is a cleaner version of the prefix-sum + HashMap technique, which removes the special-cased `if (sum == k)` check by **pre-seeding the map with the empty prefix**.

### Idea

- Before the loop starts, initialize `map.put(sum, 1)` while `sum = 0`. This represents the "empty prefix" — the idea that a prefix sum of `0` has occurred once, **before** any elements have been added. This single seed value elegantly handles the case where a subarray starting from index `0` sums exactly to `k`, without needing a separate check.
- Maintain a running `sum` as the array is traversed.
- At each step, compute `rem = sum - k`. If `rem` has occurred as a prefix sum before (including the seeded `0`), add its occurrence count (`map.getOrDefault(rem, 0)`) directly to `count`.
- Record the current `sum` in the map, incrementing its occurrence count using `getOrDefault` for a concise one-line update.

Because the empty-prefix case is folded into the same map lookup as every other case, the logic becomes simpler and more uniform than checking `sum == k` separately.

---

## Code

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();

        int sum = 0;
        int count = 0;
        map.put(sum, 1);

        for (int i = 0; i < nums.length; i++) {
            sum = sum + nums[i];
            int rem = sum - k;
            count = count + map.getOrDefault(rem, 0);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,1,1], k = 2
```

**Initial:** `sum = 0`, `count = 0`, `map = {0: 1}` (seeded for the empty prefix)

| i | nums[i] | sum | rem = sum-k | map.getOrDefault(rem, 0) | count | map after step |
|---|---------|-----|--------------|-----------------------------|-------|-----------------|
|0|1|1|-1|0|0|{0:1, 1:1}|
|1|1|2|0|1 (seeded value)|1|{0:1, 1:1, 2:1}|
|2|1|3|1|1|2|{0:1, 1:1, 2:1, 3:1}|

Output:

```text
2
```

This matches the two valid subarrays: `[1,1]` (indices 0–1) and `[1,1]` (indices 1–2). Notice how at `i = 1`, the seeded `map[0] = 1` is what correctly credits the subarray from the very start of the array (`[1,1]`, indices 0–1) without any special-case branch.

---

## Why This Approach Works

The relationship `S_i = S_j - k` (where `S_i` and `S_j` are prefix sums at two indices) still holds exactly as before. The only refinement here is recognizing that a subarray beginning at index `0` is just the special case where the "earlier" prefix sum is `0`, corresponding to no elements at all.

By seeding `map.put(0, 1)` before the loop starts, that special case is captured naturally by the same `rem` lookup used for every other index, removing the need for a separate `if (sum == k)` branch.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array with O(1) HashMap operations
- **Space Complexity:** `O(n)` — for storing prefix sum counts in the HashMap

---

## Key Insight

Seeding the map with the "empty prefix" (`sum = 0` occurring once) is a common and powerful pattern in prefix-sum problems — it unifies the handling of subarrays that start from index `0` with all other subarrays, resulting in cleaner, more symmetric code compared to handling that case separately.

---

## Concepts Used

- Array
- HashMap
- Hashing
- Prefix Sum
- Frequency Counting
- Base Case Seeding