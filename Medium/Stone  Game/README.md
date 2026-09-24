# LeetCode 877 - Stone Game

## Problem Statement

Alice and Bob play a game with piles of stones. There are an **even** number of piles arranged in a row, and each pile has a **positive** integer number of stones `piles[i]`.

The objective of the game is to end with the most stones. The **total** number of stones across all the piles is **odd**, so there are no ties.

Alice and Bob take turns, with **Alice starting first**. Each turn, a player takes the entire pile of stones either from the **beginning** or from the **end** of the row. This continues until there are no more piles left, at which point the person with the most stones wins.

Assuming Alice and Bob play optimally, return `true` *if Alice wins the game, or* `false` *if Bob wins*.

---

## Example 1:
```
Input: piles = [5,3,4,5]
Output: true
Explanation:
Alice starts first, and can only take the first 5 or the last 5.
Say she takes the first 5, so that the row becomes [3,4,5].
If Bob takes 3, then the board is [4,5], and Alice takes 5 to win with 10 points.
If Bob takes the last 5, then the board is [3,4], and Alice takes 4 to win with 9 points.
This demonstrated that taking the first 5 was a winning move for Alice, so we return true.
```

## Example 2:
```
Input: piles = [3,7,2,3]
Output: true
```

## Constraints:
- `2 <= piles.length <= 500`
- `piles.length` is even.
- `1 <= piles[i] <= 500`
- `sum(piles[i])` is odd.

---

## Approach

This solution doesn't simulate the game or run any search/DP at all — it exploits a mathematical guarantee that follows directly from this problem's specific constraints: **under these exact conditions (an even number of piles, all positive, with an odd total sum), Alice can always force a win with optimal play, no matter what the pile values are.** So the function simply returns `true` unconditionally.

### Idea

- Split all the piles into two groups by index parity: the **even-indexed** piles (`piles[0], piles[2], piles[4], ...`) and the **odd-indexed** piles (`piles[1], piles[3], piles[5], ...`). Since `piles.length` is guaranteed even, these two groups exactly partition the whole row, with equal counts of piles in each group.
- Because the total sum is guaranteed **odd**, the sum of the even-indexed group and the sum of the odd-indexed group **cannot be equal** — one of the two groups always has a strictly larger sum than the other.
- **Alice can always guarantee she collects every pile in whichever group has the larger sum**, regardless of what Bob does. The reasoning (sketched further below) is that removing a pile from either end of the row always flips the parity of what the *new* endpoints represent relative to the original indexing, in a way that lets Alice keep steering every one of Bob's forced choices back toward leaving her the pile she wants from her target group.
- Since Alice's target group's sum is strictly greater than the other group's sum (and together they make up the entire — odd — total), Alice guaranteeing all of her target group's stones is enough to guarantee her strictly more than half the total stones, which means she always wins.
- Because this holds for **any** valid input satisfying the problem's constraints, no case analysis on the actual pile values is needed — the function returns `true` every time.

---

## Code

```java
class Solution {
    public boolean stoneGame(int[] piles) {
        return true;
    }
}
```

---

## Dry Run

### Input

```text
piles = [5,3,4,5]
```

The method is called and immediately returns `true` — there is no loop or branching to trace, since the constraints alone guarantee the answer.

Output:

```text
true
```

This matches the expected output. It also agrees with the problem's own worked explanation: indices `0` and `2` (`5` and `4`, the even-indexed piles) sum to `9`, while indices `1` and `3` (`3` and `5`, the odd-indexed piles) sum to `8` — the even-indexed group has the strictly larger sum, and Alice's optimal strategy (demonstrated in the example: take the first `5`, then always match Bob's move to eventually claim the second `5`) secures her exactly that `9`-vs-`8` advantage, winning `10` to `9` as shown.

### Second check

```text
piles = [3,7,2,3]
```

Again, the method returns `true` immediately. Even-indexed piles (`3`, `2`) sum to `5`; odd-indexed piles (`7`, `3`) sum to `10` — here the *odd*-indexed group is larger, but the guarantee still holds: whichever group is larger, Alice can force herself to collect it, so she wins regardless of which side happens to be bigger.

Output:

```text
true
```

This matches the expected output.

---

## Why This Approach Works

The key strategic fact is: **Alice can always choose, on her very first move, to commit to collecting either "all even-indexed piles" or "all odd-indexed piles" (whichever sums higher), and she can maintain that commitment no matter how Bob plays.**

Here's the intuition for why she can always follow through: think of the original piles as colored alternately, say even indices "red" and odd indices "blue." Suppose Alice decides to go after all the red piles. At the very start, `piles[0]` is red and `piles[n-1]` is also red (since `n` is even, index `n-1` is odd... actually the two *original* end positions are index `0` (red/even) and index `n-1` (since `n` is even, `n-1` is odd, so blue). So initially, Alice can take the red end (`piles[0]`). After her move, the row is `piles[1..n-1]`, and its two new end positions are `piles[1]` (originally blue) and `piles[n-1]` (originally blue) — **both** of Bob's available choices are blue piles! So whatever Bob takes, one blue pile is removed, and the row `piles[2..n-1]` (if Bob took `piles[1]`) or `piles[1..n-2]` (if Bob took `piles[n-1]`) is left with its two ends being one red and one blue pile again — restoring the same situation Alice started in, one level smaller. This pattern repeats: Alice always has a red pile available at one end on her turn, and Bob is always forced to choose between two blue piles on his. By induction, Alice ends up with every red-indexed pile and Bob ends up with every blue-indexed pile (or vice versa, if Alice instead commits to blue from the start).

Since the total sum is odd, red and blue sums can never tie, so Alice simply commits to whichever color has the larger sum at the outset, guaranteeing herself strictly more than half of all the stones — an unconditional win. This is a well-known result specific to this problem's exact constraints (even pile count, all positive values, odd total sum); it does **not** generalize to variants without these guarantees, where actual game-tree search or dynamic programming would be required instead.

---

## Complexity Analysis

- **Time Complexity:** `O(1)` — no computation is performed on the input at all; the result is a constant determined purely by the problem's guaranteed constraints
- **Space Complexity:** `O(1)` — no additional data structures are used

---

## Key Insight

Not every problem that *looks* like it needs a full game-theoretic simulation (minimax, DP over subarrays, etc.) actually does — sometimes the specific constraints given (here: even pile count, strictly positive values, and a guaranteed-odd total) are exactly the ingredients of a known combinatorial guarantee that lets the "optimal play" outcome be determined instantly, without ever exploring the game tree. Recognizing when a problem's constraints encode such a guarantee (rather than treating every constraint as merely a bound on brute-force feasibility) can turn an apparently `O(n²)` DP problem into an `O(1)` one.

---

## Concepts Used

- Game Theory
- Combinatorial / Parity Argument
- Mathematical Proof-Based Shortcut (vs. Simulation or DP)
- Constant Time Algorithm