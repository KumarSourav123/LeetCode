# 11. Container With Most Water

## Problem Statement
You are given an integer array `height`, where each element represents the height of a vertical line.

Find two lines that, together with the x-axis, form a container that holds the maximum amount of water.

---

## Approach (Two Pointer)

- Place one pointer at the beginning (`left`) and one at the end (`right`).
- Calculate the current area:
  - **Area = min(height[left], height[right]) × (right - left)**
- Update the maximum area if needed.
- Move the pointer having the **smaller height** because:
  - The shorter line limits the container's height.
  - Moving the taller line cannot increase the height, but it decreases the width.
- Continue until both pointers meet.

---

## Algorithm

1. Initialize:
   - `left = 0`
   - `right = n - 1`
   - `maxArea = 0`
2. While `left < right`
   - Calculate the area.
   - Update `maxArea`.
   - Move the pointer with the smaller height.
3. Return `maxArea`.

---

## Java Solution

```java
class Solution {
    public int maxArea(int[] height) {
        int s = 0;
        int e = height.length - 1;
        int maximumArea = 0;

        while (s < e) {
            int area = Math.min(height[s], height[e]) * (e - s);
            maximumArea = Math.max(maximumArea, area);

            if (height[s] < height[e]) {
                s++;
            } else {
                e--;
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
```

| Left | Right | Heights | Width | Area | Max Area | Move |
|------|-------|---------|------|------|----------|------|
|0|8|1,7|8|8|8|Left++|
|1|8|8,7|7|49|49|Right--|
|1|7|8,3|6|18|49|Right--|
|1|6|8,8|5|40|49|Right--|
|1|5|8,4|4|16|49|Right--|
|1|4|8,5|3|15|49|Right--|
|1|3|8,2|2|4|49|Right--|
|1|2|8,6|1|6|49|Right--|

**Answer = 49**

---

## Time Complexity

- **O(n)**

Each pointer moves at most `n` times.

---

## Space Complexity

- **O(1)**

Only constant extra space is used.

---

## Why This Works

The container's area depends on:

- **Height = minimum of the two lines**
- **Width = distance between them**

Since the shorter line limits the area, moving the taller line can never increase the height but always decreases the width. Therefore, the only useful move is to advance the pointer pointing to the shorter line.

---

## My Solution

- ✅ Approach: Two Pointer
- ✅ Time Complexity: **O(n)**
- ✅ Space Complexity: **O(1)**
- ✅ Optimal Solution