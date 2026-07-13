# 219. Contains Duplicate II

**Difficulty:** Easy  
**Language:** Java

## Problem Statement

Given an integer array `nums` and an integer `k`, return `true` if there are two distinct indices `i` and `j` in the array such that:

- `nums[i] == nums[j]`
- `abs(i - j) <= k`

Otherwise, return `false`.

---

## Approach

- Use a `HashMap` to store each number as the **key** and its **latest index** as the **value**.
- Traverse the array.
- If the current number already exists in the map:
  - Calculate the distance between the current index and the previous index.
  - If the distance is less than or equal to `k`, return `true`.
  - Otherwise, update the stored index with the current index.
- If the number is not present, store its index.
- If no such pair is found, return `false`.

---

## Algorithm

1. Create a `HashMap<Integer, Integer>`.
2. Traverse the array.
3. If the current value exists in the map:
   - Check if `currentIndex - previousIndex <= k`.
   - If yes, return `true`.
   - Otherwise, update the index.
4. If the value does not exist, insert it into the map.
5. Return `false`.

---

## Java Solution

```java
class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                if (i - map.get(nums[i]) <= k) {
                    return true;
                } else {
                    map.put(nums[i], i);
                }
            } else {
                map.put(nums[i], i);
            }
        }

        return false;
    }
}
```

---

## Time Complexity

- **O(n)**

Each element is processed only once, and every `HashMap` operation takes **O(1)** on average.

---

## Space Complexity

- **O(n)**

The HashMap may store every unique element in the array.

---

## Key Learning

- Store **value → latest index** in the HashMap.
- Always update the index if the previous occurrence is farther than `k`.
- Using the latest index gives the smallest possible distance for future duplicates.

---

## Example

### Input

```text
nums = [1,2,3,1]
k = 3
```

### HashMap Traversal

| Index | Value | HashMap Before | Action |
|------:|------:|---------------|--------|
| 0 | 1 | {} | Store (1 → 0) |
| 1 | 2 | {1→0} | Store (2 → 1) |
| 2 | 3 | {1→0, 2→1} | Store (3 → 2) |
| 3 | 1 | {1→0, 2→1, 3→2} | Distance = 3 - 0 = 3 ≤ k → Return `true` |

---

## Result

```text
Output: true
```