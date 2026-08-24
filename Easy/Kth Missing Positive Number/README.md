# LeetCode 1539 - Kth Missing Positive Number

## Problem Statement

Given an array `arr` of positive integers sorted in a **strictly increasing order**, and an integer `k`.

Return the **kth positive integer that is missing** from the array.

---

## Example 1:

```text
Input: arr = [2,3,4,7,11], k = 5

Output: 9
```

### Explanation:

The missing positive integers are:

```text
[1,5,6,8,9,10,12,13,...]
```

The `5th` missing positive integer is `9`.

---

## Example 2:

```text
Input: arr = [1,2,3,4], k = 2

Output: 6
```

### Explanation:

The missing positive integers are:

```text
[5,6,7,...]
```

The `2nd` missing positive integer is `6`.

---

## Constraints:

* `1 <= arr.length <= 1000`
* `1 <= arr[i] <= 1000`
* `1 <= k <= 1000`
* `arr[i] < arr[j]` for `1 <= i < j <= arr.length`
* The array is sorted in **strictly increasing order**.

---

## Follow Up

Can you solve this problem in less than `O(n)` complexity?

---

## Approach

This solution uses **binary search** to find the position where the `kth` missing positive number should be located.

Instead of checking every positive number one by one, we calculate how many positive integers are missing **before each index**.

### Idea

For an element at index `mid`:

```text
arr[mid]
```

there should normally be:

```text
mid + 1
```

positive numbers from `1` up to the position represented by this index.

For example:

```text
arr = [2,3,4,7,11]

Index:    0  1  2  3   4
Array:    2  3  4  7  11
Expected: 1  2  3  4   5
```

At index `3`:

```text
arr[3] = 7
```

If there were no missing numbers, the value should have been:

```text
mid + 1 = 4
```

Therefore, the number of missing positive integers before `7` is:

```text
7 - 4 = 3
```

This gives the formula used in the code:

```text
missingNumber = arr[mid] - (mid + 1)
```

### Binary Search Decision

For every `mid`, calculate:

```text
missingNumber = arr[mid] - (mid + 1)
```

Then:

* If `missingNumber < k`, there are **not enough missing numbers** before `mid`, so we need to search on the **right side**.

  ```text
  start = mid + 1
  ```

* If `missingNumber >= k`, we have found a position where at least `k` numbers are missing. However, there may be an earlier position that also satisfies this condition, so search on the **left side**.

  ```text
  end = mid - 1
  ```

The binary search therefore finds the **first index where the number of missing positive integers is at least `k`**.

Finally, the answer is:

```text
start + k
```

---

## Why `start + k` Gives the Answer

After the binary search, `start` represents the number of elements in the array that are positioned before the answer.

If there are:

```text
start
```

elements from the array before the answer, and we need the:

```text
kth
```

missing number, then the answer is:

```text
start + k
```

For example:

```text
arr = [2,3,4,7,11]
k = 5
```

After binary search:

```text
start = 4
```

Therefore:

```text
answer = start + k
        = 4 + 5
        = 9
```

---

## Code

```java
class Solution {
    public int findKthPositive(int[] arr, int k) {

        int start = 0;
        int end = arr.length-1;
        int missingNumber = 0;

        while(start <= end){

            int mid = start + (end-start)/2;

            missingNumber = arr[mid] - (mid+1);

            if(missingNumber < k){
                start = mid+1;
            }else{
                end = mid-1;
            }
        }

        return start+k;
    }
}
```

---

## Dry Run

### Input

```text
arr = [2,3,4,7,11]
k = 5
```

Initial values:

```text
start = 0
end = 4
```

### Binary Search

| start | end | mid | `arr[mid]` | `arr[mid] - (mid+1)` | Missing `< k`? | Action      |
| ----- | --- | --- | ---------- | -------------------- | -------------- | ----------- |
| 0     | 4   | 2   | 4          | `4 - 3 = 1`          | `1 < 5` → Yes  | `start = 3` |
| 3     | 4   | 3   | 7          | `7 - 4 = 3`          | `3 < 5` → Yes  | `start = 4` |
| 4     | 4   | 4   | 11         | `11 - 5 = 6`         | `6 < 5` → No   | `end = 3`   |

Now:

```text
start = 4
end = 3
```

Since:

```text
start > end
```

the binary search stops.

Finally:

```text
answer = start + k
       = 4 + 5
       = 9
```

Therefore:

```text
Output = 9
```

---

## Detailed Dry Run

Let's understand the missing-number calculation at each index.

```text
arr = [2,3,4,7,11]
```

### Index 0

```text
arr[0] = 2
```

Expected value:

```text
0 + 1 = 1
```

Missing numbers:

```text
2 - 1 = 1
```

So, `1` positive number is missing before `2`:

```text
[1]
```

Since:

```text
1 < 5
```

we need to search right.

---

### Index 2

```text
arr[2] = 4
```

Expected value:

```text
2 + 1 = 3
```

Missing numbers:

```text
4 - 3 = 1
```

So only `1` number is missing before `4`:

```text
[1]
```

Again:

```text
1 < 5
```

Search right.

---

### Index 3

```text
arr[3] = 7
```

Expected value:

```text
3 + 1 = 4
```

Missing numbers:

```text
7 - 4 = 3
```

The missing numbers before `7` are:

```text
[1,5,6]
```

There are `3` missing numbers.

Since:

```text
3 < 5
```

we still need more missing numbers, so search right.

---

### Index 4

```text
arr[4] = 11
```

Expected value:

```text
4 + 1 = 5
```

Missing numbers:

```text
11 - 5 = 6
```

The missing numbers before `11` are:

```text
[1,5,6,8,9,10]
```

There are `6` missing numbers.

Since:

```text
6 >= 5
```

the `5th` missing number must be at or before this position.

Therefore, search left.

---

## Why This Approach Works

The number of missing positive integers before each element is **monotonically increasing**.

For:

```text
arr = [2,3,4,7,11]
```

we get:

```text
Index        0   1   2   3   4
arr          2   3   4   7  11
Missing      1   1   1   3   6
```

The missing count never decreases.

This creates a monotonic pattern:

```text
Missing count:

1   1   1   3   6
            ↑
          k = 5
```

We need to find the **first position where the missing count becomes greater than or equal to `k`**.

That is exactly the type of problem where binary search can be used.

Once that position is found, the answer can be calculated directly using:

```text
start + k
```

Thus, instead of checking all positive integers, the algorithm finds the answer in **logarithmic time**.

---

## Complexity Analysis

* **Time Complexity:** `O(log n)`

  Binary search reduces the search space by half in every iteration.

* **Space Complexity:** `O(1)`

  Only a few integer variables are used, so no additional data structure is required.

---

## Key Insight

The most important observation is the formula:

```text
missingNumber = arr[mid] - (mid + 1)
```

It tells us exactly how many positive integers are missing before `arr[mid]`.

Then binary search finds the first index where:

```text
missingNumber >= k
```

After finding that position:

```text
answer = start + k
```

So the complete idea is:

```text
Calculate missing numbers
          ↓
Use binary search
          ↓
Find first index where missing >= k
          ↓
Answer = start + k
```

This converts the original `O(n)` search into an efficient **`O(log n)` binary search solution**.

---

## Concepts Used

* Array
* Binary Search
* Binary Search on Monotonic Condition
* Missing Number Calculation
* Index-Based Mathematical Formula
* Monotonic Function
* Lower Bound Concept
* `O(log n)` Optimization
* Constant Space