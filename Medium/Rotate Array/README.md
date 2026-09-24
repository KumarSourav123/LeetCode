# LeetCode 189 - Rotate Array

## Problem Statement

Given an integer array `nums`, rotate the array to the right by `k` steps, where `k` is non-negative.

---

## Example 1:
```
Input: nums = [1,2,3,4,5,6,7], k = 3
Output: [5,6,7,1,2,3,4]
Explanation:
rotate 1 steps to the right: [7,1,2,3,4,5,6]
rotate 2 steps to the right: [6,7,1,2,3,4,5]
rotate 3 steps to the right: [5,6,7,1,2,3,4]
```

## Example 2:
```
Input: nums = [-1,-100,3,99], k = 2
Output: [3,99,-1,-100]
Explanation:
rotate 1 steps to the right: [99,-1,-100,3]
rotate 2 steps to the right: [3,99,-1,-100]
```

## Constraints:
- `1 <= nums.length <= 10^5`
- `-2^31 <= nums[i] <= 2^31 - 1`
- `0 <= k <= 10^5`

**Follow up:** Try to come up with as many solutions as you can. There are at least three different ways to solve this problem. Could you do it in-place with `O(1)` extra space?

---

## Approach

This solution uses the classic **three-reversal trick** for in-place array rotation, but implemented as three manual index-swapping loops rather than calling a `reverse()` helper: reverse the first `n - k` elements, reverse the last `k` elements, then reverse the *entire* array — which together produce the rotated result without any extra array.

### Idea

- **Normalize `k`:** `k = k % nums.length` collapses away any full rotations (rotating by the full array length returns it to its original order), so only the "effective" rotation amount matters.
- **Loop 1 — reverse the first `n - k` elements** (indices `0` through `n - k - 1`): a standard in-place reversal, swapping `nums[i]` with the mirrored index on the other side of that sub-range, `nums[nums.length - 1 - k - i]`, for `i` from `0` up to the sub-range's midpoint. This is the front portion of the array — the part that will end up *after* the rotation point.
- **Loop 2 — reverse the last `k` elements** (indices `n - k` through `n - 1`): using a separate counter `j` (starting at `0`) to track the mirrored position from the *end* of the array, this swaps `nums[i]` with `nums[nums.length - 1 - j]` while `i` walks from `n - k` up to that sub-range's midpoint. This is the back portion — the part that will end up moved to the *front* after rotation.
- **Loop 3 — reverse the entire array** (indices `0` through `n - 1`): a full standard reversal, swapping `nums[i]` with `nums[nums.length - 1 - i]` for `i` up to the whole array's midpoint.
- After all three reversals, the array holds the rotated result. Since the array is modified **in place**, the method returns `void` — no value is returned.

---

## Code

```java
class Solution {
    public void rotate(int[] nums, int k) {
        k = k%nums.length;

        for(int i = 0 ; i < (nums.length - k)/2 ; i++){
            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - k - i];
            nums[nums.length - 1 - k - i] = temp;
        }
        int j = 0;
        for(int i = nums.length - k ; i < ((nums.length) + (nums.length-k))/2  ; i++){
            int temp = nums[i];
            nums[i] = nums[nums.length-1-j];
            nums[nums.length-1-j] = temp;
            j++;
        }
        for(int i = 0; i< nums.length/2 ; i++){
            int temp = nums[i];
            nums[i] = nums[nums.length-1-i];
            nums[nums.length-1-i] = temp;
        }
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,2,3,4,5,6,7]
index:   0 1 2 3 4 5 6
k = 3, n = 7
```

`k = 3 % 7 = 3` (unchanged, already less than `n`)

### Loop 1 — Reverse the first n - k = 4 elements (indices 0-3)

Loop runs `i` from `0` while `i < (7-3)/2 = 2`

| i | temp = nums[i] | Swap target: nums[n-1-k-i] | Assignment | Array after |
|---|-----------------|-------------------------------|-------------|-------------|
|0|`nums[0] = 1`|`nums[7-1-3-0] = nums[3] = 4`|`nums[0]=4, nums[3]=1`|`[4,2,3,1,5,6,7]`|
|1|`nums[1] = 2`|`nums[7-1-3-1] = nums[2] = 3`|`nums[1]=3, nums[2]=2`|`[4,3,2,1,5,6,7]`|

