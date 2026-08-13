# LeetCode 540 - Single Element in a Sorted Array

## Problem Statement

You are given a sorted array consisting of only integers where every element appears exactly twice, except for one element which appears exactly once.

Return *the single element that appears only once*.

Your solution must run in `O(log n)` time and `O(1)` space.

---

## Example 1:
```
Input: nums = [1,1,2,3,3,4,4,8,8]
Output: 2
```

## Example 2:
```
Input: nums = [3,3,7,7,10,11,11]
Output: 10
```

## Constraints:
- `1 <= nums.length <= 10^5`
- `0 <= nums[i] <= 10^5`

---

## Approach

This solution uses **binary search combined with pairing/parity logic**: in a properly-paired sorted array, each pair should sit at an (even, odd) index — the single element breaks this pattern, and binary search exploits that break to converge on it.

### Idea

- **Boundary checks first:**
  - If `low == high`, only one element remains — it must be the single one, so return `nums[low]`.
  - If `nums[low] != nums[low+1]`, the first element has no matching pair — it's the single element, return `nums[low]`.
  - If `nums[high] != nums[high-1]`, the last element has no matching pair — return `nums[high]`.
- If neither boundary is the answer, narrow the search to the interior: `low++`, `high--`, then binary search:
  - Compute `mid`. If `nums[mid] != nums[mid-1]` **and** `nums[mid] != nums[mid+1]`, `mid` matches neither neighbor — it's the single element, return `nums[mid]`.
  - Otherwise, use the **parity of `mid`** to decide which direction still has a "balanced" pairing pattern:
    - If `mid` is **even**: check whether `nums[mid] == nums[mid+1]`. If so, the pairing is still intact up through `mid`, meaning the single element must be **after** this point — search right (`low = mid + 1`). Otherwise, the pattern has already broken **before** this point — search left (`high = mid - 1`).
    - If `mid` is **odd**: the logic mirrors, but checks `nums[mid] == nums[mid+1]` too — if true, the pairing is intact and the single element is to the **left** (`high = mid - 1`); if false, search right (`low = mid + 1`).
- Continue narrowing until the single element is found.

---

## Code

```java
class Solution {
    public int singleNonDuplicate(int[] nums) {
        int low = 0;
        int high = nums.length - 1;

        if (low == high) {
            return nums[low];
        }
        if (nums[low] != nums[low + 1]) {
            return nums[low];
        }
        if (nums[high] != nums[high - 1]) {
            return nums[high];
        }

        low++;
        high--;

        while (low <= high) {
            int mid = (low + high) / 2;
            if ((nums[mid] != nums[mid - 1]) && (nums[mid] != nums[mid + 1])) {
                return nums[mid];
            } else if (mid % 2 == 0) {
                if (nums[mid] == nums[mid + 1]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            } else {
                if (nums[mid] == nums[mid + 1]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
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
nums = [1,1,2,3,3,4,4,8,8]
```

`low=0`, `high=8`. `low==high`? No. `nums[0]=1 != nums[1]=1`? No (equal). `nums[8]=8 != nums[7]=8`? No (equal). Neither boundary is the answer.

**Narrow interior:** `low++ → 1`, `high-- → 7`

| low | high | mid | nums[mid-1], nums[mid], nums[mid+1] | Check | Action |
|-----|------|-----|----------------------------------------|-------|--------|
|1|7|4|3, 3, 4|nums[mid]≠nums[mid-1]? No (3==3) → not single. mid=4 is **even** → nums[mid]==nums[mid+1]? 3==4? No|high = mid-1 = 3|
|1|3|2|1, 2, 3|nums[mid]≠nums[mid-1] (2≠1, true) && nums[mid]≠nums[mid+1] (2≠3, true) → **single element found**|return nums[2] = 2|

Output:

```text
2
```

This matches the expected output.

---

## Why This Approach Works

In a correctly-paired sorted array (without the single element), each pair naturally lands on an (even, odd) index pair: index `0`&`1`, `2`&`3`, `4`&`5`, and so on. Once the single element appears at some point, it shifts every subsequent pair by one position, flipping this even/odd alignment for everything after it.

Checking `nums[mid] == nums[mid+1]` alongside the parity of `mid` tells us whether the "pairing alignment" is still intact up to that point — if it is, the single element must be further along (search past `mid`); if the alignment has already broken, the single element must be at or before this point (search before `mid`). This directional logic is what allows binary search to correctly halve the search space despite the array not being "sorted" in the traditional sense used for standard binary search.

---

## Complexity Analysis

- **Time Complexity:** `O(log n)` — binary search over the array
- **Space Complexity:** `O(1)` — only a constant number of variables are used

---

## Key Insight

This problem cleverly repurposes binary search by using **index parity** as the ordering signal instead of value comparison: since the single element disrupts the expected (even, odd) pairing pattern for every index after it, checking whether that pattern is still intact at `mid` reveals which half of the array must contain the "broken" pairing — and therefore the single element.

---

## Concepts Used

- Array
- Binary Search
- Pairing/Parity Logic
- Boundary Case Handling