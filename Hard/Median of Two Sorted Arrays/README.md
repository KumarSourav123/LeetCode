# LeetCode 4 - Median of Two Sorted Arrays

## Problem Statement

Given two sorted arrays `nums1` and `nums2` of size `m` and `n` respectively, return the **median of the two sorted arrays**.

The overall run time complexity should be:

```text
O(log(min(m,n)))
```

---

## Example 1:

```text
Input: nums1 = [1,3], nums2 = [2]

Output: 2.00000
```

### Explanation:

The merged sorted array is:

```text
[1,2,3]
```

Since there are `3` elements, the middle element is `2`.

Therefore, the median is:

```text
2
```

---

## Example 2:

```text
Input: nums1 = [1,2], nums2 = [3,4]

Output: 2.50000
```

### Explanation:

The merged sorted array is:

```text
[1,2,3,4]
```

Since there are `4` elements, the median is the average of the two middle elements:

```text
(2 + 3) / 2 = 2.5
```

Therefore, the median is:

```text
2.5
```

---

## Constraints:

* `nums1.length == m`
* `nums2.length == n`
* `0 <= m <= 1000`
* `0 <= n <= 1000`
* `1 <= m + n <= 2000`
* `-10^6 <= nums1[i], nums2[i] <= 10^6`
* Both `nums1` and `nums2` are sorted in **non-decreasing order**.

---

## Approach

This solution uses **Binary Search on the partition**.

The main idea is to divide the two sorted arrays into a **left half** and a **right half** such that:

* Every element on the left side is less than or equal to every element on the right side.
* The left side contains exactly half of the total elements.
* Once the correct partition is found, the median can be calculated directly from the boundary elements.

### Important Optimization

The binary search is always performed on the **smaller array**.

```java
if(n1 > n2){
    return findMedianSortedArrays(nums2, nums1);
}
```

This ensures that:

```text
O(log(min(n1,n2)))
```

is achieved.

---

## Partition Idea

Suppose we have:

```text
nums1 = [1,3]
nums2 = [2,4,5]
```

We want to divide the combined elements into two halves.

For example:

```text
Left side  | Right side
-----------|-----------
1, 2, 3    | 4, 5
```

The partition must satisfy:

```text
left elements <= right elements
```

The binary search determines how many elements should be taken from `nums1`.

Let:

```text
mid1 = number of elements taken from nums1
```

Then the number of elements that must be taken from `nums2` is:

```text
mid2 = left - mid1
```

where:

```text
left = (n1 + n2 + 1) / 2
```

The `+1` makes the calculation convenient for both odd and even total lengths.

---

## Understanding `mid1` and `mid2`

Suppose:

```text
nums1 = [1,3]
nums2 = [2,4,5]
```

Total number of elements:

```text
n = 2 + 3 = 5
```

Therefore:

```text
left = (5 + 1) / 2
     = 3
```

If we choose:

```text
mid1 = 1
```

then:

```text
mid2 = 3 - 1
     = 2
```

So the partition becomes:

```text
nums1: [1 | 3]

nums2: [2,4 | 5]
```

The left side contains:

```text
[1,2,4]
```

and the right side contains:

```text
[3,5]
```

This partition is invalid because:

```text
4 > 3
```

So we need to move the partition in `nums1`.

---

## Four Important Boundary Values

For every partition, we only need four values:

```text
l1 = largest element on the left side of nums1
r1 = smallest element on the right side of nums1

l2 = largest element on the left side of nums2
r2 = smallest element on the right side of nums2
```

The correct partition must satisfy:

```text
l1 <= r2
```

and:

```text
l2 <= r1
```

Therefore, the valid partition condition is:

```text
if(l1 <= r2 && l2 <= r1)
```

---

## Handling Boundary Cases

Sometimes the partition can be at the beginning or end of an array.

For example, if:

```text
mid1 = 0
```

there is no element on the left side of `nums1`.

The code handles this by using:

```java
int l1 = Integer.MIN_VALUE;
```

Similarly, if the partition is at the end of an array, there is no element on the right side.

So the code uses:

```java
int r1 = Integer.MAX_VALUE;
```

