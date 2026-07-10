# LeetCode 27 - Remove Element

## Problem
Given an integer array `nums` and an integer `val`, remove all occurrences of `val` **in-place** and return the number of elements that are not equal to `val`.

- The order of the remaining elements **does not matter**.
- Use **O(1)** extra space.

## Approach
This solution uses the **Two Pointer** technique.

- `s` (start) points to the current element.
- `h` (end) points to the last valid element.
- If `nums[s] == val`, swap it with `nums[h]` and decrease `h`.
- Do **not** increment `s` after swapping because the new element at index `s` still needs to be checked.
- If `nums[s] != val`, increment `s`.

The process continues until `s > h`.

## Code

```java
class Solution {
    public int removeElement(int[] nums, int val) {
        int s = 0;
        int h = nums.length - 1;

        while (s <= h) {
            if (nums[s] == val) {
                int temp = nums[s];
                nums[s] = nums[h];
                nums[h] = temp;
                h--;
            } else {
                s++;
            }
        }

        return s;
    }
}
```

## Dry Run

### Input

```
nums = [3,2,2,3]
val = 3
```

| s | h | Array | Action |
|---|---|--------|--------|
|0|3|[3,2,2,3]|Swap 3 with 3, h--|
|0|2|[3,2,2,3]|Swap 3 with 2, h--|
|0|1|[2,2,3,3]|nums[s] != val, s++|
|1|1|[2,2,3,3]|nums[s] != val, s++|

Stop because `s > h`.

**Output**

```
k = 2
nums = [2,2,_,_]
```

## Complexity

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(1)`

## Key Observation

The array **does not need to be sorted** to use the two-pointer technique.

This approach works because:
- We only need to remove occurrences of `val`.
- The order of the remaining elements is **not important**.
- Swapping unwanted elements to the end allows us to solve the problem in-place using constant extra space.