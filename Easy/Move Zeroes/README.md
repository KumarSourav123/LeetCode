# LeetCode 283 - Move Zeroes

## Problem Statement

Given an integer array `nums`, move all `0`'s to the end of it while maintaining the relative order of the non-zero elements.

**Note** that you must do this in-place without making a copy of the array.

---

## Example 1:
```
Input: nums = [0,1,0,3,12]
Output: [1,3,12,0,0]
```

## Example 2:
```
Input: nums = [0]
Output: [0]
```

## Constraints:
- `1 <= nums.length <= 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`

**Follow up:** Could you minimize the total number of operations done?

---

## Approach

This solution uses **two pointers**, `zero` and `number`, with `zero` always tracking the leftmost position that's a candidate to hold a zero, and `number` scanning ahead looking for a non-zero value to swap into that spot.

### Idea

- Initialize `zero = 0` and `number = 1` — `number` starts one step ahead of `zero`, since `zero` needs somewhere ahead of it to look for a non-zero candidate.
- **Loop** `while(number < nums.length && zero < nums.length)`:
  - **If `nums[zero]` is a zero and `nums[number]` is non-zero:** these are exactly the two positions that need to trade places — swap them (`nums[zero]` and `nums[number]`). This moves a non-zero value into the earliest available zero slot, and pushes a zero further back.
  - **Otherwise**, no swap is possible or needed this iteration, so the pointers need to move forward instead:
    - **If `nums[zero]` is (still) a zero:** the value at `number` must also have been zero (otherwise the swap case above would have fired), so there's nothing useful to swap yet — only `number` advances (`number++`) to keep searching for a non-zero value, while `zero` stays put waiting to receive it.
    - **Otherwise, `nums[zero]` is already non-zero:** this position is already correctly settled (holding a non-zero value in its rightful relative position), so both pointers advance together (`zero++; number++`) to move on to evaluating the next position.
- The loop continues until either pointer runs off the end of the array. Since the method modifies `nums` in place and returns `void`, nothing is explicitly returned.

---

## Code

```java
class Solution {
    public void moveZeroes(int[] nums) {
        int zero = 0;
        int number = 1;

        while(number < nums.length && zero < nums.length){
            if((nums[zero] == 0) && (nums[number] != 0)){
                int temp = nums[zero];
                nums[zero] = nums[number];
                nums[number] = temp;
            }else{
                if(nums[zero] == 0){
                    number++;
                }else{
                    zero++;
                    number++;
                }
            }
        }
    }
}
```

---

## Dry Run

### Input

```text
nums = [0, 1, 0, 3, 12]
index:  0  1  2  3   4
```

`zero = 0`, `number = 1`

| Iteration | zero | number | nums[zero] | nums[number] | Condition (zero=0 && number≠0) | Action | Array after | zero/number after |
|-----------|------|--------|-------------|-------------------|-----------------------------------|--------|-------------|-------------------------|
|1|0|1|0|1|true|swap|`[1,0,0,3,12]`|0, 1 (unchanged)|
|2|0|1|1|—|false (nums[zero]≠0)|`zero++; number++`|`[1,0,0,3,12]`|1, 2|
|3|1|2|0|0|false (nums[number]=0)|`number++`|`[1,0,0,3,12]`|1, 3|
|4|1|3|0|3|true|swap|`[1,3,0,0,12]`|1, 3 (unchanged)|
|5|1|3|3|—|false (nums[zero]≠0)|`zero++; number++`|`[1,3,0,0,12]`|2, 4|
|6|2|4|0|12|true|swap|`[1,3,12,0,0]`|2, 4 (unchanged)|
|7|2|4|12|—|false (nums[zero]≠0)|`zero++; number++`|`[1,3,12,0,0]`|3, 5|

**Loop check:** `number(5) < nums.length(5)`? false → loop exits.

**Final array:**

```text
[1,3,12,0,0]
```

This matches the expected output — all non-zero elements (`1`, `3`, `12`) retain their original relative order, with both zeros pushed to the end.

---

## Why This Approach Works

The invariant maintained throughout is that everything at index `< zero` is already correctly settled — either a non-zero value in its rightful relative position, or (implicitly) accounted for as part of the "zeros pushed to the end" region. `zero` only ever advances once the position it's sitting on holds a non-zero value, meaning that slot is finalized and the search can move on. `number` acts as the scout, always searching ahead for the next non-zero value to bring back to `zero`'s position whenever `zero` is currently sitting on a zero.

Notice that a swap does **not** immediately advance either pointer — the very next iteration re-examines the same `zero`/`number` pair. This is deliberately correct, not an oversight: right after a swap, `nums[zero]` has just become non-zero, and the pointers-haven't-moved-yet next iteration is exactly what allows the `else` branch's `nums[zero] == 0` check to now come out false, triggering the "both pointers advance" case — effectively confirming the swap landed correctly before moving on. It costs one extra loop iteration per swap compared to advancing immediately, but doesn't affect correctness.

Because every non-zero element is only ever moved forward (never past another non-zero element it hasn't already passed), and zeros are only ever displaced backward as a side effect of a non-zero being pulled forward into their slot, the relative order among non-zero elements is preserved exactly as required.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — although a swap doesn't advance the pointers on that same iteration, `number` strictly increases by at least one every single iteration (whether via a plain `number++` or the combined `zero++; number++`), so the total number of iterations is bounded by `O(n)`
- **Space Complexity:** `O(1)` — only a fixed set of index variables and one `temp` variable are used; the rearrangement happens entirely in place within the original array, with no copy made

---

## Key Insight

This is a variant of the classic **two-pointer partitioning** pattern (similar in spirit to the partition step of quicksort, or to the "remove element in place" family of problems): one pointer marks the boundary of the "already correct" region, while a second pointer scans ahead to find the next element that belongs there, swapping it into place when found. The specific detail here — that a swap and the subsequent pointer advance happen on separate iterations rather than combined into one — is a stylistic choice that doesn't change the algorithm's correctness or its overall `O(n)` time bound, only the exact iteration count.

---

## Concepts Used

- Array
- Two Pointers
- In-Place Partitioning
- Constant Space Algorithm