The same logic is applied to `l2` and `r2`.

This allows the same comparison logic to work even at the boundaries.

---

## Binary Search Decision

After calculating the four boundary values:

```text
l1, r1, l2, r2
```

we check whether the partition is correct.

### Case 1: Correct Partition

If:

```text
l1 <= r2 && l2 <= r1
```

then the partition is correct.

Now we can calculate the median.

---

### Case 2: `l1 > r2`

If:

```text
l1 > r2
```

then we have taken **too many elements from `nums1`** on the left side.

Therefore, move the partition of `nums1` to the left:

```java
high = mid1 - 1;
```

---

### Case 3: `l2 > r1`

If:

```text
l2 > r1
```

then we have taken **too few elements from `nums1`**.

Therefore, move the partition to the right:

```java
low = mid1 + 1;
```

---

## Finding the Median

Once the correct partition is found, the median depends on whether the total number of elements is odd or even.

### Odd Number of Elements

If:

```text
n % 2 == 1
```

the median is the largest element on the left side:

```text
max(l1,l2)
```

So the code returns:

```java
Math.max(l1,l2)
```

---

### Even Number of Elements

If:

```text
n % 2 == 0
```

there are two middle elements.

They are:

```text
max(l1,l2)
```

and:

```text
min(r1,r2)
```

Therefore:

```text
median = (max(l1,l2) + min(r1,r2)) / 2
```

The code uses:

```java
((double)Math.max(l1,l2) + Math.min(r1,r2)) / 2.0
```

to make sure the result is calculated as a `double`.

---

## Code

```java
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        int n1 = nums1.length;
        int n2 = nums2.length;

        if(n1 > n2){
            return findMedianSortedArrays(nums2, nums1);
        }

        int low = 0;
        int high = n1;

        int left = (n1+n2+1)/2;
        int n = n1+n2;

        while(low <= high){

            int mid1 = (low+high)/2;
            int mid2 = left-mid1;

            int l1 = Integer.MIN_VALUE;
            int l2 = Integer.MIN_VALUE;

            int r1 = Integer.MAX_VALUE;
            int r2 = Integer.MAX_VALUE;

            if(mid1 < n1)
                r1 = nums1[mid1];

            if(mid2 < n2)
                r2 = nums2[mid2];

            if(mid1 - 1 >= 0)
                l1 = nums1[mid1-1];

            if(mid2 - 1 >= 0)
                l2 = nums2[mid2-1];

            if((l1 <= r2) && (l2 <= r1)){

                if(n % 2 == 1){
                    return Math.max(l1,l2);
                }else{
                    return ((double)Math.max(l1,l2) 
                            + Math.min(r1,r2)) / 2.0;
                }

            }else if(l1 > r2){
                high = mid1-1;
            }else{
                low = mid1+1;
            }
        }

        return 0;
    }
}
```

---

## Dry Run

### Input

```text
nums1 = [1,2]
nums2 = [3,4]
```

We have:

```text
n1 = 2
n2 = 2
```

Since `n1` is not greater than `n2`, we continue.

```text
low = 0
high = 2
```

Total number of elements:

```text
n = 2 + 2 = 4
```

Number of elements required on the left:

```text
left = (2 + 2 + 1) / 2
     = 2
```

---

### Iteration 1

Calculate:

```text
mid1 = (0 + 2) / 2
     = 1
```

Therefore:

```text
mid2 = left - mid1
     = 2 - 1
     = 1
```

The partition is:

```text
nums1 = [1 | 2]

nums2 = [3 | 4]
```

So:

```text
l1 = 1
r1 = 2

l2 = 3
r2 = 4
```

Check:

```text
l1 <= r2
1 <= 4  → true

l2 <= r1
3 <= 2  → false
```

Therefore, the partition is invalid.

Since:

```text
l2 > r1
3 > 2
```

we need to move the partition in `nums1` to the right:

```text
low = mid1 + 1
    = 2
```

---

### Iteration 2

Now:

```text
low = 2
high = 2
```

Calculate:

```text
mid1 = (2 + 2) / 2
     = 2
```

Then:

```text
mid2 = 2 - 2
     = 0
```

The partition becomes:

