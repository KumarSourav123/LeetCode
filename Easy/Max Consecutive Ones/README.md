# Max Consecutive Ones - LeetCode

## Problem

Given a binary array `nums`, return the **maximum number of consecutive `1`s** in the array.

## Example

### Example 1

**Input**
```text
nums = [1,1,0,1,1,1]
```

**Output**
```text
3
```

**Explanation**

- The first two `1`s form a consecutive sequence of length `2`.
- The last three `1`s form a consecutive sequence of length `3`.
- Therefore, the maximum consecutive `1`s is `3`.

---

### Example 2

**Input**
```text
nums = [1,0,1,1,0,1]
```

**Output**
```text
2
```

**Explanation**

- The longest consecutive sequence of `1`s is `[1,1]`.
- Therefore, the answer is `2`.

---

## Approach

- Initialize two variables:
  - `count` to keep track of the current consecutive `1`s.
  - `maxCount` to store the maximum consecutive `1`s found so far.
- Traverse the array once.
- If the current element is `1`:
  - Increment `count`.
  - Update `maxCount` if `count` is greater.
- If the current element is `0`:
  - Reset `count` to `0` since the consecutive sequence is broken.
- Return `maxCount`.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(1)

