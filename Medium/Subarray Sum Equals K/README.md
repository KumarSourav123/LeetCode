# LeetCode 560 - Subarray Sum Equals K

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

This solution uses a **running prefix sum combined with a HashMap** that tracks how many times each prefix sum has occurred.

### Idea

- Maintain a running `sum` as the array is traversed, representing the prefix sum from index `0` up to the current index.
- If `sum == k` at any point, the subarray from the start of the array up to the current index sums to `k`, so increment `count` directly.
- For every current `sum`, check whether `sum - k` (the "remainder," `rem`) has occurred as a prefix sum before. If it has, it means there exists some earlier index where the prefix sum was `rem`, so the subarray **between that earlier point and the current index** sums to exactly `k`. Add the number of times `rem` has occurred (`map.get(rem)`) to `count`, since each earlier occurrence marks a distinct valid subarray ending at the current index.
- Finally, record the current `sum` in the map, incrementing its occurrence count for future indices to reference.

---

## Code

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        int count = 0;

        for (int i = 0; i < nums.length; i++) {
            sum = sum + nums[i];

            if (sum == k) {
                count++;
            }

            int rem = sum - k;
            if (map.containsKey(rem)) {
                count += map.get(rem);
            }

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

**Initial:** `sum = 0`, `count = 0`, `map = {}`

| i | nums[i] | sum | sum==k? | rem = sum-k | map.containsKey(rem)? | count | map after step |
|---|---------|-----|---------|--------------|---------------------------|-------|-----------------|
|0|1|1|No|-1|No|0|{1:1}|
|1|1|2|Yes → count++|0|No|1|{1:1, 2:1}|
|2|1|3|No|1|Yes (map[1]=1) → count += 1|2|{1:1, 2:1, 3:1}|

Output:

```text
2
```

This matches the two valid subarrays: `[1,1]` (indices 0–1) and `[1,1]` (indices 1–2).

---

## Why This Approach Works

If the prefix sum up to index `j` is `S_j`, and the prefix sum up to an earlier index `i` is `S_i`, then the sum of the subarray between `i+1` and `j` is `S_j - S_i`. We want this difference to equal `k`, which rearranges to `S_i = S_j - k`.

So at every index, checking how many times `sum - k` has appeared before tells us exactly how many earlier starting points create a subarray ending here that sums to `k`. Storing counts (not just presence) in the map correctly handles cases where the same prefix sum occurs multiple times, since each occurrence represents a distinct valid subarray.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array with O(1) HashMap operations
- **Space Complexity:** `O(n)` — for storing prefix sum counts in the HashMap

---

## Key Insight

Instead of checking every possible subarray with nested loops (`O(n^2)`), tracking prefix sums and their frequencies lets us determine, in constant time per index, how many earlier positions would form a valid subarray summing to `k` — reducing the problem to a single linear pass.

---

## Concepts Used

- Array
- HashMap
- Hashing
- Prefix Sum
- Frequency Counting
- Constant-Time Lookup