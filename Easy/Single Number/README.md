# LeetCode 136 - Single Number

## Problem Statement

Given a **non-empty** array of integers `nums`, every element appears **twice** except for one. Find that single one.

You must implement a solution with a **linear** runtime complexity and use only **constant** extra space.

---

## Example 1:
```
Input: nums = [2,2,1]
Output: 1
```

## Example 2:
```
Input: nums = [4,1,2,1,2]
Output: 4
```

## Example 3:
```
Input: nums = [1]
Output: 1
```

## Constraints:
- `1 <= nums.length <= 3 * 10^4`
- `-3 * 10^4 <= nums[i] <= 3 * 10^4`
- Each element in the array appears twice except for one element which appears only once.

---

## Approach

This solution uses the classic **bitwise XOR trick**: XOR-ing every element of the array together in a single pass, since XOR-ing any value with itself cancels it out to `0`, and XOR-ing anything with `0` leaves it unchanged.

### Idea

- Initialize `answer = 0`.
- **Loop** `for(int i = 0; i < nums.length; i++)`: `answer = answer ^ nums[i]` — XOR the running result with each element in turn.
- Because XOR is both commutative and associative (the order of operations doesn't matter), every value that appears **exactly twice** in the array ends up XOR-ed with itself at some point during this accumulation, canceling to `0` and contributing nothing to the final result — regardless of where in the array its two occurrences happen to sit.
- The one value that appears only **once** has no partner to cancel it out, so it survives the entire accumulation and is exactly what's left in `answer` once the loop finishes.
- Return `answer`.

---

## Code

```java
class Solution {
    public int singleNumber(int[] nums) {
        int answer = 0;
        for(int i = 0; i<nums.length ;i++){
            answer = answer ^ nums[i];
        }
        return answer;
    }
}
```

---

## Dry Run

### Input

```text
nums = [4, 1, 2, 1, 2]
index:  0  1  2  3  4
```

`answer = 0`

| i | nums[i] | answer = answer ^ nums[i] | Binary check |
|---|---------|---------------------------------|-------------------|
|0|4|`0 ^ 4 = 4`|`000 ^ 100 = 100`|
|1|1|`4 ^ 1 = 5`|`100 ^ 001 = 101`|
|2|2|`5 ^ 2 = 7`|`101 ^ 010 = 111`|
|3|1|`7 ^ 1 = 6`|`111 ^ 001 = 110`|
|4|2|`6 ^ 2 = 4`|`110 ^ 010 = 100`|

**Loop check:** `i(5) < nums.length(5)`? false → loop exits.

**Return:** `answer = 4`

Output:

```text
4
```

This matches the expected output. Notice how the two `1`s (at indices 1 and 3) and the two `2`s (at indices 2 and 4) each effectively cancel out over the course of the accumulation — the final result is exactly `4`, the one value with no duplicate.

---

## Why This Approach Works

XOR has three properties that combine to make this work: `x ^ x = 0` (a value XOR-ed with itself cancels out), `x ^ 0 = x` (XOR-ing with zero is a no-op), and XOR is both **commutative** (`a ^ b = b ^ a`) and **associative** (`(a ^ b) ^ c = a ^ (b ^ c)`), meaning the order elements are XOR-ed together doesn't affect the final result.

Because of commutativity and associativity, XOR-ing the entire array together is equivalent to XOR-ing all occurrences of each distinct value together first, then XOR-ing those per-value results together. Every value that appears exactly twice contributes `x ^ x = 0` to the total — vanishing entirely — while the single unpaired value contributes itself (`x ^ 0 = x` from the running total up to that point). What's left after every value has been accounted for is exactly the one element without a partner.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array, with one constant-time XOR operation per element
- **Space Complexity:** `O(1)` — only a single integer variable is used, no auxiliary arrays, hash sets, or other data structures — satisfying the problem's constant-space requirement exactly

---

## Key Insight

The XOR trick is a go-to technique whenever a problem's constraints guarantee that every value appears an even number of times except for one (or, in related variants, except for a small fixed number of outliers) — it collapses what might otherwise seem to require a hash set or sorting-based approach (both at least `O(n)` space, or `O(n log n)` time) into a single linear pass using only one extra integer, by exploiting XOR's self-canceling property rather than explicitly tracking counts.

---

## Concepts Used

- Array
- Bit Manipulation (XOR)
- Single-Pass Traversal
- Constant Space Algorithm