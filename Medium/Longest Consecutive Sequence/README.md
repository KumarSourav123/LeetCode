# LeetCode 128 - Longest Consecutive Sequence (HashSet Approach)

## Problem Statement

Given an unsorted array of integers `nums`, return *the length of the longest consecutive elements sequence*.

You must write an algorithm that runs in `O(n)` time.

---

## Example 1:
```
Input: nums = [100,4,200,1,3,2]
Output: 4
Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
```

## Example 2:
```
Input: nums = [0,3,7,2,5,8,4,6,0,1]
Output: 9
```

## Example 3:
```
Input: nums = [1,0,1,2]
Output: 3
```

## Constraints:
- `0 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`

---

## Approach

This solution uses a **HashSet combined with a "start-of-sequence" check** to expand each consecutive run exactly once.

### Idea

- Add all elements of `nums` into a `HashSet` to allow O(1) lookups and remove duplicates.
- For each number `num` in the set:
  - If `num - 1` exists in the set, skip it — `num` is **not** the start of a sequence, since a smaller consecutive number already precedes it (it will be counted when its sequence's actual start is processed).
  - If `num - 1` does **not** exist, `num` must be the **start** of a consecutive sequence. Expand forward using a `while` loop, checking `num + 1`, `num + 2`, and so on, incrementing `count` and `current` as long as consecutive numbers exist in the set.
  - Update `maxLength` with the larger of the current `count` and the previous `maxLength`.

Because a sequence is only expanded when its true starting number is found, each element in the array is visited **at most twice overall** (once in the outer loop, and once during the inner `while` expansion of its own sequence).

---

## Code

```java
class Solution {
    public int longestConsecutive(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        int n = nums.length;
        int maxLength = 1;

        if (n == 0) {
            return 0;
        }

        for (int num : nums) {
            set.add(num);
        }

        for (int num : set) {
            if (set.contains(num - 1)) {
                continue;
            } else {
                int current = num;
                int count = 1;
                while (set.contains(current + 1)) {
                    count++;
                    current++;
                }
                maxLength = Math.max(count, maxLength);
            }
        }

        return maxLength;
    }
}
```

---

## Dry Run

### Input

```text
nums = [100,4,200,1,3,2]
```

**Set built from nums:** `{100, 4, 200, 1, 3, 2}`

| num | set.contains(num-1)? | Action | count | maxLength |
|-----|------------------------|--------|-------|-----------|
|100|No (99 absent)|Start sequence → expand: 101 absent|1|1|
|4|Yes (3 present)|Skip (not a sequence start)|-|1|
|200|No (199 absent)|Start sequence → expand: 201 absent|1|1|
|1|No (0 absent)|Start sequence → expand: 2✓,3✓,4✓, then 5 absent|4|4|
|3|Yes (2 present)|Skip (not a sequence start)|-|4|
|2|Yes (1 present)|Skip (not a sequence start)|-|4|

Output:

```text
4
```

This matches the sequence `[1, 2, 3, 4]`, correctly identified starting from `1`.

---

## Why This Approach Works

Checking `set.contains(num - 1)` before expanding ensures that each consecutive sequence is only ever expanded from its **true starting point**, avoiding redundant re-scans of the same run from the middle.

Since every number is either skipped immediately (O(1) check) or is a sequence start that gets expanded exactly once through its own run, the total work across all numbers stays linear.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — every element is visited a constant number of times overall
- **Space Complexity:** `O(n)` — for storing all elements in the HashSet

---

## Key Insight

Compared to the HashMap-based boundary-merging approach, this HashSet approach avoids updating boundary values altogether — instead, it relies on the simple observation that **only numbers with no left neighbor in the set can be the start of a sequence**, making the expansion logic more straightforward while keeping the same `O(n)` guarantee.

---

## Concepts Used

- Array
- HashSet
- Hashing
- Sequence Start Detection
- Set Membership Lookup
- Constant-Time Lookup