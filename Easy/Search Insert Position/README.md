# Search Insert Position - LeetCode

## Problem

Given a sorted array of distinct integers `nums` and a target value `target`, return the index if the target is found.

If not found, return the index where it would be inserted in order.

You must write an algorithm with **O(log n)** runtime complexity.

## Example

### Example 1

**Input**
```text
nums = [1,3,5,6]
target = 5
```

**Output**
```text
2
```

### Example 2

**Input**
```text
nums = [1,3,5,6]
target = 2
```

**Output**
```text
1
```

### Example 3

**Input**
```text
nums = [1,3,5,6]
target = 7
```

**Output**
```text
4
```

## Approach

- Use **Binary Search** because the array is sorted.
- Keep two pointers: `start` and `end`.
- Find the middle index.
- If `nums[index]` equals target, return index.
- If target is greater, search the right half.
- Otherwise search the left half.
- If not found, return the insertion position.

## Complexity

**Time Complexity:**  
O(log n)

**Space Complexity:**  
O(1)

