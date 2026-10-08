# LeetCode 1310 - XOR Queries of a Subarray

## Problem Statement

You are given an array `arr` of positive integers. You are also given the array `queries` where `queries[i] = [left_i, right_i]`.

For each query `i` compute the **XOR** of elements from `left_i` to `right_i` (that is, `arr[left_i] XOR arr[left_i + 1] XOR ... XOR arr[right_i]`).

Return an array `answer` where `answer[i]` is the answer to the `ith` query.

---

## Example 1:
```
Input: arr = [1,3,4,8], queries = [[0,1],[1,2],[0,3],[3,3]]
Output: [2,7,14,8]
Explanation:
The binary representation of the elements in the array are:
1 = 0001
3 = 0011
4 = 0100
8 = 1000
The XOR values for queries are:
[0,1] = 1 xor 3 = 2
[1,2] = 3 xor 4 = 7
[0,3] = 1 xor 3 xor 4 xor 8 = 14
[3,3] = 8
```

## Example 2:
```
Input: arr = [4,8,2,10], queries = [[2,3],[1,3],[0,0],[0,3]]
Output: [8,0,4,4]
```

## Constraints:
- `1 <= arr.length, queries.length <= 3 * 10^4`
- `1 <= arr[i] <= 10^9`
- `queries[i].length == 2`
- `0 <= left_i <= right_i < arr.length`

---

## Approach

This solution answers each query by **XOR-ing the range's elements using two pointers that close in from both ends**. Each step folds the two outermost remaining elements into the running answer, then moves both pointers inward, until they meet or cross.

### Idea

- Create `result`, an array with one slot per query.
- **For every query** `i`:
  - Start with `ans = 0`, `start = queries[i][0]` (left end) and `end = queries[i][1]` (right end).
  - **Loop** `while(start <= end)`:
    - **If `start == end`**, only one element is left (the exact middle of an odd-length range). XOR it in once, `ans = ans ^ arr[start]`, and `break`.
    - **Otherwise**, fold both ends in together: `ans = (arr[start] ^ arr[end]) ^ ans`, then move inward with `start++` and `end--`.
  - Store the finished value: `result[i] = ans`.
- The `start == end` branch matters because XOR-ing the same element in twice would cancel it out, so the middle element of an odd-length range must be included exactly once.
- Return `result`.

---

## Code

```java
class Solution {
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = queries.length;
        int[] result = new int[n];

        for(int i = 0 ; i < n ; i++){
            int ans = 0;
            int start = queries[i][0];
            int end = queries[i][1];

            while(start <= end){
                if(start == end){
                    ans = ans ^ arr[start];
                    break;
                }else{
                    ans = (arr[start] ^ arr[end]) ^ ans;
                    start++;
                    end--;
                }
            }
            result[i] = ans;
        }

        return result;
    }
}
```

---

## Dry Run

### Input

```text
arr = [1, 3, 4, 8]
index:  0  1  2  3
queries = [[0,1], [1,2], [0,3], [3,3]]
```

`n = 4`, `result = [0, 0, 0, 0]`

### Query 0: `[0, 1]`

`ans = 0`, `start = 0`, `end = 1`

| Check | Action | ans | start / end after |
|-------|--------|-----|--------------------|
|`start(0) <= end(1)`, `start != end`|`ans = (arr[0] ^ arr[1]) ^ 0 = (1 ^ 3) ^ 0`|2|1, 0|
|`start(1) <= end(0)`? false|loop exits|2|—|

`result[0] = 2`

### Query 1: `[1, 2]`

`ans = 0`, `start = 1`, `end = 2`

| Check | Action | ans | start / end after |
|-------|--------|-----|--------------------|
|`start(1) <= end(2)`, `start != end`|`ans = (arr[1] ^ arr[2]) ^ 0 = (3 ^ 4) ^ 0`|7|2, 1|
|`start(2) <= end(1)`? false|loop exits|7|—|

`result[1] = 7`

### Query 2: `[0, 3]`

`ans = 0`, `start = 0`, `end = 3`

| Check | Action | ans | start / end after |
|-------|--------|-----|--------------------|
|`start(0) <= end(3)`, `start != end`|`ans = (arr[0] ^ arr[3]) ^ 0 = (1 ^ 8) ^ 0`|9|1, 2|
|`start(1) <= end(2)`, `start != end`|`ans = (arr[1] ^ arr[2]) ^ 9 = (3 ^ 4) ^ 9 = 7 ^ 9`|14|2, 1|
|`start(2) <= end(1)`? false|loop exits|14|—|

`result[2] = 14`

### Query 3: `[3, 3]`

`ans = 0`, `start = 3`, `end = 3`

| Check | Action | ans |
|-------|--------|-----|
|`start(3) <= end(3)`, `start == end`|`ans = 0 ^ arr[3] = 8`, then `break`|8|

`result[3] = 8`

**Return:** `result = [2, 7, 14, 8]`

Output:

```text
[2,7,14,8]
```

This matches the expected output.

---

## Why This Approach Works

XOR is both **commutative** and **associative**, so the order in which the elements of a range are combined has no effect on the final result. That frees the algorithm to pick elements in any pattern, such as taking them in pairs from both ends.

The two pointers visit every index of the range exactly once:

- For an **even-length** range, the pointers pass each other after pairing everything up, and the loop ends naturally.
- For an **odd-length** range, the pointers land on the same middle index. The `start == end` branch handles that single leftover element (and `break`s), so it's included exactly once rather than being cancelled by a second XOR.

Because each element contributes exactly one time, `ans` ends up as `arr[left] ^ ... ^ arr[right]`, which is precisely what each query asks for.

---

## Complexity Analysis

- **Time Complexity:** `O(Q * L)`, where `Q` is the number of queries and `L` is the average length of a queried range — each query walks its range once, doing about `L / 2` iterations. In the worst case (many queries covering nearly the whole array) this approaches `O(Q * N)`.
- **Space Complexity:** `O(1)` extra space, aside from the `result` array that the problem requires as output

---

## Key Insight

XOR-ing a range by pairing ends is a valid way to combine elements precisely because XOR doesn't care about order. It's worth knowing that when there are many queries, a **prefix XOR array** can answer each one in `O(1)`: build `prefix[i] = arr[0] ^ ... ^ arr[i-1]` once in `O(N)`, and then `XOR(left..right) = prefix[right + 1] ^ prefix[left]`, since the shared prefix cancels itself out. That brings the whole problem down to `O(N + Q)`, versus the `O(Q * L)` of the per-query scan used here.

---

## Concepts Used

- Array
- Bit Manipulation (XOR)
- Two Pointers (closing in from both ends)
- Range Queries