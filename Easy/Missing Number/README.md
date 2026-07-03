# Missing Number - LeetCode

## Problem

Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the **only number** in the range that is missing from the array.

## Example

### Example 1

**Input**
```text
nums = [3,0,1]
```

**Output**
```text
2
```

**Explanation**

`n = 3`, so the numbers should be in the range `[0,3]`.
The number `2` is missing from the array.

---

### Example 2

**Input**
```text
nums = [0,1]
```

**Output**
```text
2
```

**Explanation**

`n = 2`, so the numbers should be in the range `[0,2]`.
The number `2` is missing from the array.

---

### Example 3

**Input**
```text
nums = [9,6,4,2,3,5,7,0,1]
```

**Output**
```text
8
```

**Explanation**

`n = 9`, so the numbers should be in the range `[0,9]`.
The number `8` is missing from the array.

## Approach

- Calculate the sum of all elements present in the array.
- Calculate the expected sum of numbers from `0` to `n`.
- The difference between the expected sum and the array sum is the missing number.
- Return the missing number.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(1)

