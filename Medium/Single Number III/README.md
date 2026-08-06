# LeetCode 260 – Single Number III

## Problem

Given an integer array `nums` where **exactly two elements appear only once** and **all other elements appear exactly twice**, return the two elements that appear only once.

The solution must run in **O(n)** time and use **O(1)** extra space.

## Concepts Used

### 1. XOR (Exclusive OR)

XOR has the following important properties.

| Expression                         | Result                |
| ---------------------------------- | --------------------- |
| `a ^ 0`                            | `a`                   |
| `a ^ a`                            | `0`                   |
| `a ^ b ^ a`                        | `b`                   |
| XOR is associative and commutative | Order does not matter |

Since every duplicate number appears twice,

```text
x ^ x = 0
```

all duplicate numbers cancel out.

### 2. XOR of All Elements

If the two unique numbers are `A` and `B`, then

```text
ans = A ^ B
```

because every duplicate disappears.

This XOR result is **not enough to identify A and B directly**, but it tells us that **A and B differ in at least one bit**.

### 3. Rightmost Set Bit

To separate the two unique numbers, find any bit where they differ.

The easiest choice is the **rightmost set bit**.

```java
int rightMost = (ans & (ans - 1)) ^ ans;
```

This extracts the lowest set bit.

Example:

```text
ans = 6 (0110)

ans - 1 = 5 (0101)

0110 & 0101 = 0100

0100 ^ 0110 = 0010
```

So

```text
rightMost = 2 (0010)
```

### 4. Bit Partitioning

Using the rightmost set bit, divide numbers into two groups.

* Group 1: bit is **0**
* Group 2: bit is **1**

Since `A` and `B` differ at this bit, they must fall into **different groups**.

All duplicate numbers fall into the **same group as their duplicate**, so they cancel out.

## Idea

1. XOR all numbers → `ans = A ^ B`.
2. Find the **rightmost set bit**.
3. Partition numbers into two groups using that bit.
4. XOR each group separately.
5. The two XOR results are the required unique numbers.

## Java Solution

```java
class Solution {
    public int[] singleNumber(int[] nums) {
        int ans = 0;

        for (int num : nums) {
            ans ^= num;
        }

        int rightMost = (ans & (ans - 1)) ^ ans;

        int a = 0;
        int b = 0;

        for (int num : nums) {
            if ((num & rightMost) == 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }

        return new int[]{a, b};
    }
}
```

## Dry Run

Example:

```text
nums = [1,2,1,3,2,5]
```

### Step 1: XOR all numbers

```text
ans = 1 ^ 2 ^ 1 ^ 3 ^ 2 ^ 5

= (1 ^ 1) ^ (2 ^ 2) ^ (3 ^ 5)

= 0 ^ 0 ^ 6

= 6
```

So

```text
ans = 6 (0110)
```

### Step 2: Find rightmost set bit

```text
rightMost = (6 & 5) ^ 6

= (0110 & 0101) ^ 0110

= 0100 ^ 0110

= 0010
```

So

```text
rightMost = 2
```

### Step 3: Partition numbers

Bit `0010` is checked.

| Number | Binary | `num & 2` | Group |
| ------ | ------ | --------- | ----- |
| 1      | 0001   | 0         | A     |
| 2      | 0010   | 2         | B     |
| 1      | 0001   | 0         | A     |
| 3      | 0011   | 2         | B     |
| 2      | 0010   | 2         | B     |
| 5      | 0101   | 0         | A     |

### Step 4: XOR each group

Group A:

```text
1 ^ 1 ^ 5

= 5
```

Group B:

```text
2 ^ 3 ^ 2

= 3
```

Final answer:

```text
[5,3]
```

(or `[3,5]`)

## Why the Rightmost Set Bit Works

Suppose

```text
A = 3 = 0011
B = 5 = 0101
```

Then

```text
A ^ B = 0110
```

The rightmost set bit is

```text
0010
```

Checking that bit:

```text
3 & 2 = 2

5 & 2 = 0
```

They go into different groups.

This guarantees that the two unique numbers are separated.

## Time Complexity

* First XOR pass: **O(n)**
* Second partition pass: **O(n)**

Total:

* **O(n)**

## Space Complexity

Only a few variables are used.

* **O(1)**

## Pattern Recognition

This problem belongs to:

* **Bit Manipulation**
* **XOR**
* **Bit Masking**
* **Partition by Set Bit**

## Interview Takeaway

Whenever a problem says:

* every number appears twice except **two unique numbers**
* linear time
* constant extra space

think of this pattern:

1. XOR all numbers.
2. Extract a distinguishing set bit.
3. Partition the array.
4. XOR each partition separately.

The key formula to remember is:

```java
int rightMost = (xor & (xor - 1)) ^ xor;
```

or equivalently

```java
int rightMost = xor & -xor;
```

This is one of the most important **XOR partitioning techniques** in coding interviews.