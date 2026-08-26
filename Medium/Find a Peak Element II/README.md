# LeetCode 1901 - Find a Peak Element II

## Problem Statement

A **peak element** in a 2D grid is an element that is strictly greater than all of its adjacent neighbors to the left, right, top, and bottom.

Given a `0-indexed` `m x n` matrix `mat` where no two adjacent cells are equal, find a peak element `mat[i][j]` and return its position `[i, j]`.

You may assume that the entire matrix is surrounded by an outer perimeter with the value `-1` in each cell.

You must write an algorithm that runs in:

```text
O(m log n) or O(n log m)
```

---

## Example 1:

```text
Input: mat = [[1,4],
              [3,2]]

Output: [0,1]
```

### Explanation:

Both `3` and `4` are peak elements.

For example:

```text
[1, 4]
[3, 2]
```

The element `4` at `[0,1]` is greater than its adjacent neighbors `1` and `2`.

The element `3` at `[1,0]` is also greater than its adjacent neighbors `1` and `2`.

Therefore, either `[0,1]` or `[1,0]` is an acceptable answer.

---

## Example 2:

```text
Input: mat = [[10,20,15],
              [21,30,14],
              [7,16,32]]

Output: [1,1]
```

### Explanation:

The element `30` at `[1,1]` is a peak element because it is greater than all four adjacent elements:

```text
       20
        |
21 ---- 30 ---- 14
        |
       16
```

Therefore, `[1,1]` is a valid answer.

`[2,2]` containing `32` is also a valid peak element.

---

## Constraints:

* `m == mat.length`
* `n == mat[i].length`
* `1 <= m, n <= 500`
* `1 <= mat[i][j] <= 10^9`
* No two adjacent cells are equal.
* Adjacent cells are only considered in the four directions: left, right, top, and bottom.

---

## Approach

This solution uses **Binary Search on Columns** combined with finding the **maximum element in the selected column**.

Instead of checking every cell of the matrix, we perform binary search over the columns.

### Idea

For every middle column:

```text
mid
```

we find the **maximum element in that entire column**.

Suppose the maximum element is:

```text
mat[row][mid]
```

Because this is the maximum element in its column, it is automatically greater than its **top and bottom neighbors**.

Therefore, we only need to compare it with its:

```text
left neighbor
right neighbor
```

If it is greater than both, we have found a peak.

---

## Step 1: Binary Search on Columns

The code initializes:

```java
int low = 0;
int high = n;
```

where `n` is the last column index:

```java
int n = mat[0].length - 1;
```

Then:

```java
while(low <= high)
```

performs binary search over the column range.

For every iteration:

```java
int mid = (low+high)/2;
```

represents the column currently being examined.

---

## Step 2: Find Maximum Element in the Column

Once we have the middle column, we scan all rows in that column.

```java
int max = mat[0][mid];
int row = 0;

for(int i = 1; i <= m; i++){
    if(mat[i][mid] > max){
        max = mat[i][mid];
        row = i;
    }
}
```

The largest element in the column is stored in:

```text
mat[row][mid]
```

Since it is the maximum element of the column:

```text
mat[row][mid] > top
mat[row][mid] > bottom
```

whenever those neighbors exist.

Therefore, we only need to check left and right.

---

## Step 3: Handle Boundary Columns

The matrix is surrounded by `-1`, so if the selected element is at the first or last column, we can treat the missing neighbor as `-1`.

The code does this using:

```java
int left = mid-1 >= 0 ? mat[row][mid-1] : -1;
int right = mid+1 <= n ? mat[row][mid+1] : -1;
```

Therefore:

### If `mid` is the first column:

```text
left = -1
```

### If `mid` is the last column:

```text
right = -1
```

This makes the comparison work uniformly for all columns.

---

## Step 4: Check Whether It Is a Peak

The selected maximum element is a peak if:

```java
mat[row][mid] > right
```

and:

```java
mat[row][mid] > left
```

So the code checks:

```java
if((mat[row][mid] > right) && (mat[row][mid] > left)){
    return new int[]{row,mid};
}
```

If this condition is true, we immediately return the position.

---

## Step 5: Decide Which Half to Search

If the current element is not a peak, we need to decide which direction can contain a peak.

### Case 1: Right Neighbor Is Greater

If:

```java
max < right
```

then the right neighbor is greater than the maximum element of the current column.

Therefore, there must be a peak somewhere toward the **right**.

So we move the binary search to the right:

```java
low = mid+1;
```

---

### Case 2: Otherwise Search Left

If:

```java
max >= right
```

and the current element was not a peak, the larger neighbor must be on the left.

Therefore, we search the left half:

```java
high = mid-1;
```

---

## Why Finding the Maximum in a Column Helps

Suppose the selected column is:

```text
       5
       |
       9
       |
       7
       |
       3
```

If `9` is the maximum element in the column, then:

```text
9 > 5
9 > 7
```

So the vertical conditions are already satisfied.

We only need to check:

```text
left < 9 > right
```

Therefore, finding the maximum in the column reduces the 2D peak problem to a comparison involving only the horizontal neighbors.

---

## Code

