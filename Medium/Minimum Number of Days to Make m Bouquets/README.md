# LeetCode 1482 - Minimum Number of Days to Make m Bouquets

## Problem Statement

You are given an integer array `bloomDay`, an integer `m`, and an integer `k`.

You want to make `m` bouquets. To make a bouquet, you need to use `k` **adjacent** flowers from the garden.

The garden consists of `n` flowers, the `i`th flower will bloom in the `bloomDay[i]` and then can be used in **exactly one** bouquet.

Return *the minimum number of days you need to wait to be able to make* `m` *bouquets from the garden*. If it is impossible to make m bouquets return `-1`.

---

## Example 1:
```
Input: bloomDay = [1,10,3,10,2], m = 3, k = 1
Output: 3
```

## Example 2:
```
Input: bloomDay = [1,10,3,10,2], m = 3, k = 2
Output: -1
```

## Example 3:
```
Input: bloomDay = [7,7,7,7,12,7,7], m = 2, k = 3
Output: 12
```

## Constraints:
- `bloomDay.length == n`
- `1 <= n <= 10^5`
- `1 <= bloomDay[i] <= 10^9`
- `1 <= m <= 10^9`
- `1 <= k <= n`

---

## Approach

This solution uses **binary search on the answer**, searching over possible "wait until day X" values to find the earliest day on which enough bouquets can be made.

### Idea

- **Impossibility check first:** if `(long) k * m > bloomDay.length`, there simply aren't enough flowers to ever form `m` bouquets of size `k`, regardless of how long we wait — return `-1` immediately. (Cast to `long` to avoid overflow when `k` and `m` are both large.)
- The search range for "day" spans from the earliest bloom day (`low`) to the latest bloom day (`high`) found in `bloomDay`, since waiting past the last flower's bloom day can never help further.
- For a candidate day `mid`, use the helper `bouquet(arr, mid, k)` to count how many complete bouquets could be formed if today were day `mid`:
  - Traverse the flowers in order, tracking a running `count` of **consecutive adjacent flowers that have already bloomed by day `mid`** (checked via `Math.ceil((double) arr[i] / mid) == 1`, meaning the flower's bloom day is `<= mid`).
  - Whenever `count` reaches `k`, a full bouquet can be formed from that adjacent run — increment `day` (which here actually represents the **bouquet count**, not a day) and reset `count` back to `0` to start looking for the next run.
  - If a flower hasn't bloomed yet, reset `count` to `0`, since bouquets require **adjacent** bloomed flowers — a gap breaks the current run.
- **Binary search decision:**
  - If the number of bouquets producible by day `mid` is `>= m`, day `mid` is a valid (sufficient) waiting time — record it in `ans`, and try to find an **earlier** valid day by searching left (`high = mid - 1`).
  - Otherwise, day `mid` isn't enough time — search right for a later day (`low = mid + 1`).
- The final `ans` holds the minimum day on which `m` bouquets can be made.

---

## Code

```java
class Solution {
    public int minDays(int[] bloomDay, int m, int k) {

        if((long) k * m > bloomDay.length){
            return -1;
        }

        int low = Integer.MAX_VALUE;
        int high = Integer.MIN_VALUE;
        int ans = 0;

        for(int bloom : bloomDay){
            low = Math.min(low, bloom);
            high = Math.max(high, bloom);
        }

        while(low <= high){
            int mid = low + (high-low)/2;
            int result = bouquet(bloomDay, mid, k);

            if(result >= m){
                ans = mid;
                high = mid-1;
            }else{
                low = mid+1;
            }
        }
        return ans;
    }

    private int bouquet(int[] arr, int mid, int k){
        int count = 0;
        int day = 0;
        for(int i = 0 ; i < arr.length ; i++){
            if(arr[i] <= mid){
                count++;
                if(count == k){
                    day++;
                    count = 0;
                }
            }else{
                count = 0;
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
bloomDay = [1,10,3,10,2], m = 3, k = 1
```

`(long) k*m = 1*3 = 3 > bloomDay.length(5)`? No — proceed. `low = 1`, `high = 10`

**Note:** since `k = 1`, each bouquet only needs a single bloomed flower — no adjacency constraint in effect here.

| low | high | mid | bouquet(bloomDay, mid, 1) trace | result | result >= m? | Action |
|-----|------|-----|-------------------------------------|--------|--------------------|--------|
|1|10|5|Bloomed by day 5: [1✓,10✗,3✓,10✗,2✓] → 3 separate single-flower bouquets|3|3 >= 3? Yes|ans = 5, high = 4|
|1|4|2|Bloomed by day 2: [1✓,10✗,3✗,10✗,2✓] → 2 bouquets|2|2 >= 3? No|low = 3|
|3|4|3|Bloomed by day 3: [1✓,10✗,3✓,10✗,2✓] → 3 bouquets|3|3 >= 3? Yes|ans = 3, high = 2|

**Loop check:** `low = 3`, `high = 2` → false → loop exits.

Output:

```text
3
```

This matches the expected output.

---

## Why This Approach Works

As the number of waiting days increases, the number of flowers that have bloomed (and therefore the number of bouquets that can be formed) **monotonically increases** (or stays the same) — waiting longer never causes fewer flowers to have bloomed. This monotonic relationship is exactly what makes binary search over the range of possible days valid.

The `bouquet` helper correctly enforces the **adjacency** requirement by resetting `count` to `0` whenever a flower hasn't bloomed yet, ensuring that only runs of *consecutive* bloomed flowers count toward forming a bouquet — this matches the problem's requirement that bouquets use `k` adjacent flowers, not just any `k` bloomed flowers.

---

## Complexity Analysis

- **Time Complexity:** `O(n log(max(bloomDay)))` — for each of the `O(log(max))` binary search steps, the `bouquet` helper scans all `n` flowers
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

This is another example of the **"binary search on the answer"** pattern combined with a **sliding adjacency count**: instead of checking every possible day one by one, binary searching over the day range — using a linear feasibility scan that respects the adjacency constraint — quickly converges on the minimum day where enough bouquets become possible.

---

## Concepts Used

- Array
- Binary Search on the Answer
- Monotonic Feasibility Function
- Sliding Window / Adjacency Counting
- Overflow-Safe Arithmetic (using `long`)