```text
nums1 = [1,2 | ]

nums2 = [ | 3,4]
```

Therefore:

```text
l1 = 2
r1 = Integer.MAX_VALUE

l2 = Integer.MIN_VALUE
r2 = 3
```

Check:

```text
l1 <= r2
2 <= 3 → true

l2 <= r1
true
```

So the partition is correct.

Since:

```text
n = 4
```

is even, we take the two middle elements:

```text
max(l1,l2) = max(2, MIN_VALUE) = 2

min(r1,r2) = min(MAX_VALUE,3) = 3
```

Therefore:

```text
median = (2 + 3) / 2
       = 2.5
```

Output:

```text
2.50000
```

---

## Another Dry Run

### Input

```text
nums1 = [1,3]
nums2 = [2]
```

Since:

```text
n1 = 2
n2 = 1
```

the smaller array should be searched.

Therefore, the code executes:

```java
if(n1 > n2){
    return findMedianSortedArrays(nums2, nums1);
}
```

The arrays become conceptually:

```text
nums1 = [2]
nums2 = [1,3]
```

Now:

```text
n1 = 1
n2 = 2
n = 3
```

Calculate:

```text
left = (1 + 2 + 1) / 2
     = 2
```

Binary search gives the correct partition:

```text
nums1 = [2 | ]

nums2 = [1 | 3]
```

Therefore:

```text
l1 = 2
l2 = 1
r1 = MAX_VALUE
r2 = 3
```

The partition is valid because:

```text
2 <= 3
1 <= MAX_VALUE
```

Since the total number of elements is odd:

```text
n = 3
```

the median is:

```text
max(l1,l2)
= max(2,1)
= 2
```

Therefore:

```text
Output = 2.00000
```

---

## Why This Approach Works

The key observation is that we **do not need to actually merge the two sorted arrays**.

A normal merge would take:

```text
O(n1 + n2)
```

time.

Instead, we find a partition such that:

```text
Left side | Right side
```

contains exactly half of the combined elements, and:

```text
max(left side) <= min(right side)
```

The correct partition guarantees that the elements around the partition are exactly the elements needed to determine the median.

The binary search works because moving the partition in `nums1` changes the partition in `nums2` in the opposite direction.

If:

```text
l1 > r2
```

we took too many elements from `nums1`, so move left.

If:

```text
l2 > r1
```

we took too few elements from `nums1`, so move right.

Eventually, we find the partition satisfying:

```text
l1 <= r2 && l2 <= r1
```

At that point, the median can be obtained directly from the four boundary values.

---

## Complexity Analysis

* **Time Complexity:** `O(log(min(n1,n2)))`

  The algorithm always performs binary search on the smaller array. Each iteration cuts the search space approximately in half.

* **Space Complexity:** `O(1)`

  Only a constant number of variables are used, and no merged array or additional data structure is created.

---

## Key Insight

The most important idea is:

> **Find the correct partition instead of merging the arrays.**

We divide the combined arrays into two halves:

```text
        LEFT              RIGHT
   ─────────────       ─────────────
      l1   l2             r1   r2
```

The correct partition satisfies:

```text
l1 <= r2
l2 <= r1
```

Then:

### If total length is odd:

```text
median = max(l1,l2)
```

### If total length is even:

```text
median = (max(l1,l2) + min(r1,r2)) / 2
```

The complete strategy is:

```text
Choose smaller array
        ↓
Binary search partition
        ↓
Calculate mid1 and mid2
        ↓
Find l1, r1, l2, r2
        ↓
Is partition valid?
     /          \
   No            Yes
   ↓              ↓
Move left/right  Calculate median
```

This allows the problem to be solved in the required **`O(log(min(n1,n2)))` time** without actually merging the two arrays.

---

## Concepts Used

* Array
* Sorted Arrays
* Binary Search
* Binary Search on Partition
* Median
* Partitioning
* Two Sorted Arrays
* Divide and Conquer
* Boundary Elements
* `Integer.MIN_VALUE`
* `Integer.MAX_VALUE`
* Odd and Even Cases
* Recursive Array Swapping
* `O(log(min(n1,n2)))` Optimization
* Constant Space