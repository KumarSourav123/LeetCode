# LeetCode 35 - Search Insert Position

## Problem Statement

Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.

You must write an algorithm with `O(log n)` runtime complexity.

---

## Example 1:
```
Input: nums = [1,3,5,6], target = 5
Output: 2
```

## Example 2:
```
Input: nums = [1,3,5,6], target = 2
Output: 1
```

## Example 3:
```
Input: nums = [1,3,5,6], target = 7
Output: 4
```

## Constraints:
- `1 <= nums.length <= 10^4`
- `-10^4 <= nums[i] <= 10^4`
- `nums` contains **distinct** values sorted in **ascending** order.
- `-10^4 <= target <= 10^4`

---

## Approach

This solution uses **binary search**, tracking the best candidate insertion index (`index`) as the search narrows down.

### Idea

- Initialize `index` to `nums.length` — this is the default insertion point if `target` turns out to be larger than every element in the array.
- Perform a standard binary search with `start` and `end` pointers:
  - Compute `mid = (start + end) / 2`.
  - If `nums[mid] >= target`, this position is a **valid candidate** for where `target` either exists or should be inserted — record it as `index`, then continue searching the **left half** (`end = mid - 1`) to check if an even earlier valid position exists.
  - Otherwise (`nums[mid] < target`), this position is too small, so search the **right half** (`start = mid + 1`).
- Once `start > end`, the search ends. The last recorded `index` is guaranteed to be either the exact position of `target` (if it exists in the array) or the correct position where it should be inserted to keep the array sorted.

---

## Code

```java
class Solution {
    public int searchInsert(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        int index = nums.length;

        while (start <= end) {

            int mid = (start + end) / 2;

            if (nums[mid] >= target) {
                index = mid;
                end = mid - 1;
            } else {
                start = mid + 1;
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
nums = [1,3,5,6], target = 5
```

**Initial:** `start = 0`, `end = 3`, `index = 4` (nums.length, the default fallback)

| start | end | mid | nums[mid] | nums[mid] >= target? | Action |
|-------|-----|-----|-------------|---------------------------|--------|
|0|3|1|3|3 >= 5? No|start = mid+1 = 2|
|2|3|2|5|5 >= 5? Yes|index = 2, end = mid-1 = 1|

**Loop check:** `start = 2`, `end = 1` → `start <= end` is false → loop exits.

Output:

```text
2
```

This matches the expected output (`target = 5` is found at index `2`).

---

### Additional Trace (target not present, insertion at the end)

```text
nums = [1,3,5,6], target = 7
```

| start | end | mid | nums[mid] | nums[mid] >= target? | Action |
|-------|-----|-----|-------------|---------------------------|--------|
|0|3|1|3|3 >= 7? No|start = 2|
|2|3|2|5|5 >= 7? No|start = 3|
|3|3|3|6|6 >= 7? No|start = 4|

**Loop check:** `start = 4`, `end = 3` → false → loop exits. `index` was never updated, so it remains at its initial value, `nums.length = 4`.

Output:

```text
4
```

This correctly indicates `target = 7` should be inserted at the very end of the array.

---

## Why This Approach Works

Since `nums` is sorted, binary search can determine at each step whether `target` could exist to the left or right of the current midpoint. Tracking `index` every time `nums[mid] >= target` is found ensures that even after narrowing further left (in search of an exact match), the most recently valid "insertion point" is preserved — so if `target` isn't found exactly, `index` still correctly reflects where it belongs.

The initial value of `index = nums.length` correctly handles the edge case where `target` is larger than every element in the array, since in that case `nums[mid] >= target` is never true, and `index` is never overwritten.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` — standard binary search
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

This problem is a natural extension of standard binary search: instead of only returning `-1` when the target isn't found, continuously tracking the last position where `nums[mid] >= target` was true doubles as tracking the correct insertion point — no separate logic is needed to handle the "not found" case.

---

## Concepts Used

- Array
- Binary Search
- Sorted Array Search
- Insertion Point Tracking