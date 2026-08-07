# LeetCode 152 – Maximum Product Subarray

## Problem

Given an integer array `nums`, find the **contiguous subarray** that has the largest product and return that product.

The answer is guaranteed to fit in a **32-bit integer**.

## Concepts Used

### 1. Prefix Product

The prefix product is the product of elements from the beginning of the array.

```text
prefix = nums[0] × nums[1] × ... × nums[i]
```

### 2. Suffix Product

The suffix product is the product of elements from the end of the array.

```text
suffix = nums[n-1] × nums[n-2] × ... × nums[i]
```

### 3. Effect of Negative Numbers

This is the key concept.

* **Even number of negative numbers** → product becomes **positive**.
* **Odd number of negative numbers** → product becomes **negative**.

The maximum product subarray may start from the left **or** from the right.

### 4. Effect of Zero

A zero breaks the product.

Example:

```text
2 × 3 × 0 × 4 × 5
```

The product before and after zero are independent.

Therefore, whenever the running product becomes `0`, we reset it to `1`.

## Idea

Traverse the array **from both directions simultaneously**.

Maintain:

* `prefix` → left to right product
* `suffix` → right to left product

At each index:

1. Update prefix product.
2. Update suffix product.
3. Update the maximum product.
4. Reset prefix or suffix to `1` if it becomes `0`.

This automatically handles:

* odd negative counts,
* even negative counts,
* zeros.

## Java Solution

```java
class Solution {
    public int maxProduct(int[] nums) {
        int totalMax = Integer.MIN_VALUE;
        int prefix = 1;
        int suffix = 1;

        for (int i = 0; i < nums.length; i++) {

            prefix *= nums[i];
            suffix *= nums[nums.length - 1 - i];

            totalMax = Math.max(totalMax, Math.max(prefix, suffix));

            if (prefix == 0)
                prefix = 1;

            if (suffix == 0)
                suffix = 1;
        }

        return totalMax;
    }
}
```

## Dry Run

Example:

```text
nums = [2,3,-2,4]
```

| i | prefix | suffix | totalMax |
| - | ------ | ------ | -------- |
| 0 | 2      | 4      | 4        |
| 1 | 6      | -8     | 6        |
| 2 | -12    | -24    | 6        |
| 3 | -48    | -48    | 6        |

Return:

```text
6
```

Subarray:

```text
[2,3]
```

## Example with Zero

```text
nums = [-2,0,-1]
```

Traversal:

* prefix = -2
* prefix becomes 0 → reset to 1
* prefix = -1

Similarly for suffix.

Maximum becomes:

```text
0
```

Return:

```text
0
```

## Why Prefix and Suffix Work

Consider:

```text
[-1,-2,-3]
```

Products from left:

```text
-1
2
-6
```

Products from right:

```text
-3
6
-6
```

The maximum product is **6**, which is found by the suffix traversal.

Similarly, some cases are found only by the prefix traversal.

Scanning from **both directions** guarantees that we do not miss the optimal subarray.

## Time Complexity

* One traversal of the array.

**O(n)**

## Space Complexity

Only a few variables are used.

**O(1)**

## Pattern Recognition

This problem belongs to:

* Prefix/Suffix Traversal
* Array
* Dynamic Product Tracking
* Handling Negative Numbers
* Kadane-like Optimization

## Interview Takeaway

For **maximum sum subarray**, Kadane’s algorithm tracks only the maximum.

For **maximum product subarray**, negatives can turn a small product into a large positive product.

The prefix-suffix method works because:

* zeros reset the product,
* odd negatives can be removed by excluding either the left prefix or the right suffix.

The key implementation pattern is:

```java
prefix *= nums[i];
suffix *= nums[n - 1 - i];

totalMax = Math.max(totalMax, Math.max(prefix, suffix));

if (prefix == 0) prefix = 1;
if (suffix == 0) suffix = 1;
```

This is one of the cleanest **O(n), O(1)** solutions for Maximum Product Subarray.