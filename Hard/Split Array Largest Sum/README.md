# LeetCode 410 - Split Array Largest Sum

## Problem Statement

Given an integer array `nums` and an integer `k`, split `nums` into `k` non-empty subarrays such that the largest sum of any subarray is **minimized**.

Return *the minimized largest sum of the split*.

A subarray is a contiguous part of the array.

---

## Example 1:
```
Input: nums = [7,2,5,10,8], k = 2
Output: 18
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [7,2,5] and [10,8], where the largest sum among the two subarrays is only 18.
```

## Example 2:
```
Input: nums = [1,2,3,4,5], k = 2
Output: 9
Explanation: There are four ways to split nums into two subarrays.
The best way is to split it into [1,2,3] and [4,5], where the largest sum among the two subarrays is only 9.
```

## Constraints:
- `1 <= nums.length <= 1000`
- `0 <= nums[i] <= 10^6`
- `1 <= k <= min(50, nums.length)`

---

## Approach

This solution uses **binary search on the answer**, searching over possible "largest subarray sum" values to find the smallest one achievable when splitting into `k` parts.

### Idea

- **Impossibility check:** if `nums.length < k`, there aren't enough elements to form `k` non-empty subarrays — return `-1`.
- The valid range for the "largest sum" spans from `max(nums)` (since any single element alone must fit within a subarray) up to `sum(nums)` (the case of putting everything into just one subarray) — these become the binary search bounds `low` and `high`, computed in a single pass.
- For a candidate maximum sum `mid`, use the helper `numCount(arr, mid)` to greedily determine how many subarrays would be needed if no subarray's sum could exceed `mid`:
  - Traverse the array, accumulating a running `value` for the current subarray.
  - If adding the next element (`value + arr[i]`) would stay within `mid`, add it to the current subarray.
  - Otherwise, that element must start a **new** subarray — increment `count` and reset `value` to just that element.
  - Return the total `count` of subarrays formed (starting from `1`, since the first subarray always begins with at least one element).
- **Binary search decision:**
  - If `numCount(nums, mid) > k`, splitting with a max sum of `mid` requires **more** subarrays than allowed — `mid` is too small, so a **larger** max sum is needed to reduce the split count — search right (`low = mid + 1`).
  - Otherwise (`numCount(nums, mid) <= k`), `mid` is large enough — this is a valid (though not necessarily minimal) candidate, so try to find a **smaller** valid max sum by searching left (`high = mid - 1`).
- Once the loop ends, `low` has converged on the minimum valid maximum sum, so it's returned directly.

---

## Code

```java
class Solution {
    public int splitArray(int[] nums, int k) {
        if (nums.length < k) {
            return -1;
        }

        int low = Integer.MIN_VALUE;
        int high = 0;

        for (int num : nums) {
            low = Math.max(low, num);
            high = high + num;
        }

        while (low <= high) {
            int mid = low + (high - low) / 2;

            int answer = numCount(nums, mid);

            if (answer > k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return low;
    }

    private int numCount(int[] arr, int mid) {
        int count = 1;
        int value = 0;

        for (int i = 0; i < arr.length; i++) {
            if (value + arr[i] <= mid) {
                value += arr[i];
            } else {
                count++;
                value = arr[i];
            }
        }

        return count;
    }
}
```

---

## Dry Run

### Input

```text
nums = [7,2,5,10,8], k = 2
```

**Setup:** `low = max(nums) = 10`, `high = sum(nums) = 32`

| low | high | mid | numCount(nums, mid) simulation | answer | answer > k? | Action |
|-----|------|-----|--------------------------------------|--------|-------------------|--------|
|10|32|21|[7,2,5] \| [10,8] → 2 subarrays|2|2 > 2? No|high = 20|
|10|20|15|[7,2,5] \| [10] \| [8] → 3 subarrays|3|3 > 2? Yes|low = 16|
|16|20|18|[7,2,5] \| [10,8] → 2 subarrays|2|2 > 2? No|high = 17|
|16|17|16|[7,2,5] \| [10] \| [8] → 3 subarrays|3|3 > 2? Yes|low = 17|
|17|17|17|[7,2,5] \| [10] \| [8] → 3 subarrays|3|3 > 2? Yes|low = 18|

**Loop check:** `low = 18`, `high = 17` → false → loop exits.

Output:

```text
18
```

This matches the expected output (`[7,2,5]` and `[10,8]`, largest sum `18`).

---

## Why This Approach Works

As the allowed "max subarray sum" increases, the number of subarrays needed to fit everything within that cap **monotonically decreases** (or stays the same) — a larger cap can always accommodate at least as much per subarray as a smaller one. This monotonicity is what makes binary search valid over the range of possible max sums.

The greedy `numCount` simulation correctly computes the *minimum* number of subarrays needed for a given cap, since always packing as much as possible into the current subarray before starting a new one is provably optimal for minimizing subarray count under a fixed sum limit.

---

## Complexity Analysis

- **Time Complexity:** `O(n log(sum(nums)))` — for each of the `O(log(sum))` binary search steps, `numCount` scans all `n` elements
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

This is another instance of the **"binary search on the answer"** pattern shared with problems like Koko Eating Bananas and Capacity to Ship Packages: rather than trying every possible split directly (which would be combinatorially expensive), binary searching over the range of possible "largest subarray sum" values — using a greedy feasibility check as the comparison — converges on the true minimum efficiently.

---

## Concepts Used

- Array
- Binary Search on the Answer
- Monotonic Feasibility Function
- Greedy Simulation