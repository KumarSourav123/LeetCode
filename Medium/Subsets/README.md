# LeetCode 78 - Subsets

## Problem Statement

Given an integer array `nums` of **unique** elements, return *all possible subsets* (the power set).

The solution set **must not** contain duplicate subsets. Return the solution in **any order**.

---

## Example 1:
```
Input: nums = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
```

## Example 2:
```
Input: nums = [0]
Output: [[],[0]]
```

## Constraints:
- `1 <= nums.length <= 10`
- `-10 <= nums[i] <= 10`
- All the numbers of `nums` are **unique**.

---

## Approach

This solution uses **recursive backtracking**, making a binary "include or exclude" decision for each element.

### Idea

- Define a recursive helper `set(i, list, arr, result)` that decides, one index at a time, whether the current element `arr[i]` is included in the subset being built.
- **Base case:** When `i == arr.length`, every element has been decided upon. Add a **copy** of the current `list` (the subset built so far) to `result`, then return.
- **Recursive case:** For the current index `i`:
  - **Include** `arr[i]`: add it to `list`, then recurse into `set(i+1, ...)` to decide the next element.
  - **Backtrack:** remove `arr[i]` from `list` (undoing the inclusion), since the next branch explores the alternative.
  - **Exclude** `arr[i]`: recurse into `set(i+1, ...)` again, but this time without `arr[i]` in `list`, exploring the subset that skips this element entirely.
- Because every element independently branches into "included" or "excluded," this generates exactly `2^n` subsets, covering the entire power set.

---

## Code

```java
class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();
        set(0, list, nums, result);
        return result;
    }

    private void set(int i, ArrayList<Integer> list, int[] arr, ArrayList<List<Integer>> result) {
        if (i == arr.length) {
            result.add(new ArrayList<>(list));
            return;
        }

        list.add(arr[i]);
        set(i + 1, list, arr, result);

        list.remove(list.size() - 1);
        set(i + 1, list, arr, result);
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,2,3]
```

Tracing the recursion tree (`I` = include current element, `E` = exclude current element):

| Call Path | list at base case (i=3) | Subset Added to result |
|-----------|----------------------------|----------------------------|
|I,I,I|[1,2,3]|[1,2,3]|
|I,I,E|[1,2]|[1,2]|
|I,E,I|[1,3]|[1,3]|
|I,E,E|[1]|[1]|
|E,I,I|[2,3]|[2,3]|
|E,I,E|[2]|[2]|
|E,E,I|[3]|[3]|
|E,E,E|[]|[]|

**Final Result:**

```text
[[1,2,3],[1,2],[1,3],[1],[2,3],[2],[3],[]]
```

This contains all `2^3 = 8` subsets of `[1,2,3]` — matching the expected set of subsets, just in a different order than LeetCode's example (which is acceptable since the problem allows **any order**).

---

## Why This Approach Works

Every subset of an `n`-element array can be uniquely described by a sequence of `n` binary decisions — for each element, either "include it" or "exclude it." Exploring both branches at every index, recursively, therefore generates every possible combination of inclusion/exclusion exactly once, producing all `2^n` subsets without duplication.

The `list.remove(list.size() - 1)` call is essential **backtracking**: it undoes the "include" decision made just before the first recursive call, restoring `list` to its prior state so that the second recursive call correctly represents the "exclude" branch instead of accidentally carrying over the included element.

---

## Complexity Analysis

- **Time Complexity:** `O(2^n × n)` — there are `2^n` subsets generated, and each subset takes `O(n)` time to copy into `result`
- **Space Complexity:** `O(2^n × n)` — for storing all generated subsets, plus `O(n)` recursion depth on the call stack

---

## Key Insight

Rather than iteratively building subsets by extending previously generated ones, this recursive "include/exclude" pattern is a standard backtracking template that naturally generalizes to many combinatorial generation problems (subsets, combinations, permutations) — the core idea being to explore both choices at each decision point and undo (backtrack) before exploring the other.

---

## Concepts Used

- Array
- Recursion
- Backtracking
- Power Set Generation
- Binary Decision Tree