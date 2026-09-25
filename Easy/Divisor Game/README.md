# LeetCode 1025 - Divisor Game

## Problem Statement

Alice and Bob take turns playing a game, with Alice starting first.

Initially, there is a number `n` on the chalkboard. On each player's turn, that player makes a move consisting of:

- Choosing any integer `x` with `0 < x < n` and `n % x == 0`.
- Replacing the number `n` on the chalkboard with `n - x`.

Also, if a player cannot make a move, they lose the game.

Return `true` *if and only if Alice wins the game, assuming both players play optimally*.

---

## Example 1:
```
Input: n = 2
Output: true
Explanation: Alice chooses 1, and Bob has no more moves.
```

## Example 2:
```
Input: n = 3
Output: false
Explanation: Alice chooses 1, Bob chooses 1, and Alice has no more moves.
```

## Constraints:
- `1 <= n <= 1000`

---

## Approach

Like several other "optimal play" game problems, this one doesn't need to simulate any moves or run dynamic programming at all — it exploits a **parity invariant** that can be proven to always hold, given the specific rule that `x` must be a divisor of the current `n`. The solution simply returns whether `n` is even.

### Idea

- **The key parity fact:** if `n` is **odd**, then *every* valid divisor `x` of `n` must also be **odd** (an odd number can never have an even factor — if it did, `n` would itself be even, a contradiction). So whenever a player faces an odd `n`, whatever legal move they make, `n - x` (odd minus odd) is always **even**, and that's what gets handed to the opponent.
- Conversely, if `n` is **even**, the player facing it can always choose `x = 1` (since `1` divides every integer, and `0 < 1 < n` holds as long as `n > 1`), producing `n - 1`, which is **odd** — handed to the opponent.
- Combining these two facts: whoever is forced to face an **odd** number is always forced to hand back an **even** number, and whoever faces an **even** number can always choose to hand back an **odd** number. Starting from an even `n` and having Alice move first, Alice always faces even numbers and Bob always faces odd numbers, turn after turn, for as long as the game continues.
- The number strictly decreases by at least `1` every turn, so the game must eventually terminate at `n = 1` — and `1` is odd, with no valid move possible (`0 < x < 1` has no integer solutions). By the invariant above, when the starting `n` is even, it is always **Bob** who ends up facing odd numbers, so Bob is the one who eventually gets stuck at `n = 1` and loses — meaning Alice always wins.
- By the same reasoning with the roles of "faces odd" and "faces even" swapped, if the starting `n` is **odd**, it's Alice who always faces odd numbers, so Alice is the one who eventually gets stuck at `n = 1` and loses.
- So: **Alice wins if and only if `n` is even.** The function simply returns `n % 2 == 0`.

---

## Code

```java
class Solution {
    public boolean divisorGame(int n) {
        return n%2 == 0;
    }
}
```

---

## Dry Run

### Input 1

```text
n = 2
```

`n % 2 == 0` → `2 % 2 = 0` → `true`

Output:

```text
true
```

This matches the expected output. Tracing the actual game confirms it: Alice faces `n = 2` (even), so she plays `x = 1` (the only valid divisor, since `0 < x < 2`), leaving `n = 1` for Bob. Bob faces `n = 1`, with no valid `x` satisfying `0 < x < 1` — Bob has no moves and loses, so Alice wins.

### Input 2

```text
n = 3
```

`n % 2 == 0` → `3 % 2 = 1` → `false`

Output:

```text
false
```

This matches the expected output. Tracing the game: Alice faces `n = 3` (odd), whose only valid divisor is `x = 1` (since `0 < x < 3` and `3 % x == 0` only holds for `x = 1`), leaving `n = 2` for Bob. Bob faces `n = 2` (even) and plays `x = 1`, leaving `n = 1` for Alice. Alice faces `n = 1`, has no valid moves, and loses.

---

## Why This Approach Works

The proof rests entirely on the divisor constraint (`x` must divide `n`), which is what forces the strict parity alternation: a player facing an **odd** number has *no choice* but to produce an even number for their opponent (since all of an odd number's divisors are odd, and odd − odd = even), while a player facing an **even** number always has the *option* to produce an odd number (by playing `x = 1`) and, critically, it's always in their interest to do so, since handing the opponent an odd number puts the opponent in the losing pattern instead.

Because the number strictly decreases every turn and is bounded below by `1`, the game is guaranteed to terminate in a finite number of moves, and by induction, the parity of the *starting* number `n` alone determines which player is permanently locked into the "always odd" role — and that's the player who will inevitably be the one stuck with `n = 1` and no legal move.

This is a constraint-specific mathematical shortcut, much like several other classic "optimal play" LeetCode problems (e.g. Stone Game) — it only holds because of this exact rule set (divisor-only moves) and would not generalize to a differently-defined game without re-deriving the invariant from scratch.

---

## Complexity Analysis

- **Time Complexity:** `O(1)` — a single modulo operation, regardless of the value of `n`
- **Space Complexity:** `O(1)` — no additional data structures are used

---

## Key Insight

Many "does the first player win?" combinatorial game problems reduce to a simple, provable invariant (often related to parity, or to some other fixed mathematical property of the starting state) rather than requiring a full game-tree search or memoized DP over reachable states. Recognizing that the **legal-move constraint itself** (here, "must be a divisor") forces a structural pattern — every odd position is a forced loss-in-waiting, every even position has an escape hatch — is what turns what looks like an exponential search problem into an `O(1)` parity check.

---

## Concepts Used

- Game Theory
- Mathematical / Parity Invariant Proof
- Mathematical Proof-Based Shortcut (vs. Simulation or DP)
- Constant Time Algorithm