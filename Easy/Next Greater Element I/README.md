# LeetCode 496 - Next Greater Element I

## Problem Statement

The **next greater element** of some element `x` in an array is the **first greater** element that is **to the right** of `x` in the same array.

You are given two **distinct 0-indexed** integer arrays `nums1` and `nums2`, where `nums1` is a subset of `nums2`.

For each `0 <= i < nums1.length`, find the index `j` such that `nums1[i] == nums2[j]` and determine the **next greater element** of `nums2[j]` in `nums2`. If there is no next greater element, then the answer for this query is `-1`.

Return *an array* `ans` *of length* `nums1.length` *such that* `ans[i]` *is the **next greater element** as described above.*

---

## Example 1:
```
Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
Output: [-1,3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 4 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
- 1 is underlined in nums2 = [1,3,4,2]. The next greater element is 3.
- 2 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
```

## Example 2:
```
Input: nums1 = [2,4], nums2 = [1,2,3,4]
Output: [3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 2 is underlined in nums2 = [1,2,3,4]. The next greater element is 3.
- 4 is underlined in nums2 = [1,2,3,4]. There is no next greater element, so the answer is -1.
```

## Constraints:
- `1 <= nums1.length <= nums2.length <= 1000`
- `0 <= nums1[i], nums2[i] <= 10^4`
- All integers in `nums1` and `nums2` are **unique**.
- All the integers of `nums1` also appear in `nums2`.

**Follow up:** Could you find an `O(nums1.length + nums2.length)` solution?

---

## Approach

This solution is a direct **brute-force simulation of the problem statement**: for each value in `nums1`, locate it inside `nums2`, then scan to the right of that position until a larger value turns up (or the array ends).

### Idea

- Create `arr`, the answer array, with one slot per element of `nums1`.
- **Outer loop** over `i`, handling one query `nums1[i]` at a time.
- **Inner loop** over `j`, scanning `nums2` to find where `nums1[i]` sits:
  - If `nums1[i] != nums2[j]`, this isn't the element — `continue` to the next `j`.
  - Otherwise, `j` is the matching position. Set `flag = 0` (nothing found yet) and start a second pointer `z = j`.
- **Scan to the right of the match**, `while(z < nums2.length - 1)`:
  - If `nums2[z + 1] > nums2[j]`, the first greater element to the right has been found: record `arr[i] = nums2[z + 1]`, set `flag = 1`, and `break`.
  - Otherwise advance `z++` and keep looking.
- **If the scan ended without a hit** (`flag == 0`), there's no greater element to the right, so `arr[i] = -1`.
- Return `arr`.

---

## Code

```java
class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {

        int[] arr = new int[nums1.length];

        for(int i = 0 ; i < nums1.length ; i++){
            for(int j = 0 ; j < nums2.length ; j++){
                if(nums1[i] != nums2[j]){
                    continue;
                }else{
                    int flag = 0;
                    int z = j;
                    while(z < nums2.length-1){
                        if(nums2[z+1] > nums2[j]){
                            arr[i] = nums2[z+1];
                            flag = 1;
                            break;
                        }
                        z++;
                    }
                    if(flag == 0){
                        arr[i] = -1;
                    }

                }
            }
        }

        return arr;
    }
}
```

---

## Dry Run

### Input

```text
nums1 = [4, 1, 2]
nums2 = [1, 3, 4, 2]
index:   0  1  2  3
```

`arr = [0, 0, 0]`

### Query `i = 0` → `nums1[0] = 4`

Scan `nums2` for a match:

| j | nums2[j] | == 4? | Action |
|---|----------|-------|--------|
|0|1|no|`continue`|
|1|3|no|`continue`|
|2|4|**yes**|`flag = 0`, `z = 2`, start right-scan|
|3|2|no|`continue`|

Right-scan from `j = 2` (`nums2[j] = 4`):

| z | z < 3? | nums2[z+1] | > 4? | Action |
|---|--------|-------------|------|--------|
|2|yes|`nums2[3] = 2`|no|`z++` → 3|
|3|no|—|—|loop exits|

`flag == 0` → `arr[0] = -1`

### Query `i = 1` → `nums1[1] = 1`

| j | nums2[j] | == 1? | Action |
|---|----------|-------|--------|
|0|1|**yes**|`flag = 0`, `z = 0`, start right-scan|
|1, 2, 3|3, 4, 2|no|`continue`|

Right-scan from `j = 0` (`nums2[j] = 1`):

| z | z < 3? | nums2[z+1] | > 1? | Action |
|---|--------|-------------|------|--------|
|0|yes|`nums2[1] = 3`|**yes**|`arr[1] = 3`, `flag = 1`, `break`|

`flag == 1` → no overwrite. `arr[1] = 3`

### Query `i = 2` → `nums1[2] = 2`

| j | nums2[j] | == 2? | Action |
|---|----------|-------|--------|
|0|1|no|`continue`|
|1|3|no|`continue`|
|2|4|no|`continue`|
|3|2|**yes**|`flag = 0`, `z = 3`, start right-scan|

Right-scan from `j = 3`: `z(3) < 3`? No → the loop never runs. `flag == 0` → `arr[2] = -1`

**Return:** `arr = [-1, 3, -1]`

Output:

```text
[-1,3,-1]
```

This matches the expected output.

---

## Why This Approach Works

The solution implements the definition literally: "the first greater element to the right of `x`" is found by starting just right of `x`'s position in `nums2` and walking right until something larger appears. The first hit is by definition the **first** greater element, so recording it and breaking immediately is correct. If the walk reaches the end without a hit, no greater element exists to the right, hence `-1`.

Because all values are **unique**, each `nums1[i]` matches exactly one position in `nums2`, so the inner `j` loop triggers the right-scan at most once per query. (The inner loop keeps scanning after the match since there's no `break`, which wastes a few iterations but can't cause a wrong answer given uniqueness.)

The `flag` variable is what distinguishes "found a greater element" from "ran off the end", so that `-1` is only written in the second case.

---

## Complexity Analysis

- **Time Complexity:** `O(n1 * n2)`, where `n1 = nums1.length` and `n2 = nums2.length` — for each query, one pass over `nums2` finds the match and at most one further right-scan of up to `n2` elements. With `n2 <= 1000` that's easily fast enough here.
- **Space Complexity:** `O(1)` extra space, aside from the `arr` output array the problem requires

---

## Key Insight

The follow-up asks for `O(n1 + n2)`, which comes from precomputing the answer for **every** element of `nums2` once, instead of re-scanning per query. Walk through `nums2` while maintaining a **monotonic decreasing stack**: whenever the current element is larger than the stack's top, that top's next greater element is the current element, so pop it and record the pair in a hash map. Anything left on the stack at the end has no next greater element (`-1`). Then each query in `nums1` is just an `O(1)` map lookup.

---

## Concepts Used

- Array
- Brute-Force Simulation
- Nested Loops with a Flag
- (Follow-up) Monotonic Stack + Hash Map