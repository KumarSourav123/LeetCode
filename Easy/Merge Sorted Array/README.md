# LeetCode 88 - Merge Sorted Array

## Problem Statement

You are given two integer arrays `nums1` and `nums2`, sorted in non-decreasing order, and two integers `m` and `n`, representing the number of valid elements in `nums1` and `nums2` respectively.

Merge `nums2` into `nums1` as one sorted array.

- The final sorted array should be stored inside `nums1`.
- `nums1` has a size of `m + n`, where the last `n` elements are empty (`0`) and reserved for the merge.

---

## Approach

This solution uses the **Three Pointer** technique.

### Pointers

- `s` → Points to the last valid element of `nums1`.
- `b` → Points to the last element of `nums2`.
- `h` → Points to the last index of `nums1`.

### Algorithm

1. Start comparing the last valid elements of both arrays.
2. Place the larger element at the end of `nums1`.
3. Move the corresponding pointer backward.
4. Continue until one array is exhausted.
5. If elements remain in `nums2`, copy them into `nums1`.

Since we fill the array **from the end**, no useful data is overwritten.

---

## Code

```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int s = m - 1;
        int b = n - 1;
        int h = (m + n) - 1;

        while (b >= 0 && s >= 0) {
            if (nums1[s] >= nums2[b]) {
                nums1[h] = nums1[s];
                s--;
                h--;
            } else {
                nums1[h] = nums2[b];
                b--;
                h--;
            }
        }

        while (b >= 0) {
            nums1[h] = nums2[b];
            b--;
            h--;
        }
    }
}
```

---

## Dry Run

### Input

```text
nums1 = [1,2,3,0,0,0]
m = 3

nums2 = [2,5,6]
n = 3
```

| s | b | h | Action | nums1 |
|---|---|---|--------|--------|
|2|2|5|6 is larger → place 6|[1,2,3,0,0,6]|
|2|1|4|5 is larger → place 5|[1,2,3,0,5,6]|
|2|0|3|3 is larger → place 3|[1,2,3,3,5,6]|
|1|0|2|2 is larger/equal → place 2|[1,2,2,3,5,6]|
|0|0|1|2 is larger → place 2|[1,2,2,3,5,6]|

Final Output:

```text
[1,2,2,3,5,6]
```

---

## Complexity Analysis

- **Time Complexity:** `O(m + n)`
- **Space Complexity:** `O(1)`

---

## Key Insight

Instead of merging from the beginning (which would overwrite useful values), we merge **from the end** of `nums1`.

This allows us to:
- Avoid using an extra array.
- Perform the merge in-place.
- Achieve the optimal space complexity of **O(1)**.

---

## Concepts Used

- Array
- Three Pointers
- Two Pointer Technique
- In-place Merge
- Greedy Placement