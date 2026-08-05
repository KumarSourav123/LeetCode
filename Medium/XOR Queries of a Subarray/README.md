# LeetCode 1310 – XOR Queries of a Subarray

## Problem

You are given an array `arr` of positive integers and an array `queries`, where each query is of the form `[left, right]`.

For each query, calculate the **XOR of all elements from index `left` to `right`**.

Return an array where `answer[i]` is the XOR result for the `i-th` query.

## Concepts Used

### 1. XOR Properties

The XOR operator (`^`) has important properties.

| Expression         | Result                      |
| ------------------ | --------------------------- |
| `a ^ 0`            | `a`                         |
| `a ^ a`            | `0`                         |
| `a ^ b ^ a`        | `b`                         |
| XOR is associative | `(a ^ b) ^ c = a ^ (b ^ c)` |

The key property used in this problem is:

```text
a ^ a = 0
```

This allows repeated prefixes to cancel out.

### 2. Prefix XOR

A prefix XOR array stores the XOR of all elements from the beginning up to index `i`.

```text
prefix[i] = arr[0] ^ arr[1] ^ ... ^ arr[i]
```

Once the prefix array is built, every range XOR query can be answered in **O(1)** time.

### 3. Range XOR Formula

For a query `[L, R]`:

If `L = 0`

```text
XOR(L,R) = prefix[R]
```

Otherwise

```text
XOR(L,R) = prefix[R] ^ prefix[L-1]
```

This works because the common prefix cancels out.

```text
prefix[R]
= arr[0] ^ arr[1] ^ ... ^ arr[L-1] ^ arr[L] ... arr[R]

prefix[L-1]
= arr[0] ^ arr[1] ^ ... ^ arr[L-1]

XORing both:

prefix[R] ^ prefix[L-1]

= arr[L] ^ arr[L+1] ... arr[R]
```

## Idea

Instead of calculating XOR separately for every query:

1. Build a **prefix XOR array**.
2. Use the prefix XOR formula to answer each query in **constant time**.

This is much faster than traversing the subarray for every query.

## Optimized Java Solution

```java
class Solution {
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = arr.length;

        int[] prefix = new int[n];
        prefix[0] = arr[0];

        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] ^ arr[i];
        }

        int[] result = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int left = queries[i][0];
            int right = queries[i][1];

            if (left == 0)
                result[i] = prefix[right];
            else
                result[i] = prefix[right] ^ prefix[left - 1];
        }

        return result;
    }
}
```

## Dry Run

Example:

```text
arr = [1,3,4,8]
queries = [[0,1],[1,2],[0,3],[3,3]]
```

Build prefix XOR:

| Index | Value | Prefix XOR |
| ----- | ----- | ---------- |
| 0     | 1     | 1          |
| 1     | 3     | 1 ^ 3 = 2  |
| 2     | 4     | 2 ^ 4 = 6  |
| 3     | 8     | 6 ^ 8 = 14 |

So

```text
prefix = [1,2,6,14]
```

### Query 1: [0,1]

```text
Answer = prefix[1]
       = 2
```

### Query 2: [1,2]

```text
Answer = prefix[2] ^ prefix[0]
       = 6 ^ 1
       = 7
```

### Query 3: [0,3]

```text
Answer = prefix[3]
       = 14
```

### Query 4: [3,3]

```text
Answer = prefix[3] ^ prefix[2]
       = 14 ^ 6
       = 8
```

Final answer:

```text
[2,7,14,8]
```

## Why the Prefix XOR Formula Works

Suppose

```text
arr = [a,b,c,d,e]
```

Then

```text
prefix[4] = a ^ b ^ c ^ d ^ e
prefix[1] = a ^ b
```

Now

```text
prefix[4] ^ prefix[1]

= (a ^ b ^ c ^ d ^ e) ^ (a ^ b)

= c ^ d ^ e
```

because

```text
a ^ a = 0
b ^ b = 0
```

So the prefix before the left boundary is removed.

## Time Complexity

### Prefix XOR Construction

* **O(n)**

### Answering Queries

* **O(q)**

Total:

* **O(n + q)**

## Space Complexity

* Prefix array: **O(n)**
* Result array: **O(q)**

Extra space excluding the output array:

* **O(n)**

## Pattern Recognition

This problem belongs to:

* **Prefix XOR**
* **Range Query**
* **Bit Manipulation**
* **Prefix Array Technique**

## Interview Takeaway

Whenever a problem asks for:

* XOR of a subarray,
* multiple range XOR queries,
* efficient repeated XOR calculations,

think of **Prefix XOR**.

The most important formula is:

```text
XOR(L,R)

= prefix[R] ^ prefix[L-1]
```

This reduces each query from **O(length of subarray)** to **O(1)**, making it one of the most important XOR range-query patterns for coding interviews.