Loop exits (`i=2` not `< 2`). State: `[4,3,2,1,5,6,7]` — first 4 elements reversed, as expected for reversing `[1,2,3,4] -> [4,3,2,1]`.

### Loop 2 — Reverse the last k = 3 elements (indices 4-6)

`j = 0`. Loop runs `i` from `n - k = 4` while `i < ((7) + (7-3))/2 = 11/2 = 5` (integer division)

| i | j | temp = nums[i] | Swap target: nums[n-1-j] | Assignment | Array after | j after |
|---|---|-----------------|-----------------------------|-------------|-------------|---------|
|4|0|`nums[4] = 5`|`nums[7-1-0] = nums[6] = 7`|`nums[4]=7, nums[6]=5`|`[4,3,2,1,7,6,5]`|1|

Loop exits (`i=5` not `< 5`). State: `[4,3,2,1,7,6,5]` — last 3 elements reversed, as expected for reversing `[5,6,7] -> [7,6,5]`.

### Loop 3 — Reverse the entire array (indices 0-6)

Loop runs `i` from `0` while `i < 7/2 = 3` (integer division)

| i | temp = nums[i] | Swap target: nums[n-1-i] | Assignment | Array after |
|---|-----------------|-------------------------------|-------------|-------------|
|0|`nums[0] = 4`|`nums[6] = 5`|`nums[0]=5, nums[6]=4`|`[5,3,2,1,7,6,4]`|
|1|`nums[1] = 3`|`nums[5] = 6`|`nums[1]=6, nums[5]=3`|`[5,6,2,1,7,3,4]`|
|2|`nums[2] = 2`|`nums[4] = 7`|`nums[2]=7, nums[4]=2`|`[5,6,7,1,2,3,4]`|

Loop exits (`i=3` not `< 3`).

**Final array:**

```text
[5,6,7,1,2,3,4]
```

This matches the expected output — `nums` has been rotated in place, and since the method's return type is `void`, nothing is explicitly returned.

---

## Why This Approach Works

Rotating an array right by `k` places is equivalent to swapping the positions of its first `n - k` elements and its last `k` elements, while preserving the internal order *within* each of those two groups. The three-reversal trick achieves exactly that with a clever bit of algebra: reversing each of the two groups individually first (Loop 1 and Loop 2) flips their internal order, and then reversing the *whole* array (Loop 3) flips everything back — both the overall left-right positions of the two groups **and** each group's internal order a second time, which restores each group's original internal order while leaving the two groups swapped in position. The net effect of "reverse, reverse, reverse-all" is precisely a rotation, achieved using only swaps within the original array.

Writing this with explicit index-swap loops (rather than calling a `reverse(nums, start, end)` helper three times) is functionally identical — each loop is doing the same "swap symmetric pairs toward the middle" work, just with the start/end bounds baked directly into the loop's index arithmetic instead of being passed as parameters.

Reducing `k` modulo `nums.length` up front is what keeps the sub-range sizes (`n - k` and `k`) valid and meaningful regardless of how large the input `k` is, since only the remainder after full rotations actually changes the array's final order.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — each of the three loops does a constant amount of work per swap, and together they touch every element a bounded number of times, so the total work is linear in the array's length
- **Space Complexity:** `O(1)` — only a fixed set of index variables and one `temp` variable (reused across all three loops) are used; all swaps happen in place within the original array, satisfying the follow-up's constant-space requirement

---

## Key Insight

The three-reversal technique is a classic way to achieve an in-place rotation using only swaps, avoiding both the `O(n)` extra space of copying into a new array and the `O(n·k)` time of naively rotating one step at a time, `k` times. The underlying trick — that reversing two sub-groups individually and then reversing everything together produces a swap of those two groups while restoring their internal order — is a reusable pattern anytime an array (or, equivalently, a sub-section of it) needs to be cyclically shifted in place.

---

## Concepts Used

- Array
- In-Place Reversal
- Three-Reversal Rotation Technique
- Modulo Arithmetic to Normalize Rotation Count
- Constant Space Algorithm