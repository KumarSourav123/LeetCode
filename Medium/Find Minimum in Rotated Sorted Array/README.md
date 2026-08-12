# LeetCode 153 - Find Minimum in Rotated Sorted Array

## Problem Statement

Suppose an array of length `n` sorted in ascending order is rotated between `1` and `n` times. For example, the array `nums = [0,1,2,4,5,6,7]` might become:

- `[4,5,6,7,0,1,2]` if it was rotated 4 times.
- `[0,1,2,4,5,6,7]` if it was rotated 7 times.

Notice that rotating an array `[a[0], a[1], a[2], ..., a[n-1]]` 1 time results in the array `[a[n-1], a[0], a[1], a[2], ..., a[n-2]]`.

Given the sorted rotated array `nums` of **unique** elements, return *the minimum element of this array*.

You must write an algorithm that runs in `O(log n)` time.

---

## Example 1:
```
Input: nums = [3,4,5,1,2]
Output: 1
Explanation: The original array was [1,2,3,4,5] rotated 3 times.
```

## Example 2:
```
Input: nums = [4,5,6,7,0,1,2]
Output: 0
Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
```

## Example 3:
```
Input: nums = [11,13,15,17]
Output: 11
Explanation: The original array was [11,13,15,17] and it was rotated 4 times.
```

## Constraints:
- `n == nums.length`
- `1 <= n <= 5000`
- `-5000 <= nums[i] <= 5000`
- All the integers of `nums` are **unique**.
- `nums` is sorted and rotated between `1` and `n` times.

---

## Approach

A binary search that keeps a running candidate for the minimum while narrowing the search space toward the actual rotation point.

### Idea

- Initialize `index = nums[0]` as a starting candidate for the minimum (handles the case where the array isn't rotated at all, i.e. `low` never moves past the true minimum).
- Standard binary search loop with `low = 0`, `high = nums.length - 1`.
- At each step, compute `mid` and update the candidate: `index = min(index, nums[mid])`.
- Decide which half to continue searching:
  - If `nums[mid] < nums[high]`, the **right half is sorted** (no rotation point in it), so the minimum can't be past `mid` — search left: `high = mid - 1`.
  - Otherwise (`nums[mid] >= nums[high]`), the rotation point is somewhere in the right half — search right: `low = mid + 1`.
- Return `index` once `low > high`.

---

## Code

```java
class Solution {
    public int findMin(int[] nums) {

        int index = nums[0];
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (nums[mid] < index) {
                index = nums[mid];
            }
            if (nums[mid] < nums[high]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return index;
    }
}
```

---

## Dry Run

### Input

```text
nums = [4,5,6,7,0,1,2]
```

`index = nums[0] = 4`, `low=0`, `high=6`

| low | high | mid | nums[mid] | index update | nums[mid] < nums[high]? | Action |
|-----|------|-----|-----------|---------------|--------------------------|--------|
|0|6|3|7|min(4,7) = 4|nums[3]=7 < nums[6]=2? No|low = mid+1 → 4|
|4|6|5|1|min(4,1) = 1|nums[5]=1 < nums[6]=2? Yes|high = mid-1 → 4|
|4|4|4|0|min(1,0) = 0|nums[4]=0 < nums[4]=0? No (equal)|low = mid+1 → 5|

Loop ends (`low=5 > high=4`).

**Final Result:**

```text
0
```

This matches the expected output.

---

## Why This Approach Works

In a rotated sorted array, exactly one of the two halves around `mid` is guaranteed to be "normally" sorted (no drop in value), while the other half contains the rotation point (and therefore the minimum). Comparing `nums[mid]` to `nums[high]` tells us which side we're on:

- If `nums[mid] < nums[high]`, the segment from `mid` to `high` is strictly sorted, so no rotation point (and no smaller value) can hide there — the minimum must be at `mid` or somewhere to its left.
- If `nums[mid] >= nums[high]`, the rotation point lies between `mid` and `high`, so the minimum must be strictly to the right of `mid`.

Tracking `index = min(index, nums[mid])` throughout guarantees the smallest value ever visited is kept, so even though the search space keeps shrinking around the rotation point, the true minimum is captured along the way.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` — guaranteed, since all elements are unique there's no ambiguous case like in problem 81
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

Comparing `nums[mid]` against `nums[high]` (rather than `nums[low]`) makes the "which half is sorted" check unambiguous, since `high` always shrinks toward the rotation point from the correct side. Because problem 153 guarantees **unique** elements (unlike problem 81), this comparison is always decisive — there's no scenario forcing a fallback to linear search, so `O(log n)` is guaranteed rather than just average-case.

---

## Concepts Used

- Array
- Binary Search
- Rotated Sorted Array
- Search Space Reduction