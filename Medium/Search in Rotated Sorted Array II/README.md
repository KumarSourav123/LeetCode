# LeetCode 81 - Search in Rotated Sorted Array II

## Problem Statement

There is an integer array `nums` sorted in non-decreasing order (not necessarily with distinct values).

Before being passed to your function, `nums` is rotated at an unknown pivot index `k` (`0 <= k < nums.length`) such that the resulting array is `[nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]` (0-indexed). For example, `[0,1,2,4,4,4,5,6,7]` might be rotated at pivot index `5` and become `[4,5,6,7,0,1,2,4,4]`.

Given the array `nums` after the rotation and an integer `target`, return `true` if `target` is in `nums`, or `false` if it is not in `nums`.

You must decrease the overall operation steps as much as possible.

---

## Example 1:
```
Input: nums = [2,5,6,0,0,1,2], target = 0
Output: true
```

## Example 2:
```
Input: nums = [2,5,6,0,0,1,2], target = 3
Output: false
```

## Constraints:
- `1 <= nums.length <= 5000`
- `-10^4 <= nums[i] <= 10^4`
- `nums` is guaranteed to be rotated at some pivot.
- `-10^4 <= target <= 10^4`

**Follow up:** This problem is similar to Search in Rotated Sorted Array, but `nums` may contain duplicates. Would this affect the runtime complexity? How and why?

---

## Approach

A modified binary search. The core difficulty compared to the no-duplicates version is that when `nums[low] == nums[mid] == nums[high]`, it's impossible to tell from those three values alone which half of the array is sorted, so that ambiguity has to be handled separately.

### Idea

- Standard binary search loop with `low`, `high`, computing `mid` each iteration.
- If `nums[mid] == target`, return `true` immediately.
- **Ambiguous case (duplicates):** if `nums[low] == nums[mid]` and `nums[mid] == nums[high]`, we can't determine which half is sorted — shrink the search space by one from each side: `low++`, `high--`.
- **Otherwise, determine which half is sorted:**
  - If `nums[low] <= nums[mid]`, the **left half** is sorted.
    - If `target` falls within `[nums[low], nums[mid]]`, search left (`high = mid - 1`).
    - Otherwise search right (`low = mid + 1`).
  - Else, the **right half** is sorted.
    - If `target` falls within `[nums[mid], nums[high]]`, search right (`low = mid + 1`).
    - Otherwise search left (`high = mid - 1`).
- If the loop exits without a match (`low > high`), return `false`.

---

## Code

```java
class Solution {
    public boolean search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (nums[mid] == target) {
                return true;
            }
            if (nums[low] == nums[mid] && nums[mid] == nums[high]) {
                low++;
                high--;
            } else if (nums[low] <= nums[mid]) {
                if (target >= nums[low] && target <= nums[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else {
                if (target >= nums[mid] && target <= nums[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }

        return false;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,0,1,1,1], target = 0
```

`low=0`, `high=4`

| low | high | mid | nums[mid] | Check | Action |
|-----|------|-----|-----------|-------|--------|
|0|4|2|1|`nums[mid]!=target`; `nums[0]==nums[2]==nums[4]` (1==1==1) → ambiguous|`low++ → 1`, `high-- → 3`|
|1|3|2|1|`nums[mid]!=target`; `nums[1]=0`,`nums[2]=1`,`nums[3]=1` not all equal. `nums[low]<=nums[mid]`? 0<=1 yes → left half sorted. Is target(0) in `[nums[low]=0, nums[mid]=1]`? Yes|`high = mid-1 → 1`|
|1|1|1|0|`nums[mid] == target` (0 == 0)|**return `true`**|

**Final Result:**

```text
true
```

This matches expectations — `0` is present in `nums`.

---

## Why This Approach Works

At every step, at least one half of the array (split at `mid`) is guaranteed to be sorted in a rotated array — that's the invariant a normal rotated-array binary search relies on. Checking `nums[low] <= nums[mid]` normally identifies which half is sorted, and once that's known, a simple range check tells us whether `target` could live in that sorted half or must be in the other one.

Duplicates break this invariant only in one specific case: when `nums[low] == nums[mid] == nums[high]`, both halves could look identical even though the actual rotation point (and therefore `target`) is hidden inside them. In that case there is no way to skip a large section safely, so the algorithm falls back to shrinking the window by one element at each end — trading some efficiency for correctness only in this narrow, duplicate-heavy scenario.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` on average; degrades to **`O(n)`** worst case when many duplicates repeatedly trigger the `low++ / high--` fallback (e.g. an array of all identical elements).
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

Without duplicates, comparing `nums[low]`, `nums[mid]`, and `nums[high]` always reveals which half of a rotated array is sorted, guaranteeing `O(log n)`. Duplicates introduce a genuine ambiguity — when the three key values are equal, no comparison-based rule can safely eliminate half the array, forcing a linear-time fallback in the worst case. This is a real, unavoidable cost of allowing duplicates, not just a suboptimal implementation choice.

---

## Concepts Used

- Array
- Binary Search
- Rotated Sorted Array
- Handling Duplicates in Binary Search