# LeetCode 121 - Best Time to Buy and Sell Stock

## Problem Statement

You are given an array `prices` where `prices[i]` is the price of a given stock on the `ith` day.

You want to maximize your profit by choosing a **single day** to buy one stock and choosing a **different day in the future** to sell that stock.

Return *the maximum profit you can achieve from this transaction*. If you cannot achieve any profit, return `0`.

---

## Example 1:
```
Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
```

## Example 2:
```
Input: prices = [7,6,4,3,1]
Output: 0
Explanation: In this case, no transactions are done and the max profit = 0.
```

## Constraints:
- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^4`

---

## Approach

This solution makes a **single pass** through the prices, keeping track of the lowest price seen *so far* and, at each day, checking what profit selling *today* against that lowest price would yield — updating the best profit found whenever a new high is reached.

### Idea

- Initialize `minPrice = prices[0]` (the lowest buy price seen so far, starting with day 0) and `maxProfit = 0` (no profit found yet).
- **Loop** `for(int i = 1; i < prices.length; i++)`, starting from day 1 since day 0 has already been used to seed `minPrice`:
  - **Update the running minimum:** if `prices[i] < minPrice`, a new lowest buy price has been found — update `minPrice = prices[i]`. This check happens *before* computing today's profit, so a day that's itself the new lowest price correctly can't be used to "sell" against itself for a profit greater than zero, since `profit` would simply come out as `0`.
  - **Compute today's profit if selling now:** `profit = prices[i] - minPrice` — the best possible profit obtainable by having bought at the lowest price seen up through today, and selling today.
  - **Update the best answer if this beats it:** `if(profit > maxProfit) maxProfit = profit;`.
- After the loop, `maxProfit` holds the best profit achievable across every valid buy-then-sell pairing considered. Return it.

---

## Code

```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;

        for(int i = 1 ; i<prices.length ; i++){
            if(prices[i] < minPrice){
                minPrice = prices[i];
            }

            int profit = prices[i] - minPrice;

            if(profit > maxProfit){
                maxProfit = profit;
            }
        }
        return maxProfit;
    }
}
```

---

## Dry Run

### Input

```text
prices = [7, 1, 5, 3, 6, 4]
day:       0  1  2  3  4  5
```

`minPrice = prices[0] = 7`, `maxProfit = 0`

| i | prices[i] | prices[i] < minPrice? | New minPrice | profit = prices[i] - minPrice | profit > maxProfit? | New maxProfit |
|---|-----------|--------------------------|-----------------|-----------------------------------|-------------------------|-------------------|
|1|1|1 < 7? yes|1|1 - 1 = 0|0 > 0? no|0|
|2|5|5 < 1? no|1|5 - 1 = 4|4 > 0? yes|**4**|
|3|3|3 < 1? no|1|3 - 1 = 2|2 > 4? no|4|
|4|6|6 < 1? no|1|6 - 1 = 5|5 > 4? yes|**5**|
|5|4|4 < 1? no|1|4 - 1 = 3|3 > 5? no|5|

**Loop check:** `i(6) < prices.length(6)`? false → loop exits.

**Return:** `maxProfit = 5`

Output:

```text
5
```

This matches the expected output — achieved by buying at the lowest price (`1`, day 1) and selling at `6` (day 4).

---

## Why This Approach Works

For any fixed selling day `i`, the best possible profit is always achieved by having bought at the *lowest* price among all the days **before** `i` — buying at any higher earlier price could only ever produce an equal or smaller profit. So rather than checking every possible `(buy day, sell day)` pair explicitly (which would be `O(n²)`), it's enough to track the running minimum price seen so far as a single pass proceeds, and at each day ask "what if I sold today, having bought at the best price available up to now?"

Because `minPrice` is only ever updated to something *lower*, and every day's potential profit against that running minimum gets compared against the best answer found so far, this single pass is guaranteed to consider the true optimal `(buy, sell)` pair at some point during the traversal — specifically, at the iteration where `i` is the actual best selling day, `minPrice` will already correctly hold the actual best buying price (since it always precedes the selling day in the array, and the running minimum by definition captures the lowest value up to any given point).

If prices are strictly decreasing throughout (as in Example 2), `minPrice` keeps chasing the current price down every day, so `profit` never becomes positive, and `maxProfit` correctly stays at its initial value of `0`.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array, with constant work done per element
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

This is the canonical example of replacing a brute-force "check every pair" approach with a single pass that maintains just enough running state (here, the minimum seen so far) to make the optimal decision at each step without needing to look back over prior elements explicitly. It's the same underlying idea as Kadane's Algorithm for maximum subarray sum — tracking a running "best so far" quantity that only ever needs to be compared and conditionally updated once per element.

---

## Concepts Used

- Array
- Single-Pass Traversal
- Running Minimum / Maximum Tracking
- Greedy Algorithm
- Constant Space Algorithm