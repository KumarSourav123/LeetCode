# 🔄 Rotate Array (LeetCode 189)

## ✅ My Approach

The array is rotated to the right by `k` positions using the **array reversal technique**.

First, `k` is reduced using:

```text
k = k % nums.length
```

This handles cases where `k` is greater than the array length.

The solution performs the rotation **in-place** by rearranging the elements through reversal operations.

---

## 💻 Code

```java
class Solution {
    public void rotate(int[] nums, int k) {
        k = k % nums.length;

        for (int i = 0; i < (nums.length - k) / 2; i++) {
            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - k - i];
            nums[nums.length - 1 - k - i] = temp;
        }

        int j = 0;

        for (int i = nums.length - k;
             i < (nums.length + nums.length - k) / 2;
             i++) {

            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - j];
            nums[nums.length - 1 - j] = temp;

            j++;
        }

        for (int i = 0; i < nums.length / 2; i++) {
            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - i];
            nums[nums.length - 1 - i] = temp;
        }
    }
}
```

---

# ⏱️ Time Complexity

The array is traversed a constant number of times.

### Overall Time Complexity

```text
O(n)
```

---

# 💾 Space Complexity

Only a temporary variable is used for swapping elements.

No extra array or data structure is created.

### Space Complexity

```text
O(1)
```

---

# 🚀 Why This Approach Is Optimal

The array is rotated **in-place**, meaning no extra array is used.

- No extra space is required.
- Each element is processed a constant number of times.
- The time complexity is `O(n)`.
- The space complexity is `O(1)`.

Therefore, this is an optimal solution for the problem.

---

# 🧠 Key Concept

The main technique used is:

## 🔁 Array Reversal

Array reversal can be used to rotate an array efficiently without using extra space.

This approach achieves:

```text
Time Complexity: O(n)
Space Complexity: O(1)
```