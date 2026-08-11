# LeetCode 162 - Find Peak Element

## Problem Statement

A peak element is an element that is strictly greater than its neighbors.

Given a **0-indexed** integer array `nums`, find a peak element, and return its index. If the array contains multiple peaks, return the index to **any of the peaks**.

You may imagine that `nums[-1] = nums[n] = -∞`. In other words, an element is always considered to be strictly greater than a neighbor that is outside the array.

You must write an algorithm that runs in `O(log n)` time.

---

## Example 1:
```
Input: nums = [1,2,3,1]
Output: 2
Explanation: 3 is a peak element and your function should return the index number 2.
```

## Example 2:
```
Input: nums = [1,2,1,3,5,6,4]
Output: 5
Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
```

## Constraints:
- `1 <= nums.length <= 1000`
- `-2^31 <= nums[i] <= 2^31 - 1`
- `nums[i] != nums[i + 1]` for all valid `i`.

---

## Approach

This solution first handles simple boundary cases directly, then uses a **binary search** over the remaining interior of the array to locate a peak.

### Idea

- **Boundary checks first:**
  - If `nums.length == 1`, the single element is trivially a peak (since both neighbors are considered `-∞`) — return index `0`.
  - If `nums[low] > nums[low+1]` (the very first element is greater than its right neighbor), the first element is a peak — return `low` (`0`).
  - If `nums[high] > nums[high-1]` (the very last element is greater than its left neighbor), the last element is a peak — return `high`.
- If neither boundary is a peak, then a peak must exist strictly **within** the interior of the array (excluding the first and last elements) — narrow the search range by moving `low++` and `high--`, then binary search that interior range.
- **`binarySearch(arr, start, end)`** — within the interior:
  - Compute `mid`.
  - If `arr[mid-1] < arr[mid]` and `arr[mid] > arr[mid+1]`, `mid` is a peak (strictly greater than both neighbors) — return `mid`.
  - Else if `arr[mid-1] > arr[mid]`, the array is still descending going into `mid` from the left, meaning a peak must exist somewhere to the left — search left (`end = mid - 1`).
  - Otherwise (the array is still ascending at `mid`, meaning `arr[mid] < arr[mid+1]`), a peak must exist somewhere to the right — search right (`start = mid + 1`).

---

## Code

```java
class Solution {
    public int findPeakElement(int[] nums) {

        int low = 0;
        int high = nums.length - 1;

        if (nums.length == 1) {
            return 0;
        }
        if (nums[low] > nums[low + 1]) {
            return low;
        }
        if (nums[high] > nums[high - 1]) {
            return high;
        }

        low++;
        high--;

        return binarySearch(nums, low, high);
    }

    private int binarySearch(int[] arr, int start, int end) {

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if ((arr[mid - 1] < arr[mid]) && (arr[mid] > arr[mid + 1])) {
                return mid;
            } else if (arr[mid - 1] > arr[mid]) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return 0;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,2,3,1]
```

`nums.length = 4` (not 1). `nums[0]=1 > nums[1]=2`? No. `nums[3]=1 > nums[2]=3`? No. Neither boundary is a peak.

**Narrow the interior:** `low++ → low=1`, `high-- → high=2`

**Binary search on interior** (`start=1`, `end=2`):

| start | end | mid | arr[mid-1], arr[mid], arr[mid+1] | Condition Check | Action |
|-------|-----|-----|---------------------------------------|----------------------|--------|
|1|2|1|arr[0]=1, arr[1]=2, arr[2]=3|1<2 (true) && 2>3 (false) → not a peak. arr[0]=1 > arr[1]=2? No|start = mid+1 = 2|
|2|2|2|arr[1]=2, arr[2]=3, arr[3]=1|2<3 (true) && 3>1 (true) → **peak found**|return mid = 2|

Output:

```text
2
```

This matches the expected output (`nums[2] = 3` is a peak).

---

## Why This Approach Works

Handling the two boundary elements separately simplifies the binary search: within the interior, every `mid` has valid neighbors on both sides to compare against, so the peak-detection condition (`arr[mid-1] < arr[mid] && arr[mid] > arr[mid+1]`) can be checked without any out-of-bounds risk.

When `mid` isn't a peak, the array must be strictly increasing or strictly decreasing at that point (since no two adjacent elements are equal, per the constraints) — if it's decreasing going into `mid` (`arr[mid-1] > arr[mid]`), a peak must exist somewhere to the left (since values were rising before this point and are now falling); otherwise, a peak must exist to the right. This directional reasoning is what allows binary search to correctly halve the search space at every step, even though the array isn't fully sorted.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` — binary search over the interior of the array
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

The "imagine `nums[-1] = nums[n] = -∞`" framing in the problem statement is what guarantees a peak always exists somewhere in the array — by explicitly checking the two true boundary elements first (where this virtual `-∞` neighbor matters) and then restricting binary search to the safe interior, the core search logic never needs to worry about out-of-bounds neighbor access.

---

## Concepts Used

- Array
- Binary Search
- Local Maximum Detection
- Boundary Case Handling