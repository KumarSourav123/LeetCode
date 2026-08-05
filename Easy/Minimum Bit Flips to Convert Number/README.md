# LeetCode 2220 – Minimum Bit Flips to Convert Number

## Problem

Given two integers `start` and `goal`, return the minimum number of bit flips required to convert `start` into `goal`.

A bit flip means changing a bit from `0 → 1` or `1 → 0`.

## Concepts Used

### 1. XOR (Exclusive OR)

The XOR operator compares two bits.

| A | B | A ^ B |
| - | - | ----- |
| 0 | 0 | 0     |
| 0 | 1 | 1     |
| 1 | 0 | 1     |
| 1 | 1 | 0     |

**Why XOR works here:**

* If the bits are the same, XOR gives `0`.
* If the bits are different, XOR gives `1`.

So `start ^ goal` creates a number where every `1` represents a bit that must be flipped.

### 2. Counting Set Bits

A **set bit** is a bit with value `1`.

The number of set bits in `start ^ goal` is exactly the number of bit flips required.

### 3. Brian Kernighan’s Algorithm

Instead of checking every bit individually, use:

```java
n = n & (n - 1)
```

This operation removes the **rightmost set bit** from `n`.

Example:

```text
n = 110100
n-1 = 110011
----------------
n & (n-1) = 110000
```

One set bit is removed in each iteration, so the loop runs only as many times as there are set bits.

## Idea

Use the **XOR (`^`) operator**.

* `start ^ goal` gives a number where:

  * `1` means the bits are different.
  * `0` means the bits are the same.
* Therefore, the answer is simply the **number of set bits (1s)** in `start ^ goal`.

To count set bits efficiently, use **Brian Kernighan’s algorithm**.

## Java Solution

```java
class Solution {
    public int minBitFlips(int start, int goal) {
        int ans = start ^ goal;
        int count = 0;

        while (ans != 0) {
            count++;
            ans = ans & (ans - 1);
        }

        return count;
    }
}
```

## Dry Run

Example:

```text
start = 10 (1010)
goal  = 7  (0111)
```

XOR:

```text
1010
0111
----
1101
```

`1101` has **3 set bits**, so the answer is **3**.

Iteration:

| ans (binary) | ans & (ans - 1) | count |
| ------------ | --------------- | ----- |
| 1101         | 1100            | 1     |
| 1100         | 1000            | 2     |
| 1000         | 0000            | 3     |

Return **3**.

## Time Complexity

* **O(k)**, where `k` is the number of set bits in `start ^ goal`.
* Worst case for a 32-bit integer: **O(32)**.

## Space Complexity

* **O(1)**

## Pattern Recognition

This problem is an example of:

* **Bit Manipulation**
* **XOR to compare binary numbers**
* **Counting set bits**
* **Brian Kernighan’s Algorithm**

## Interview Takeaway

Whenever a problem asks:

* minimum bit changes,
* differing bits between two numbers,
* Hamming distance,
* or number of set bits,

think of:

1. **XOR (`a ^ b`)** to find different bits.
2. **`n & (n - 1)`** to count set bits efficiently.
