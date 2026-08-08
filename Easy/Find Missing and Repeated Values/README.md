# LeetCode 2965 – Find Missing and Repeated Values

## Problem

You are given an `n × n` matrix `grid` containing numbers from `1` to `n²`.

* One number appears **twice** (repeated value).
* One number is **missing**.

Return an array:

```text
[repeated, missing]
```

## Concepts Used

### 1. HashMap (Frequency Counting)

Use a `HashMap` to count the frequency of each number.

* If a number is already present in the map, it is the **repeated number**.
* Otherwise, insert it with frequency `1`.

### 2. XOR (Exclusive OR)

XOR has the following important properties:

| Expression                         | Result                |
| ---------------------------------- | --------------------- |
| `a ^ a`                            | `0`                   |
| `a ^ 0`                            | `a`                   |
| XOR is associative and commutative | Order does not matter |

If we XOR:

* all numbers from `1` to `n²`
* all values in the grid

then every number that appears exactly once cancels out.

The remaining value becomes:

```text
missing ^ repeated
```

Since the repeated number is already known from the HashMap,

```text
missing = (missing ^ repeated) ^ repeated
```

## Idea

1. Traverse the grid.
2. Use a `HashMap` to find the repeated value.
3. XOR all numbers from `1` to `n²` with all grid values.
4. The result becomes `missing ^ repeated`.
5. XOR it with the repeated value to obtain the missing value.

## Java Solution

```java
class Solution {
    public int[] findMissingAndRepeatedValues(int[][] grid) {

        HashMap<Integer, Integer> map = new HashMap<>();
        int m = grid.length;
        int repeated = 0;
        int missing = 0;

        // Find repeated number
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if (map.containsKey(grid[i][j])) {
                    map.put(grid[i][j], map.get(grid[i][j]) + 1);
                    repeated = grid[i][j];
                } else {
                    map.put(grid[i][j], 1);
                }
            }
        }

        // XOR all numbers and grid values
        int ans = 0;
        int count = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                ans = ans ^ count ^ grid[i][j];
                count++;
            }
        }

        // Recover missing number
        missing = ans ^ repeated;

        return new int[]{repeated, missing};
    }
}
```

## Dry Run

Example:

```text
grid = [[1,3],
        [2,2]]
```

Expected:

```text
[2,4]
```

### Step 1: Find repeated number

Traverse the grid:

| Number | Map            | Repeated |
| ------ | -------------- | -------- |
| 1      | {1}            | -        |
| 3      | {1,3}          | -        |
| 2      | {1,2,3}        | -        |
| 2      | already exists | 2        |

So:

```text
repeated = 2
```

### Step 2: XOR

Numbers from `1` to `4`:

```text
1 ^ 2 ^ 3 ^ 4
```

Grid values:

```text
1 ^ 3 ^ 2 ^ 2
```

Combined XOR:

```text
(1 ^ 2 ^ 3 ^ 4) ^ (1 ^ 3 ^ 2 ^ 2)

= 4 ^ 2

= 6
```

So:

```text
ans = missing ^ repeated

6 = 4 ^ 2
```

### Step 3: Recover missing

```text
missing = ans ^ repeated

= 6 ^ 2

= 4
```

Return:

```text
[2,4]
```

## Why XOR Works

Suppose:

```text
Numbers expected: 1 2 3 4

Grid values:       1 2 2 3
```

XOR everything:

```text
1 ^ 2 ^ 3 ^ 4 ^ 1 ^ 2 ^ 2 ^ 3
```

Cancel equal numbers:

```text
4 ^ 2
```

Result:

```text
missing ^ repeated
```

Since `repeated` is already known from the HashMap, the missing number can be obtained using XOR.

## Time Complexity

Two traversals of the grid:

```text
O(n²)
```

## Space Complexity

The `HashMap` stores up to `n²` values:

```text
O(n²)
```

## Pattern Recognition

This problem belongs to:

* HashMap
* Frequency Counting
* XOR
* Missing Number
* Bit Manipulation

## Interview Takeaway

This solution combines **HashMap + XOR**.

* HashMap identifies the repeated number.
* XOR identifies the relationship between the missing and repeated numbers.

The key XOR pattern is:

```java
ans ^= expectedNumber ^ gridValue;
```

which leaves:

```text
missing ^ repeated
```

Although this solution is **time-optimal**, it uses **O(n²)** extra space.

A more optimal interview solution uses **pure XOR partitioning or mathematical equations** to achieve **O(1)** extra space.