# Remove Element - LeetCode

## Problem

Given an integer array `nums` and an integer `val`, remove all occurrences of `val` in-place.

The order of elements may be changed. Then return the number of elements in `nums` which are not equal to `val`.

## Example

### Example 1

**Input**
```text
nums = [3,2,2,3]
val = 3
```

**Output**
```text
2, nums = [2,2]
```

### Example 2

**Input**
```text
nums = [0,1,2,2,3,0,4,2]
val = 2
```

**Output**
```text
5, nums = [0,1,3,0,4]
```

## Approach

- Create a new array to store elements that are not equal to `val`.
- Traverse the original array.
- If the element is not equal to `val`, add it to the new array.
- Copy the new values back into `nums`.
- Return the count of remaining elements.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(n)
