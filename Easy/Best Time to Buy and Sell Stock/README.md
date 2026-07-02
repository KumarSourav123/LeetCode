# Best Time to Buy and Sell Stock - LeetCode

## Problem

You are given an array `prices` where `prices[i]` is the price of a given stock on the `iᵗʰ` day.

You want to maximize your profit by choosing a **single day** to buy one stock and choosing a **different day in the future** to sell that stock.

Return the **maximum profit** you can achieve from this transaction. If you cannot achieve any profit, return `0`.

## Example

### Example 1

**Input**
```text
prices = [7,1,5,3,6,4]
```

**Output**
```text
5
```

**Explanation**

- Buy on day 2 (price = 1)
- Sell on day 5 (price = 6)
- Profit = 6 - 1 = 5

---

### Example 2

**Input**
```text
prices = [7,6,4,3,1]
```

**Output**
```text
0
```

**Explanation**

No profitable transaction is possible, so the maximum profit is `0`.

## Approach

- Initialize `minPrice` with the first day's price.
- Initialize `maxProfit` as `0`.
- Traverse the array once.
- If the current price is smaller than `minPrice`, update `minPrice`.
- Calculate the profit by selling on the current day:
  - `profit = currentPrice - minPrice`
- If this profit is greater than `maxProfit`, update `maxProfit`.
- Return `maxProfit` after traversing the array.

## Complexity

**Time Complexity:**  
O(n)

**Space Complexity:**  
O(1)

