# LeetCode 56 - Merge Intervals

## Problem Statement

Given an array of `intervals` where `intervals[i] = [start_i, end_i]`, merge all overlapping intervals, and return *an array of the non-overlapping intervals that cover all the intervals in the input*.

---

## Example 1:
```
Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
```

## Example 2:
```
Input: intervals = [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.
```

## Example 3:
```
Input: intervals = [[4,7],[1,4]]
Output: [[1,7]]
Explanation: Intervals [1,4] and [4,7] are considered overlapping.
```

## Constraints:
- `1 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= start_i <= end_i <= 10^4`

---

## Approach

This solution **sorts the intervals by their start time**, then performs a **single linear scan**, merging each interval into the last one in the result if they overlap.

### Idea

- Sort `intervals` by their starting value (`interval[0]`) in ascending order using a custom comparator. This ensures that any interval that could overlap with a previous one will always be encountered immediately after it, never before.
- Maintain a `list` of merged, non-overlapping intervals built so far.
- For each `interval` in the sorted array:
  - If `list` is empty, or the current interval's start (`interval[0]`) is **greater than** the end of the last merged interval in `list`, there's no overlap — simply add this interval to `list` as a new, separate interval.
  - Otherwise, the current interval **overlaps** with the last one in `list`, so merge them by updating the last interval's end value to `Math.max(interval[1], list.get(list.size()-1)[1])` — extending it to cover whichever end value is larger.
- Finally, convert the `list` (an `ArrayList<int[]>`) back into a plain `int[][]` array to return.

---

## Code

```java
class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        ArrayList<int[]> list = new ArrayList<>();

        for (int[] interval : intervals) {
            if (list.isEmpty() || interval[0] > list.get(list.size() - 1)[1]) {
                list.add(interval);
            } else {
                list.get(list.size() - 1)[1] = Math.max(interval[1], list.get(list.size() - 1)[1]);
            }
        }
        return list.toArray(new int[list.size()][]);
    }
}
```

---

## Dry Run

### Input

```text
intervals = [[1,3],[2,6],[8,10],[15,18]]
```

Already sorted by start value.

| interval | list.isEmpty() OR interval[0] > last end? | Action | list after step |
|----------|------------------------------------------------|--------|--------------------|
|[1,3]|list is empty|Add as new interval|[[1,3]]|
|[2,6]|2 > 3? No (overlaps)|Merge: last end = max(6,3) = 6|[[1,6]]|
|[8,10]|8 > 6? Yes|Add as new interval|[[1,6],[8,10]]|
|[15,18]|15 > 10? Yes|Add as new interval|[[1,6],[8,10],[15,18]]|

Final Output:

```text
[[1,6],[8,10],[15,18]]
```

This matches the expected output.

---

## Why This Approach Works

Sorting by start time guarantees that once an interval is processed, no later interval in the sorted order can have a start value smaller than any interval already added to `list` — so the only interval that a new one could possibly overlap with is the **most recently added** one in `list`.

This means checking and merging against just `list.get(list.size() - 1)` (the last merged interval) is always sufficient; there's never a need to look further back, since any overlap with an earlier interval would already have been captured by the merge with the interval immediately before it.

---

## Complexity Analysis

- **Time Complexity:** `O(n log n)` — dominated by the initial sort; the merging scan itself is `O(n)`
- **Space Complexity:** `O(n)` — for the `list` storing merged intervals (plus whatever space the sort uses internally)

---

## Key Insight

Sorting by start time transforms an otherwise complex interval-overlap problem into a simple linear scan: since overlaps can only ever occur between an interval and its immediate predecessor in sorted order, merging becomes a matter of comparing against just the last entry added to the result, rather than checking against every previously seen interval.

---

## Concepts Used

- Array
- Sorting
- Custom Comparator
- Interval Merging
- Greedy