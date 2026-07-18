# LeetCode 350 - Intersection of Two Arrays II

## Problem Statement

Given two integer arrays `nums1` and `nums2`, return an array of their **intersection**.

Each element in the result must appear **as many times as it shows in both arrays**, and you may return the result in **any order**.

---

## Approach

This solution uses a **HashMap-based frequency count**.

### Idea

- Traverse `nums1` and build a `HashMap` storing the frequency of each element.
- Traverse `nums2`, and for each element, check if it exists in the map with a count greater than `0`.
- If it does, add it to the result `list` and decrement its count in the map by `1`.
- Convert the `list` into an array and return it.

Decrementing the count ensures that an element is only added as many times as it appears in **both** arrays.

---

## Code

```java
class Solution {
    public int[] intersect(int[] nums1, int[] nums2) {

        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (int element : nums1) {
            if (map.containsKey(element)) {
                map.put(element, map.get(element) + 1);
            } else {
                map.put(element, 1);
            }
        }

        for (int element : nums2) {
            if ((map.containsKey(element)) && (map.get(element) > 0)) {
                list.add(element);
                map.put(element, map.get(element) - 1);
            }
        }

        int[] result = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
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

**Step 1: Build frequency map from nums1**

| Element | map after insertion |
|---------|----------------------|
|4|{4:1}|
|9|{4:1, 9:1}|
|5|{4:1, 9:1, 5:1}|

**Step 2: Traverse nums2**

| Element | map.get(element) > 0? | Action | list |
|---------|------------------------|--------|------|
|9|Yes (1)|Add 9, map[9] = 0|[9]|
|4|Yes (1)|Add 4, map[4] = 0|[9,4]|
|9|No (0)|Skip|[9,4]|
|8|No (not in map)|Skip|[9,4]|
|4|No (0)|Skip|[9,4]|

Output:

```text
[9, 4]
```

---

## Why This Approach Works

Storing the frequency of each element in `nums1` lets us verify not just **whether** an element exists in `nums2`, but also **how many times** it can still be matched.

Decrementing the count after each match ensures duplicates are only added up to the number of times they actually appear in both arrays, correctly handling repeated elements.

---

## Complexity Analysis

- **Time Complexity:** `O(n + m)`
- **Space Complexity:** `O(n)`

---

## Follow-up Considerations

- **If the arrays are already sorted:** Use the **two-pointer technique** instead of a HashMap, comparing elements at both pointers and advancing the smaller one. This avoids extra space for a map.
- **If `nums1`'s size is small compared to `nums2`'s size:** Build the HashMap from the smaller array (`nums1`) to minimize memory usage, then iterate over the larger array (`nums2`).
- **If elements of `nums2` are stored on disk with limited memory:** Load `nums2` in chunks, and for each chunk, check against the in-memory HashMap built from `nums1`, writing matches to the result incrementally.

---

## Key Insight

Unlike LeetCode 349 (which uses `HashSet`s to keep only unique intersection elements), this problem requires preserving **duplicate counts**, so a `HashMap` tracking frequency is necessary instead of a `HashSet`.

---

## Concepts Used

- Array
- HashMap
- Hashing
- Frequency Counting
- Two Pointers (alternative approach for sorted arrays)
- Constant-Time Lookup