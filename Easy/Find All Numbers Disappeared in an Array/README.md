# 448. Find All Numbers Disappeared in an Array

## Problem Statement

Given an integer array `nums` of length `n` where each integer is in the range `[1, n]`, return all the integers in the range `[1, n]` that do not appear in `nums`.

You must solve it in **O(n)** time without using extra space (excluding the output list).

---

## Approach (Negative Marking)

Since every number lies between `1` and `n`, every value can be treated as an index.

### Step 1: Mark Visited Numbers

- Traverse the array.
- For each number:
  - Take its absolute value because it might already be negative.
  - Convert it into an index using:
    ```java
    index = Math.abs(nums[i]) - 1;
    ```
  - Make the value at that index negative (only if it's positive).

This indicates that the corresponding number exists in the array.

---

### Step 2: Find Missing Numbers

- Traverse the array again.
- If an element is still positive, it means its index was never visited.
- Therefore, the missing number is:

```java
i + 1
```

Add it to the answer list.

---

## Dry Run

Input:

```text
nums = [4,3,2,7,8,2,3,1]
```

### After Marking

```text
[-4,-3,-2,-7,8,2,-3,-1]
```

Notice:

- Index 4 is positive → Number **5** is missing.
- Index 5 is positive → Number **6** is missing.

Answer:

```text
[5,6]
```

---

## Algorithm

1. Traverse the array.
2. Convert each value into its corresponding index.
3. Mark that index as negative.
4. Traverse again.
5. Every positive value indicates a missing number.
6. Return the list.

---

## Complexity Analysis

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(1)` (excluding the output list)

---

## Java Solution

```java
class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int index = Math.abs(nums[i]) - 1;

            if (nums[index] > 0) {
                nums[index] = -nums[index];
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                list.add(i + 1);
            }
        }

        return list;
    }
}
```

---

## Key Observation

Instead of using an extra boolean array or HashSet to record visited numbers, we reuse the input array itself.

- Negative value → Number exists.
- Positive value → Number is missing.

This trick achieves the required **O(n)** time and **O(1)** extra space.

---

## Concepts Used

- Arrays
- Index Mapping
- In-place Modification
- Negative Marking Technique
- Mathematical Indexing (`value - 1`)