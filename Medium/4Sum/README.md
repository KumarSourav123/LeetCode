# LeetCode 18 - 4Sum

## Problem Statement

Given an array `nums` of `n` integers, return an array of all the **unique** quadruplets `[nums[a], nums[b], nums[c], nums[d]]` such that:

- `0 <= a, b, c, d < n`
- `a`, `b`, `c`, and `d` are **distinct**.
- `nums[a] + nums[b] + nums[c] + nums[d] == target`

You may return the answer in **any order**.

---

## Example 1:
```
Input: nums = [1,0,-1,0,-2,2], target = 0
Output: [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
```

## Example 2:
```
Input: nums = [2,2,2,2,2], target = 8
Output: [[2,2,2,2]]
```

## Constraints:
- `1 <= nums.length <= 200`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`

---

## Approach

This solution extends the **sort + two-pointer 3Sum** technique by fixing **two** elements (`first` and `second`) instead of one, then applying the two-pointer search on the remaining pair.

### Idea

- **Sort** `nums` first, enabling both duplicate skipping and predictable two-pointer movement.
- Use a nested loop with two fixed indices, `first` and `second`:
  - `first` iterates from `0` to `nums.length - 1`, skipping duplicate values (if `first > 0` and `nums[first] == nums[first-1]`, skip).
  - `second` iterates from `first + 1` to `nums.length - 1`, similarly skipping duplicates relative to the previous `second`.
- For each fixed `(first, second)` pair, use two pointers `i` (starting at `second + 1`) and `j` (starting at the array's end) to search for the remaining two values:
  - Compute the sum of all four values as a `long` (to avoid integer overflow, since four large values could exceed `int` range).
  - If `sum < target`, move `i` forward to increase the sum.
  - If `sum > target`, move `j` backward to decrease the sum.
  - If `sum == target`, record the quadruplet, then advance `i` and pull back `j`, skipping duplicates at their new positions to avoid repeated quadruplets.

---

## Code

```java
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);

        for (int first = 0; first < nums.length; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }

            for (int second = first + 1; second < nums.length; second++) {
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }

                int i = second + 1;
                int j = nums.length - 1;

                while (i < j) {
                    long sum = (long) nums[first] + nums[second] + nums[i] + nums[j];

                    if (sum < target) {
                        i++;
                    } else if (sum > target) {
                        j--;
                    } else {
                        ArrayList<Integer> list = new ArrayList<>();
                        list.add(nums[first]);
                        list.add(nums[second]);
                        list.add(nums[i]);
                        list.add(nums[j]);
                        result.add(list);

                        i++;
                        while (i < j && nums[i] == nums[i - 1]) {
                            i++;
                        }

                        j--;
                        while (j > i && nums[j] == nums[j + 1]) {
                            j--;
                        }
                    }
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
nums = [1,0,-1,0,-2,2], target = 0
```

**After sorting:** `nums = [-2,-1,0,0,1,2]`

**first = 0** (`nums[0] = -2`):

| second | i, j walkthrough | Quadruplets Found |
|--------|---------------------|----------------------|
|1 (`-1`)|i=2,j=5: sum=-1<0→i++; i=3,j=5: sum=-1<0→i++; i=4,j=5: sum=(-2)+(-1)+1+2=0 → **add [-2,-1,1,2]**; i++,j-- → loop ends|[-2,-1,1,2]|
|2 (`0`)|i=3,j=5: sum=(-2)+0+0+2=0 → **add [-2,0,0,2]**; i++,j-- → loop ends|[-2,0,0,2]|
|3 (`0`)|duplicate of second=2 → skipped|-|
|4 (`1`)|i=5,j=5: i<j false, no iterations|-|
|5 (`2`)|i=6,j=5: i<j false, no iterations|-|

**first = 1** (`nums[1] = -1`):

| second | i, j walkthrough | Quadruplets Found |
|--------|---------------------|----------------------|
|2 (`0`)|i=3,j=5: sum=(-1)+0+0+2=1>0→j--; i=3,j=4: sum=(-1)+0+0+1=0 → **add [-1,0,0,1]**; i++,j-- → loop ends|[-1,0,0,1]|
|3 (`0`)|duplicate of second=2 → skipped|-|
|4 (`1`)|i=5,j=5: no iterations|-|
|5 (`2`)|i=6,j=5: no iterations|-|

**first = 2** (`nums[2] = 0`):

| second | i, j walkthrough | Quadruplets Found |
|--------|---------------------|----------------------|
|3 (`0`)|i=4,j=5: sum=0+0+1+2=3>0→j-- → loop ends (i<j fails)|-|
|4 (`1`)|i=5,j=5: no iterations|-|
|5 (`2`)|i=6,j=5: no iterations|-|

**first = 3**: `nums[3] = 0 == nums[2] = 0` → skipped (duplicate).

**first = 4** (`nums[4] = 1`): only `second = 5` possible, but `i=6, j=5` gives no iterations.

**first = 5**: no valid `second` remains.

**Final Result:**

```text
[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
```

This exactly matches the expected output.

---

## Why This Approach Works

4Sum is essentially 3Sum with one additional fixed element layered on top: fixing `first` and `second` reduces the problem to finding two remaining numbers that sum to `target - nums[first] - nums[second]`, which the two-pointer technique on the sorted remainder of the array solves efficiently.

Skipping duplicates at all four levels (`first`, `second`, and the two pointers `i`/`j`) ensures that no combination of values is counted more than once, satisfying the "unique quadruplets" requirement. Using `long` for the sum calculation avoids integer overflow, since summing four values near the extremes of the `int` range (`±10^9`) can exceed what a 32-bit `int` can hold.

---

## Complexity Analysis

- **Time Complexity:** `O(n^3)` — sorting takes `O(n log n)`, and the nested `first`/`second` loops combined with the inner two-pointer scan take `O(n^2)` for each `first`, giving `O(n^3)` overall
- **Space Complexity:** `O(1)` extra space (excluding the output and the sort's internal space usage)

---

## Key Insight

Generalizing the 3Sum two-pointer pattern by adding one more fixed outer loop is a natural extension: the same core idea (sort, fix elements one at a time, use two pointers on the remainder, and skip duplicates at every level) scales cleanly from "kSum" problems with `k=3` to `k=4`, and could be extended further using recursion for arbitrary `k`.

---

## Concepts Used

- Array
- Sorting
- Two Pointers
- Duplicate Handling
- Overflow-Safe Arithmetic (using `long`)
- Generalized kSum Pattern