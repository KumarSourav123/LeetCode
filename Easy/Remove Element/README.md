# LeetCode 27 - Remove Element

## Problem Statement

Given an integer array `nums` and an integer `val`, remove all occurrences of `val` in `nums` **in-place**. The order of the elements may be changed. Then return *the number of elements in* `nums` *which are not equal to* `val`.

Consider the number of elements in `nums` which are not equal to `val` be `k`, to get accepted, you need to do the following things:

- Change the array `nums` such that the first `k` elements of `nums` contain the elements which are not equal to `val`. The remaining elements of `nums` are not important as well as the size of `nums`.
- Return `k`.

**Custom Judge:**

The judge will test your solution with the following code:

```
int[] nums = [...]; // Input array
int val = ...; // Value to remove
int[] expectedNums = [...]; // The expected answer with correct length.
                             // It is sorted with no values equaling val.

int k = removeElement(nums, val); // Calls your implementation

assert k == expectedNums.length;
sort(nums, 0, k); // Sort the first k elements of nums
for (int i = 0; i < actualLength; i++) {
    assert nums[i] == expectedNums[i];
}
```

If all assertions pass, then your solution will be accepted.

---

## Example 1:
```
Input: nums = [3,2,2,3], val = 3
Output: 2, nums = [2,2,_,_]
Explanation: Your function should return k = 2, with the first two elements of nums being 2.
It does not matter what you leave beyond the returned k (hence they are underscores).
```

## Example 2:
```
Input: nums = [0,1,2,2,3,0,4,2], val = 2
Output: 5, nums = [0,1,4,0,3,_,_,_]
Explanation: Your function should return k = 5, with the first five elements of nums containing 0, 0, 1, 3, and 4.
Note that the five elements can be returned in any order.
It does not matter what you leave beyond the returned k (hence they are underscores).
```

## Constraints:
- `0 <= nums.length <= 100`
- `0 <= nums[i] <= 50`
- `0 <= val <= 100`

---

## Approach

This solution uses **two pointers converging from opposite ends** of the array: `s` scans forward looking for occurrences of `val`, and `h` marks the current "back boundary." Whenever `s` finds a `val`, it's swapped with whatever is currently at the back (`h`), effectively pulling an unprocessed value in from the end while pushing the unwanted `val` toward the back.

### Idea

- Initialize `s = 0` (scans forward from the start) and `h = nums.length - 1` (marks the current last valid position).
- **Loop** `while(s <= h)`:
  - **If `nums[s] == val`**, this position holds a value that needs removing. Rather than shifting every subsequent element left (which would be `O(n)` per removal), swap it with whatever currently sits at the back boundary `h`: `nums[s]` and `nums[h]` are exchanged, then `h--` shrinks the "active" region by one, effectively discarding what's now at the old `h` position (which used to be the val, now moved to the back and no longer considered part of the active range). Note `s` is **not** advanced here — the value just swapped into position `s` (formerly at `h`) hasn't been checked yet, so it needs to be examined on the next iteration.
  - **Otherwise** (`nums[s] != val`), this element is fine as-is — advance past it: `s++`.
- The loop continues until `s` and `h` cross, at which point every position from `0` to `s-1` has been confirmed to not equal `val`.
- Return `s` — since `s` is the count of positions confirmed valid (it only advances past confirmed non-`val` elements), it directly equals `k`, the required count.

---

## Code

```java
class Solution {
    public int removeElement(int[] nums, int val) {
        int s = 0;
        int h = nums.length-1;
        while(s <= h){
            if(nums[s] == val){
                int temp = nums[s];
                nums[s] = nums[h];
                nums[h] = temp;
                h--;
            }else{
                s++;
            }
        }
        return s;
    }
}
```

---

## Dry Run

### Input

```text
nums = [3, 2, 2, 3]
index:  0  1  2  3
val = 3
```

`s = 0`, `h = 3`

| s | h | nums[s] | == val? | Action | Array after | s / h after |
|---|---|---------|---------|--------|-------------|--------------------|
|0|3|3|yes|swap `nums[0]` & `nums[3]` (both value 3), `h--`|`[3,2,2,3]` (unchanged in value, both swapped elements were 3)|0, 2|
|0|2|3|yes|swap `nums[0]` & `nums[2]`, `h--`|`[2,2,3,3]`|0, 1|
|0|1|2|no|`s++`|`[2,2,3,3]`|1, 1|
|1|1|2|no|`s++`|`[2,2,3,3]`|2, 1|

**Loop check:** `s(2) <= h(1)`? false → loop exits.

**Return:** `s = 2`

Output:

```text
2, nums = [2,2,_,_]
```

This matches the expected output — `k = 2`, and the first two elements of the modified array (`[2,2,3,3]`) are both `2`, correctly containing only the values not equal to `val = 3`. What's left beyond index `1` doesn't matter, per the problem's own rules.

---

## Why This Approach Works

The invariant maintained is: everything at index `< s` has already been confirmed to **not** equal `val`, and everything at index `> h` has already been fully processed and pushed to the "discard" region at the back (whether it happened to equal `val` or not — since a swap moves *some* value there, not necessarily a `val` itself). The region between `s` and `h` (inclusive) is what's still unexamined.

When `nums[s]` equals `val`, swapping it with `nums[h]` accomplishes two things at once: it removes the unwanted value from position `s` (replacing it with whatever was at the back, an as-yet-unexamined value), and it shrinks the active region by one from the back (`h--`), since the position that used to hold `nums[h]` no longer needs consideration — the `val` that was at `s` now effectively occupies that discarded slot. Crucially, `s` doesn't advance in this case, because the newly swapped-in value at `s` hasn't been checked yet — it could itself be `val`, and needs its own turn through the loop.

Because the order of elements doesn't matter (as explicitly stated in the problem), this "swap with the back and shrink" approach is valid, and it moves at most `O(n)` total elements rather than potentially `O(n)` work *per* removed element (which a naive shift-left approach would incur).

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — the region between `s` and `h` strictly shrinks by exactly one position every single iteration (either `s` advances or `h` retreats), so the total number of iterations is bounded by the array's length
- **Space Complexity:** `O(1)` — only a fixed set of index variables and one `temp` variable are used; the rearrangement happens entirely in place within the original array

---

## Key Insight

When an array-modification problem explicitly states that element order doesn't matter (as this one does), a "swap with the back and shrink the active range" technique is often more efficient than a "shift everything left" approach, since it avoids the `O(n)` cost of shifting on every single match — each element is moved at most once, giving a true `O(n)` total, regardless of how many occurrences of `val` exist or where they're located.

---

## Concepts Used

- Array
- Two Pointers (Converging from Opposite Ends)
- In-Place Removal via Swap-and-Shrink
- Constant Space Algorithm