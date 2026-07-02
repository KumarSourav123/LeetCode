# Plus One - LeetCode

## Problem

You are given a large integer represented as an integer array `digits`, where each `digits[i]` is the `iᵗʰ` digit of the integer.

The digits are ordered from most significant to least significant in left-to-right order. The large integer does not contain any leading `0`s.

Increment the large integer by one and return the resulting array of digits.

## Example

### Example 1

**Input**
```text
digits = [1,2,3]
```

**Output**
```text
[1,2,4]
```

**Explanation**

The array represents the integer `123`.

Incrementing by one gives `124`.

---

### Example 2

**Input**
```text
digits = [4,3,2,1]
```

**Output**
```text
[4,3,2,2]
```

**Explanation**

The array represents the integer `4321`.

Incrementing by one gives `4322`.

---

### Example 3

**Input**
```text
digits = [9]
```

**Output**
```text
[1,0]
```

**Explanation**

The array represents the integer `9`.

Incrementing by one gives `10`.

## Approach

- Check if the last digit is not `9`.
  - If true, increment the last digit and return the array.
- Count how many digits are `9`.
- If not all digits are `9`:
  - Traverse from the end.
  - Change trailing `9`s to `0`.
  - Increment the first non-`9` digit and return the array.
- If every digit is `9`:
  - Create a new array with one extra digit.
  - Set the first element to `1` and the remaining elements to `0`.
  - Return the new array.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(n) *(because a new array is created when all digits are `9`)*

