# LeetCode 53 - Maximum Subarray

## Problem Statement

Given an integer array `nums`, find the subarray with the largest sum, and return *its sum*.

---

## Example 1:
```
Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
Output: 6
Explanation: The subarray [4,-1,2,1] has the largest sum 6.
```

## Example 2:
```
Input: nums = [1]
Output: 1
Explanation: The subarray [1] has the largest sum 1.
```

## Example 3:
```
Input: nums = [5,4,-1,7,8]
Output: 23
Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.
```

## Constraints:
- `1 <= nums.length <= 10^5`
- `-10^4 <= nums[i] <= 10^4`

**Follow up:** If you have figured out the `O(n)` solution, try coding another solution using the **divide and conquer** approach, which is more subtle.

---

## Approach

This solution implements **Kadane's Algorithm**: walk through the array once, maintaining a running sum of "the best subarray ending here." Whenever that running sum dips below zero, it's discarded and restarted from zero, since a negative running sum can only ever drag down any future subarray it's attached to.

### Idea

- Initialize `index = 0`, `sum = 0` (the running sum), and `totalSum = nums[0]` — seeding the best-answer tracker with the first element up front, so the algorithm correctly handles arrays that are entirely negative (where the "best" subarray is just the least-negative single element).
- **Loop** `while(index < nums.length)`:
  - `sum = sum + nums[index]` — extend the running sum by including the current element.
  - **Update the best answer if this is a new high:** `if(sum > totalSum) totalSum = sum;` — checked *before* any potential reset, so a running sum that peaks and then would go negative still gets its peak value recorded first.
  - **Reset if the running sum has gone negative:** `if(sum < 0) sum = 0;` — a negative running sum can never help any subarray that continues from here (adding a negative number only ever decreases what follows), so it's abandoned and restarted fresh from the next element.
  - `index++` — advance to the next element.
- Return `totalSum`, the largest sum seen across every position the running sum reached.

---

## Code

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int index = 0;
        int sum = 0;
        int totalSum = nums[0];

        while(index < nums.length){

            sum = sum + nums[index];

            if(sum > totalSum){
                totalSum = sum;
            }

            if(sum < 0 ){
                sum = 0;
            }

            index++;
        }

        return totalSum;
    }
}
```

---

## Dry Run

### Input

```text
nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
index:   0  1   2  3   4  5  6   7  8
```

`index = 0`, `sum = 0`, `totalSum = nums[0] = -2`

| index | nums[index] | sum = sum + nums[index] | sum > totalSum? | New totalSum | sum < 0? | sum after reset |
|-------|-------------|---------------------------|-------------------|-----------------|-----------|-------------------|
|0|-2|0 + (-2) = -2|-2 > -2? no|-2|-2 < 0? yes|0|
|1|1|0 + 1 = 1|1 > -2? yes|**1**|1 < 0? no|1|
|2|-3|1 + (-3) = -2|-2 > 1? no|1|-2 < 0? yes|0|
|3|4|0 + 4 = 4|4 > 1? yes|**4**|4 < 0? no|4|
|4|-1|4 + (-1) = 3|3 > 4? no|4|3 < 0? no|3|
|5|2|3 + 2 = 5|5 > 4? yes|**5**|5 < 0? no|5|
|6|1|5 + 1 = 6|6 > 5? yes|**6**|6 < 0? no|6|
|7|-5|6 + (-5) = 1|1 > 6? no|6|1 < 0? no|1|
|8|4|1 + 4 = 5|5 > 6? no|6|5 < 0? no|5|

**Loop check:** `index(9) < nums.length(9)`? false → loop exits.

**Return:** `totalSum = 6`

Output:

```text
6
```

This matches the expected output — reached while the running sum walked through the subarray `[4,-1,2,1]` (indices 3 through 6), which sums to `4 + (-1) + 2 + 1 = 6`.

---

## Why This Approach Works

The core insight is that a running sum which has dropped below zero can never be worth keeping as a *prefix* of a future subarray — appending any number `x` to a negative running sum always produces something smaller than `x` alone would be. So the moment `sum` goes negative, the best strategy is to abandon it entirely and start fresh from the next element, since no subarray that spans across a negative-sum stretch could ever beat simply starting after it.

Because `totalSum` is updated with `sum`'s value at **every single position** before any reset happens, every possible "best subarray ending at index `i`" gets a chance to be recorded as the overall best, even the ones that are immediately followed by a reset. This is what guarantees the algorithm finds the true global maximum, not just the maximum of whatever subarray happens to survive to the end of the array.

Seeding `totalSum = nums[0]` (rather than `0`) is what correctly handles all-negative arrays: if every element is negative, `sum` resets to `0` after every single step and never exceeds `totalSum` again, so the initial seed — capturing the single best (least negative) starting value — remains the answer, matching the requirement that a subarray must be non-empty.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array, with constant work done per element
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

Kadane's Algorithm is the canonical `O(n)` solution to the maximum subarray problem, built on a simple but powerful local decision: at each position, decide whether extending the current run is still worthwhile (running sum stays non-negative) or whether it's better to cut losses and restart from here. Making that one greedy local choice at every step, while separately tracking the best value seen so far, is enough to guarantee the globally optimal answer — no need to explicitly examine all `O(n²)` possible subarrays.

---

## Concepts Used

- Array
- Kadane's Algorithm
- Dynamic Programming (implicit, via the running-sum state)
- Greedy Reset on Negative Prefix
- Constant Space Algorithm