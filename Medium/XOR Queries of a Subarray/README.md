# LeetCode 1310 – XOR Queries of a Subarray (Brute Force)

## Problem

You are given an array `arr` of positive integers and an array `queries`, where each query is of the form `[left, right]`.

For each query, calculate the **XOR of all elements from index `left` to `right`**.

Return an array where `answer[i]` is the XOR result for the `i-th` query.

## Concepts Used

### 1. XOR (Exclusive OR)

The XOR operator (`^`) compares two bits.

| A | B | A ^ B |
| - | - | ----- |
| 0 | 0 | 0     |
| 0 | 1 | 1     |
| 1 | 0 | 1     |
| 1 | 1 | 0     |

Important properties:

* `a ^ 0 = a`
* `a ^ a = 0`
* XOR is **commutative**
* XOR is **associative**

### 2. Accumulating XOR

To find the XOR of a range:

```text
arr[L] ^ arr[L+1] ^ ... ^ arr[R]
```

We continuously XOR each element with the current answer.

### 3. Two-Pointer Traversal

The brute-force solution uses two pointers:

* `start`
* `end`

It XORs elements from both ends of the subarray and moves inward until the pointers meet.

## Idea

For every query:

1. Initialize `ans = 0`.
2. Set:

   * `start = left`
   * `end = right`
3. XOR elements from both ends.
4. Move:

   * `start++`
   * `end--`
5. If both pointers meet, XOR the middle element.

Store the result for each query.

## Java Solution

```java
class Solution {
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = queries.length;
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            int ans = 0;
            int start = queries[i][0];
            int end = queries[i][1];

            while (start <= end) {
                if (start == end) {
                    ans = ans ^ arr[start];
                    break;
                } else {
                    ans = (arr[start] ^ arr[end]) ^ ans;
                    start++;
                    end--;
                }
            }

            result[i] = ans;
        }

        return result;
    }
}
```

## Dry Run

Example:

```text
arr = [1,3,4,8]
queries = [[1,2]]
```

Query:

```text
left = 1
right = 2
```

Initial:

```text
start = 1
end = 2
ans = 0
```

Iteration 1:

```text
ans = (arr[1] ^ arr[2]) ^ ans
    = (3 ^ 4) ^ 0
    = 7
```

Move pointers:

```text
start = 2
end = 1
```

Loop ends.

Final answer:

```text
7
```

## Another Example

```text
arr = [1,3,4,8]
query = [0,3]
```

Step 1:

```text
ans = (1 ^ 8)
    = 9
```

Step 2:

```text
ans = (3 ^ 4) ^ 9
    = 7 ^ 9
    = 14
```

Final answer:

```text
14
```

## Time Complexity

For each query:

* Traverse the subarray.
* Worst case: **O(n)**

For `q` queries:

* **O(n × q)**

## Space Complexity

* Result array: **O(q)**
* Extra variables: **O(1)**

Extra space excluding output:

* **O(1)**

## Why This Solution Can Be Slow

If:

* `n = 30,000`
* `q = 30,000`

Worst-case operations become:

```text
O(n × q)
= 30,000 × 30,000
= 9 × 10^8
```

This may exceed the time limit.

An optimized solution uses **Prefix XOR**, which answers each query in **O(1)** after **O(n)** preprocessing.

## Pattern Recognition

This problem belongs to:

* **Bit Manipulation**
* **XOR**
* **Range Query**
* **Array Traversal**

## Interview Takeaway

The brute-force approach is useful for understanding XOR operations.

However, when a problem contains **multiple range XOR queries**, think about **Prefix XOR**, because it reduces the complexity from **O(n × q)** to **O(n + q)**.