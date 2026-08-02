# LeetCode 238 - Product of Array Except Self

## Problem Statement

Given an integer array `nums`, return an array `answer` such that `answer[i]` is equal to the product of all the elements of `nums` except `nums[i]`.

The product of any prefix or suffix of `nums` is **guaranteed** to fit in a **32-bit integer**.

You must write an algorithm that runs in `O(n)` time and **without using the division operation**.

---

## Example 1:
```
Input: nums = [1,2,3,4]
Output: [24,12,8,6]
```

## Example 2:
```
Input: nums = [-1,1,0,-3,3]
Output: [0,0,9,0,0]
```

## Constraints:
- `2 <= nums.length <= 10^5`
- `-30 <= nums[i] <= 30`
- The input is generated such that `answer[i]` is guaranteed to fit in a **32-bit integer**.

## Follow up:
Can you solve the problem in `O(1)` extra space complexity? (The output array does not count as extra space for space complexity analysis.)

---

## Approach

This solution handles the problem based on **how many zeros appear in the array**, using the product of all non-zero elements as a shortcut for those cases.

### Idea

- **First pass:** Compute `product`, the product of all **non-zero** elements in `nums`. Along the way, also track:
  - `count` — set to `1` if at least one non-zero element was found (used to confirm the product is meaningful).
  - `index` — the index of the last zero encountered in the array.
  - `value` — a running count of how many zeros appear in total.
- **Second pass:** Determine each `arr[i]` based on the number of zeros found:
  - **Two or more zeros (`value >= 2`):** Every product-except-self value must be `0`, since every position has at least one zero elsewhere in the array contributing to its product.
  - **Exactly one zero, and `nums[i]` is that zero:** The answer at this position is the product of all the other (non-zero) elements, which is exactly `product`.
  - **Exactly one zero, and `nums[i]` is not the zero:** The answer at every other position is `0`, since the single zero elsewhere in the array zeroes out that position's product.
  - **No zeros at all:** The answer is simply `product / nums[i]` — the total product of all elements, divided by the current element.

---

## Code

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] arr = new int[nums.length];
        int product = 1;
        int count = 0;
        int index = Integer.MIN_VALUE;
        int value = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                product = product * nums[i];
                count = 1;
            }
            if (nums[i] == 0) {
                index = i;
                value++;
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (value >= 2) {
                arr[i] = 0;
            } else if (nums[i] == 0) {
                if (count == 1) {
                    arr[i] = product;
                } else {
                    arr[i] = 0;
                }
            } else {
                if (index >= 0 && index <= nums.length - 1) {
                    arr[i] = 0;
                } else {
                    arr[i] = product / nums[i];
                }
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
nums = [-1,1,0,-3,3]
```

**First pass: compute product of non-zero elements, track zero info**

| i | nums[i] | product after | count | index | value |
|---|---------|-----------------|-------|-------|-------|
|0|-1|1 * -1 = -1|1|MIN_VALUE|0|
|1|1|-1 * 1 = -1|1|MIN_VALUE|0|
|2|0|-1 (unchanged)|1|2|1|
|3|-3|-1 * -3 = 3|1|2|1|
|4|3|3 * 3 = 9|1|2|1|

After first pass: `product = 9`, `count = 1`, `index = 2`, `value = 1`

**Second pass: build result** (`value = 1`, not `>= 2`)

| i | nums[i] | Branch Taken | arr[i] |
|---|---------|----------------|--------|
|0|-1|nums[i]≠0, index(2) is valid → arr[i]=0|0|
|1|1|nums[i]≠0, index(2) is valid → arr[i]=0|0|
|2|0|nums[i]==0, count==1 → arr[i]=product|9|
|3|-3|nums[i]≠0, index(2) is valid → arr[i]=0|0|
|4|3|nums[i]≠0, index(2) is valid → arr[i]=0|0|

Final Output:

```text
[0, 0, 9, 0, 0]
```

This matches the expected output exactly.

---

## Why This Approach Works

If there are **two or more zeros**, every position's product-except-self must include at least one of those zeros, forcing the result to `0` everywhere.

If there is **exactly one zero**, only the position of that zero can have a non-zero product (the product of everything else); every other position's product is forced to `0` by that single zero.

If there are **no zeros**, the total product divided by the current element gives exactly the product of all the other elements — which is mathematically valid as long as none of the elements are `0` (avoiding any division-by-zero issue).

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — two linear passes through the array
- **Space Complexity:** `O(1)` extra space (excluding the output array), satisfying the follow-up's constant-space requirement

---

## Important Note on the "No Division" Requirement

The problem statement explicitly requires solving this **without using the division operation** — however, this solution does use division (`product / nums[i]`) in the case where the array contains no zeros. While it produces correct results (and division is safe here since a guard against dividing by an actual `0` is already handled by the zero-counting logic), it technically does not meet the strict "no division" requirement stated in the problem. A fully compliant `O(n)` time, no-division solution would instead use two passes building **prefix products** and **suffix products** separately, then multiply `prefix[i] * suffix[i]` for each position.

---

## Key Insight

Categorizing the problem by the number of zeros present is a clever shortcut: it reduces most cases (one zero, or two-or-more zeros) to trivial constant-time lookups without needing division at all, and only falls back to division for the zero-free case — though a true division-free approach would replace even that final case with a prefix/suffix product technique.

---

## Concepts Used

- Array
- Prefix/Suffix Product (partially)
- Zero-Case Handling
- Edge Case Analysis