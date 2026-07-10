# LeetCode 169 - Majority Element

## Problem Statement

Given an integer array `nums` of size `n`, return the **majority element**.

The majority element is the element that appears **more than ⌊n / 2⌋ times**.

It is guaranteed that the majority element always exists.

---

## Approach

This solution uses **Moore's Voting Algorithm (Boyer-Moore Voting Algorithm)**.

### Idea

- Maintain a **candidate** and a **count**.
- If the count becomes `0`, choose the current element as the new candidate.
- If the current element is equal to the candidate, increase the count.
- Otherwise, decrease the count.

Since the majority element appears more than `n / 2` times, it cannot be completely canceled out by the other elements.

---

## Code

```java
class Solution {
    public int majorityElement(int[] nums) {
        int count = 0;
        int candidate = 0;

        for (int i = 0; i < nums.length; i++) {

            if (count == 0) {
                candidate = nums[i];
            }

            if (nums[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }

        return candidate;
    }
}
```

---

## Dry Run

### Input

```text
nums = [2,2,1,1,1,2,2]
```

| Current Element | Candidate | Count | Action |
|-----------------|-----------|------:|--------|
|2|-|0|Candidate = 2, Count = 1|
|2|2|1|Count = 2|
|1|2|2|Count = 1|
|1|2|1|Count = 0|
|1|2|0|Candidate = 1, Count = 1|
|2|1|1|Count = 0|
|2|1|0|Candidate = 2, Count = 1|

Final Candidate:

```text
2
```

Output:

```text
2
```

---

## Why Moore's Voting Algorithm Works

Think of every occurrence of the majority element as a **+1 vote**.

Every occurrence of any other element **cancels one vote**.

Since the majority element appears **more than half of the array**, it can never be completely canceled.

Therefore, the remaining candidate at the end must be the majority element.

---

## Complexity Analysis

- **Time Complexity:** `O(n)`
- **Space Complexity:** `O(1)`

---

## Key Insight

Instead of storing the frequency of every element using a `HashMap`, Moore's Voting Algorithm keeps track of only:

- A **candidate**
- A **count**

This reduces the space complexity from **O(n)** to **O(1)** while still finding the majority element in a single traversal.

---

## Concepts Used

- Array
- Moore's Voting Algorithm
- Boyer-Moore Voting Algorithm
- Greedy
- Frequency Cancellation
- Constant Space Algorithm