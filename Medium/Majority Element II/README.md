# LeetCode 229 - Majority Element II (Boyer-Moore Voting Approach)

## Problem Statement

Given an integer array of size `n`, find all elements that appear more than `⌊n / 3⌋` times.

---

## Example 1:
```
Input: nums = [3,2,3]
Output: [3]
```

## Example 2:
```
Input: nums = [1]
Output: [1]
```

## Example 3:
```
Input: nums = [1,2]
Output: [1,2]
```

## Constraints:
- `1 <= nums.length <= 5 * 10^4`
- `-10^9 <= nums[i] <= 10^9`

## Follow up:
Could you solve the problem in linear time and in `O(1)` space?

---

## Approach

This solution uses the **Extended Boyer-Moore Voting Algorithm**, tracking **two candidates** simultaneously since at most two elements can appear more than `n/3` times.

### Idea

- Maintain two candidate slots (`el1`, `el2`) and two counters (`count1`, `count2`), initializing the candidates to `Integer.MIN_VALUE` as sentinel "no candidate yet" values.
- **First pass (candidate selection):** For each number:
  - If `count1` is `0` and the number isn't currently `el2`, claim it as the new `el1` and set `count1 = 1`.
  - Otherwise, if `count2` is `0` and the number isn't currently `el1`, claim it as the new `el2` and set `count2 = 1`.
  - Otherwise, if the number matches `el1`, increment `count1`.
  - Otherwise, if the number matches `el2`, increment `count2`.
  - Otherwise (the number matches neither candidate and both slots are occupied), decrement **both** `count1` and `count2` — this is the "cancellation" step, similar to the single-candidate Boyer-Moore algorithm.
- After this pass, `el1` and `el2` hold the only two possible candidates that *could* exceed the `n/3` threshold — but this doesn't guarantee they actually do.
- **Second pass (verification):** Reset `count1` and `count2` to `0`, then recount the actual number of occurrences of `el1` and `el2` in the array.
- Finally, only add `el1` and/or `el2` to the result list if their true counts exceed `nums.length / 3`.

---

## Code

```java
class Solution {
    public List<Integer> majorityElement(int[] nums) {
        ArrayList<Integer> list = new ArrayList<>();

        int count1 = 0;
        int count2 = 0;
        int el1 = Integer.MIN_VALUE;
        int el2 = Integer.MIN_VALUE;

        for (int i = 0; i < nums.length; i++) {
            if (count1 == 0 && (nums[i] != el2)) {
                count1 = 1;
                el1 = nums[i];
            } else if (count2 == 0 && (nums[i] != el1)) {
                count2 = 1;
                el2 = nums[i];
            } else if (el1 == nums[i]) {
                count1++;
            } else if (el2 == nums[i]) {
                count2++;
            } else {
                count1--;
                count2--;
            }
        }

        count1 = 0;
        count2 = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == el1) {
                count1++;
            }
            if (nums[i] == el2) {
                count2++;
            }
        }

        int limit = nums.length / 3;

        if (count1 > limit) {
            list.add(el1);
        }
        if (count2 > limit) {
            list.add(el2);
        }

        return list;
    }
}
```

---

## Dry Run

### Input

```text
nums = [3,2,3]
```

**First pass (candidate selection):** `count1 = 0`, `count2 = 0`, `el1 = MIN`, `el2 = MIN`

| i | nums[i] | Condition Matched | Action | el1 | el2 | count1 | count2 |
|---|---------|---------------------|--------|-----|-----|--------|--------|
|0|3|count1==0 && 3≠el2|el1=3, count1=1|3|MIN|1|0|
|1|2|count2==0 && 2≠el1(3)|el2=2, count2=1|3|2|1|1|
|2|3|el1==nums[i] (3==3)|count1++|3|2|2|1|

After first pass: `el1 = 3`, `el2 = 2` (candidates only — not yet verified)

**Second pass (verification):** Reset `count1 = 0`, `count2 = 0`

| i | nums[i] | nums[i]==el1(3)? | nums[i]==el2(2)? | count1 | count2 |
|---|---------|----------------------|----------------------|--------|--------|
|0|3|Yes|No|1|0|
|1|2|No|Yes|1|1|
|2|3|Yes|No|2|1|

**Final check:** `limit = nums.length / 3 = 3 / 3 = 1`

- `count1 (2) > limit (1)`? Yes → `list.add(el1 = 3)`
- `count2 (1) > limit (1)`? No → `el2` is not added

Output:

```text
[3]
```

This matches the expected output.

---

## Why This Approach Works

Since more than two elements can never simultaneously exceed the `n/3` threshold (a mathematical guarantee — three groups each exceeding `n/3` would already sum to more than `n`), tracking exactly two candidate/count pairs is always sufficient to capture every possible qualifying element.

The cancellation step (decrementing both counts when a number matches neither candidate) mirrors the classic single-candidate Boyer-Moore logic: any element that isn't part of either dominant group "cancels out" support for both current candidates equally. However, since cancellation can occasionally produce a candidate that doesn't truly meet the threshold, the second verification pass is essential to confirm the real counts before adding elements to the result.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — two linear passes through the array
- **Space Complexity:** `O(1)` — only a fixed number of variables are used, regardless of input size

---

## Key Insight

Extending the Boyer-Moore Voting Algorithm from tracking one candidate to tracking two candidates (with independent counters and cancellation) generalizes elegantly to any "more than `n/k`" threshold problem — here `k = 3` requires two candidate slots, since `k - 1` elements can potentially exceed the threshold. The mandatory verification pass afterward is what makes this approach correct, since candidate selection alone doesn't guarantee an actual majority.

---

## Concepts Used

- Array
- Boyer-Moore Voting Algorithm (Extended)
- Candidate Cancellation
- Two-Pass Verification
- Constant Space Algorithm