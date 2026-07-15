# 877. Stone Game

## Solution

```java
class Solution {
    public boolean stoneGame(int[] piles) {
        return true;
    }
}
```

## Time Complexity

- **O(1)**

## Space Complexity

- **O(1)**

## Why does `return true` work?

The problem guarantees:

- There are an **even number of piles**.
- Every pile contains a **positive** number of stones.
- The **total number of stones is odd**, so a tie is impossible.
- Alice always moves first.
- Both players play **optimally**.

### Key Observation

Consider the indices of the piles.

Example:

```
Index:  0  1  2  3
Piles: [5, 3, 4, 5]
```

There are two groups:

- Even indices → `5 + 4 = 9`
- Odd indices → `3 + 5 = 8`

Alice compares the sum of stones at:

- Even indices
- Odd indices

She chooses the group with the larger total.

Because there are an **even number of piles**, Alice can always force herself to take **all piles of one parity (either all even-indexed or all odd-indexed piles)**.

Since the total number of stones is **odd**, the sums of the even-indexed and odd-indexed piles cannot be equal.

Therefore, one parity always has a strictly larger total, and Alice chooses that parity.

Hence, Alice is guaranteed to collect more stones than Bob.

So the answer is always:

```java
return true;
```