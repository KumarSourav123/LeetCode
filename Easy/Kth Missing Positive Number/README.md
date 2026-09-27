# LeetCode 1539 - Kth Missing Positive Number

## Problem Statement

Given an array `arr` of positive integers sorted in a **strictly increasing order**, and an integer `k`.

Return *the* `kth` *positive integer that is missing from this array*.

---

## Example 1:
```
Input: arr = [2,3,4,7,11], k = 5
Output: 9
Explanation: The missing positive integers are [1,5,6,8,9,10,12,13,...]. The 5th missing positive integer is 9.
```

## Example 2:
```
Input: arr = [1,2,3,4], k = 2
Output: 6
Explanation: The missing positive integers are [5,6,7,...]. The 2nd missing positive integer is 6.
```

## Constraints:
- `1 <= arr.length <= 1000`
- `1 <= arr[i] <= 1000`
- `1 <= k <= 1000`
- `arr[i] < arr[j]` for `1 <= i < j <= arr.length`

**Follow up:** Could you solve this problem in less than `O(n)` complexity?

---

## Approach

This solution uses **binary search on the count of missing numbers before each array position**, exploiting a key fact: since `arr` is strictly increasing, the number of positive integers missing *before* index `i` can be computed directly from `arr[i]` and `i` alone, without ever explicitly listing out the missing numbers.

### Idea

- **The key formula:** at any index `i` (0-indexed), if no numbers were missing, `arr[i]` would equal `i + 1` (the `(i+1)`th positive integer). The actual number of positive integers missing *up through* this point is therefore `arr[i] - (i + 1)`. Because `arr` is strictly increasing, this "missing count so far" is a **non-decreasing** function of `i` — it only ever grows or stays flat as `i` increases — which is exactly the monotonic structure binary search needs.
- Initialize `start = 0`, `end = arr.length - 1`, and `missingNumber = 0`.
- **Binary search loop**, `while(start <= end)`:
  - `mid = start + (end - start) / 2` — a standard overflow-safe midpoint calculation.
  - `missingNumber = arr[mid] - (mid + 1)` — the count of missing positive integers up through index `mid`.
  - **If `missingNumber < k`**, not enough numbers are missing yet by this point — the `k`th missing number must lie further to the right, so search the right half: `start = mid + 1`.
  - **Otherwise** (`missingNumber >= k`), *at least* `k` numbers are already missing by this point, meaning the `k`th missing number occurs at or before this position — search the left half: `end = mid - 1`.
- Once the loop ends, `start` has converged to the position **just past** the last index where fewer than `k` numbers were missing — in other words, `start` numbers from `1` to `start` have already been accounted for as "present or already counted as missing" in a way that means exactly `start` extra numbers need to be added on top of `k` to reach the true answer. Return `start + k`.

---

## Code

```java
class Solution {
    public int findKthPositive(int[] arr, int k) {
        int start = 0;
        int end = arr.length-1;
        int missingNumber = 0;

        while(start <= end){
            int mid = start + (end-start)/2;
            missingNumber = arr[mid] - (mid+1);

            if(missingNumber < k){
                start = mid+1;
            }else{
                end = mid-1;
            }
        }

        return start+k;
    }
}
```

---

## Dry Run

### Input

```text
arr = [2, 3, 4, 7, 11]
index: 0  1  2  3   4
k = 5
```

`start = 0`, `end = 4`

**Iteration 1:** `start(0) <= end(4)` → runs

| Step | Calculation | Result |
|------|-------------|--------|
|mid|`0 + (4-0)/2`|2|
|missingNumber|`arr[2] - (2+1) = 4 - 3`|1|
|Check|`missingNumber(1) < k(5)`|true|
|Action|`start = mid+1`|`start = 3`|

**Iteration 2:** `start(3) <= end(4)` → runs

| Step | Calculation | Result |
|------|-------------|--------|
|mid|`3 + (4-3)/2 = 3 + 0`|3|
|missingNumber|`arr[3] - (3+1) = 7 - 4`|3|
|Check|`missingNumber(3) < k(5)`|true|
|Action|`start = mid+1`|`start = 4`|

**Iteration 3:** `start(4) <= end(4)` → runs

| Step | Calculation | Result |
|------|-------------|--------|
|mid|`4 + (4-4)/2`|4|
|missingNumber|`arr[4] - (4+1) = 11 - 5`|6|
|Check|`missingNumber(6) < k(5)`|false|
|Action|`else`: `end = mid-1`|`end = 3`|

**Loop check:** `start(4) <= end(3)`? false → loop exits.

**Return:** `start + k = 4 + 5 = 9`

Output:

```text
9
```

This matches the expected output. Sanity check against the problem's own explanation: the missing positive integers are `[1,5,6,8,9,10,12,13,...]`, and the 5th one in that list is indeed `9`.

---

## Why This Approach Works

The quantity `arr[i] - (i+1)` precisely counts how many positive integers are missing from `1` up through `arr[i]`, because in a "no gaps" scenario `arr[i]` would exactly equal `i+1`; any excess above that (`arr[i] - (i+1)`) must be attributable to that many positive integers having been skipped somewhere before this point. Since `arr` is strictly increasing (each step forward increases `arr[i]` by at least `1`, i.e. by at least as much as `i` itself increases), this missing-count can never decrease as `i` grows — it's monotonically non-decreasing, which is exactly the property binary search relies on to correctly narrow down a "first position where some threshold is crossed."

The binary search converges on the boundary between "fewer than `k` numbers missing so far" and "at least `k` numbers missing so far." Once `start` settles past that boundary, everything from `1` to `arr[start-1]`'s vicinity has been accounted for, and the missing numbers from that point onward are simply consecutive integers (since no more `arr` elements constrain them within the search's relevant region) — so the `k`th missing number is exactly `start` positions past `k` itself: `start + k`. This elegantly sidesteps needing to explicitly enumerate or count through any missing numbers one at a time.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` — binary search over the array's index range, satisfying the follow-up's requirement of beating `O(n)`
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

Whenever a problem's "count of some property up to position `i`" can be computed in `O(1)` directly from properties of the sorted input (here, `arr[i] - (i+1)` for the missing-count), and that count is monotonic in `i`, binary search becomes applicable even though the original question ("what's the `k`th missing number?") doesn't look like a classic "find a target value" search at first glance. Recognizing and deriving this kind of monotonic auxiliary quantity is often the key step in turning an `O(n)` scanning solution into an `O(log n)` binary search.

---

## Concepts Used

- Array
- Binary Search
- Monotonic Function Property
- Mathematical Derivation (index-vs-value gap counting)
- Constant Space Algorithm