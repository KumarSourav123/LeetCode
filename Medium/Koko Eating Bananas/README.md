# LeetCode 875 - Koko Eating Bananas

## Problem Statement

Koko loves to eat bananas. There are `n` piles of bananas, the `i`th pile has `piles[i]` bananas. The guards have gone and will come back in `h` hours.

Koko can decide her bananas-per-hour eating speed of `k`. Each hour, she chooses some pile of bananas and eats `k` bananas from that pile. If the pile has less than `k` bananas, she eats all of them instead and will not eat any more bananas during this hour.

Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.

Return *the minimum integer* `k` *such that she can eat all the bananas within* `h` *hours*.

---

## Example 1:
```
Input: piles = [3,6,7,11], h = 8
Output: 4
```

## Example 2:
```
Input: piles = [30,11,23,4,20], h = 5
Output: 30
```

## Example 3:
```
Input: piles = [30,11,23,4,20], h = 6
Output: 23
```

## Constraints:
- `1 <= piles.length <= 10^4`
- `piles.length <= h <= 10^9`
- `1 <= piles[i] <= 10^9`

---

## Approach

This solution uses **binary search on the answer** — searching over possible eating speeds `k` (rather than over the array itself) to find the minimum speed that still finishes within `h` hours.

### Idea

- The eating speed `k` can range from `1` (slowest possible) up to `max(piles)` (fast enough to finish any single pile in one hour) — this defines the binary search bounds `start = 1` and `end = max`.
- For a given candidate speed `mid`, calculate the total number of hours needed to finish all piles using the helper `hours(arr, mid)`:
  - For each pile, the hours needed is `ceil(piles[i] / mid)`, computed without floating-point math as `(piles[i] + mid - 1) / mid` (integer division).
  - Sum this across all piles to get `totalHours`.
- **Binary search decision:**
  - If `totalHours > h`, the current speed `mid` is too slow (takes too long) — a **faster** speed is needed, so search the right half (`start = mid + 1`).
  - Otherwise (`totalHours <= h`), speed `mid` is fast enough (or more than fast enough) — this is a valid candidate, so record it in `value` and try to find an even **slower** (smaller) valid speed by searching the left half (`end = mid - 1`).
- The final `value` holds the minimum speed that still finishes within `h` hours.

---

## Code

```java
class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int max = piles[0];
        long totalHours = 0;
        int value = max;

        for (int i = 1; i < piles.length; i++) {
            if (piles[i] > max) {
                max = piles[i];
            }
        }

        int start = 1;
        int end = max;

        while (start <= end) {
            int mid = start + (end - start) / 2;
            totalHours = hours(piles, mid);

            if (totalHours > h) {
                start = mid + 1;
            } else {
                value = mid;
                end = mid - 1;
            }
        }

        return (int) value;
    }

    private long hours(int[] arr, int mid) {
        long ans = 0;
        for (int i = 0; i < arr.length; i++) {
            ans += (arr[i] + (long) mid - 1) / mid;
        }
        return ans;
    }
}
```

---

## Dry Run

### Input

```text
piles = [3,6,7,11], h = 8
```

`max = 11`, so `start = 1`, `end = 11`

| start | end | mid | hours(piles, mid) calculation | totalHours | totalHours > h? | Action |
|-------|-----|-----|-----------------------------------|------------|----------------------|--------|
|1|11|6|⌈3/6⌉+⌈6/6⌉+⌈7/6⌉+⌈11/6⌉ = 1+1+2+2|6|6 > 8? No|value = 6, end = 5|
|1|5|3|⌈3/3⌉+⌈6/3⌉+⌈7/3⌉+⌈11/3⌉ = 1+2+3+4|10|10 > 8? Yes|start = 4|
|4|5|4|⌈3/4⌉+⌈6/4⌉+⌈7/4⌉+⌈11/4⌉ = 1+2+2+3|8|8 > 8? No|value = 4, end = 3|

**Loop check:** `start = 4`, `end = 3` → false → loop exits.

Output:

```text
4
```

This matches the expected output.

---

## Why This Approach Works

As the eating speed `k` increases, the total hours needed to finish all piles **monotonically decreases** (or stays the same) — a faster speed can never require more hours. This monotonic relationship is exactly what makes binary search applicable here, even though the search is over the space of possible **answers** (speeds) rather than over a sorted array of values.

Every time a valid speed (`totalHours <= h`) is found, it's recorded as a candidate and the search continues toward smaller speeds to see if an even better (slower, more "leisurely") answer exists — since the goal is the *minimum* valid speed, not just any valid one.

---

## Complexity Analysis

- **Time Complexity:** `O(n log m)`, where `n` is the number of piles and `m = max(piles)` — for each of the `O(log m)` binary search steps, computing `hours()` takes `O(n)`
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

This is a classic **"binary search on the answer"** pattern: whenever a problem asks for the minimum (or maximum) value satisfying some monotonic feasibility condition — here, "can Koko finish within `h` hours at speed `k`?" — binary searching directly over the range of possible answers, using a feasibility check as the comparison function, is far more efficient than trying every possible speed one at a time.

---

## Concepts Used

- Array
- Binary Search on the Answer
- Monotonic Feasibility Function
- Ceiling Division