```java
class Solution {
    public int[] findPeakGrid(int[][] mat) {

        int m = mat.length-1;
        int n = mat[0].length-1;

        int low = 0;
        int high = n;

        while(low <= high){

            int mid = (low+high)/2;

            int max = mat[0][mid];
            int row = 0;

            for(int i = 1; i <= m; i++){
                if(mat[i][mid] > max){
                    max = mat[i][mid];
                    row = i;
                }
            }

            int left = mid-1 >= 0 ? mat[row][mid-1] : -1;
            int right = mid+1 <= n ? mat[row][mid+1] : -1;

            if((mat[row][mid] > right) && (mat[row][mid] > left)){
                return new int[]{row,mid};
            }else if(max < right){
                low = mid+1;
            }else{
                high = mid-1;
            }
        }

        return new int[]{0,0};
    }
}
```

---

## Dry Run

### Input

```text
mat = [[1,4],
       [3,2]]
```

We have:

```text
m = mat.length - 1
  = 2 - 1
  = 1

n = mat[0].length - 1
  = 2 - 1
  = 1
```

Initial binary search:

```text
low = 0
high = 1
```

---

### Iteration 1

Calculate:

```text
mid = (low + high) / 2
    = (0 + 1) / 2
    = 0
```

So we examine column `0`.

Column `0` is:

```text
[1]
[3]
```

Find the maximum:

```text
max = 3
row = 1
```

Therefore:

```text
mat[row][mid]
= mat[1][0]
= 3
```

Now calculate the horizontal neighbors.

### Left Neighbor

Since:

```text
mid = 0
```

there is no column to the left.

Therefore:

```text
left = -1
```

### Right Neighbor

The right column is column `1`:

```text
right = mat[1][1]
      = 2
```

Now check whether `3` is a peak:

```text
3 > 2  → true
3 > -1 → true
```

Therefore:

```text
mat[1][0] = 3
```

is a peak element.

The algorithm returns:

```text
[1,0]
```

`[1,0]` is accepted because the problem allows **any valid peak element**.

---

## Dry Run Table

| `low` | `high` | `mid` | Maximum in Column | `row` | Left | Right | Peak? | Action         |
| ----- | ------ | ----- | ----------------- | ----- | ---- | ----- | ----- | -------------- |
| 0     | 1      | 0     | 3                 | 1     | -1   | 2     | Yes   | Return `[1,0]` |

Therefore:

```text
Output = [1,0]
```

Although the example shows `[0,1]`, `[1,0]` is also a correct answer.

---

## Another Dry Run

### Input

```text
mat = [[10,20,15],
       [21,30,14],
       [7,16,32]]
```

The matrix has:

```text
3 rows
3 columns
```

Therefore:

```text
low = 0
high = 2
```

---

### Iteration 1

Calculate:

```text
mid = (0 + 2) / 2
    = 1
```

We examine column `1`:

```text
20
30
16
```

The maximum element is:

```text
30
```

at:

```text
row = 1
```

So:

```text
mat[1][1] = 30
```

Its horizontal neighbors are:

```text
left  = mat[1][0] = 21
right = mat[1][2] = 14
```

Check:

```text
30 > 21 → true
30 > 14 → true
```

Therefore, `30` is a peak.

The algorithm returns:

```text
[1,1]
```

---

## Why This Approach Works

The key observation is that we do not need to examine every element in the matrix.

For a selected column:

1. Find its maximum element.
2. Because it is the column maximum, it is already greater than its vertical neighbors.
3. Only compare it with its left and right neighbors.
4. If it is not a peak and the right neighbor is larger, move right.
5. Otherwise, move left.

The important property is that if the current maximum has a larger neighbor on one side, moving toward that larger neighbor guarantees that we can continue toward a peak.

This allows us to eliminate approximately half of the columns after every binary search step.

The process looks like:

```text
All Columns
     ↓
Choose Middle Column
     ↓
Find Maximum Element
     ↓
Check Left & Right
     ↓
   Peak?
  /     \
Yes      No
 ↓        ↓
Return   Move toward
         larger neighbor
              ↓
       Repeat Binary Search
```

Because every step eliminates approximately half of the columns, the algorithm is much faster than checking every cell.

---

## Complexity Analysis

Let:

```text
m = number of rows
n = number of columns
```

### Time Complexity

For every binary search iteration, we scan all `m` rows to find the maximum element in the selected column.

Binary search takes:

```text
O(log n)
```

iterations.

Therefore:

```text
O(m log n)
```

is the overall time complexity.

### Space Complexity

Only a constant number of variables are used:

```text
max
row
low
high
mid
left
right
```

Therefore:

```text
O(1)
```

extra space is used.

---

## Key Insight

This problem is a classic example of **Binary Search on a 2D Matrix**.

Instead of searching every cell:

```text
O(m × n)
```

we perform binary search on the columns.

For each selected column:

```text
Find maximum element
        ↓
Check left and right
        ↓
Is it a peak?
    /        \
  Yes         No
   ↓           ↓
Return     Move toward
           larger side
```

The most important idea is:

> **Find the maximum element of the middle column because its top and bottom neighbors are automatically smaller. Then only the left and right neighbors need to be checked.**

This reduces the problem from a complete matrix traversal to:

```text
O(m log n)
```

which satisfies the required complexity.

---

## Concepts Used

* 2D Array / Matrix
* Binary Search
* Binary Search on Columns
* Peak Element
* Maximum Element in a Column
* Divide and Conquer
* Greedy Direction Selection
* Boundary Handling
* `Integer` / Sentinel Value `-1`
* Monotonic Search Direction
* `O(m log n)` Optimization
* Constant Space