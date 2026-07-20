# LeetCode 75 - Sort Colors

## Problem Statement

Given an array `nums` with `n` objects colored red, white, or blue, sort them **in-place** so that objects of the same color are adjacent, with the colors in the order red, white, and blue.

We will use the integers `0`, `1`, and `2` to represent the color red, white, and blue, respectively.

You must solve this problem **without using the library's sort function**.

---

## Example 1:
```
Input: nums = [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]
```

## Example 2:
```
Input: nums = [2,0,1]
Output: [0,1,2]
```

## Constraints:
- `n == nums.length`
- `1 <= n <= 300`
- `nums[i]` is either `0`, `1`, or `2`

## Follow up:
Could you come up with a one-pass algorithm using only constant extra space?

---

## Approach

This solution uses **three pointers (`zero`, `one`, `two`)** to partition the array into three regions in a single pass, similar in spirit to the **Dutch National Flag algorithm**.

### Idea

- First, find the index of the first non-zero element and initialize `zero`, `one`, and `two` to that index.
- While `one` is within bounds:
  - If `zero` is still within bounds, check `nums[zero]`:
    - If it's not `0`, advance `zero`.
    - Otherwise, swap it with `nums[two]` and advance both `zero` and `two`.
  - Sync `one` to `two` after handling the zero region.
  - Once `zero` moves past the array, switch to placing `1`s and `2`s correctly by checking `nums[two]` and `nums[one]` and swapping when a `2` needs to move past a `1`.

The pointers work together to push all `0`s to the front, `1`s to the middle, and `2`s to the end.

---

## Code

```java
class Solution {
    public void sortColors(int[] nums) {
        int zero = 0;
        int one = 0;
        int two = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                zero = i;
                one = i;
                two = i;
                break;
            }
        }

        while (one < nums.length) {
            if (zero < nums.length) {

                if (nums[zero] != 0) {
                    zero++;
                } else {
                    int temp = nums[two];
                    nums[two] = nums[zero];
                    nums[zero] = temp;
                    zero++;
                    two++;
                }

                one = two;
            } else {
                if ((nums[two] == 2) && (nums[one] == 1)) {
                    int temp = nums[two];
                    nums[two] = nums[one];
                    nums[one] = temp;
                    one++;
                    two++;
                } else {
                    one++;
                    if (nums[two] == 1) {
                        two++;
                    }
                }
            }
        }
    }
}
```

---

## Dry Run

### Input

```text
nums = [2,0,2,1,1,0]
```

**Initial setup:** First non-zero element is at index `0` (`nums[0] = 2`), so `zero = one = two = 0`.

| Step | zero | one | two | Array State | Action |
|------|------|-----|-----|-------------|--------|
|1|0→1|0|0|[2,0,2,1,1,0]|nums[zero]=2≠0 → zero++|
|2|1→2|0→1|0→1|[0,2,2,1,1,0]|nums[zero]=0 → swap(zero,two) → zero++, two++|
|3|2→3|1|1|[0,2,2,1,1,0]|nums[zero]=2≠0 → zero++|
|4|3→4|1|1|[0,2,2,1,1,0]|nums[zero]=1≠0 → zero++|
|5|4→5|1|1|[0,2,2,1,1,0]|nums[zero]=1≠0 → zero++|
|6|5→6|1→2|1→2|[0,0,2,1,1,2]|nums[zero]=0 → swap(zero,two) → zero++, two++|
|7|-|2→3|2|[0,0,2,1,1,2]|zero≥length → check nums[two]=2, nums[one]=2 → no match → one++|
|8|-|3→4|2→3|[0,0,1,2,1,2]|nums[two]=2, nums[one]=1 → swap(two,one) → one++, two++|
|9|-|4→5|3→4|[0,0,1,1,2,2]|nums[two]=2, nums[one]=1 → swap(two,one) → one++, two++|
|10|-|5→6|4|[0,0,1,1,2,2]|nums[two]=2, nums[one]=2 → no match → one++|

Loop ends when `one = 6` (`one < nums.length` is false).

Final Array:

```text
[0, 0, 1, 1, 2, 2]
```

---

## Why This Approach Works

Every element the algorithm scans is placed into one of three logical partitions — zeros, ones, and twos — through pointer movement and swapping rather than a comparison-based sort.

Because `zero` always finds and relocates `0`s to the front, and the `one`/`two` phase resolves the ordering of `1`s and `2`s afterward, the array becomes fully partitioned in a **single overall pass without using extra data structures**, satisfying the constant-space follow-up requirement.

---

## Complexity Analysis

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(1)`

---

## Key Insight

Instead of sorting the array using a general-purpose comparison sort (`O(n log n)`), tracking three pointers lets us partition the array into three known value groups (`0`, `1`, `2`) in a single linear pass, achieving `O(n)` time with `O(1)` extra space.

---

## Concepts Used

- Array
- Two/Three Pointers
- In-Place Swapping
- Dutch National Flag Algorithm
- Partitioning
- Constant Space Algorithm