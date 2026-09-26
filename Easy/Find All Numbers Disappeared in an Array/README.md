# LeetCode 448 - Find All Numbers Disappeared in an Array

## Problem Statement

Given an array `nums` of `n` integers where `nums[i]` is in the range `[1, n]`, return *an array of all the integers in the range* `[1, n]` *that do not appear in* `nums`.

---

## Example 1:
```
Input: nums = [4,3,2,7,8,2,3,1]
Output: [5,6]
```

## Example 2:
```
Input: nums = [1,1]
Output: [2]
```

## Constraints:
- `n == nums.length`
- `1 <= n <= 10^5`
- `1 <= nums[i] <= n`

**Follow up:** Could you do it without extra space and in `O(n)` runtime? You may assume the returned list does not count as extra space.

---

## Approach

This solution uses a clever **in-place negative-marking trick**, exploiting the fact that every value in `nums` is guaranteed to fall in the range `[1, n]` — the same range as the array's own valid indices (after a `-1` shift). Instead of using a separate boolean array or hash set to track "which numbers have been seen," it repurposes the sign bit of the array's own elements as that marker.

### Idea

- **Phase 1 — mark every number that's present:** for each element `nums[i]`, treat its **value** (via `Math.abs(nums[i]) - 1`, since a slot might already have been negated by an earlier marking) as an **index** to visit: `index = Math.abs(nums[i]) - 1`. If `nums[index]` is still positive, negate it: `nums[index] = -nums[index]`. This marks "the value `index + 1` has been seen somewhere in the array" by flipping the sign of whatever sits at that position.
  - Using `Math.abs(nums[i])` (rather than `nums[i]` directly) is essential, since `nums[i]` itself might already have been negated by a *previous* iteration's marking — taking the absolute value recovers the original value regardless.
  - The `if(nums[index] > 0)` check avoids re-negating an already-negative value back to positive, which would corrupt the marking.
- **Phase 2 — collect the unmarked positions:** walk through the array again. Any index `i` where `nums[i]` is **still positive** means the value `i + 1` was **never** marked as seen — i.e., it never appeared anywhere in the original array. Add `i + 1` to the result list.
- Return the list of all such "disappeared" numbers.

---

## Code

```java
class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> list = new ArrayList<>();
        int index = 0;
        for(int i =0 ; i<nums.length ; i++){
            index = Math.abs(nums[i]) - 1;
            if(nums[index] > 0){
                nums[index] = -nums[index];
            }
        }
        for(int i = 0;i<nums.length ;i++){
            if(nums[i] > 0){
                list.add(i+1);
            }
        }
        return list;
    }
}
```

---

## Dry Run

### Input

```text
nums = [4, 3, 2, 7, 8, 2, 3, 1]
index:   0  1  2  3  4  5  6  7
n = 8
```

### Phase 1 — Mark every number that's present

| i | nums[i] | index = \|nums[i]\| - 1 | nums[index] before | nums[index] > 0? | Action | Array after |
|---|---------|---------------------------|------------------------|----------------------|--------|-------------|
|0|4|3|7|yes|negate|`[4,3,2,-7,8,2,3,1]`|
|1|3|2|2|yes|negate|`[4,3,-2,-7,8,2,3,1]`|
|2|-2|1|3|yes|negate|`[4,-3,-2,-7,8,2,3,1]`|
|3|-7|6|3|yes|negate|`[4,-3,-2,-7,8,2,-3,1]`|
|4|8|7|1|yes|negate|`[4,-3,-2,-7,8,2,-3,-1]`|
|5|2|1|-3|no|skip|`[4,-3,-2,-7,8,2,-3,-1]`|
|6|-3|2|-2|no|skip|`[4,-3,-2,-7,8,2,-3,-1]`|
|7|-1|0|4|yes|negate|`[-4,-3,-2,-7,8,2,-3,-1]`|

State after Phase 1: `nums = [-4, -3, -2, -7, 8, 2, -3, -1]`

### Phase 2 — Collect the unmarked (still-positive) positions

| i | nums[i] | nums[i] > 0? | Action |
|---|---------|-------------------|--------|
|0|-4|no|—|
|1|-3|no|—|
|2|-2|no|—|
|3|-7|no|—|
|4|8|**yes**|`list.add(5)`|
|5|2|**yes**|`list.add(6)`|
|6|-3|no|—|
|7|-1|no|—|

**Return:** `list = [5, 6]`

Output:

```text
[5,6]
```

This matches the expected output — the values `5` and `6` never appeared anywhere in the original array `[4,3,2,7,8,2,3,1]`, and every other value from `1` to `8` did appear at least once.

---

## Why This Approach Works

Because every value in `nums` is constrained to the range `[1, n]`, subtracting `1` from any value always produces a **valid array index**. This is the entire foundation of the trick: instead of needing a separate `n`-sized structure to track "has value `v` been seen," the value `v`'s own designated slot in the *original* array (`nums[v-1]`) can be repurposed to store that information, using its sign as a single bit of extra state layered on top of the value already stored there.

The `Math.abs()` in Phase 1 is what makes repeated negation safe: once a slot has been marked (negated), a *later* iteration might land on that same value again (e.g. duplicate values like the `2`s and `3`s in the example) — taking the absolute value first ensures the correct target index is always recomputed from the true underlying value, not from a possibly-already-negated one.

After Phase 1, a position `i` remains positive **if and only if** no element in the array ever pointed to it — i.e., the value `i + 1` never occurred anywhere in `nums`. Phase 2 simply reads off exactly those positions, which is precisely the set of numbers in `[1, n]` missing from the array.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — two separate single passes over the array, each doing constant work per element
- **Space Complexity:** `O(1)` extra space — the marking is done entirely in place by manipulating the sign of the existing array's elements; the only additional memory is the output list itself, which the problem explicitly states does not count as extra space. This satisfies the follow-up's constraints exactly.

---

## Key Insight

When an array's values are constrained to exactly the same range as its own valid indices, that overlap can often be exploited to encode auxiliary "seen/unseen" information directly within the array itself — here, by using the sign bit as a spare piece of state — completely eliminating the need for a separate hash set or boolean array. This negative-marking trick is a common pattern for "find missing/duplicate numbers in `[1,n]`"-style problems when `O(1)` extra space is required.

---

## Concepts Used

- Array
- In-Place Marking (Sign Bit as a Flag)
- Index-Value Mapping
- Constant Space Algorithm