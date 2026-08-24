# LeetCode 1011 - Capacity To Ship Packages Within D Days

## Problem Statement

A conveyor belt has packages that must be shipped from one port to another within `days` days.

The `i`th package on the conveyor belt has a weight of `weights[i]`. Each day, we load the ship with packages from the conveyor belt **in the order given by `weights`**.

We may not load more weight than the maximum weight capacity of the ship.

Return the **least weight capacity of the ship** that will result in all the packages being shipped within `days` days.

---

## Example 1:

```text
Input: weights = [1,2,3,4,5,6,7,8,9,10], days = 5

Output: 15

Explanation:
A ship capacity of 15 is the minimum to ship all the packages in 5 days like this:

1st day: 1, 2, 3, 4, 5
2nd day: 6, 7
3rd day: 8
4th day: 9
5th day: 10
```

Note that the cargo must be shipped in the order given, so using a ship of capacity 14 and splitting the packages into parts like `(2, 3, 4, 5), (1, 6, 7), (8), (9), (10)` is not allowed.

## Example 2:

```text
Input: weights = [3,2,2,4,1,4], days = 3

Output: 6

Explanation:
A ship capacity of 6 is the minimum to ship all the packages in 3 days like this:

1st day: 3, 2
2nd day: 2, 4
3rd day: 1, 4
```

## Example 3:

```text
Input: weights = [1,2,3,1,1], days = 4

Output: 3

Explanation:
1st day: 1, 2
2nd day: 3
3rd day: 1, 1
4th day: 1
```

## Constraints:

* `1 <= days <= weights.length <= 5 * 10^4`
* `1 <= weights[i] <= 500`

---

## Approach

This solution uses **binary search on the answer**.

Instead of trying every possible ship capacity, we search for the minimum capacity that can ship all packages within the given number of days.

### Idea

* The **minimum possible capacity** is the weight of the heaviest package because the ship must be able to carry every individual package.

  ```text
  start = maximum weight in weights
  ```

* The **maximum possible capacity** is the sum of all package weights because, with this capacity, all packages could theoretically be shipped in one day.

  ```text
  end = sum of all weights
  ```

* For every possible capacity `mid`, use the helper method `range(weights, mid)` to calculate how many days are required.

* The helper processes packages **in their original order**:

  * Keep adding package weights to the current day.
  * If adding the next package would make the total exceed `mid`, start a new day.
  * Continue until all packages are processed.

* **Binary search decision:**

  * If the required number of days is `<= days`, then the current capacity is sufficient. We try a **smaller capacity** by moving `end` to `mid - 1`.
  * If the required number of days is `> days`, the capacity is too small. We need a **larger capacity**, so move `start` to `mid + 1`.

* When the binary search finishes, `start` represents the **smallest valid ship capacity**.

### Why the Search Range Is Correct

Suppose:

```text
weights = [1,2,3,4,5]
```

The heaviest package is `5`, so a capacity smaller than `5` is impossible.

Therefore:

```text
start = 5
```

The total weight is:

```text
1 + 2 + 3 + 4 + 5 = 15
```

A capacity of `15` can ship everything in one day.

Therefore:

```text
end = 15
```

So the answer must lie between:

```text
5 <= answer <= 15
```

---

## Code

```java
class Solution {

    public int shipWithinDays(int[] weights, int days) {

        int start = 0;
        int end = 0;

        for(int weight : weights){
            start = Math.max(start, weight);
            end += weight;
        }

        while(start <= end){

            int mid = start + (end-start)/2;

            int result = range(weights, mid);

            if(result <= days){
                end = mid-1;
            }else{
                start = mid+1;
            }
        }

        return start;
    }

    private int range(int[] arr, int mid){

        int count = 0;
        int day = 1;

        for(int num : arr){

            if(count + num > mid){
                day++;
                count = num;
            }else{
                count += num;
            }
        }

        return day;
    }
}
```

---

## Dry Run

### Input

