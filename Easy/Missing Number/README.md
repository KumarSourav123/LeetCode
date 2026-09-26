# LeetCode 268 - Missing Number

## Problem Statement

Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return *the only number in the range that is missing from the array*.

---

## Example 1:
```
Input: nums = [3,0,1]
Output: 2
Explanation: n = 3 since there are 3 numbers, so all numbers are in the range [0,3]. 2 is the missing number in the range since it does not appear in nums.
```

## Example 2:
```
Input: nums = [0,1]
Output: 2
Explanation: n = 2 since there are 2 numbers, so all numbers are in the range [0,2]. 2 is the missing number in the range since it does not appear in nums.
```

## Example 3:
```
Input: nums = [9,6,4,2,3,5,7,0,1]
Output: 8
Explanation: n = 9 since there are 9 numbers, so all numbers are in the range [0,9]. 8 is the missing number in the range since it does not appear in nums.
```

## Constraints:
- `n == nums.length`
- `1 <= n <= 10^4`
- `0 <= nums[i] <= n`
- All the numbers of `nums` are **unique**.

**Follow up:** Could you implement a solution using only `O(1)` extra space complexity and `O(n)` runtime complexity?

---

## Approach

This solution uses a **sum-difference trick**: compute what the sum of *every* number from `0` to `n` *should* be, compute the *actual* sum of the numbers present in `nums`, and the difference between the two is exactly the single missing number.

### Idea

- Initialize `sum = 0` (will accumulate the actual sum of the array's elements) and `actualSum = nums.length` — cleverly seeded with `n` itself (i.e. `nums.length`) rather than `0`, as a head start on building the *expected* total.
- **Single loop**, `for(int i = 0; i < nums.length; i++)`:
  - `sum = sum + nums[i]` — accumulate the actual sum of the array's elements.
  - `actualSum = actualSum + i` — accumulate the sum of every index `0` through `n-1`. Combined with the initial seed of `n`, by the end of the loop `actualSum` ends up holding `n + (0 + 1 + 2 + ... + (n-1))`, which is algebraically identical to the sum of every integer from `0` to `n` inclusive (`0 + 1 + ... + n`) — just computed in a slightly rearranged order.
- **Compute the missing number:** `missingNumber = actualSum - sum` — the expected total minus the actual total leaves exactly the one value that's absent from `nums` (since every other value from `0` to `n` that *is* present contributes identically to both sums and cancels out in the subtraction).
- Return `missingNumber`.

---

## Code

```java
class Solution {
    public int missingNumber(int[] nums) {
        int sum = 0;
        int actualSum = nums.length;

        for(int i =0 ; i< nums.length ; i++){
            sum = sum + nums[i];
            actualSum = actualSum + i;
        }

        int missingNumber = actualSum - sum;
        return missingNumber;
    }
}
```

---

## Dry Run

### Input

```text
nums = [3, 0, 1]
index:  0  1  2
n = nums.length = 3
```

`sum = 0`, `actualSum = 3` (seeded with `n`)

| i | nums[i] | sum = sum + nums[i] | actualSum = actualSum + i |
|---|---------|--------------------------|---------------------------------|
|0|3|0 + 3 = 3|3 + 0 = 3|
|1|0|3 + 0 = 3|3 + 1 = 4|
|2|1|3 + 1 = 4|4 + 2 = 6|

**Loop check:** `i(3) < nums.length(3)`? false → loop exits.

State: `sum = 4`, `actualSum = 6`

**Compute the missing number:** `missingNumber = actualSum - sum = 6 - 4 = 2`

**Return:** `2`

Output:

```text
2
```

This matches the expected output. Sanity check: the expected sum of every integer from `0` to `3` is `0+1+2+3 = 6` (matching the computed `actualSum`), and the actual sum of `nums = [3,0,1]` is `3+0+1 = 4` (matching `sum`). The difference, `6 - 4 = 2`, is indeed the value missing from `nums`.

---

## Why This Approach Works

If every integer from `0` to `n` were present exactly once, the array's sum would equal the well-known closed-form total `n(n+1)/2` (equivalently, `0+1+2+...+n`). Since exactly one number in that range is missing (and all `n` present numbers are distinct), the *actual* sum of the array falls short of that expected total by precisely the value of the missing number — so subtracting the actual sum from the expected sum isolates it directly, without needing to sort, hash, or explicitly search for which value is absent.

The way `actualSum` is built here — seeded with `n` and then accumulating `0` through `n-1` via the loop index `i` — is just a rearranged way of computing `0 + 1 + ... + n`: since the loop already iterates `i` from `0` to `n-1` for the purpose of indexing into `nums`, reusing that same `i` to simultaneously build the expected sum avoids needing a second, separate loop (or the closed-form formula `n*(n+1)/2`) to compute it.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array, with constant work done per element
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures, satisfying the follow-up's constraints exactly

---

## Key Insight

Whenever a problem guarantees a complete, distinct range of values except for exactly one missing (or one extra) entry, comparing the *expected* aggregate (sum, XOR, product, etc.) against the *actual* aggregate of what's present is a powerful trick — the contribution from every correctly-present value cancels out, leaving only the discrepancy caused by the missing (or extra) value. This sum-based version is one instance of that broader pattern; an XOR-based version of the same idea is equally common and avoids any risk of integer overflow on very large inputs.

---

## Concepts Used

- Array
- Sum / Arithmetic Series
- Single-Pass Traversal
- Constant Space Algorithm