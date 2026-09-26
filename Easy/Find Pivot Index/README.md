# LeetCode 724 - Find Pivot Index

## Problem Statement

Given an array of integers `nums`, calculate the **pivot index** of this array.

The pivot index is the index where the sum of all the numbers **strictly to the left** of the index is equal to the sum of all the numbers **strictly to the index's right**.

If the index is on the left edge of the array, then the left sum is `0` because there are no elements to the left. This also applies to the right edge of the array.

Return *the leftmost pivot index*. If no such index exists, return `-1`.

---

## Example 1:
```
Input: nums = [1,7,3,6,5,6]
Output: 3
Explanation:
The pivot index is 3.
Left sum = nums[0] + nums[1] + nums[2] = 1 + 7 + 3 = 11
Right sum = nums[4] + nums[5] = 5 + 6 = 11
```

## Example 2:
```
Input: nums = [1,2,3]
Output: -1
Explanation: There is no index that satisfies the conditions in the problem statement.
```

## Example 3:
```
Input: nums = [2,1,-1]
Output: 0
Explanation:
The pivot index is 0.
Left sum = 0 (no elements to the left of index 0)
Right sum = nums[1] + nums[2] = 1 + -1 = 0
```

## Constraints:
- `1 <= nums.length <= 10^4`
- `-1000 <= nums[i] <= 1000`

**Note:** This question is the same as 1991: [Find the Middle Index in Array](https://leetcode.com/problems/find-the-middle-index-in-array/)

---

## Approach

This solution precomputes two auxiliary arrays — a running sum from the left and a running sum from the right, both **inclusive** of the current index — then scans for the first index where these two inclusive sums are equal.

### Idea

- **Build `leftSum`:** walk left to right, accumulating `lsum = nums[i] + lsum` and storing it at `leftSum[i]`. Note this is the sum of everything **from index `0` through `i`, inclusive** — not strictly the numbers to the left of `i`.
- **Build `rightSum`:** walk right to left, accumulating `rsum = nums[i] + rsum` and storing it at `rightSum[i]`. This is likewise the sum from index `i` through the end, **inclusive** of `i`.
- **Find the leftmost pivot:** scan left to right, and return the first index `i` where `leftSum[i] == rightSum[i]`.
- If no such index is found by the end of the scan, return `-1`.

The subtle but important detail is that `leftSum[i]` and `rightSum[i]` both **include `nums[i]` itself**, whereas the problem asks about sums *strictly* to the left and right of `i` (excluding `nums[i]`). This works out correctly anyway: since `nums[i]` is added to *both* sides equally, `leftSum[i] == rightSum[i]` is mathematically equivalent to `(strictly-left sum) == (strictly-right sum)` — the shared `nums[i]` term cancels out of the comparison either way.

---

## Code

```java
class Solution {
    public int pivotIndex(int[] nums) {
        int[] leftSum = new int[nums.length];
        int[] rightSum = new int[nums.length];
        int lsum = 0;
        int rsum = 0;

        for(int i = 0; i< nums.length ; i++){
            lsum = nums[i] + lsum;
            leftSum[i] = lsum;
        }

        for(int i = nums.length-1 ; i>=0 ; i--){
            rsum = nums[i] + rsum;
            rightSum[i] = rsum;
        }

        for(int i = 0; i< nums.length ; i++){
            if(leftSum[i] == rightSum[i]){
                return i;
            }
        }
        return -1;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1, 7, 3, 6, 5, 6]
index:  0  1  2  3  4  5
```

### Phase 1 — Build leftSum (inclusive prefix sums)

`lsum = 0`

| i | nums[i] | lsum = nums[i] + lsum | leftSum[i] |
|---|---------|---------------------------|-----------------|
|0|1|0 + 1 = 1|1|
|1|7|1 + 7 = 8|8|
|2|3|8 + 3 = 11|11|
|3|6|11 + 6 = 17|17|
|4|5|17 + 5 = 22|22|
|5|6|22 + 6 = 28|28|

`leftSum = [1, 8, 11, 17, 22, 28]`

### Phase 2 — Build rightSum (inclusive suffix sums)

`rsum = 0`

| i | nums[i] | rsum = nums[i] + rsum | rightSum[i] |
|---|---------|---------------------------|------------------|
|5|6|0 + 6 = 6|6|
|4|5|6 + 5 = 11|11|
|3|6|11 + 6 = 17|17|
|2|3|17 + 3 = 20|20|
|1|7|20 + 7 = 27|27|
|0|1|27 + 1 = 28|28|

`rightSum = [28, 27, 20, 17, 11, 6]`

### Phase 3 — Find the leftmost pivot

| i | leftSum[i] | rightSum[i] | Equal? |
|---|-------------|------------------|--------|
|0|1|28|no|
|1|8|27|no|
|2|11|20|no|
|3|17|17|**yes** → `return 3`|

**Return:** `3`

Output:

```text
3
```

This matches the expected output. Sanity check against the strictly-excluding definition: at index 3, the strict left sum is `nums[0]+nums[1]+nums[2] = 1+7+3 = 11` and the strict right sum is `nums[4]+nums[5] = 5+6 = 11` — equal, confirming index 3 is genuinely the pivot. (The inclusive sums compared in code, `17` and `17`, are each simply `11 + nums[3] = 11 + 6 = 17` — the shared `nums[3]` term added to both sides, which is why the comparison still holds.)

---

## Why This Approach Works

Precomputing both prefix and suffix sums in linear passes means every index's left-of and right-of totals are available in `O(1)` lookup time during the final scan, rather than needing to re-sum sub-ranges for every candidate index (which would cost `O(n)` per index, `O(n²)` overall).

The inclusive-vs-exclusive subtlety is resolved by simple algebra: `leftSum[i] = strictLeft(i) + nums[i]` and `rightSum[i] = strictRight(i) + nums[i]`. Setting these inclusive sums equal to each other, `strictLeft(i) + nums[i] = strictRight(i) + nums[i]`, and subtracting `nums[i]` from both sides recovers exactly `strictLeft(i) = strictRight(i)` — the problem's actual condition. So comparing the inclusive sums is provably equivalent to comparing the exclusive ones, meaning no separate adjustment (like subtracting `nums[i]` before comparing) is actually necessary.

Scanning for the pivot left-to-right, rather than checking all indices and picking the smallest match afterward, is what guarantees the **leftmost** valid pivot is returned, since the function returns immediately upon finding the first match.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — three separate linear passes over the array (building `leftSum`, building `rightSum`, and scanning for the pivot), so `O(n) + O(n) + O(n) = O(n)` overall
- **Space Complexity:** `O(n)` — two auxiliary arrays of size `n` are allocated to hold the prefix and suffix sums

---

## Key Insight

Precomputing prefix and suffix sums is a standard technique whenever a problem requires comparing "everything before index `i`" against "everything after index `i`" for many different values of `i` — computing these once in linear time and reusing them avoids the quadratic cost of re-summing sub-ranges from scratch for each candidate index. It's also worth noticing, as this solution demonstrates, that comparing *inclusive* running sums can sometimes stand in cleanly for an *exclusive* comparison, when the extra shared term cancels out algebraically — saving the need for an explicit `-nums[i]` adjustment.

---

## Concepts Used

- Array
- Prefix Sum / Suffix Sum
- Single-Pass Precomputation
- Linear Scan