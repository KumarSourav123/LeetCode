# LeetCode 1011 - Capacity To Ship Packages Within D Days

## Problem Statement

A conveyor belt has packages that must be shipped from one port to another within `days` days.

The `ith` package on the conveyor belt has a weight of `weights[i]`. Each day, we load the ship with packages on the conveyor belt (in the order given by `weights`). We may not load more weight than the maximum weight capacity of the ship.

Return the least weight capacity of the ship that will result in all the packages on the conveyor belt being shipped within `days` days.

---

## Example 1:
```
Input: weights = [1,2,3,4,5,6,7,8,9,10], days = 5
Output: 15
Explanation: A ship capacity of 15 is the minimum to ship all the packages in 5 days like this:
1st day: 1, 2, 3, 4, 5
2nd day: 6, 7
3rd day: 8
4th day: 9
5th day: 10

Note that the cargo must be shipped in the order given, so using a ship of capacity 14 and splitting the packages into parts like (2, 3, 4, 5), (1, 6, 7), (8), (9), (10) is not allowed.
```

## Example 2:
```
Input: weights = [3,2,2,4,1,4], days = 3
Output: 6
Explanation: A ship capacity of 6 is the minimum to ship all the packages in 3 days like this:
1st day: 3, 2
2nd day: 2, 4
3rd day: 1, 4
```

## Example 3:
```
Input: weights = [1,2,3,1,1], days = 4
Output: 3
Explanation:
1st day: 1
2nd day: 2
3rd day: 3
4th day: 1, 1
```

## Constraints:
- `1 <= days <= weights.length <= 5 * 10^4`
- `1 <= weights[i] <= 500`

---

## Approach

This solution uses **binary search on the answer itself** — the ship's capacity. Rather than searching through the input array, it searches through the range of *possible capacities*, and for each candidate capacity checks (with a simple greedy simulation) how many days would be needed to ship everything. Since a larger capacity can never need *more* days than a smaller one, the "feasible / not feasible" outcome is monotonic across capacities, which is exactly the structure binary search needs.

### Idea

- **Determine the search range for capacity:**
  - The **minimum** possible capacity (`start`) is the **heaviest single package** (`Math.max` over all weights) — a ship that can't carry even the heaviest package alone could never ship it at all.
  - The **maximum** useful capacity (`end`) is the **sum of all weights** — a ship this large ships everything in a single day, so no larger capacity could ever be needed.
- **Binary search loop**, `while(start <= end)`:
  - `mid = start + (end - start) / 2` — the candidate capacity to test.
  - `result = range(weights, mid)` — the helper computes the **number of days needed** to ship all packages, in order, with a ship of capacity `mid`.
  - **If `result <= days`**, this capacity is *sufficient* (ships everything within the allowed days) — but a smaller capacity might also work, so try the left half: `end = mid - 1`.
  - **Otherwise** (`result > days`), this capacity is *too small* (would take too many days) — need a larger capacity: `start = mid + 1`.
- Once the search ends, `start` sits at the **smallest capacity that was ever found sufficient** (since every sufficient `mid` pushed `end` down below it, and every insufficient one pushed `start` up past it). Return `start`.
- **`range(arr, mid)` helper** — a greedy simulation of loading the ship day by day, in the order given:
  - Keep a running `count` for the current day's load, starting at `0`, and `day = 1` (the first day).
  - For each package `num`: if adding it would exceed the capacity (`count + num > mid`), the current day is full — start a **new day** (`day++`) and begin it with this package (`count = num`). Otherwise, load it onto the current day (`count += num`).
  - Return `day` — the total number of days used.

---

## Code

```java
class Solution {
    public int shipWithinDays(int[] weights, int days) {
        int start = 0;
        int end = 0;

        for(int weight : weights){
            start = Math.max(start, weight);
            end += weight;
        }

        while(start <= end){
            int mid = start + (end-start)/2;
            int result = range(weights, mid);

            if(result <= days){
                end = mid-1;
            }else{
                start = mid+1;
            }
        }
        return start;
    }

    private int range(int[] arr, int mid){
        int count = 0;
        int day = 1;
        for(int num : arr){
            if(count + num > mid){
                day++;
                count = num;
            }else{
                count += num;
            }
        }

        return day;
    }
}
```

---

## Dry Run

### Input

```text
weights = [3, 2, 2, 4, 1, 4]
days = 3
```

### Phase 1 — Determine the search range

| weight | start = max(start, weight) | end += weight |
|--------|-------------------------------|-----------------|
|3|3|3|
|2|3|5|
|2|3|7|
|4|4|11|
|1|4|12|
|4|4|16|

`start = 4` (heaviest package), `end = 16` (total weight)

### Phase 2 — Binary search over capacity

**Iteration 1:** `start(4) <= end(16)` → runs. `mid = 4 + (16-4)/2 = 10`

`range(weights, 10)` — greedy load with capacity 10:

| Package | count + num | > 10? | Action | day | count |
|---------|---------------|--------|--------|-----|-------|
|3|0+3 = 3|no|`count += 3`|1|3|
|2|3+2 = 5|no|`count += 2`|1|5|
|2|5+2 = 7|no|`count += 2`|1|7|
|4|7+4 = 11|**yes**|`day++`, `count = 4`|2|4|
|1|4+1 = 5|no|`count += 1`|2|5|
|4|5+4 = 9|no|`count += 4`|2|9|

