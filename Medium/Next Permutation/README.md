# LeetCode 31 - Next Permutation

## Problem Statement

A **permutation** of an array of integers is an arrangement of its members into a sequence or linear order.

- For example, for `arr = [1,2,3]`, the following are all the permutations of `arr`: `[1,2,3]`, `[1,3,2]`, `[2,1,3]`, `[2,3,1]`, `[3,1,2]`, `[3,2,1]`.

The **next permutation** of an array of integers is the next lexicographically greater permutation of its integer. More formally, if all the permutations of the array are sorted in one container according to their lexicographical order, then the **next permutation** of that array is the permutation that follows it in the sorted container. If such arrangement is not possible, the array must be rearranged as the lowest possible order (i.e., sorted in ascending order).

- For example, the next permutation of `arr = [1,2,3]` is `[1,3,2]`.
- Similarly, the next permutation of `arr = [2,3,1]` is `[3,1,2]`.
- While the next permutation of `arr = [3,2,1]` is `[1,2,3]` because `[3,2,1]` does not have a lexicographical larger rearrangement.

Given an array of integers `nums`, find the next permutation of `nums`.

The replacement must be **in place** and use only **constant extra memory**.

---

## Example 1:
```
Input: nums = [1,2,3]
Output: [1,3,2]
```

## Example 2:
```
Input: nums = [3,2,1]
Output: [1,2,3]
```

## Example 3:
```
Input: nums = [1,1,5]
Output: [1,5,1]
```

## Constraints:
- `1 <= nums.length <= 100`
- `0 <= nums[i] <= 100`

---

## Approach

This solution follows the classic **"find pivot, find successor, swap, then reverse the suffix"** strategy.

### Idea

- **Find the pivot (`first`):** Scan the array from left to right, tracking the rightmost index `i` where `nums[i] < nums[i+1]`. This marks the point where the sequence stops being non-increasing from the right. If no such index is updated, the array is fully descending (already the largest permutation).
- **Find the successor (`second`):** Starting from the end of the array, find the first element (scanning right to left, down to just after `first`) that is greater than `nums[first]`. This is the smallest value larger than `nums[first]` that lies to its right.
- **Swap:** If a valid pivot was found (`first != second`), swap `nums[first]` and `nums[second]`. The suffix starting right after `first` is still in descending order.
- **Reverse the suffix:** Reverse everything from just after `first` (or from `first` itself if no pivot was found, meaning the whole array must be reversed) to the end, turning the descending suffix into ascending order — the smallest possible arrangement for that suffix.

---

## Code

```java
class Solution {
    public void nextPermutation(int[] nums) {
        int first = 0;
        int second = 0;
        int start = 0;
        int end = nums.length - 1;

        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] < nums[i + 1]) {
                first = i;
            }
        }

        for (int i = nums.length - 1; i > first; i--) {
            if (nums[i] > nums[first]) {
                second = i;
                break;
            }
        }

        if (first != second) {
            int change = nums[first];
            nums[first] = nums[second];
            nums[second] = change;
            start = first + 1;
        } else {
            start = first;
        }

        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }
}
```

---

## Dry Run

### Input

```text
nums = [3,2,1]
```

**Step 1: Find pivot (`first`)**

| i | nums[i] < nums[i+1]? | first |
|---|------------------------|-------|
|0|nums[0]=3 < nums[1]=2? No|0 (unchanged)|
|1|nums[1]=2 < nums[2]=1? No|0 (unchanged)|

Since `first` was never updated, it stays at its initial value `0`.

**Step 2: Find successor (`second`)**

| i | nums[i] > nums[first]? | second |
|---|--------------------------|--------|
|2|nums[2]=1 > nums[0]=3? No|0 (unchanged)|
|1|nums[1]=2 > nums[0]=3? No|0 (unchanged)|

Loop ends (`i > first` fails), `second` stays at its initial value `0`.

**Step 3: Compare and swap**

`first == second` (both `0`) → no distinct pivot/successor pair exists → `start = first = 0`.

**Step 4: Reverse from `start` to `end`**

| start | end | Array Before Swap | Array After Swap |
|-------|-----|--------------------|--------------------|
|0|2|[3,2,1]|[1,2,3]|
|1|1|[1,2,3]|loop stops (start < end fails)|

Final Array:

```text
[1, 2, 3]
```

This confirms: since `[3,2,1]` is the fully descending (largest) permutation, the algorithm wraps around to the smallest permutation `[1,2,3]`.

---

## Why This Approach Works

The pivot search identifies the rightmost position where increasing the value at that position (by swapping in a slightly larger digit from the suffix) would still produce the smallest possible increase overall.

Reversing the suffix afterward converts it from descending to ascending order, which guarantees the smallest possible arrangement for that suffix — giving the immediate next lexicographical permutation.

When no pivot exists at all (the array is entirely non-increasing), the whole array is the maximum permutation, so reversing it fully produces the minimum permutation, correctly wrapping around.

---

## Complexity Analysis

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(1)`

---

## Key Insight

Rather than generating all permutations and searching for the next one (which would be factorial time), this approach exploits the structure of lexicographical ordering: only a **single pivot swap plus a suffix reversal** is needed to move from one permutation to the very next one, all done **in place** with constant extra space.

---

## Concepts Used

- Array
- Two Pointers
- In-Place Reversal
- Lexicographic Ordering
- Greedy
- Constant Space Algorithm