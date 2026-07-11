# LeetCode 217 - Contains Duplicate

## Problem Statement

Given an integer array `nums`, return `true` if any value appears **at least twice** in the array, and return `false` if every element is distinct.

---

## Approach

This solution uses a **HashMap** to keep track of the elements that have already been seen.

### Algorithm

1. Create an empty `HashMap`.
2. Traverse the array.
3. If the current element already exists in the map, return `true`.
4. Otherwise, insert the element into the map.
5. If the traversal completes without finding any duplicate, return `false`.

> **Note:** A `HashSet` is a more suitable data structure for this problem since only the presence of elements is required. However, the `HashMap` approach is also correct and accepted.

---

## Code

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int num : nums) {
            if (map.containsKey(num)) {
                return true;
            } else {
                map.put(num, 1);
            }
        }

        return false;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,2,3,1]
```

| Current Element | HashMap | Duplicate Found |
|-----------------|---------|-----------------|
|1|{1}|No|
|2|{1,2}|No|
|3|{1,2,3}|No|
|1|Already Exists|Yes → Return `true`|

Output:

```text
true
```

---

### Example 2

Input

```text
nums = [1,2,3,4]
```

| Current Element | HashMap |
|-----------------|---------|
|1|{1}|
|2|{1,2}|
|3|{1,2,3}|
|4|{1,2,3,4}|

No duplicates are found.

Output:

```text
false
```

---

## Complexity Analysis

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(n)`

---

## Key Insight

The `HashMap` stores every element encountered during traversal.

- If an element is found again, it is a duplicate.
- Since `HashMap` provides average `O(1)` lookup and insertion, the entire solution runs in linear time.

---

## Alternative Approach

A cleaner implementation uses a **HashSet**, since only unique elements need to be stored.

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        HashSet<Integer> set = new HashSet<>();

        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }

        return false;
    }
}
```

Both approaches have:

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(n)`

---

## Concepts Used

- Array
- HashMap
- HashSet
- Hashing
- Frequency Checking
- Linear Traversal