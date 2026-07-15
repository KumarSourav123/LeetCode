# 🔍 Search in Rotated Sorted Array (LeetCode 33)

## ✅ My Approach

### Idea
1. Traverse the array once to find the **smallest element**.
2. The index of the smallest element is the **rotation point**.
3. Depending on the target, decide which half to perform binary search on.
4. Run normal binary search.

---

## Code

```java
class Solution {
    public int search(int[] nums, int target) {

        int small = nums[0];
        int count = 0;
        int index = 0;
        int low = 0;
        int high = nums.length - 1;
        int mid = 0;

        // Find smallest element (rotation point)
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < small) {
                small = nums[i];
                index = i;
                count = 1;
            }
        }

        // Decide which half to search
        if (count == 1) {
            if (nums[index] == target) {
                return index;
            } else if (nums[0] > target) {
                low = index + 1;
            } else {
                high = index - 1;
            }
        }

        // Standard Binary Search
        while (low <= high) {
            mid = (low + high) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return -1;
    }
}
```

---

# Time Complexity

- Finding rotation point: **O(n)**
- Binary Search: **O(log n)**

**Overall:** `O(n)`

---

# Space Complexity

- `O(1)`

---

# Why this is NOT the optimal solution

The problem **requires an `O(log n)` algorithm**.

Your solution first performs a linear scan to find the rotation point, making the total complexity **O(n)**, which violates the problem's requirement.

---

# Optimal Idea (No Code)

Instead of first finding the rotation point:

- Use **binary search directly**.
- In every iteration:
  - One half (left or right) is always sorted.
  - Determine which half is sorted.
  - Check whether the target lies in that sorted half.
  - Discard the other half.

This keeps every step in binary search and achieves **O(log n)** time.

