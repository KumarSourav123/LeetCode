# 81. Search in Rotated Sorted Array II

**Difficulty:** Medium
**Tags:** Array, Binary Search
**Status:** Solved ✅

## Problem

There is an integer array `nums` sorted in non-decreasing order (not necessarily with distinct values).

Before being passed to your function, `nums` is rotated at an unknown pivot index `k`
(`0 <= k < nums.length`) such that the resulting array is
`[nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]` (0-indexed).

For example, `[0,1,2,4,4,4,5,6,7]` might be rotated at pivot index 5 and become
`[4,5,6,7,0,1,2,4,4]`.

Given the array `nums` after the rotation and an integer `target`, return `true` if `target`
is in `nums`, or `false` if it is not in `nums`.

You must decrease the overall operation steps as much as possible.

### Example 1
```
Input: nums = [2,5,6,0,0,1,2], target = 0
Output: true
```

### Example 2
```
Input: nums = [2,5,6,0,0,1,2], target = 3
Output: false
```

### Constraints
- `1 <= nums.length <= 5000`
- `-10^4 <= nums[i] <= 10^4`
- `nums` is guaranteed to be rotated at some pivot.
- `-10^4 <= target <= 10^4`

**Follow up:** This problem is similar to Search in Rotated Sorted Array, but `nums` may
contain duplicates. Would this affect the runtime complexity? How and why?

## Approach

Modified binary search:

1. Find `mid`. If `nums[mid] == target`, return `true`.
2. **Duplicate handling:** if `nums[low] == nums[mid] == nums[high]`, we can't tell which
   half is sorted, so shrink the search space by one from both ends (`low++`, `high--`).
3. Otherwise, check which half is sorted:
   - If `nums[low] <= nums[mid]`, the **left half** is sorted.
     - If `target` lies within `[nums[low], nums[mid]]`, search left (`high = mid - 1`).
     - Else search right (`low = mid + 1`).
   - Otherwise, the **right half** is sorted.
     - If `target` lies within `[nums[mid], nums[high]]`, search right (`low = mid + 1`).
     - Else search left (`high = mid - 1`).
4. If the loop ends without finding `target`, return `false`.

## Complexity

- **Time:** O(log n) average case, **O(n)** worst case (when many duplicates force the
  `low++ / high--` shrink, e.g. all elements equal).
- **Space:** O(1)

## Code (Java)

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

## Notes / Follow-up Answer

Yes, duplicates affect the runtime. Without duplicates, we can always tell which half is
sorted by comparing `nums[low]`, `nums[mid]`, `nums[high]`, giving guaranteed O(log n).
With duplicates, when `nums[low] == nums[mid] == nums[high]`, that comparison gives no
information about which side is sorted, so we fall back to shrinking the window by one
element at a time — degrading worst-case time to **O(n)** (e.g. `[1,1,1,1,1,1,1]`,
target = `2`).