# LeetCode 57 - Insert Interval

## Problem Statement

You are given an array of **non-overlapping intervals** `intervals` where `intervals[i] = [start_i, end_i]` represent the start and end of the ith interval, and `intervals` is sorted in ascending order by `start_i`.

You are also given another interval `newInterval = [start, end]` that represents the start and end of another interval.

Insert `newInterval` into `intervals` such that `intervals` is still sorted in ascending order by `start`, and `intervals` still does not have any overlapping intervals (merge overlapping intervals if necessary).

Return `intervals` after the insertion.

---

## Example 1:

```
Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
Output: [[1,5],[6,9]]
Explanation: The new interval [2,5] overlaps with [1,3], so they are merged into [1,5].
```

## Example 2:

```
Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
Output: [[1,2],[3,10],[12,16]]
Explanation: The new interval [4,8] overlaps with [3,5], [6,7], and [8,10], so they are merged into [3,10].
```

## Example 3:

```
Input: intervals = [[1,5]], newInterval = [0,3]
Output: [[0,5]]
Explanation: The new interval starts before the existing interval and overlaps with it, so they merge into [0,5].
```

## Constraints:

* `0 <= intervals.length <= 10^4`
* `intervals[i].length == 2`
* `0 <= start_i <= end_i <= 10^5`
* `intervals` is sorted by `start_i` in ascending order.
* `newInterval.length == 2`

---

## Approach

This solution processes the intervals in **three phases**:

1. Add all intervals that come **before** `newInterval`.
2. Merge all intervals that **overlap** with `newInterval`.
3. Add all intervals that come **after** `newInterval`.

### Idea

* Create an `ArrayList<int[]>` to store the result.
* Traverse the intervals using an index `i`.
* First, add every interval whose end is **less than** `newInterval[0]`. These intervals cannot overlap with `newInterval`.
* Then, while intervals overlap with `newInterval` (`interval[0] <= newInterval[1]`), merge them by updating:

  * `newInterval[0] = Math.min(newInterval[0], interval[0])`
  * `newInterval[1] = Math.max(newInterval[1], interval[1])`
* After merging all overlapping intervals, add the updated `newInterval` to the result.
* Finally, add the remaining intervals that start after the merged interval.
* Convert the `ArrayList<int[]>` back into `int[][]`.

---

## Code

```java
import java.util.*;

class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {

        ArrayList<int[]> list = new ArrayList<>();
        int count = 0;

        for(int[] interval : intervals){

            if(count == 0){
                if(newInterval[1] < interval[0]){
                    list.add(newInterval);
                    list.add(interval);
                    count = 1;
                }else if(newInterval[1] >= interval[0] && newInterval[0] <= interval[1]){
                    int a = Math.min(interval[0] , newInterval[0]);
                    int b = Math.max(interval[1] , newInterval[1]);
                    list.add(new int[]{a,b});
                    count = 1;
                }else{
                    list.add(interval);
                }
            }else{
                if(list.isEmpty() || interval[0] > list.get(list.size()-1)[1] ){
                    list.add(interval);
                }else{
                    list.get(list.size()-1)[1] = Math.max(list.get(list.size()-1)[1], interval[1]);
                }
            }
        }

        if(count == 0){
            list.add(newInterval);
        }

        return list.toArray(new int[list.size()][]);
    }
}
```

---

## Dry Run

### Input

```text
intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]]
newInterval = [4,8]
```

| Step    | Condition | Action                 | Result               |
| ------- | --------- | ---------------------- | -------------------- |
| [1,2]   | 2 < 4     | Add directly           | [[1,2]]              |
| [3,5]   | 3 <= 8    | Merge with newInterval | newInterval = [3,8]  |
| [6,7]   | 6 <= 8    | Merge                  | newInterval = [3,8]  |
| [8,10]  | 8 <= 8    | Merge                  | newInterval = [3,10] |
| [12,16] | 12 > 10   | Stop merging           | Add merged interval  |

Final Result:

```text
[[1,2],[3,10],[12,16]]
```

This matches the expected output.

---

## Why This Approach Works

Because the intervals are already **sorted and non-overlapping**, every interval belongs to exactly one of three groups:

* Completely before `newInterval`
* Overlapping with `newInterval`
* Completely after `newInterval`

This guarantees that a **single linear scan** is sufficient.

---

## Complexity Analysis

* **Time Complexity:** `O(n)` — each interval is processed at most once.
* **Space Complexity:** `O(n)` — for the `ArrayList<int[]>` storing the final intervals.

---

## Key Insight

The important observation is that **only the intervals overlapping with `newInterval` need to be merged**. All intervals before it and after it can be added directly, which allows the entire problem to be solved in a single pass.

---

## Concepts Used

* Array
* ArrayList
* Interval Merging
* Greedy
* Linear Traversal