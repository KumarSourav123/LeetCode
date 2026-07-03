# Move Zeroes - LeetCode

## Problem

Given an integer array `nums`, move all `0`s to the end of the array while maintaining the relative order of the non-zero elements.

**Note:** You must do this **in-place** without making a copy of the array.

## Example

### Example 1

**Input**
```text
nums = [0,1,0,3,12]
```

**Output**
```text
[1,3,12,0,0]
```

---

### Example 2

**Input**
```text
nums = [0]
```

**Output**
```text
[0]
```

## Approach

- Traverse the array from left to right.
- Whenever a `0` is encountered, keep track of consecutive zeros.
- If a non-zero element appears after one or more zeros, swap it with the earliest zero position.
- Continue this process until all non-zero elements are shifted to the front.
- The zeros automatically move to the end while maintaining the relative order of non-zero elements.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(1)
