# LeetCode 349 - Intersection of Two Arrays

## Problem Statement

Given two integer arrays `nums1` and `nums2`, return an array of their **intersection**.

Each element in the result must be **unique**, and you may return the result in **any order**.

---

## Approach

This solution uses a **HashSet-based lookup**.

### Idea

- Add all elements of `nums1` into a `HashSet` to remove duplicates and allow O(1) lookups.
- Traverse `nums2`, and for each element, check if it exists in the set built from `nums1`.
- If it exists, add it to a second `HashSet` (the `intersection` set), which automatically ensures uniqueness.
- Convert the `intersection` set into an array and return it.

Since we use two separate sets, duplicate values are naturally eliminated on both sides.

---

## Code

```java
class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {

        HashSet<Integer> set = new HashSet<>();

        for (int element : nums1) {
            set.add(element);
        }

        HashSet<Integer> intersection = new HashSet<>();

        for (int element : nums2) {
            if (set.contains(element)) {
                intersection.add(element);
            }
        }

        int[] result = new int[intersection.size()];

        int i = 0;
        for (int element : intersection) {
            result[i] = element;
            i++;
        }

        return result;
    }
}
```

---

## Dry Run

### Input

```text
nums1 = [4,9,5], nums2 = [9,4,9,8,4]
```

| Step | Element | In set (from nums1)? | intersection set |
|------|---------|----------------------|-------------------|
|1|9|Yes|{9}|
|2|4|Yes|{9,4}|
|3|9|Yes (already added)|{9,4}|
|4|8|No|{9,4}|
|5|4|Yes (already added)|{9,4}|

Final Intersection Set:

```text
{9, 4}
```

Output:

```text
[9, 4]
```

---

## Why This Approach Works

Storing `nums1` in a `HashSet` lets us check membership in **O(1)** time instead of scanning the array repeatedly.

Using a second `HashSet` for the result automatically handles duplicate elimination, since sets cannot contain repeated values.

By the end of the traversal, the `intersection` set contains exactly the unique elements common to both arrays.

---

## Complexity Analysis

- **Time Complexity:** `O(n + m)`
- **Space Complexity:** `O(n + m)`

---

## Key Insight

Instead of using nested loops to compare every element of `nums1` with every element of `nums2` (which would take **O(n × m)** time), using two `HashSet`s reduces the time complexity to **O(n + m)** while also handling duplicate removal for free.

---

## Concepts Used

- Array
- HashSet
- Hashing
- Set Operations
- Two Pointers (alternative approach)
- Constant-Time Lookup
