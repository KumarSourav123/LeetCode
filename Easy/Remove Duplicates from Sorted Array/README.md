# LeetCode 26 - Remove Duplicates from Sorted Array

## Problem Statement

Given an integer array `nums` sorted in **non-decreasing order**, remove the duplicates **in-place** such that each unique element appears only once. The relative order of the elements should be kept the same. Then return *the number of unique elements in* `nums`.

Consider the number of unique elements of `nums` to be `k`. To get accepted, you need to do the following things:

- Change the array `nums` such that the first `k` elements of `nums` contain the unique elements in the order they were present in `nums` initially. The remaining elements of `nums` are not important as well as the size of `nums`.
- Return `k`.

**Custom Judge:**

The judge will test your solution with the following code:

```
int[] nums = [...]; // Input array
int[] expectedNums = [...]; // The expected answer with correct length

int k = removeDuplicates(nums); // Calls your implementation

assert k == expectedNums.length;
for (int i = 0; i < k; i++) {
    assert nums[i] == expectedNums[i];
}
```

If all assertions pass, then your solution will be accepted.

---

## Example 1:
```
Input: nums = [1,1,2]
Output: 2, nums = [1,2,_]
Explanation: Your function should return k = 2, with the first two elements of nums being 1 and 2 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
```

## Example 2:
```
Input: nums = [0,0,1,1,1,2,2,3,3,4]
Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
Explanation: Your function should return k = 5, with the first five elements of nums being 0, 1, 2, 3, and 4 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
```

## Constraints:
- `1 <= nums.length <= 3 * 10^4`
- `-100 <= nums[i] <= 100`
- `nums` is sorted in **non-decreasing** order.

---

## Approach

This solution uses **two pointers**, a "slow" pointer `s` marking the last confirmed-unique position and a "fast" pointer `f` scanning ahead for the next genuinely new value, swapping values into place as it goes rather than just overwriting them.

### Idea

- Initialize `s = 0` (points at the most recently placed unique value) and `f = 1` (scans ahead looking for the next distinct value).
- **Loop** `while(f < nums.length)`:
  - **If `nums[s] != nums[f]`**, a new distinct value has been found at `f`. Advance `s` first (`s++`), then **swap** `nums[s]` and `nums[f]` — placing the new unique value into the next available "unique" slot, while the value previously sitting there gets moved out to where `f` currently is (harmless, since the problem doesn't care what's left beyond index `k-1`).
  - Either way (match or not), `f++` — always continue scanning forward.
- Because `nums` is sorted, `nums[s]` always ends up holding the most recently confirmed unique value, so comparing it against `nums[f]` is enough to detect the next new value without needing to check against every previously-seen value.
- Once the loop finishes, `s` is the index of the **last** unique value placed. Since `s` is zero-indexed, the total count of unique elements is `s + 1` — this is what's returned.

---

## Code

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int s = 0;
        int f = 1;
        while(f < nums.length){
            if(nums[s] != nums[f]){
                s++;
                int temp = nums[s];
                nums[s] = nums[f];
                nums[f] = temp;
            }
            f++;
        }
        return s+1;
    }
}
```

---

## Dry Run

### Input

```text
nums = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]
index:  0  1  2  3  4  5  6  7  8  9
```

`s = 0`, `f = 1`

| f | nums[s] | nums[f] | Equal? | Action | Array after | s after |
|---|---------|---------|--------|--------|-------------|---------|
|1|0|0|yes|—|`[0,0,1,1,1,2,2,3,3,4]`|0|
|2|0|1|no|`s++; swap nums[1]&nums[2]`|`[0,1,0,1,1,2,2,3,3,4]`|1|
|3|1|1|yes|—|`[0,1,0,1,1,2,2,3,3,4]`|1|
|4|1|1|yes|—|`[0,1,0,1,1,2,2,3,3,4]`|1|
|5|1|2|no|`s++; swap nums[2]&nums[5]`|`[0,1,2,1,1,0,2,3,3,4]`|2|
|6|2|2|yes|—|`[0,1,2,1,1,0,2,3,3,4]`|2|
|7|2|3|no|`s++; swap nums[3]&nums[7]`|`[0,1,2,3,1,0,2,1,3,4]`|3|
|8|3|3|yes|—|`[0,1,2,3,1,0,2,1,3,4]`|3|
|9|3|4|no|`s++; swap nums[4]&nums[9]`|`[0,1,2,3,4,0,2,1,3,1]`|4|

**Loop check:** `f(10) < nums.length(10)`? false → loop exits.

**Return:** `s + 1 = 4 + 1 = 5`

Output:

```text
5, nums = [0,1,2,3,4,_,_,_,_,_]
```

This matches the expected output — `k = 5`, and the first 5 elements of the modified array are exactly `[0,1,2,3,4]` in their original relative order. What's left beyond index `4` (`[0,2,1,3,1]` in this trace) doesn't matter, per the problem's own rules.

---

## Why This Approach Works

Because `nums` is sorted in non-decreasing order, all occurrences of any given value are guaranteed to be **consecutive**. This means detecting "is this a new distinct value?" only ever requires comparing against the single most recently confirmed unique value (`nums[s]`) — there's no need to check against a running set of all previously seen values, since a value can't reappear after a different value has already appeared (sortedness rules that out).

Using a **swap** rather than a plain overwrite (`nums[s] = nums[f]`) is a subtle but deliberate choice: it means the values displaced from the "unique" prefix aren't lost or overwritten destructively — they're relocated further back in the array. Since the problem only requires the first `k` elements to be correct and explicitly doesn't care what's left beyond that, a plain overwrite would have worked just as well here; the swap is simply a stylistic in-place technique that avoids any risk of losing information, useful in variants of this pattern where preserving the full original set of values (just reordered) matters.

The final answer `s + 1` follows directly from `s` being a zero-indexed pointer to the last placed unique element — converting a zero-indexed position into a count just requires adding one.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the array; `f` advances by one every iteration regardless of which branch is taken
- **Space Complexity:** `O(1)` — only a fixed set of index/temp variables is used; all rearrangement happens in place within the original array

---

## Key Insight

This is the standard **slow/fast two-pointer in-place deduplication** pattern for sorted arrays: the slow pointer always marks the boundary of the "confirmed unique" region, and the fast pointer scans ahead, only triggering an update when it finds something genuinely new — relying entirely on the array's sortedness to make a single-value comparison sufficient for detecting duplicates, rather than needing a hash set or any extra memory.

---

## Concepts Used

- Array
- Two Pointers (Slow & Fast)
- In-Place Deduplication
- Sorted Array Traversal
- Constant Space Algorithm