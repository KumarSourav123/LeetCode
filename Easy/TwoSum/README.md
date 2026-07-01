# Two Sum - LeetCode

## Problem
Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.

You may assume that each input has exactly one solution, and you may not use the same element twice.

## Example

### Example 1
**Input**
```text
nums = [2,7,11,15]
target = 9
```

**Output**
```text
[0,1]
```

### Example 2
**Input**
```text
nums = [3,2,4]
target = 6
```

**Output**
```text
[1,2]
```

## Approach
- Use two nested loops.
- Check every possible pair.
- If the sum of two numbers equals the target, return their indices.

## Complexity

**Time Complexity:**  
O(n²)

**Space Complexity:**  
O(1)

