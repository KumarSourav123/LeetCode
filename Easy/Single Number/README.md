# Single Number - LeetCode

## Problem

Given a **non-empty** array of integers `nums`, every element appears **twice** except for one. Find that single one.

You must implement a solution with **linear time complexity** and use only **constant extra space**.

## Example

### Example 1

**Input**
```text
nums = [2,2,1]
```

**Output**
```text
1
```

---

### Example 2

**Input**
```text
nums = [4,1,2,1,2]
```

**Output**
```text
4
```

---

### Example 3

**Input**
```text
nums = [1]
```

**Output**
```text
1
```

## Approach

- Initialize a variable `answer` with `0`.
- Traverse the array once.
- Perform the **XOR (`^`)** operation between `answer` and each element.
- XOR has the following properties:
  - `a ^ a = 0`
  - `a ^ 0 = a`
- Since every number appears twice, duplicate numbers cancel each other out.
- The remaining value is the number that appears only once.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(1)
