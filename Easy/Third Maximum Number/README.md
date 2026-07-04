# Third Maximum Number - LeetCode

## Problem

Given an integer array `nums`, return the **third distinct maximum** number in the array. If the third distinct maximum does not exist, return the **maximum** number.

## Example

### Example 1

**Input**
```text
nums = [3,2,1]
```

**Output**
```text
1
```

**Explanation**

- First distinct maximum = `3`
- Second distinct maximum = `2`
- Third distinct maximum = `1`

---

### Example 2

**Input**
```text
nums = [1,2]
```

**Output**
```text
2
```

**Explanation**

The third distinct maximum does not exist, so return the maximum element.

---

### Example 3

**Input**
```text
nums = [2,2,3,1]
```

**Output**
```text
1
```

**Explanation**

The distinct numbers are `{3,2,1}`.
Although `2` appears twice, it is counted only once.

## Approach

- Traverse the array once to find the maximum element.
- Traverse again to find the second maximum that is distinct from the maximum.
- Traverse a third time to find the third maximum that is distinct from both the maximum and second maximum.
- If the third distinct maximum exists, return it.
- Otherwise, return the maximum element.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(1)