```text
weights = [1,2,3,4,5,6,7,8,9,10]
days = 5
```

First find the binary search range:

```text
start = maximum weight = 10
end = sum of all weights = 55
```

So:

```text
10 <= answer <= 55
```

### Binary Search

| start | end | mid | Days Required | `result <= days`? | Action       |
| ----- | --- | --- | ------------- | ----------------- | ------------ |
| 10    | 55  | 32  | 2             | Yes               | `end = 31`   |
| 10    | 31  | 20  | 3             | Yes               | `end = 19`   |
| 10    | 19  | 14  | 6             | No                | `start = 15` |
| 15    | 19  | 17  | 4             | Yes               | `end = 16`   |
| 15    | 16  | 15  | 5             | Yes               | `end = 14`   |

Now:

```text
start = 15
end = 14
```

Since:

```text
start > end
```

the binary search stops.

Therefore:

```text
Output = 15
```

---

## Detailed Dry Run of `range()`

Let's understand why a capacity of `15` requires exactly `5` days.

```text
weights = [1,2,3,4,5,6,7,8,9,10]
mid = 15
```

Initially:

```text
count = 0
day = 1
```

| Package | Current `count` | `count + package` | Action  | New `count` | Day |
| ------- | --------------- | ----------------- | ------- | ----------- | --- |
| 1       | 0               | 1                 | Add     | 1           | 1   |
| 2       | 1               | 3                 | Add     | 3           | 1   |
| 3       | 3               | 6                 | Add     | 6           | 1   |
| 4       | 6               | 10                | Add     | 10          | 1   |
| 5       | 10              | 15                | Add     | 15          | 1   |
| 6       | 15              | 21                | New day | 6           | 2   |
| 7       | 6               | 13                | Add     | 13          | 2   |
| 8       | 13              | 21                | New day | 8           | 3   |
| 9       | 8               | 17                | New day | 9           | 4   |
| 10      | 9               | 19                | New day | 10          | 5   |

Therefore:

```text
Required days = 5
```

Since:

```text
5 <= 5
```

a capacity of `15` is sufficient.

---

## Why This Approach Works

The important observation is that the required number of days is **monotonic** with respect to ship capacity.

For example:

```text
Small capacity  -> More days required
Large capacity  -> Fewer days required
```

If a capacity of `15` is sufficient to ship all packages within `5` days, then any capacity greater than `15` will also be sufficient.

Similarly, if a capacity of `14` requires more than `5` days, then every capacity smaller than `14` will also require more than `5` days.

This creates a pattern like:

```text
Invalid Invalid Invalid | Valid Valid Valid
          ↑
     Minimum Answer
```

Because of this monotonic behavior, **binary search** can efficiently find the smallest valid capacity.

The `range()` helper also preserves the package order. Whenever adding the next package would exceed the current capacity, it starts a new day instead of rearranging or skipping packages.

---

## Complexity Analysis

* **Time Complexity:** `O(n log(sum(weights)))`

  The binary search performs approximately `O(log(sum(weights)))` iterations, and for each iteration the `range()` method scans all `n` packages.

* **Space Complexity:** `O(1)`

  Only a constant number of variables are used. No extra array or data structure is required.

---

## Key Insight

This problem is a classic example of **Binary Search on the Answer**.

Instead of searching for an index or element, we search for the **minimum possible ship capacity**.

The key steps are:

```text
Minimum capacity = maximum package weight
Maximum capacity = total weight of all packages
```

For every candidate capacity:

```text
Candidate capacity
        ↓
Calculate required days
        ↓
Required days <= given days?
       / \
     Yes  No
      ↓    ↓
Search   Search
left     right
```

The helper function `range()` acts as the **feasibility check**, determining whether a particular capacity can ship all packages within the required number of days.

---

## Concepts Used

* Array
* Binary Search
* Binary Search on the Answer
* Monotonic Feasibility Function
* Greedy Approach
* Capacity / Partitioning
* Simulation
* Order Preservation
* Helper Function
* Time Complexity Optimization