Returns `day = 2` (groups: `[3,2,2] | [4,1,4]`). `result(2) <= days(3)` → true → `end = mid - 1 = 9`

**Iteration 2:** `start(4) <= end(9)` → runs. `mid = 4 + (9-4)/2 = 6`

`range(weights, 6)` — capacity 6:

| Package | count + num | > 6? | Action | day | count |
|---------|---------------|-------|--------|-----|-------|
|3|0+3 = 3|no|`count += 3`|1|3|
|2|3+2 = 5|no|`count += 2`|1|5|
|2|5+2 = 7|**yes**|`day++`, `count = 2`|2|2|
|4|2+4 = 6|no (`6 > 6` false)|`count += 4`|2|6|
|1|6+1 = 7|**yes**|`day++`, `count = 1`|3|1|
|4|1+4 = 5|no|`count += 4`|3|5|

Returns `day = 3` (groups: `[3,2] | [2,4] | [1,4]`). `result(3) <= days(3)` → true → `end = mid - 1 = 5`

**Iteration 3:** `start(4) <= end(5)` → runs. `mid = 4 + (5-4)/2 = 4`

`range(weights, 4)` — capacity 4:

| Package | count + num | > 4? | Action | day | count |
|---------|---------------|-------|--------|-----|-------|
|3|0+3 = 3|no|`count += 3`|1|3|
|2|3+2 = 5|**yes**|`day++`, `count = 2`|2|2|
|2|2+2 = 4|no|`count += 2`|2|4|
|4|4+4 = 8|**yes**|`day++`, `count = 4`|3|4|
|1|4+1 = 5|**yes**|`day++`, `count = 1`|4|1|
|4|1+4 = 5|**yes**|`day++`, `count = 4`|5|4|

Returns `day = 5` (groups: `[3] | [2,2] | [4] | [1] | [4]`). `result(5) <= days(3)` → false → `start = mid + 1 = 5`

**Iteration 4:** `start(5) <= end(5)` → runs. `mid = 5 + (5-5)/2 = 5`

`range(weights, 5)` — capacity 5:

| Package | count + num | > 5? | Action | day | count |
|---------|---------------|-------|--------|-----|-------|
|3|0+3 = 3|no|`count += 3`|1|3|
|2|3+2 = 5|no (`5 > 5` false)|`count += 2`|1|5|
|2|5+2 = 7|**yes**|`day++`, `count = 2`|2|2|
|4|2+4 = 6|**yes**|`day++`, `count = 4`|3|4|
|1|4+1 = 5|no|`count += 1`|3|5|
|4|5+4 = 9|**yes**|`day++`, `count = 4`|4|4|

Returns `day = 4` (groups: `[3,2] | [2] | [4,1] | [4]`). `result(4) <= days(3)` → false → `start = mid + 1 = 6`

**Loop check:** `start(6) <= end(5)`? false → loop exits.

**Return:** `start = 6`

Output:

```text
6
```

This matches the expected output — capacity `6` is the smallest that gets everything shipped within `3` days (capacity `5` needs `4` days, capacity `6` needs exactly `3`).

---

## Why This Approach Works

The critical property is **monotonicity**: if a ship of capacity `C` can ship everything within `days` days, then any capacity larger than `C` can too (extra capacity can only help or make no difference, never hurt). Symmetrically, if capacity `C` is too small, every smaller capacity is also too small. This means the set of feasible capacities forms a clean "everything from some threshold upward" range, and the problem reduces to finding that threshold — precisely what binary search over the answer space does.

Each candidate capacity is evaluated by a **greedy** simulation (`range`) that's provably optimal for a fixed capacity: since packages must be shipped in the given order and a day can be extended as long as the next package fits, always packing as much as possible into the current day before starting a new one uses the minimum possible number of days for that capacity. If even this best-case packing exceeds `days`, that capacity is definitively infeasible.

The lower bound of the search (`max(weights)`) guarantees no candidate capacity is ever smaller than the heaviest single package, which would make the greedy simulation's assumption (every package can be loaded on *some* day by itself) invalid. The upper bound (`sum(weights)`) is a safe cap, since it always ships everything in exactly one day.

---

## Complexity Analysis

- **Time Complexity:** `O(n * log(S))` — where `n` is the number of packages and `S` is the sum of all weights (the size of the capacity search range); the binary search performs `O(log S)` iterations, and each iteration runs the `O(n)` greedy `range` simulation
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

This is the canonical **"binary search on the answer"** pattern: when the question is "what's the minimum (or maximum) value of some parameter such that a condition holds," and the condition is monotonic in that parameter (feasible for all values beyond some threshold), binary searching over the parameter's *range of possible values* — with a separate `O(n)` feasibility check per candidate — is often far more efficient than any direct construction. Recognizing this shape (a monotonic feasibility check combined with an optimization target) is the key skill.

---

## Concepts Used

- Array
- Binary Search on Answer
- Greedy Simulation (Feasibility Check)
- Monotonic Predicate
- Helper Function Decomposition