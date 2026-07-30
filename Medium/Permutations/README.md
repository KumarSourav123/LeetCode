# LeetCode 46 - Permutations

## Problem Statement

Given an array `nums` of distinct integers, return all the possible **permutations**. You can return the answer in **any order**.

---

## Example 1:
```
Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
```

## Example 2:
```
Input: nums = [0,1]
Output: [[0,1],[1,0]]
```

## Example 3:
```
Input: nums = [1]
Output: [[1]]
```

## Constraints:
- `1 <= nums.length <= 6`
- `-10 <= nums[i] <= 10`
- All the integers of `nums` are **unique**

---

## Approach

This solution reuses the **"Next Permutation"** technique (from LeetCode 31) and applies it repeatedly `n!` times, capturing a snapshot of the array after each transformation.

### Idea

- Compute `n!` (the total number of permutations possible for an array of this length) by multiplying `2 × 3 × ... × nums.length`, stored in `check`.
- Repeat exactly `check` (i.e., `n!`) times:
  - Call the helper `Permutation(nums)`, which mutates `nums` **in place** into its next lexicographical permutation (using the same pivot-find, successor-swap, and suffix-reverse logic as the classic Next Permutation algorithm), and returns a snapshot of the array at that point as a `List<Integer>`.
  - Add that snapshot to the `result` list.
- Because applying "next permutation" exactly `n!` times cycles through **every** permutation of the array exactly once and wraps back around to the starting arrangement, this loop is guaranteed to visit all distinct permutations.

Since the order in which permutations are returned doesn't matter for this problem, it's fine that the first call already advances past the original array — the loop still ends up covering the complete set of `n!` permutations, just starting one step ahead of (and wrapping back around to) the initial arrangement.

---

## Code

```java
class Solution {
    public List<List<Integer>> permute(int[] nums) {

        ArrayList<List<Integer>> result = new ArrayList<>();

        int check = 1;
        for (int i = 2; i <= nums.length; i++) {
            check = check * i;
        }

        while (check > 0) {
            result.add(Permutation(nums));
            check--;
        }

        return result;
    }

    private List<Integer> Permutation(int[] arr) {

        ArrayList<Integer> list = new ArrayList<>();

        int first = 0;
        int second = 0;
        int start = 0;
        int end = arr.length - 1;

        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] < arr[i + 1]) {
                first = i;
            }
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] > arr[first]) {
                second = i;
                int temp = arr[first];
                arr[first] = arr[second];
                arr[second] = temp;
                break;
            }
        }

        if (first != second) {
            start = first + 1;
        } else {
            start = first;
        }

        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }

        for (int num : arr) {
            list.add(num);
        }

        return list;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,2,3]
```

`nums.length = 3`, so `check = 2 × 3 = 6` (there are `3! = 6` total permutations).

| Call | arr (before) | Pivot swap | After suffix reverse | Snapshot Added | arr (after) |
|------|----------------|------------|------------------------|------------------|--------------|
|1|[1,2,3]|swap arr[1],arr[2] → [1,3,2]|no reversal needed|[1,3,2]|[1,3,2]|
|2|[1,3,2]|swap arr[0],arr[2] → [2,3,1]|reverse suffix → [2,1,3]|[2,1,3]|[2,1,3]|
|3|[2,1,3]|swap arr[1],arr[2] → [2,3,1]|no reversal needed|[2,3,1]|[2,3,1]|
|4|[2,3,1]|swap arr[0],arr[1] → [3,2,1]|reverse suffix → [3,1,2]|[3,1,2]|[3,1,2]|
|5|[3,1,2]|swap arr[1],arr[2] → [3,2,1]|no reversal needed|[3,2,1]|[3,2,1]|
|6|[3,2,1]|no valid pivot found (fully descending)|reverse entire array → [1,2,3]|[1,2,3]|[1,2,3]|

Final `result`:

```text
[[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1],[1,2,3]]
```

This set contains all `6` permutations of `[1,2,3]` — just in a different starting order than LeetCode's example output, which is acceptable since the problem allows the answer in **any order**.

---

## Why This Approach Works

The "Next Permutation" algorithm defines a strict total ordering that visits every permutation of an array exactly once before cycling back to the smallest (starting) arrangement. Repeating this transformation `n!` times therefore guarantees that every one of the `n!` distinct permutations is captured exactly once, regardless of which permutation you begin from.

Since `nums` is mutated in place with each call, and each snapshot is taken as a separate `ArrayList` copy, previously stored results remain unaffected by later mutations.

---

## Complexity Analysis

- **Time Complexity:** `O(n! × n)` — `n!` calls to `Permutation`, each doing `O(n)` work to find the pivot, swap, and reverse
- **Space Complexity:** `O(n! × n)` — for storing all `n!` permutations, each of length `n`, in the result list

---

## Key Insight

Rather than using classic backtracking (swapping elements into each position recursively), this solution cleverly repurposes the **Next Permutation** logic as a generator: since applying it exactly `n!` times is mathematically guaranteed to enumerate every permutation exactly once, no explicit recursion, visited-tracking, or branching logic is needed at all.

---

## Concepts Used

- Array
- Next Permutation Algorithm
- Lexicographic Ordering
- In-Place Mutation
- Cyclic Enumeration