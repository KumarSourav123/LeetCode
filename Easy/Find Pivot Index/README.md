# 724. Find Pivot Index

## My Solution

```java
class Solution {
    public int pivotIndex(int[] nums) {
        int[] leftSum = new int[nums.length];
        int[] rightSum = new int[nums.length];
        int lsum = 0;
        int rsum = 0;

        // Prefix Sum
        for (int i = 0; i < nums.length; i++) {
            lsum += nums[i];
            leftSum[i] = lsum;
        }

        // Suffix Sum
        for (int i = nums.length - 1; i >= 0; i--) {
            rsum += nums[i];
            rightSum[i] = rsum;
        }

        // Find Pivot
        for (int i = 0; i < nums.length; i++) {
            if (leftSum[i] == rightSum[i]) {
                return i;
            }
        }

        return -1;
    }
}
```

---

## Approach

- Create a **prefix sum** array where `leftSum[i]` stores the sum of elements from index `0` to `i`.
- Create a **suffix sum** array where `rightSum[i]` stores the sum of elements from index `i` to `n-1`.
- Traverse the array once more.
- If `leftSum[i] == rightSum[i]`, then index `i` is the pivot index.

---

## Example

```
nums = [1,7,3,6,5,6]

leftSum  = [1,8,11,17,22,28]
rightSum = [28,27,20,17,11,6]
```

At index `3`:

```
leftSum[3] == rightSum[3]

17 == 17
```

So the pivot index is:

```
3
```

---

## Time Complexity

- Prefix Sum: **O(n)**
- Suffix Sum: **O(n)**
- Traversal: **O(n)**

**Overall:** `O(n)`

---

## Space Complexity

- Prefix Sum Array: `O(n)`
- Suffix Sum Array: `O(n)`

**Overall:** `O(n)`

---

# Optimal Solution

The extra arrays are unnecessary.

```java
class Solution {
    public int pivotIndex(int[] nums) {

        int total = 0;

        for (int num : nums) {
            total += num;
        }

        int leftSum = 0;

        for (int i = 0; i < nums.length; i++) {

            if (leftSum == total - leftSum - nums[i]) {
                return i;
            }

            leftSum += nums[i];
        }

        return -1;
    }
}
```

---

## Optimal Approach

1. Compute the total sum of the array.
2. Keep a running `leftSum`.
3. The right sum is:

```
rightSum = total - leftSum - nums[i]
```

4. If

```
leftSum == rightSum
```

then `i` is the pivot index.

---

## Optimal Complexity

- **Time:** `O(n)`
- **Space:** `O(1)`

---

## Verdict

✅ Your solution is correct.

- Time Complexity: **O(n)**
- Space Complexity: **O(n)**

The optimal solution improves the space complexity to **O(1)** by avoiding the prefix and suffix arrays.