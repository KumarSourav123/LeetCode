# LeetCode 128 - Longest Consecutive Sequence

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

This solution uses a **HashMap to track the length of the consecutive sequence each number belongs to**, merging neighboring sequences on the fly.

### Idea

- For each number `num` in `nums` (skipping it if it's already been processed and exists in the map):
  - Look up the length of the sequence ending just before `num` (`left = map.get(num - 1)`) and the length of the sequence starting just after `num` (`right = map.get(num + 1)`), defaulting to `0` if they don't exist.
  - The new total sequence length that `num` creates by bridging these two sequences is `left + right + 1`.
  - Store this `total` at `num` itself.
  - If a left-neighboring sequence exists, update the entry at its **starting boundary** (`num - left`) to reflect the new merged `total`, so future lookups from that end get the correct length.
  - Similarly, if a right-neighboring sequence exists, update the entry at its **ending boundary** (`num + right`) to reflect the new merged `total`.
- After processing all numbers, scan the map for the maximum value stored, which represents the length of the longest consecutive sequence found.

---

## Code

```java
class Solution {
    public int longestConsecutive(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int length = 0;

        for (int num : nums) {
            if (map.containsKey(num)) {
                continue;
            }

            int left = map.getOrDefault(num - 1, 0);
            int right = map.getOrDefault(num + 1, 0);

            int total = left + right + 1;

            map.put(num, total);

            if (left > 0) {
                map.put(num - left, total);
            }

            if (right > 0) {
                map.put(num + right, total);
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > length) {
                length = entry.getValue();
            }
        }

        return length;
    }
}
```

---

## Dry Run

### Input

```text
nums = [100,4,200,1,3,2]
```

| num | left | right | total | Boundary Updates | map after step |
|-----|------|-------|-------|-------------------|-----------------|
|100|0|0|1|none|{100:1}|
|4|0|0|1|none|{100:1, 4:1}|
|200|0|0|1|none|{100:1, 4:1, 200:1}|
|1|0|0|1|none|{100:1, 4:1, 200:1, 1:1}|
|3|0|1 (from 4)|2|right>0 → map[4]=2|{100:1, 4:2, 200:1, 1:1, 3:2}|
|2|1 (from 1)|2 (from 3)|4|left>0 → map[1]=4; right>0 → map[4]=4|{100:1, 4:4, 200:1, 1:4, 3:2, 2:4}|

Scanning the final map for the maximum value: `1, 4, 1, 4, 2, 4` → maximum is `4`.

Output:

```text
4
```

This matches `[1, 2, 3, 4]`, the longest consecutive sequence in the array.

---

## Why This Approach Works

Every time a new number is added, it checks both its immediate neighbors (`num - 1` and `num + 1`) to see if it's extending or bridging existing sequences.

By updating the values stored at the **boundary** endpoints (the far left and far right ends of the merged sequence) rather than every element inside it, each number is only touched a constant number of times, and any future number that connects to the same sequence will correctly find the merged length by looking at its own neighbor's boundary entry.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — each number triggers at most a constant number of map operations
- **Space Complexity:** `O(n)` — for storing sequence lengths in the HashMap

---

## Key Insight

Instead of sorting the array first (which would cost `O(n log n)`) or repeatedly expanding outward from every number (which can degrade to `O(n^2)` in the worst case), maintaining sequence lengths only at the **boundaries** of each run lets sequences merge in **O(1)** per number, achieving true `O(n)` overall time.

---

## Concepts Used

- Array
- HashMap
- Hashing
- Union-by-Boundary Technique
- Sequence Merging
- Constant-Time Lookup