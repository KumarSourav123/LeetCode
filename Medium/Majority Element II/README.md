# LeetCode 229 - Majority Element II

## Problem Statement

Given an integer array of size `n`, find all elements that appear more than `⌊n / 3⌋` times.

---

## Example 1:
```
Input: nums = [3,2,3]
Output: [3]
```

## Example 2:
```
Input: nums = [1]
Output: [1]
```

## Example 3:
```
Input: nums = [1,2]
Output: [1,2]
```

## Constraints:
- `1 <= nums.length <= 5 * 10^4`
- `-10^9 <= nums[i] <= 10^9`

## Follow up:
Could you solve the problem in linear time and in `O(1)` space?

---

## Approach

This solution uses a **HashMap-based frequency count**, followed by a filtering pass to select qualifying elements.

### Idea

- Traverse `nums` and build a `HashMap` where each key is a distinct element and its value is how many times that element appears.
- Once the full frequency map is built, iterate over its entries. For every element whose frequency is **strictly greater than** `nums.length / 3`, add it to the result `list`.
- Return the `list` containing all elements that satisfy the majority condition.

Since at most two elements can appear more than `n/3` times in any array (a mathematical property of this threshold), the result list will always contain `0`, `1`, or `2` elements.

---

## Code

```java
class Solution {
    public List<Integer> majorityElement(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (int num : nums) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > (nums.length / 3)) {
                list.add(entry.getKey());
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
nums = [3,2,3]
```

**Step 1: Build frequency map**

| num | map after insertion |
|-----|----------------------|
|3|{3:1}|
|2|{3:1, 2:1}|
|3|{3:2, 2:1}|

**Step 2: Filter entries where count > nums.length / 3 (= 3 / 3 = 1)**

| Entry | Count | Count > 1? | Added to list? |
|-------|-------|------------|------------------|
|3|2|Yes|Yes → list = [3]|
|2|1|No|No|

Output:

```text
[3]
```

This matches the expected output, since `3` appears twice, more than the threshold of `1`.

---

## Why This Approach Works

By counting the exact frequency of every distinct element first, the algorithm can directly compare each frequency against the required threshold (`nums.length / 3`) without needing to guess candidates ahead of time.

Filtering after the full count ensures correctness even in edge cases like `[1,2]`, where both elements individually appear only once but still exceed the threshold of `2/3 = 0`, since integer division makes the threshold `0` and any positive count qualifies.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — one pass to build the map, one pass to filter entries
- **Space Complexity:** `O(n)` — for storing frequencies in the HashMap

---

## Follow-up Considerations

The problem's follow-up asks for a solution in **linear time and O(1) space**. This can be achieved using the **Boyer-Moore Voting Algorithm extended to track two candidates simultaneously** (since at most two elements can exceed the `n/3` threshold), maintaining two candidate/count pairs instead of a HashMap, followed by a final verification pass to confirm each candidate truly exceeds `n/3` occurrences.

---

## Key Insight

Since more than one element can exceed a `n/3` threshold (unlike the simple majority element problem, which only allows one), the HashMap approach naturally handles multiple qualifying elements without extra bookkeeping — at the cost of `O(n)` space, which the follow-up challenges you to eliminate.

---

## Concepts Used

- Array
- HashMap
- Hashing
- Frequency Counting
- Boyer-Moore Voting Algorithm (optimal follow-up approach)
- Constant Space Algorithm (follow-up)