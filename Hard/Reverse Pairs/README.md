# LeetCode 493 - Reverse Pairs

## Problem Statement

Given an integer array `nums`, return *the number of reverse pairs in the array*.

A reverse pair is a pair `(i, j)` where:

- `0 <= i < j < nums.length`
- `nums[i] > 2 * nums[j]`

---

## Example 1:
```
Input: nums = [1,3,2,3,1]
Output: 2
Explanation: The reverse pairs are:
(1, 4) --> nums[1] = 3, nums[4] = 1, 3 > 2 * 1
(3, 4) --> nums[3] = 3, nums[4] = 1, 3 > 2 * 1
```

## Example 2:
```
Input: nums = [2,4,3,5,1]
Output: 3
Explanation: The reverse pairs are:
(1, 4) --> nums[1] = 4, nums[4] = 1, 4 > 2 * 1
(2, 4) --> nums[2] = 3, nums[4] = 1, 3 > 2 * 1
(3, 4) --> nums[3] = 5, nums[4] = 1, 5 > 2 * 1
```

## Constraints:
- `1 <= nums.length <= 5 * 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`

---

## Approach

This solution uses a **modified Merge Sort**, counting cross-half reverse pairs during the divide-and-conquer process before merging the two sorted halves together.

### Idea

- **`mergeSort(arr, start, end)`:** Recursively sort the left half (`start` to `mid`) and right half (`mid+1` to `end`), accumulating the count of reverse pairs found within each half.
- **`countPairs(arr, start, mid, end)`:** After both halves are individually sorted (but not yet merged), count reverse pairs **across** the two halves using a two-pointer sweep:
  - For each index `i` in the sorted left half, advance a `right` pointer through the sorted right half as long as `arr[i] > 2 * arr[right]` — since the right half is already sorted, once this condition fails for a given `right`, it will also fail for all remaining values from that point onward for the *current* `i` (they're only larger), so the pointer never needs to reset.
  - Because `right` only ever moves forward (never backward) across the entire scan of the left half, this counting step runs in `O(n)` total, not `O(n²)`.
  - Add `(right - (mid + 1))` to the count for each `i` — this represents how many elements in the right half satisfy the reverse-pair condition with the current `arr[i]`.
- **`merge(arr, start, mid, end)`:** After counting, perform the standard merge step to combine the two sorted halves into one sorted subarray, which is required for the counting logic to remain valid at the next level up the recursion.
- The total reverse pair count returned by `mergeSort` is the sum of pairs found in the left half, the right half, and pairs counted across the two halves at every level of the recursion.

---

## Code

```java
class Solution {
    public int reversePairs(int[] nums) {
        int start = 0;
        int end = nums.length - 1;
        return mergeSort(nums, start, end);
    }

    private int mergeSort(int[] arr, int start, int end) {
        if (start >= end) {
            return 0;
        }
        int count = 0;
        int mid = (start + end) / 2;
        count += mergeSort(arr, start, mid);
        count += mergeSort(arr, mid + 1, end);
        count += countPairs(arr, start, mid, end);
        merge(arr, start, mid, end);
        return count;
    }

    private int countPairs(int[] arr, int start, int mid, int end) {
        int count = 0;
        int right = mid + 1;

        for (int i = start; i <= mid; i++) {
            while (right <= end && (arr[i] > 2L * arr[right])) {
                right++;
            }
            count += (right - (mid + 1));
        }
        return count;
    }

    private void merge(int[] arr, int start, int mid, int end) {
        ArrayList<Integer> list = new ArrayList<>();

        int i = start;
        int j = mid + 1;

        while (i <= mid && j <= end) {
            if (arr[i] > arr[j]) {
                list.add(arr[j]);
                j++;
            } else {
                list.add(arr[i]);
                i++;
            }
        }
        while (i <= mid) {
            list.add(arr[i]);
            i++;
        }
        while (j <= end) {
            list.add(arr[j]);
            j++;
        }
        for (int k = 0; k < list.size(); k++) {
            arr[start + k] = list.get(k);
        }
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,3,2,3,1]
```

**Recursive breakdown:** `mergeSort(0,4)` splits into `mergeSort(0,2)` (left: `[1,3,2]`) and `mergeSort(3,4)` (right: `[3,1]`).

**Left half `mergeSort(0,2)`:** further splits into `[1,3]` and `[2]`; both `countPairs` calls at this level return `0` (no reverse pairs), and merging produces the sorted subarray `[1,2,3]`.
→ `mergeSort(0,2)` returns `0`

**Right half `mergeSort(3,4)`:** splits into `[3]` and `[1]`. `countPairs(arr,3,3,4)`: `arr[3]=3 > 2*arr[4]=2`? Yes → `right` advances past bounds, contributing `1` pair. Merging produces the sorted subarray `[1,3]`.
→ `mergeSort(3,4)` returns `1`

**Cross-half count `countPairs(arr,0,2,4)`** (left half now sorted `[1,2,3]`, right half now sorted `[1,3]`, `mid=2`, `right` starts at `3`):

| i | arr[i] | arr[i] > 2×arr[right]? | right after | count contribution |
|---|--------|------------------------------|----------------|---------------------|
|0|1|1 > 2×arr[3]=2? No|3|0|
|1|2|2 > 2×arr[3]=2? No (not strictly greater)|3|0|
|2|3|3 > 2×arr[3]=2? Yes → right++ → right=4; then 3 > 2×arr[4]=6? No, stop|4|4-3=1|

`countPairs(0,2,4)` returns `1`

**Total count:** `mergeSort(0,2) + mergeSort(3,4) + countPairs(0,2,4) = 0 + 1 + 1 = 2`

Output:

```text
2
```

This matches the expected output.

---

## Why This Approach Works

Reverse pairs `(i, j)` where `i < j` and `nums[i] > 2 * nums[j]` can always be classified into three groups relative to any split point: pairs entirely within the left half, pairs entirely within the right half, and pairs that span across both halves. Merge sort's divide-and-conquer structure naturally handles the first two groups recursively — the key insight is efficiently counting the third group (**cross-half pairs**) using the fact that **both halves are already individually sorted** at the point of comparison.

Because both halves are sorted, the `right` pointer in `countPairs` never needs to move backward as `i` advances through the left half — this monotonic pointer movement is what keeps the cross-half counting step linear instead of quadratic, preserving the overall `O(n log n)` complexity of merge sort.

---

## Complexity Analysis

- **Time Complexity:** `O(n log n)` — standard merge sort recurrence, with the linear-time `countPairs` step folded into each merge level
- **Space Complexity:** `O(n)` — for the temporary `ArrayList` used during merging, plus `O(log n)` recursion stack depth

---

## Key Insight

This is a classic example of **counting via divide-and-conquer during merge sort**: instead of checking every pair with a brute-force `O(n²)` scan, exploiting the fact that both halves become sorted through the recursive process allows cross-half pairs to be counted with a single linear sweep using two pointers — the same underlying idea used to count inversions in an array.

---

## Concepts Used

- Array
- Merge Sort
- Divide and Conquer
- Two Pointers
- Counting via Sorted Subarrays