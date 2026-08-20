# LeetCode 1283 - Find the Smallest Divisor Given a Threshold

## Problem Statement

Given an array of integers `nums` and an integer `threshold`, we will choose a positive integer `divisor`, divide all the array by it, and sum the division's result. Find the smallest `divisor` such that the result mentioned above is less than or equal to `threshold`.

Each result of the division is rounded to the nearest integer greater than or equal to that element. (For example: `7/3 = 3` and `10/2 = 5`.)

The test cases are generated so that there will be an answer.

---

## Example 1:
```
Input: nums = [1,2,5,9], threshold = 6
Output: 5
Explanation: We can get a sum to 17 (1+2+5+9) if the divisor is 1.
If the divisor is 4 we can get a sum of 7 (1+1+2+3) and if the divisor is 5 the sum will be 5 (1+1+1+2).
```

## Example 2:
```
Input: nums = [44,22,33,11,1], threshold = 5
Output: 44
```

## Constraints:
- `1 <= nums.length <= 5 * 10^4`
- `1 <= nums[i] <= 10^6`
- `nums.length <= threshold <= 10^6`

---

## Approach

This solution uses **binary search on the answer**, similar in spirit to "Koko Eating Bananas" — searching over possible divisor values rather than over the array itself, exploiting the fact that a larger divisor always produces a smaller (or equal) division sum.

### Idea

- The divisor can range from `1` (no reduction at all) up to `max(nums)` (large enough that every element divides down to `1`) — this defines the binary search bounds `start = 1` and `end = max`.
- For a candidate divisor `mid`, calculate the total sum using the helper `result(arr, div)`:
  - For each element, compute `Math.ceil((double) arr[i] / div)` — the rounded-up division result — and accumulate the sum.
- **Binary search decision:**
  - If the resulting `check` sum is `<= threshold`, `mid` is a **valid** divisor — record it in `ans`, and try to find an even **smaller** valid divisor by searching the left half (`end = mid - 1`).
  - Otherwise (`check > threshold`), `mid` is too small a divisor (producing too large a sum) — a **larger** divisor is needed, so search the right half (`start = mid + 1`).
- The final `ans` holds the smallest divisor that keeps the sum within `threshold`.

---

## Code

```java
class Solution {
    public int smallestDivisor(int[] nums, int threshold) {
        int max = nums[0];
        int ans = 0;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
        }

        int start = 1;
        int end = max;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            int check = result(nums, mid);

            if (check <= threshold) {
                ans = mid;
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }

        return ans;
    }

    private int result(int[] arr, int div) {
        int ans = 0;

        for (int i = 0; i < arr.length; i++) {
            ans += (int) Math.ceil((double) arr[i] / div);
        }

        return ans;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1,2,5,9], threshold = 6
```

`max = 9`, so `start = 1`, `end = 9`

| start | end | mid | result(nums, mid) calculation | check | check <= threshold? | Action |
|-------|-----|-----|------------------------------------|-------|---------------------------|--------|
|1|9|5|⌈1/5⌉+⌈2/5⌉+⌈5/5⌉+⌈9/5⌉ = 1+1+1+2|5|5 <= 6? Yes|ans = 5, end = 4|
|1|4|2|⌈1/2⌉+⌈2/2⌉+⌈5/2⌉+⌈9/2⌉ = 1+1+3+5|10|10 <= 6? No|start = 3|
|3|4|3|⌈1/3⌉+⌈2/3⌉+⌈5/3⌉+⌈9/3⌉ = 1+1+2+3|7|7 <= 6? No|start = 4|
|4|4|4|⌈1/4⌉+⌈2/4⌉+⌈5/4⌉+⌈9/4⌉ = 1+1+2+3|7|7 <= 6? No|start = 5|

**Loop check:** `start = 5`, `end = 4` → false → loop exits.

Output:

```text
5
```

This matches the expected output.

---

## Why This Approach Works

As the divisor increases, the resulting sum of ceiling-divisions **monotonically decreases** (or stays the same) — dividing by a larger number can never produce a larger rounded-up quotient. This monotonic relationship is what makes binary search applicable directly over the range of possible divisors, rather than needing to test every divisor from `1` to `max` individually.

Every time a valid divisor (`check <= threshold`) is found, it's recorded and the search continues toward smaller divisors, since the goal is the *smallest* valid divisor, not just any valid one — this mirrors the same "record and narrow toward the boundary" pattern used in other binary-search-on-the-answer problems.

---

## Complexity Analysis

- **Time Complexity:** `O(n log m)`, where `n` is the number of elements and `m = max(nums)` — for each of the `O(log m)` binary search steps, computing `result()` takes `O(n)`
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

This is another instance of the **"binary search on the answer"** pattern: whenever increasing a candidate value produces a monotonically changing outcome (here, a larger divisor always yields a smaller or equal sum), binary searching directly over the range of possible answers — using a feasibility check as the comparison — finds the optimal boundary value far more efficiently than linear search.

---

## Concepts Used

- Array
- Binary Search on the Answer
- Monotonic Feasibility Function
- Ceiling Division