# LeetCode 4 - Median of Two Sorted Arrays

## Problem Statement

Given two sorted arrays `nums1` and `nums2` of size `m` and `n` respectively, return **the median** of the two sorted arrays.

The overall run time complexity should be `O(log (m+n))`.

---

## Example 1:
```
Input: nums1 = [1,3], nums2 = [2]
Output: 2.00000
Explanation: merged array = [1,2,3] and median is 2.
```

## Example 2:
```
Input: nums1 = [1,2], nums2 = [3,4]
Output: 2.50000
Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
```

## Constraints:
- `nums1.length == m`
- `nums2.length == n`
- `0 <= m <= 1000`
- `0 <= n <= 1000`
- `1 <= m + n <= 2000`
- `-10^6 <= nums1[i], nums2[i] <= 10^6`

---

## Approach

This solution uses **binary search on the smaller array's partition point**, avoiding ever actually merging the two arrays. The core idea: find a way to split both arrays into a combined "left half" and "right half" (of the correct total sizes) such that every element in the left half is `<=` every element in the right half — at which point the median can be read directly off the boundary elements, without ever sorting or merging anything.

### Idea

- **Ensure `nums1` is the shorter array:** if `n1 > n2`, recurse with the arguments swapped, `findMedianSortedArrays(nums2, nums1)`. Binary searching over the shorter array keeps the search space (and thus the time complexity) tied to `O(log(min(m,n)))`, well within the required `O(log(m+n))` bound.
- **Set up the binary search bounds:** `low = 0`, `high = n1` — searching over how many elements of `nums1` (`mid1`, from `0` to `n1`) go into the "left half."
- **Compute the fixed target left-half size:** `left = (n1 + n2 + 1) / 2` — this is how many total elements (from both arrays combined) should end up in the left half. The `+1` before dividing ensures that for an odd total length, the left half gets the extra element (which is what makes the odd-length case resolve to a single middle value).
- **Binary search loop**, `while(low <= high)`:
  - `mid1 = (low + high) / 2` — try taking this many elements from `nums1` into the left half.
  - `mid2 = left - mid1` — the remaining left-half elements must come from `nums2`, so this is forced once `mid1` and the target `left` size are fixed.
  - **Identify the four boundary values** around this partition:
    - `r1 = nums1[mid1]` (the first element of `nums1`'s right half), or `Integer.MAX_VALUE` if `mid1 == n1` (nothing left in `nums1`'s right half).
    - `r2 = nums2[mid2]` similarly, or `MAX_VALUE` if `mid2 == n2`.
    - `l1 = nums1[mid1-1]` (the last element of `nums1`'s left half), or `Integer.MIN_VALUE` if `mid1 == 0` (nothing in `nums1`'s left half).
    - `l2 = nums2[mid2-1]` similarly, or `MIN_VALUE` if `mid2 == 0`.
  - **Check if this partition is valid:** `if(l1 <= r2 && l2 <= r1)` — every left-half element must be `<=` every right-half element. Using the sentinel `MIN_VALUE`/`MAX_VALUE` values means a "missing" boundary (because one array contributed nothing to that half) is automatically treated as never violating the condition.
    - If valid, the median can be computed immediately:
      - **Odd total length** (`n % 2 == 1`): the median is the single largest value in the left half, `Math.max(l1, l2)`.
      - **Even total length**: the median is the average of the largest left-half value and the smallest right-half value: `(Math.max(l1,l2) + Math.min(r1,r2)) / 2.0`.
    - **If `l1 > r2`**, `nums1`'s left half reaches too far into large values — too many elements were taken from `nums1`, so search a smaller `mid1`: `high = mid1 - 1`.
    - **Otherwise** (`l2 > r1`, implicitly, since the valid case and the `l1 > r2` case were already ruled out), not enough was taken from `nums1` — search a larger `mid1`: `low = mid1 + 1`.
- A final `return 0;` is an unreachable fallback, since the binary search is guaranteed to find a valid partition and return from within the loop before it can exit naturally.

---

## Code

```java
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;

        if(n1 > n2){
            return findMedianSortedArrays(nums2, nums1);
        }

        int low = 0;
        int high = n1;

        int left = (n1+n2+1)/2;
        int n = n1+n2;

        while(low <= high){
            int mid1 = (low+high)/2;
            int mid2 = left - mid1;

            int l1 = Integer.MIN_VALUE;
            int l2 = Integer.MIN_VALUE;
            int r1 = Integer.MAX_VALUE;
            int r2 = Integer.MAX_VALUE;

            if(mid1 < n1) r1 = nums1[mid1];
            if(mid2 < n2) r2 = nums2[mid2];
            if(mid1 - 1 >= 0) l1 = nums1[mid1-1];
            if(mid2 - 1 >= 0) l2 = nums2[mid2-1];

            if((l1 <= r2) && (l2 <= r1)){
                if(n % 2 == 1){
                    return Math.max(l1,l2);
                }else{
                    return ((double)(Math.max(l1,l2)) + (Math.min(r1,r2)))/2.0;
                }
            }else if(l1 > r2){
                high = mid1-1;
            }else{
                low = mid1+1;
            }
        }

        return 0;
    }
}
```

