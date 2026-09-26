# LeetCode 1 - Two Sum

## Problem Statement

Given an array of integers `nums` and an integer `target`, return *indices of the two numbers such that they add up to* `target`.

You may assume that each input would have **exactly one solution**, and you may not use the *same* element twice.

You can return the answer in any order.

---

## Example 1:
```
Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
```

## Example 2:
```
Input: nums = [3,2,4], target = 6
Output: [1,2]
```

## Example 3:
```
Input: nums = [3,3], target = 6
Output: [0,1]
```

## Constraints:
- `2 <= nums.length <= 10^4`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`
- Only one valid answer exists.

**Follow-up:** Can you come up with an algorithm that is less than `O(n²)` time complexity?

---

## Approach

This solution uses a **hash map to check for the complement value on the fly**, making a single pass through the array: at each element, it asks "have I already seen the number that would pair with this one to reach `target`?" before recording the current number for future lookups.

### Idea

- Create `map`, a `HashMap<Integer, Integer>` mapping each **value** seen so far to its **index**.
- **Loop** `for(int i = 0; i < nums.length; i++)`:
  - Compute the complement needed to reach the target from the current value: `num = target - nums[i]`.
  - **Check if that complement has already been seen:** `if(map.containsKey(num))` — if so, a valid pair has been found: the current index `i` and the previously-recorded index for `num` (`map.get(num)`) together sum to `target`. Return them immediately as `new int[]{i, map.get(num)}`.
  - **Otherwise**, this value hasn't formed a pair yet — record it for future iterations to check against: `map.put(nums[i], i)`.
- If the loop finishes without finding a pair, return an empty array (`new int[]{}`) as a fallback — though the problem guarantees exactly one valid answer always exists, so this line is never actually reached on valid input.

---

## Code

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {

        HashMap<Integer, Integer> map = new HashMap<>();
        int num;

        for(int i = 0 ; i<nums.length ; i++){
            num = target - nums[i];

            if(map.containsKey(num)){
                return new int[]{i,map.get(num)};
            }
            map.put(nums[i],i);

        }
        return new int[]{};
    }
}
```

---

## Dry Run

### Input

```text
nums = [3, 2, 4]
index:  0  1  2
target = 6
```

`map = {}`

| i | nums[i] | num = target - nums[i] | map.containsKey(num)? | Action | map after |
|---|---------|--------------------------------|-----------------------------|--------|-----------|
|0|3|6 - 3 = 3|no|`map.put(3, 0)`|`{3:0}`|
|1|2|6 - 2 = 4|no|`map.put(2, 1)`|`{3:0, 2:1}`|
|2|4|6 - 4 = 2|**yes** (index 1)|`return new int[]{2, 1}`|—|

**Return:** `[2, 1]`

Output:

```text
[2,1]
```

Note this differs in order from the problem's stated expected output of `[1,2]`, but the problem explicitly states *"you can return the answer in any order"* — `[2,1]` and `[1,2]` both correctly identify that `nums[1] + nums[2] = 2 + 4 = 6`, they just list the two indices in the opposite order (current index first, then the previously-stored index).

---

## Why This Approach Works

For any element `nums[i]` that's part of a valid answer pair, its partner must satisfy `nums[j] = target - nums[i]` — so rather than searching the rest of the array for that exact value every time (which would cost `O(n)` per element, `O(n²)` overall), the hash map lets that check become an `O(1)` average-case lookup, since every value encountered so far is already recorded with its index.

Checking `map.containsKey(num)` **before** inserting the current element (`map.put(nums[i], i)`) is what correctly enforces "may not use the same element twice": if `nums[i]` itself happened to equal `num` (i.e. its own complement), it wouldn't yet be in the map at the time of the check, so it can't accidentally pair with itself. By the time a later element could look it up, it will have been legitimately added — meaning any match found is always between two genuinely distinct indices.

Since the problem guarantees exactly one valid solution exists, the loop is guaranteed to find and return a match before ever reaching the fallback empty array at the end.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array; each iteration performs `O(1)` expected-time hash map operations (`containsKey`, `get`, `put`), satisfying the follow-up's requirement of beating `O(n²)`
- **Space Complexity:** `O(n)` — in the worst case, the hash map ends up storing nearly every element of the array before a match is found

---

## Key Insight

Whenever a problem asks "find a pair (or pairs) satisfying some sum/difference relationship," maintaining a hash map of "values seen so far → their index/position" as a single pass proceeds is the standard technique for turning an `O(n²)` brute-force pairwise check into an `O(n)` solution — trading the nested-loop search for constant-time average-case lookups.

---

## Concepts Used

- Array
- Hash Map
- Single-Pass Traversal
- Complement Lookup