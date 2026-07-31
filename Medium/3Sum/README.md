# LeetCode 15 - 3Sum (Refined For-Loop Version)

## Problem Statement

Given an integer array `nums`, return all the triplets `[nums[i], nums[j], nums[k]]` such that `i != j`, `i != k`, and `j != k`, and `nums[i] + nums[j] + nums[k] == 0`.

Notice that the solution set must not contain duplicate triplets.

---

## Example 1:
```
Input: nums = [-1,0,1,2,-1,-4]
Output: [[-1,-1,2],[-1,0,1]]
```

## Example 2:
```
Input: nums = [0,1,1]
Output: []
```

## Example 3:
```
Input: nums = [0,0,0]
Output: [[0,0,0]]
```

## Constraints:
- `3 <= nums.length <= 3000`
- `-10^5 <= nums[i] <= 10^5`

---

## Approach

This is a cleaner restructuring of the **sort + two-pointer** 3Sum approach, using a standard `for` loop with a `continue` statement to skip duplicate fixed elements, instead of manually managing the outer index with a `while` loop.

### Idea

- **Sort** `nums` in ascending order first, enabling both the two-pointer search and straightforward duplicate skipping.
- Iterate through the array with a `for` loop using index `i`, treating `nums[i]` as the fixed first element of the triplet.
  - If `i > 0` and `nums[i] == nums[i - 1]`, this fixed element was already processed in a previous iteration, so `continue` to skip it — avoiding duplicate triplets that share the same first element.
- For each valid `i`, use two pointers: `j` starting right after `i`, and `k` starting at the end of the array. While `j < k`:
  - Compute `sum = nums[i] + nums[j] + nums[k]`.
  - If `sum == 0`, record the triplet, then advance `j` forward and pull `k` backward, skipping over any duplicate values encountered at the new positions.
  - If `sum < 0`, move `j` forward to increase the sum.
  - If `sum > 0`, move `k` backward to decrease the sum.

---

## Code

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {

        ArrayList<List<Integer>> result = new ArrayList<>();

        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            int j = i + 1;
            int k = nums.length - 1;

            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];

                if (sum == 0) {
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    result.add(list);

                    j++;
                    while (j < k && nums[j] == nums[j - 1]) {
                        j++;
                    }

                    k--;
                    while (k > j && nums[k] == nums[k + 1]) {
                        k--;
                    }
                } else if (sum < 0) {
                    j++;
                } else {
                    k--;
                }
            }
        }

        return result;
    }
}
```

---

## Dry Run

### Input

```text
nums = [-1,0,1,2,-1,-4]
```

**After sorting:** `nums = [-4,-1,-1,0,1,2]`

| i | nums[i] | Skip? (dup check) | j, k walkthrough | Triplets Found |
|---|---------|----------------------|---------------------|------------------|
|0|-4|No (i=0)|sum stays negative throughout (j reaches k) → no match|none|
|1|-1|No (nums[1]≠nums[0])|j=2,k=5: sum=0 → add [-1,-1,2]; j=3,k=4: sum=0 → add [-1,0,1]; loop ends|[-1,-1,2], [-1,0,1]|
|2|-1|Yes (nums[2]==nums[1]) → continue|-|-|
|3|0|No (nums[3]≠nums[2])|j=4,k=5: sum=3>0 → k-- → loop ends (j<k fails)|none|
|4|1|No (nums[4]≠nums[3])|j=5,k=5: j<k false, no iterations|none|
|5|2|No (nums[5]≠nums[4])|j=6,k=5: j<k false, no iterations|none|

Final Result:

```text
[[-1,-1,2],[-1,0,1]]
```

This exactly matches the expected output (same result as the `while`-loop version, since the underlying logic is identical).

---

## Why This Approach Works

The correctness argument is identical to the original `while`-loop version: sorting enables predictable, monotonic movement of the two pointers, and skipping duplicate values — both for the fixed index `i` (via the `continue` check) and for the moving pointers `j`/`k` after a match — ensures no duplicate triplet is ever added.

The only difference here is stylistic: using a `for` loop with an early `continue` for duplicate-skipping is a more idiomatic and slightly more readable way to express "skip this iteration" compared to manually incrementing `i` inside a `while` loop with a separate duplicate-check afterward.

---

## Complexity Analysis

- **Time Complexity:** `O(n^2)` — sorting takes `O(n log n)`, and for each of the `n` values of `i`, the two-pointer scan takes `O(n)`
- **Space Complexity:** `O(1)` extra space (excluding the output and the sort's internal space usage)

---

## Key Insight

This version demonstrates that the same two-pointer 3Sum algorithm can be expressed with either a `while` loop (manually advancing and checking duplicates) or a `for` loop with `continue` (letting the loop's own increment handle advancement) — both are functionally equivalent, but the `for`-loop version tends to read more clearly since duplicate-skipping and iteration are handled in a single, self-contained step at the top of the loop body.

---

## Concepts Used

- Array
- Sorting
- Two Pointers
- Duplicate Handling
- Loop Refactoring