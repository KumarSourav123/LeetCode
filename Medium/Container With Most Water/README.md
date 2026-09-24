# LeetCode 11 - Container With Most Water

## Problem Statement

You are given an integer array `height` of length `n`. There are `n` vertical lines drawn such that the two endpoints of the `ith` line are `(i, 0)` and `(i, height[i])`.

Find two lines that together with the x-axis form a container, such that the container contains the most water.

Return *the maximum amount of water a container can store*.

**Notice** that you may not slant the container.

---

## Example 1:
```
Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case, the max area of water (blue section) the container can contain is 49.
```

## Example 2:
```
Input: height = [1,1]
Output: 1
```

## Constraints:
- `n == height.length`
- `2 <= n <= 10^5`
- `0 <= height[i] <= 10^4`

---

## Approach

This solution uses the classic **two-pointer inward-closing** technique: start with the widest possible container (the two outermost lines) and repeatedly move whichever pointer sits at the **shorter** line inward, since that's the only move that has any chance of finding a taller container to compensate for the width lost.

### Idea

- Start `s = 0` (left pointer) and `e = height.length - 1` (right pointer) — the widest possible container.
- Track `area` (the current container's size) and `maximumArea` (the best seen so far), both starting at `0`.
- **Loop** `while(s <= e)`:
  - The container's height is limited by the **shorter** of the two lines (water can't rise above the shorter wall), so compare `height[s]` and `height[e]`:
    - If `height[s] <= height[e]`, the left line is the limiting height: `area = height[s] * (e - s)`, then move the left pointer inward (`s++`) — since the left line was the bottleneck, keeping it in place while shrinking the width could only ever produce an equal-or-worse result, so there's no value in re-testing it against anything else.
    - Otherwise, the right line is the limiting (or equal) height: `area = height[e] * (e - s)`, then move the right pointer inward (`e--`) by the same logic.
  - After computing `area`, check if it beats the running best: `if(area > maximumArea) maximumArea = area;`.
- Once `s` and `e` cross, every possible pair has effectively been accounted for by this narrowing process — return `maximumArea`.

---

## Code

```java
class Solution {
    public int maxArea(int[] height) {
        int s = 0;
        int e = height.length-1;
        int area = 0;
        int maximumArea = 0;

        while(s <= e){
            if(height[s] <= height[e]){
                area = height[s] * (e-s);
                s++;
            }else{
                area = height[e] * (e-s);
                e--;
            }

            if(area > maximumArea){
                maximumArea = area;
            }
        }
        return maximumArea;
    }
}
```

---

## Dry Run

### Input

```text
height = [1,8,6,2,5,4,8,3,7]
index:    0 1 2 3 4 5 6 7 8
```

`s = 0`, `e = 8`, `area = 0`, `maximumArea = 0`

| s | e | height[s] | height[e] | Comparison | area = ... | New maximumArea | Pointer move |
|---|---|-----------|-----------|------------|------------|-------------------|--------------|
|0|8|1|7|`1 <= 7` → true|`1 * (8-0) = 8`|8|`s++` → s=1|
|1|8|8|7|`8 <= 7` → false|`7 * (8-1) = 49`|**49**|`e--` → e=7|
|1|7|8|3|`8 <= 3` → false|`3 * (7-1) = 18`|49 (unchanged)|`e--` → e=6|
|1|6|8|8|`8 <= 8` → true|`8 * (6-1) = 40`|49 (unchanged)|`s++` → s=2|
|2|6|6|8|`6 <= 8` → true|`6 * (6-2) = 24`|49 (unchanged)|`s++` → s=3|
|3|6|2|8|`2 <= 8` → true|`2 * (6-3) = 6`|49 (unchanged)|`s++` → s=4|
|4|6|5|8|`5 <= 8` → true|`5 * (6-4) = 10`|49 (unchanged)|`s++` → s=5|
|5|6|4|8|`4 <= 8` → true|`4 * (6-5) = 4`|49 (unchanged)|`s++` → s=6|
|6|6|8|8|`8 <= 8` → true|`8 * (6-6) = 0`|49 (unchanged)|`s++` → s=7|

**Loop check:** `s(7) <= e(6)`? false → loop exits.

**Return:** `maximumArea = 49`

Output:

```text
49
```

This matches the expected output — achieved at the step where `s=1` (height 8) and `e=8` (height 7), giving `7 * 7 = 49`.

---

## Why This Approach Works

Starting from the widest possible container (`s=0`, `e=n-1`) and only ever narrowing means every candidate pair gets implicitly considered without needing to check all `O(n²)` pairs explicitly. The key correctness argument is in *which* pointer moves at each step: at any given `(s, e)`, the container's area is capped by `min(height[s], height[e]) * (e - s)`. If, say, `height[s]` is the shorter (limiting) side, then pairing that same left line with *any* line further right than `e` would only decrease the width while the height cap stays at `height[s]` or worse — so no pairing of the current `s` with an index further inward than `e` could ever beat what's already been computed. That means it's always safe to permanently retire the shorter line's pointer and move it inward, since every "better" candidate involving the taller line has already been captured by the current computation or will be captured by future ones.

Because each step retires exactly one pointer's current position for good, and there are `n` starting positions total, the two pointers are guaranteed to meet after at most `n` steps — having implicitly ruled out every pairing that couldn't possibly be optimal along the way.

One small efficiency note: the loop condition `s <= e` allows one extra iteration where `s == e` (a container of width `0`, contributing `area = 0`). This is harmless since it can never update `maximumArea`, but `s < e` would be the tighter, more idiomatic condition and would skip that guaranteed-useless final comparison.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — `s` and `e` move toward each other, with each step advancing exactly one of them by one position, so the loop runs at most `n` times total
- **Space Complexity:** `O(1)` — only a fixed set of integer variables is used, no auxiliary arrays or data structures

---

## Key Insight

This is a textbook example of the **two-pointer inward-closing** pattern for problems involving pairs from an array where the answer is bounded by `min(...)` of the pair combined with their distance apart. The crucial insight that makes the greedy pointer movement provably safe — rather than just a heuristic — is that the shorter line is *always* the bottleneck for its current pairing, so keeping it fixed while shrinking the width can never help, making it always correct to advance past it.

---

## Concepts Used

- Array
- Two Pointers (Inward-Closing)
- Greedy Elimination
- Constant Space Algorithm