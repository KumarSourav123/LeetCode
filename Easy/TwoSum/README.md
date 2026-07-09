# 1. Two Sum

**Difficulty:** Easy  
**Topic:** Array, HashMap

## Problem Statement

Given an array of integers `nums` and an integer `target`, return the **indices** of the two numbers such that they add up to the target.

You may assume that each input has **exactly one solution**, and you may not use the same element twice.

You can return the answer in any order.

---

## Example

### Input

```text
nums = [2,7,11,15]
target = 9
```

### Output

```text
[0,1]
```

### Explanation

```text
nums[0] + nums[1] = 2 + 7 = 9
```

---

## Approach

Use a **HashMap** to store each number along with its index while traversing the array.

For every element:

1. Calculate the required complement.

```text
complement = target - nums[i]
```

2. Check whether the complement already exists in the HashMap.
3. If it exists, return both indices.
4. Otherwise, store the current number and its index.

Since HashMap lookup takes **O(1)** time on average, the entire solution runs in linear time.

---

## Dry Run

### Input

```text
nums = [2,7,11,15]
target = 9
```

### Iteration 1

```text
i = 0
nums[i] = 2

Complement = 9 - 2 = 7

HashMap = {}

7 not found

Store

{
2 → 0
}
```

---

### Iteration 2

```text
i = 1
nums[i] = 7

Complement = 9 - 7 = 2

HashMap

{
2 → 0
}

2 found

Return

[1,0]
```

LeetCode also accepts

```text
[0,1]
```

because the order of indices does not matter.

---

## Java Solution

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {

        HashMap<Integer, Integer> map = new HashMap<>();
        int num;

        for(int i = 0; i < nums.length; i++) {

            num = target - nums[i];

            if(map.containsKey(num)) {
                return new int[]{i, map.get(num)};
            }

            map.put(nums[i], i);
        }

        return new int[]{};
    }
}
```

---

## Complexity Analysis

### Time Complexity

```text
O(n)
```

- Traverse the array once.
- HashMap operations (`put`, `get`, `containsKey`) take **O(1)** on average.

---

### Space Complexity

```text
O(n)
```

In the worst case, every element is stored in the HashMap.

---

## Key Learning

- Use **HashMap** when fast lookup is required.
- Store:
  - **Key → Number**
  - **Value → Index**
- Always check for the complement **before** inserting the current element.
- Duplicate keys do not affect the correctness of the algorithm because the answer is returned before overwriting can occur.

---

## Concepts Used

- Array
- HashMap
- One-pass traversal
- Time Optimization

---

## LeetCode

**Problem:** 1. Two Sum  
**Difficulty:** Easy