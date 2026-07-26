# LeetCode 54 - Spiral Matrix

## Problem Statement

Given an `m x n` `matrix`, return all elements of the `matrix` in spiral order.

---

## Example 1:
```
Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [1,2,3,6,9,8,7,4,5]
```

## Example 2:
```
Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
Output: [1,2,3,4,8,12,11,10,9,5,6,7]
```

## Constraints:
- `m == matrix.length`
- `n == matrix[i].length`
- `1 <= m, n <= 10`
- `-100 <= matrix[i][j] <= 100`

---

## Approach

This solution uses **four boundary pointers** (`left`, `right`, `top`, `bottom`) that shrink inward after each full side of the spiral is traversed.

### Idea

- Maintain four boundaries marking the current unvisited rectangular region of the matrix: `left`, `right`, `top`, and `bottom`.
- While the boundaries still form a valid region (`left <= right` and `top <= bottom`), traverse one full "ring" of the spiral in four steps:
  1. **Traverse left to right** along the `top` row, then move `top` down by one (that row is now visited).
  2. **Traverse top to bottom** along the `right` column, then move `right` left by one (that column is now visited).
  3. If `top <= bottom` still holds (there's a remaining row to traverse), **traverse right to left** along the `bottom` row, then move `bottom` up by one.
  4. If `left <= right` still holds (there's a remaining column to traverse), **traverse bottom to top** along the `left` column, then move `left` right by one.
- The extra `if(top <= bottom)` and `if(left <= right)` checks before the third and fourth steps prevent re-processing a row or column that's already been fully covered by earlier steps in the same ring (which matters for matrices that are a single row or single column wide).

---

## Code

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        ArrayList<Integer> list = new ArrayList<>();

        int left = 0;
        int right = matrix[0].length - 1;
        int top = 0;
        int bottom = matrix.length - 1;

        while ((left <= right) && (top <= bottom)) {
            for (int i = left; i <= right; i++) {
                list.add(matrix[top][i]);
            }
            top++;

            for (int i = top; i <= bottom; i++) {
                list.add(matrix[i][right]);
            }
            right--;

            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    list.add(matrix[bottom][i]);
                }
                bottom--;
            }

            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    list.add(matrix[i][left]);
                }
                left++;
            }
        }

        return list;
    }
}
```

---

## Dry Run

### Input

```text
matrix = [[1,2,3],
          [4,5,6],
          [7,8,9]]
```

**Initial:** `left = 0`, `right = 2`, `top = 0`, `bottom = 2`

**Ring 1** (`left=0, right=2, top=0, bottom=2`):

| Step | Traversal | Elements Added | list | Boundary Update |
|------|-----------|------------------|------|-------------------|
|1|Top row, left→right|matrix[0][0..2] = 1,2,3|[1,2,3]|top++ → top=1|
|2|Right col, top→bottom|matrix[1..2][2] = 6,9|[1,2,3,6,9]|right-- → right=1|
|3|Bottom row, right→left (top<=bottom: 1<=2 ✓)|matrix[2][1..0] = 8,7|[1,2,3,6,9,8,7]|bottom-- → bottom=1|
|4|Left col, bottom→top (left<=right: 0<=1 ✓)|matrix[1..1][0] = 4|[1,2,3,6,9,8,7,4]|left++ → left=1|

**Ring 2** (`left=1, right=1, top=1, bottom=1`):

| Step | Traversal | Elements Added | list | Boundary Update |
|------|-----------|------------------|------|-------------------|
|1|Top row, left→right|matrix[1][1] = 5|[1,2,3,6,9,8,7,4,5]|top++ → top=2|
|2|Right col, top→bottom (top=2 > bottom=1, no iterations)|none|[1,2,3,6,9,8,7,4,5]|right-- → right=0|
|3|Skipped (top<=bottom: 2<=1 ✗)|-|-|bottom unchanged|
|4|Skipped (left<=right: 1<=0 ✗)|-|-|left unchanged|

**Loop check:** `left=1, right=0` → `left <= right` is false → loop exits.

Final Output:

```text
[1, 2, 3, 6, 9, 8, 7, 4, 5]
```

This matches the expected result.

---

## Why This Approach Works

Each full pass around the current boundary "ring" visits every cell along the outer edge of the remaining unvisited region exactly once, and shrinking the boundaries inward afterward ensures the next iteration only processes the next inner ring.

The conditional checks before the bottom-row and left-column traversals prevent double-counting cells in matrices where the ring has collapsed into a single row or single column — for example, if `top` and `bottom` have crossed, there's no separate bottom row left to traverse from the top row already covered in step 1.

---

## Complexity Analysis

- **Time Complexity:** `O(m × n)` — every cell in the matrix is visited exactly once
- **Space Complexity:** `O(1)` extra space (excluding the output list, which is required to hold `m × n` elements)

---

## Key Insight

Rather than trying to track direction changes with a single moving pointer and complex turning logic, using four independent shrinking boundaries turns the spiral traversal into four simple, predictable straight-line loops per ring, with the edge-case checks handling the collapse of a ring into a single row or column cleanly.

---

## Concepts Used

- Array
- Matrix
- Boundary Pointers
- Simulation
- Two-Dimensional Traversal