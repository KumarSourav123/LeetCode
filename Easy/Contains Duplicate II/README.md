# LeetCode 219 - Contains Duplicate II

## Problem Statement

Given an integer array `nums` and an integer `k`, return `true` if there are two **distinct indices** `i` and `j` in the array such that `nums[i] == nums[j]` and `abs(i - j) <= k`.

---

## Example 1:
```
Input: nums = [1,2,3,1], k = 3
Output: true
```

## Example 2:
```
Input: nums = [1,0,1,1], k = 1
Output: true
```

## Example 3:
```
Input: nums = [1,2,3,1,2,3], k = 2
Output: false
```

## Constraints:
- `1 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`
- `0 <= k <= 10^5`

---

## Approach

This solution uses a **hash map to remember the most recent index each value was seen at**, scanning through the array once: for every element, it checks whether that same value was seen recently enough (within `k` positions) to satisfy the condition, and otherwise just records/updates where it was last seen.

### Idea

- Create `map`, a `HashMap<Integer, Integer>` mapping each value to the **most recent index** it was seen at.
- **Loop** `for(int i = 0; i < nums.length; i++)`:
  - **If `nums[i]` has been seen before** (`map.containsKey(nums[i])`):
    - Check the distance from the last seen index to the current one: `if((i - map.get(nums[i])) <= k)`. Since `i` only ever increases as the loop progresses, `i - map.get(nums[i])` is always non-negative, so this is equivalent to checking `abs(i - j) <= k` for the two indices.
    - If the distance qualifies, a valid pair has been found — return `true` immediately.
    - Otherwise, the previous occurrence is too far away to ever matter for a *future* index (since any future index would only be even farther from that old position). Update the map with the current, more recent index instead: `map.put(nums[i], i)`.
  - **If `nums[i]` hasn't been seen before**, simply record it: `map.put(nums[i], i)`.
- If the loop completes without ever finding a qualifying pair, no such pair exists — return `false`.

---

## Code

```java
class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for(int i = 0 ; i< nums.length ; i++){
            if(map.containsKey(nums[i])){
                if((i - map.get(nums[i])) <= k){
                    return true;
                }else{
                    map.put(nums[i] , i);
                }
            }else{
                map.put(nums[i], i);
            }
        }
        return false;
    }
}
```

---

## Dry Run

### Input

```text
nums = [1, 0, 1, 1]
index:  0  1  2  3
k = 1
```

`map = {}`

| i | nums[i] | map.containsKey(nums[i])? | Check / Action | map after |
|---|---------|-------------------------------|-------------------|-----------|
|0|1|no|`map.put(1, 0)`|`{1:0}`|
|1|0|no|`map.put(0, 1)`|`{1:0, 0:1}`|
|2|1|yes (last seen at index 0)|`(2 - 0) <= 1`? `2 <= 1` → false → `map.put(1, 2)`|`{1:2, 0:1}`|
|3|1|yes (last seen at index 2)|`(3 - 2) <= 1`? `1 <= 1` → **true** → `return true`|—|

**Return:** `true`

Output:

```text
true
```

This matches the expected output — the two `1`s at indices `2` and `3` are only `1` apart, satisfying `abs(i - j) <= k` for `k = 1`.

---

## Why This Approach Works

The problem only cares whether *some* pair of equal values exists within a window of `k` indices — it doesn't matter which specific earlier occurrence is used, as long as one within range exists. This is why it's always safe to overwrite the map with the **most recent** index for a value whenever an older occurrence turns out to be too far away: a more recent occurrence can only ever be *closer* to (or the same distance from) any future index than an older one, so keeping the freshest index strictly maximizes the chances of a future match — the older, out-of-range index could never have produced a valid pair that the updated, closer index wouldn't also produce.

Because `i` increases monotonically through the loop, `i - map.get(nums[i])` is guaranteed non-negative whenever it's computed, which is what allows the simpler `<= k` check to stand in for the problem's `abs(i - j) <= k` condition without needing an explicit absolute value.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array; each iteration does `O(1)` expected work for the hash map lookup, get, and put operations
- **Space Complexity:** `O(min(n, k))` in practice — the map holds at most one entry per distinct value, and since any value's index is overwritten once it falls more than `k` positions behind, at most `k+1` entries are ever "relevant" at a time (though in the worst case with no overwrites this is bounded by `O(n)`, the number of distinct values in the array)

---

## Key Insight

A hash map that stores "the most recent index seen" (rather than, say, a list of all indices) is often sufficient — and preferable — whenever a problem only requires finding *any* qualifying match rather than enumerating every match, since older stale entries can be safely discarded the moment they fall out of range, keeping both the lookup logic and the memory footprint minimal.

---

## Concepts Used

- Array
- Hash Map
- Single-Pass Traversal
- Sliding Window (conceptually, via the `k`-distance constraint)