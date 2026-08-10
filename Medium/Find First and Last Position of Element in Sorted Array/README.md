# LeetCode 34 - Find First and Last Position of Element in Sorted Array

## Problem Statement

Given an array of integers `nums` sorted in non-decreasing order, find the starting and ending position of a given `target` value.

If `target` is not found in the array, return `[-1, -1]`.

You must write an algorithm with `O(log n)` runtime complexity.

---

## Example 1:
```
Input: nums = [5,7,7,8,8,10], target = 8
Output: [3,4]
```

## Example 2:
```
Input: nums = [5,7,7,8,8,10], target = 6
Output: [-1,-1]
```

## Example 3:
```
Input: nums = [], target = 0
Output: [-1,-1]
```

## Constraints:
- `0 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`
- `nums` is a non-decreasing array.
- `-10^9 <= target <= 10^9`

---

## Approach

This solution runs **two separate binary searches**: one to find the **first** occurrence of `target`, and another to find the **last** occurrence.

### Idea

- Handle the empty array edge case upfront: if `nums.length == 0`, immediately return `[-1, -1]`.
- **`firstIndex(arr, target)`** — finds the leftmost occurrence of `target`:
  - Standard binary search with `low`, `high`, and a tracked candidate `ans`.
  - If `arr[mid] >= target`, this position could be (or be past) the first occurrence — record `ans = mid` and continue searching **left** (`high = mid - 1`) for an even earlier occurrence.
  - Otherwise, search **right** (`low = mid + 1`).
  - After the loop, verify that `arr[ans]` actually equals `target` (since `ans` might have landed on a value greater than `target` if `target` doesn't exist in the array) — return `ans` if it matches, or `-1` otherwise.
- **`secondIndex(arr, target)`** — finds the rightmost occurrence of `target`, using the mirrored logic:
  - If `arr[mid] <= target`, this position could be (or be before) the last occurrence — record `ans = mid` and search **right** (`low = mid + 1`) for a later occurrence.
  - Otherwise, search **left** (`high = mid - 1`).
  - Again, verify `arr[ans] == target` before returning `ans`, or return `-1` if it doesn't match.
- Combine both results into a single `int[]{a, b}` and return it.

---

## Code

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) {
            return new int[]{-1, -1};
        }

        int a = firstIndex(nums, target);
        int b = secondIndex(nums, target);

        return new int[]{a, b};
    }

    private int firstIndex(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        int ans = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] >= target) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        if (target == arr[ans]) {
            return ans;
        } else {
            return -1;
        }
    }

    private int secondIndex(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        int ans = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] <= target) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (target == arr[ans]) {
            return ans;
        } else {
            return -1;
        }
    }
}
```

---

## Dry Run

### Input

```text
nums = [5,7,7,8,8,10], target = 8
```

**`firstIndex` search:** `low=0`, `high=5`, `ans=0`

| low | high | mid | arr[mid] | arr[mid] >= target? | Action |
|-----|------|-----|------------|--------------------------|--------|
|0|5|2|7|7 >= 8? No|low = 3|
|3|5|4|8|8 >= 8? Yes|ans = 4, high = 3|
|3|3|3|8|8 >= 8? Yes|ans = 3, high = 2|

Loop ends (`low=3 > high=2`). Check: `arr[3] = 8 == target(8)`? Yes → `firstIndex` returns `3`

**`secondIndex` search:** `low=0`, `high=5`, `ans=0`

| low | high | mid | arr[mid] | arr[mid] <= target? | Action |
|-----|------|-----|------------|--------------------------|--------|
|0|5|2|7|7 <= 8? Yes|ans = 2, low = 3|
|3|5|4|8|8 <= 8? Yes|ans = 4, low = 5|
|5|5|5|10|10 <= 8? No|high = 4|

Loop ends (`low=5 > high=4`). Check: `arr[4] = 8 == target(8)`? Yes → `secondIndex` returns `4`

**Final Result:**

```text
[3, 4]
```

This matches the expected output.

---

## Why This Approach Works

The `firstIndex` search always moves left whenever it finds a value `>= target`, which means it keeps narrowing toward the **earliest** position satisfying that condition — since the array is sorted, the earliest position where `arr[mid] >= target` holds is exactly where `target` first appears (if it exists at all).

Symmetrically, `secondIndex` always moves right whenever it finds a value `<= target`, converging on the **latest** position where `target` could still appear. The final verification step (`arr[ans] == target`) is essential in both functions, since without it, `ans` could land on the position of the next-larger (or next-smaller) value if `target` isn't actually present in the array — silently returning a wrong index instead of `-1`.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` — two independent binary searches
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

Rather than performing a single binary search to find *any* occurrence of `target` and then scanning outward linearly to find the boundaries (which could degrade to `O(n)` if there are many duplicates), running two separately-biased binary searches — one that always prefers moving left on a match, one that always prefers moving right — directly finds both boundaries in `O(log n)` each, achieving the required overall `O(log n)` complexity regardless of how many duplicates exist.

---

## Concepts Used

- Array
- Binary Search
- Boundary Search (Leftmost/Rightmost Occurrence)
- Sorted Array Search