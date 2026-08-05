# LeetCode 137 – Single Number II

## Problem

Given an integer array `nums` where every element appears **three times except for one**, which appears **exactly once**, return the single element.

The solution must run in **O(n)** time and use **O(1)** extra space.

## Concepts Used

### 1. Bit Manipulation

Instead of storing the frequency of every number, we track the frequency of **each bit**.

Since every number appears **three times**, each bit count is considered **modulo 3**.

For every bit position:

* 1st occurrence → store in `ones`
* 2nd occurrence → move to `twos`
* 3rd occurrence → remove from both

### 2. XOR (`^`)

XOR toggles a bit.

| A | B | A ^ B |
| - | - | ----- |
| 0 | 0 | 0     |
| 0 | 1 | 1     |
| 1 | 0 | 1     |
| 1 | 1 | 0     |

`ones ^ num` toggles the bits of `num` in `ones`.

### 3. Bit Masking (`&` and `~`)

The complement operator `~` is used to clear bits.

```java
ones = (ones ^ num) & ~twos;
twos = (twos ^ num) & ~ones;
```

These operations ensure that:

* a bit cannot exist in both `ones` and `twos`
* once a bit appears the third time, it is removed from both.

### 4. Finite State Machine (FSM)

Each bit moves through three states.

| Number of occurrences | twos | ones |
| --------------------- | ---- | ---- |
| 0                     | 0    | 0    |
| 1                     | 0    | 1    |
| 2                     | 1    | 0    |
| 3                     | 0    | 0    |

The third occurrence resets the bit to zero.

## Idea

Use two variables:

* `ones` → bits seen exactly once
* `twos` → bits seen exactly twice

For each number:

1. Update `ones`.
2. Update `twos`.
3. Any bit appearing three times is automatically cleared.

After processing all numbers, `ones` contains the element that appeared only once.

## Java Solution

```java
class Solution {
    public int singleNumber(int[] nums) {
        int ones = 0;
        int twos = 0;

        for (int num : nums) {
            ones = (ones ^ num) & ~twos;
            twos = (twos ^ num) & ~ones;
        }

        return ones;
    }
}
```

## Dry Run

Example:

```text
nums = [2, 2, 3, 2]
```

Binary representation:

```text
2 = 10
3 = 11
```

| Current number | ones | twos |
| -------------- | ---- | ---- |
| Start          | 00   | 00   |
| 2 (10)         | 10   | 00   |
| 2 (10)         | 00   | 10   |
| 3 (11)         | 01   | 00   |
| 2 (10)         | 11   | 00   |

Final value:

```text
ones = 11 (binary) = 3
```

Return **3**.

## Why This Works

Every bit appearing three times cycles through:

```text
00 → 01 → 10 → 00
```

So all bits belonging to numbers appearing three times disappear.

Only the bits of the unique number remain in `ones`.

## Time Complexity

* **O(n)**

Each element is processed once.

## Space Complexity

* **O(1)**

Only two integer variables are used.

## Pattern Recognition

This problem belongs to:

* **Bit Manipulation**
* **Bit Masking**
* **Finite State Machine (FSM)**
* **Modulo-based bit counting**

## Interview Takeaway

When a problem states:

* every element appears **k times except one**
* **constant extra space**
* **linear time**

think about **counting bits modulo k**.

For `k = 3`, the optimized solution uses the **`ones` and `twos` state-machine pattern**, which is one of the most important advanced bit-manipulation techniques for coding interviews.