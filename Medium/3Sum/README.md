# LeetCode 15 - 3Sum

## Problem Statement

Given an integer array `nums`, return all the triplets `[nums[i], nums[j], nums[k]]` such that `i != j`, `i != k`, and `j != k`, and `nums[i] + nums[j] + nums[k] == 0`.

Notice that the solution set must not contain duplicate triplets.

---

## Example 1:
```
Input: nums = [-1,0,1,2,-1,-4]
Output: [[-1,-1,2],[-1,0,1]]
Explanation:
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
The distinct triplets are [-1,0,1] and [-1,-1,2].
Notice that the order of the output and the order of the triplets does not matter.
```

## Example 2:
```
Input: nums = [0,1,1]
Output: []
Explanation: The only possible triplet does not sum up to 0.
```

## Example 3:
```
Input: nums = [0,0,0]
Output: [[0,0,0]]
Explanation: The only possible triplet sums up to 0.
```

## Constraints:
- `3 <= nums.length <= 3000`
- `-10^5 <= nums[i] <= 10^5`

---

## Approach

This solution uses **sorting combined with the two-pointer technique**, fixing one element at a time and searching for the remaining pair.

### Idea

- First, **sort** `nums` in ascending order. Sorting makes it possible to use two pointers efficiently and makes skipping duplicates straightforward.
- Iterate through the array with index `i`, treating `nums[i]` as the fixed first element of the triplet.
- For each `i`, use two pointers: `j` starting right after `i`, and `k` starting at the end of the array. While `j < k`:
  - Compute `sum = nums[i] + nums[j] + nums[k]`.
  - If `sum == 0`, a valid triplet is found — add it to the result. Then advance `j` and pull back `k`, additionally skipping over any duplicate values at the new `j` and `k` positions to avoid adding the same triplet twice.
  - If `sum < 0`, the sum is too small, so move `j` forward to increase it.
  - If `sum > 0`, the sum is too large, so move `k` backward to decrease it.
- After finishing the inner two-pointer search for a given `i`, move to the next `i`, skipping over any duplicate values of `nums[i]` to avoid processing the same fixed element more than once.

---

## Code

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {

        ArrayList<List<Integer>> result = new ArrayList<>();

        Arrays.sort(nums);

        int i = 0;
        while (i < nums.length) {
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

            i++;
            while (i < nums.length && nums[i] == nums[i - 1]) {
                i++;
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

**i = 0** (`nums[0] = -4`): `j = 1`, `k = 5`

| j | k | sum | Action |
|---|---|-----|--------|
|1|5|-4+(-1)+2=-3|sum<0 → j++|
|2|5|-4+(-1)+2=-3|sum<0 → j++|
|3|5|-4+0+2=-2|sum<0 → j++|
|4|5|-4+1+2=-1|sum<0 → j++ → j=5, loop ends (j<k fails)|

No triplet found for `i=0`. Advance `i` → `i=1` (`nums[1]=-1 ≠ nums[0]=-4`, no skip needed).

**i = 1** (`nums[1] = -1`): `j = 2`, `k = 5`

| j | k | sum | Action |
|---|---|-----|--------|
|2|5|-1+(-1)+2=0|**Triplet found: [-1,-1,2]** → j++ (no dup skip), k-- (no dup skip)|
|3|4|-1+0+1=0|**Triplet found: [-1,0,1]** → j++ → j=4, k-- → k=3, loop ends (j<k fails)|

Advance `i` → `i=2` (`nums[2]=-1 == nums[1]=-1`) → skip → `i=3` (`nums[3]=0 ≠ nums[2]=-1`, stop skipping).

**i = 3** (`nums[3] = 0`): `j = 4`, `k = 5`

| j | k | sum | Action |
|---|---|-----|--------|
|4|5|0+1+2=3|sum>0 → k-- → k=4, loop ends (j<k fails)|

No triplet for `i=3`. Advance `i` → `i=4`.

**i = 4** (`nums[4] = 1`): `j = 5`, `k = 5` → `j < k` is false immediately, no iterations.

Advance `i` → `i=5`.

**i = 5** (`nums[5] = 2`): `j = 6`, `k = 5` → `j < k` is false immediately, no iterations.

Advance `i` → `i=6` → loop ends (`i < nums.length` fails).

**Final Result:**

```text
[[-1,-1,2],[-1,0,1]]
```

This exactly matches the expected output.

---

## Why This Approach Works

Sorting the array first allows the two-pointer technique to work correctly: since the array is ordered, moving `j` forward always increases the sum and moving `k` backward always decreases it, so the search space can be narrowed predictably without missing any valid combination.

Explicitly skipping over duplicate values — both for the fixed index `i` and for the moving pointers `j`/`k` after a match — is what prevents the same triplet (in value) from being added to the result more than once, satisfying the "no duplicate triplets" requirement.

---

## Complexity Analysis

- **Time Complexity:** `O(n^2)` — sorting takes `O(n log n)`, and for each of the `n` values of `i`, the two-pointer scan takes `O(n)`
- **Space Complexity:** `O(1)` extra space (excluding the output and the space used by the sort), or `O(log n)` to `O(n)` depending on the sorting algorithm's internal space usage

---

## Key Insight

Instead of checking all possible triplets with a brute-force `O(n^3)` approach, sorting the array first enables a linear-time two-pointer search for each fixed element, reducing the overall complexity to `O(n^2)` while also making duplicate elimination straightforward through simple adjacent-element comparisons.

---

## Concepts Used

- Array
- Sorting
- Two Pointers
- Duplicate Handling
- Two Sum (as a subproblem)