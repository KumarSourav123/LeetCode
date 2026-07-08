# Divisor Game - LeetCode

## Problem

Alice and Bob take turns playing a game, with **Alice starting first**.

Initially, there is a number `n` on the chalkboard.

On each player's turn, they must:

- Choose an integer `x` such that:
  - `0 < x < n`
  - `n % x == 0`
- Replace `n` with `n - x`.

If a player cannot make a move, they lose the game.

Return **`true`** if Alice wins the game assuming both players play optimally; otherwise return **`false`**.

---

## Example

### Example 1

**Input**
```text
n = 2
```

**Output**
```text
true
```

**Explanation**

- Alice chooses `x = 1`.
- `n` becomes `1`.
- Bob has no valid move.
- Alice wins.

---

### Example 2

**Input**
```text
n = 3
```

**Output**
```text
false
```

**Explanation**

- Alice can only choose `x = 1`.
- `n` becomes `2`.
- Bob chooses `x = 1`.
- `n` becomes `1`.
- Alice has no valid move and loses.

---

## Approach

A mathematical observation solves this problem.

- If `n` is **even**, Alice can always choose an odd divisor, leaving Bob with an odd number.
- If `n` is **odd**, every divisor is also odd, so subtracting it always leaves an even number for Bob.
- This strategy guarantees that:
  - Even `n` → Alice wins.
  - Odd `n` → Alice loses.

Therefore, simply check whether `n` is even.

---

## Complexity

**Time Complexity:**  
O(1)

**Space Complexity:**  
O(1)
