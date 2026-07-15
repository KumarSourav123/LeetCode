# Maximum Subarray (LeetCode 53)

## Approach (Kadane's Algorithm)

### Intuition
- Keep a running sum of the current subarray.
- If the running sum becomes negative, discard it because it can never help increase the sum of any future subarray.
- Keep track of the maximum sum seen so far.

---

## Algorithm
1. Initialize:
   - `sum = 0`
   - `totalSum = nums[0]`
2. Traverse the array.
3. Add the current element to `sum`.
4. Update `totalSum` if `sum` is greater.
5. If `sum` becomes negative, reset it to `0`.
6. Return `totalSum`.

---

## Code

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int index = 0;
        int sum = 0;
        int totalSum = nums[0];

        while (index < nums.length) {

            sum = sum + nums[index];

            if (sum > totalSum) {
                totalSum = sum;
            }

            if (sum < 0) {
                sum = 0;
            }

            index++;
        }

        return totalSum;
    }
}
```

---

## Dry Run

### Input

```text
nums = [-2,1,-3,4,-1,2,1,-5,4]
```

| Index | nums[i] | Running Sum | Max Sum | Action |
|------:|---------:|------------:|---------:|--------|
| 0 | -2 | -2 | -2 | Reset sum to 0 |
| 1 | 1 | 1 | 1 | Keep |
| 2 | -3 | -2 | 1 | Reset sum to 0 |
| 3 | 4 | 4 | 4 | Keep |
| 4 | -1 | 3 | 4 | Keep |
| 5 | 2 | 5 | 5 | Update |
| 6 | 1 | 6 | 6 | Update |
| 7 | -5 | 1 | 6 | Keep |
| 8 | 4 | 5 | 6 | Keep |

Final Answer:

```text
6
```

Maximum Subarray:

```text
[4, -1, 2, 1]
```

---

## Why reset when `sum < 0`?

Suppose:

```text
Current Sum = -5
Next Number = 8
```

Continuing:

```text
-5 + 8 = 3
```

Starting fresh:

```text
8
```

Clearly,

```text
8 > 3
```

A negative sum only decreases the future sum, so discard it.

---

## Complexity Analysis

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(1)`