---

## Dry Run

### Input

```text
nums1 = [1, 2]
nums2 = [3, 4]
```

`n1 = 2`, `n2 = 2`. `n1 > n2`? false → no swap needed.

`low = 0`, `high = 2`, `left = (2+2+1)/2 = 5/2 = 2` (integer division), `n = 4`

### Iteration 1

`mid1 = (0+2)/2 = 1`, `mid2 = left - mid1 = 2 - 1 = 1`

| Boundary | Condition | Value |
|----------|-----------|-------|
|r1|`mid1(1) < n1(2)` → true|`nums1[1] = 2`|
|r2|`mid2(1) < n2(2)` → true|`nums2[1] = 4`|
|l1|`mid1-1(0) >= 0` → true|`nums1[0] = 1`|
|l2|`mid2-1(0) >= 0` → true|`nums2[0] = 3`|

**Validity check:** `(l1 <= r2) && (l2 <= r1)` → `(1 <= 4) && (3 <= 2)` → `true && false` → **false**

`l1 > r2`? `1 > 4` → false → `else`: `low = mid1 + 1 = 2`

State: `low = 2`, `high = 2`

### Iteration 2

`mid1 = (2+2)/2 = 2`, `mid2 = left - mid1 = 2 - 2 = 0`

| Boundary | Condition | Value |
|----------|-----------|-------|
|r1|`mid1(2) < n1(2)` → false|`Integer.MAX_VALUE`|
|r2|`mid2(0) < n2(2)` → true|`nums2[0] = 3`|
|l1|`mid1-1(1) >= 0` → true|`nums1[1] = 2`|
|l2|`mid2-1(-1) >= 0` → false|`Integer.MIN_VALUE`|

**Validity check:** `(l1 <= r2) && (l2 <= r1)` → `(2 <= 3) && (MIN_VALUE <= MAX_VALUE)` → `true && true` → **true**, valid partition found!

`n % 2 == 1`? `4 % 2 = 0` → false → **even-length case**: return `(Math.max(l1,l2) + Math.min(r1,r2)) / 2.0`

`Math.max(2, MIN_VALUE) = 2`, `Math.min(MAX_VALUE, 3) = 3`

**Return:** `(2 + 3) / 2.0 = 2.5`

Output:

```text
2.50000
```

This matches the expected output. The valid partition places `[1, 2]` in the combined left half and `[3, 4]` in the combined right half — exactly matching the merged array `[1,2,3,4]`, whose median is `(2+3)/2 = 2.5`.

---

## Why This Approach Works

The median of a sorted sequence is defined entirely by where the sequence's "middle" sits — specifically, for a partition of the full merged array into a left half and right half of the correct sizes, the median can be read directly off the boundary between them **as long as that partition is valid** (every left element `<=` every right element), without needing to know the exact order of every element within each half.

The binary search works because increasing `mid1` (taking more elements from `nums1` into the left half) monotonically affects the boundary values in a predictable direction: it makes `l1` larger (or equal) and `r1` larger (or equal), while `mid2` (forced to `left - mid1`) correspondingly decreases, making `l2` smaller (or equal) and `r2` smaller (or equal). So if `l1 > r2` (too much taken from `nums1`), decreasing `mid1` is the correct direction to search in; the converse holds for the other invalid case. This monotonic relationship is exactly what makes binary search applicable — there's a well-defined "too far one way / too far the other way" structure to search over.

Using `Integer.MIN_VALUE`/`MAX_VALUE` as sentinels for boundary values that don't actually exist (because one array contributed nothing to that side of the partition) is what allows the same `l1 <= r2 && l2 <= r1` check to work uniformly, without needing separate handling for the edge cases where `mid1` or `mid2` lands at `0` or at the end of its array.

---

## Complexity Analysis

- **Time Complexity:** `O(log(min(m,n)))` — the binary search operates over the range `[0, n1]` where `n1` is the length of the *shorter* array (guaranteed by the swap at the top), satisfying the required `O(log(m+n))` bound with room to spare
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used per call; the one possible recursive call (to ensure `nums1` is the shorter array) happens at most once, so this doesn't accumulate into meaningful extra space

---

## Key Insight

This is the canonical `O(log(min(m,n)))` solution to the two-sorted-arrays median problem, and it hinges on reframing "find the median" as "find a valid partition point" rather than "merge and index into the middle." Binary searching over just one array's partition index (with the other array's index forced by the fixed target left-half size) is what collapses what looks like it needs `O(m+n)` merging work down to a logarithmic search — a pattern worth recognizing whenever a problem's answer can be characterized by a single monotonic "partition" or "threshold" property.

---

## Concepts Used

- Array
- Binary Search
- Partition-Based Search
- Sentinel Values (Integer.MIN_VALUE / MAX_VALUE)
- Divide and Conquer