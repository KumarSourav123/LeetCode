# LeetCode 33 - Search in Rotated Sorted Array

## Problem Statement

There is an integer array `nums` sorted in ascending order (with **distinct** values).

Prior to being passed to your function, `nums` is possibly rotated at an unknown pivot index `k` (`1 <= k < nums.length`) such that the resulting array is `[nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]` (**0-indexed**). For example, `[0,1,2,4,5,6,7]` might be rotated at pivot index `3` and become `[4,5,6,7,0,1,2]`.

Given the array `nums` after the possible rotation and an integer `target`, return *the index of* `target` *if it is in* `nums`, *or* `-1` *if it is not in* `nums`.

You must write an algorithm with `O(log n)` runtime complexity.

---

## Example 1:
```
Input: nums = [4,5,6,7,0,1,2], target = 0
Output: 4
```

## Example 2:
```
Input: nums = [4,5,6,7,0,1,2], target = 3
Output: -1
```

## Example 3:
```
Input: nums = [1], target = 0
Output: -1
```

## Constraints:
- `1 <= nums.length <= 5000`
- `-10^4 <= nums[i] <= 10^4`
- All values of `nums` are **unique**.
- `nums` is an ascending array that is possibly rotated.
- `-10^4 <= target <= 10^4`

---

## Approach

This solution uses a **modified binary search** that first identifies which half of the array is properly sorted at each step, then decides whether `target` could lie within that sorted half.

### Idea

- Standard binary search with `low` and `high` pointers. At each step, compute `mid`:
  - If `arr[mid] == target`, the value has been found — return `mid` immediately.
  - **Determine which half is sorted:** Since the array is only rotated once, at least one of the two halves (`low` to `mid`, or `mid` to `high`) must be in proper ascending order at any given point.
    - If `arr[low] <= arr[mid]`, the **left half** is sorted:
      - If `target` falls within the sorted range `[arr[low], arr[mid]]`, it must be in the left half — search there (`high = mid - 1`).
      - Otherwise, `target` must be in the right half — search there (`low = mid + 1`).
    - Otherwise, the **right half** must be sorted instead:
      - If `target` falls within the sorted range `[arr[mid], arr[high]]`, it must be in the right half — search there (`low = mid + 1`).
      - Otherwise, `target` must be in the left half — search there (`high = mid - 1`).
- If the loop exhausts the search space without finding `target`, return `-1`.

---

## Code

```java
class Solution {
    public int search(int[] nums, int target) {
        return binarySearch(nums, target);
    }

    private int binarySearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[low] <= arr[mid]) {
                if (arr[low] <= target && (target <= arr[mid])) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else {
                if (arr[mid] <= target && (target <= arr[high])) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -1;
    }
}
```

---

## Dry Run

### Input

```text
nums = [4,5,6,7,0,1,2], target = 0
```

**Initial:** `low = 0`, `high = 6`

| low | high | mid | arr[mid] | Which half sorted? | target in that range? | Action |
|-----|------|-----|------------|--------------------------|----------------------------|--------|
|0|6|3|7|arr[0]=4 <= arr[3]=7 → **left sorted**|0 in [4,7]? No|low = mid+1 = 4|
|4|6|5|1|arr[4]=0 <= arr[5]=1 → **left sorted**|0 in [0,1]? Yes|high = mid-1 = 4|
|4|4|4|0|arr[4] == target|**Match found → return 4**|-|

Output:

```text
4
```

This matches the expected output.

---

### Additional Trace (target not present)

```text
nums = [4,5,6,7,0,1,2], target = 3
```

| low | high | mid | arr[mid] | Which half sorted? | target in that range? | Action |
|-----|------|-----|------------|--------------------------|----------------------------|--------|
|0|6|3|7|left sorted ([4,7])|3 in [4,7]? No|low = 4|
|4|6|5|1|left sorted ([0,1])|3 in [0,1]? No|low = 6|
|6|6|6|2|left sorted ([2,2], trivially)|3 in [2,2]? No|low = 7|

**Loop check:** `low = 7`, `high = 6` → false → loop exits → return `-1`.

This matches the expected output.

---

## Why This Approach Works

Because the array is rotated at exactly one pivot point, splitting it at any `mid` guarantees that **at least one** of the two resulting halves is still in standard ascending order — the rotation "break point" can only exist in one of the two halves, never both.

Once the sorted half is identified, checking whether `target` falls within that half's known range (`arr[low]` to `arr[mid]`, or `arr[mid]` to `arr[high]`) tells us definitively whether to search there or in the other (still-rotated) half — this preserves the halving behavior of binary search even though the array as a whole isn't fully sorted.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` — each step still eliminates half of the remaining search space, just like standard binary search
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

The key adaptation from standard binary search is recognizing that a rotated sorted array still contains **one guaranteed sorted half** at every split point — by checking which half is sorted first, and then applying a simple range check to decide where `target` could be, this approach preserves `O(log n)` performance without needing to explicitly find the rotation pivot first.

---

## Concepts Used

- Array
- Binary Search
- Rotated Sorted Array
- Modified Search Conditions