# LeetCode 2149 - Rearrange Array Elements by Sign

## Problem Statement

You are given a **0-indexed** integer array `nums` of **even length**, consisting of an **equal number of positive and negative integers**.

You should return the array of `nums` such that the array follows the given conditions:

1. Every consecutive pair of integers have **opposite signs**.
2. For all integers with the same sign, the **order** in which they were present in `nums` is preserved.
3. The rearranged array **begins with a positive integer**.

Return the modified array after rearranging the elements to satisfy the aforementioned conditions.

---

## Example 1:
```
Input: nums = [3,1,-2,-5,2,-4]
Output: [3,-2,1,-5,2,-4]
Explanation:
The positive integers in nums are [3,1,2]. The negative integers are [-2,-5,-4].
The only possible way to rearrange them such that they satisfy all conditions is [3,-2,1,-5,2,-4].
Other ways such as [1,-2,2,-5,3,-4], [3,1,-2,-5,2,-4], [-2,3,-5,1,-4,2] are incorrect because they do not satisfy one or more conditions.
```

## Example 2:
```
Input: nums = [-1,1]
Output: [1,-1]
Explanation:
1 is the only positive integer and -1 the only negative integer in nums.
So nums is rearranged to [1,-1].
```

## Constraints:
- `2 <= nums.length <= 2 * 10^5`
- `nums.length` is even
- `1 <= |nums[i]| <= 10^5`
- `nums` consists of equal number of positive and negative integers

**Note:** It is not required to do the modifications in-place.

---

## Approach

This solution uses **two pointers** to place positive and negative numbers into their correct alternating positions in a new array.

### Idea

- Create a new array `arr` of the same length as `nums`.
- Use pointer `p` starting at index `0` to track where the **next positive** number should go.
- Use pointer `n` starting at index `1` to track where the **next negative** number should go.
- Traverse `nums` in order:
  - If the current element is positive, place it at `arr[p]` and increment `p` by `2`.
  - If the current element is negative, place it at `arr[n]` and increment `n` by `2`.
- Since positives and negatives are placed at even and odd indices respectively (in the order they were encountered), the relative order within each sign group is automatically preserved.

---

## Code

```java
class Solution {
    public int[] rearrangeArray(int[] nums) {
        int[] arr = new int[nums.length];

        int p = 0;
        int n = 1;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                arr[p] = nums[i];
                p = p + 2;
            } else {
                arr[n] = nums[i];
                n = n + 2;
            }
        }

        return arr;
    }
}
```

---

## Dry Run

### Input

```text
nums = [3,1,-2,-5,2,-4]
```

**Initial:** `p = 0`, `n = 1`

| i | nums[i] | Sign | Action | p | n | arr State |
|---|---------|------|--------|---|---|-----------|
|0|3|Positive|arr[0] = 3|0→2|1|[3,_,_,_,_,_]|
|1|1|Positive|arr[2] = 1|2→4|1|[3,_,1,_,_,_]|
|2|-2|Negative|arr[1] = -2|4|1→3|[3,-2,1,_,_,_]|
|3|-5|Negative|arr[3] = -5|4|3→5|[3,-2,1,-5,_,_]|
|4|2|Positive|arr[4] = 2|4→6|5|[3,-2,1,-5,2,_]|
|5|-4|Negative|arr[5] = -4|6|5→7|[3,-2,1,-5,2,-4]|

Final Array:

```text
[3, -2, 1, -5, 2, -4]
```

---

## Why This Approach Works

Since positives always land at even indices (`0, 2, 4, ...`) and negatives always land at odd indices (`1, 3, 5, ...`), the alternating sign condition is guaranteed by construction.

Because each sign group is placed into `arr` in the same order it was scanned from `nums`, the relative order of positives among themselves (and negatives among themselves) is naturally preserved without needing extra sorting or tracking.

---

## Complexity Analysis

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(n)` (for the new result array)

---

## Key Insight

Rather than separating positives and negatives into two lists and merging them afterward, using two independent pointers that stride by `2` lets us build the final alternating array in a **single pass**, avoiding any extra merging step.

---

## Concepts Used

- Array
- Two Pointers
- Simulation
- Order Preservation