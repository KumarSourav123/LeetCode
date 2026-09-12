# LeetCode 876 - Middle of the Linked List

## Problem Statement

Given the `head` of a singly linked list, return *the middle node of the linked list*.

If there are two middle nodes, return **the second middle node**.

---

## Example 1:
```
Input: head = [1,2,3,4,5]
Output: [3,4,5]
Explanation: The middle node of the list is node 3.
```

## Example 2:
```
Input: head = [1,2,3,4,5,6]
Output: [4,5,6]
Explanation: Since the list has two middle nodes with values 3 and 4, we return the second one.
```

## Constraints:
- The number of nodes in the list is in the range `[1, 100]`.
- `1 <= Node.val <= 100`

---

## Approach

This solution uses a **two-pass** technique: first counting the total number of nodes, then walking forward exactly half that many steps to land on the middle node.

### Idea

- **First pass — count the size:** traverse the entire list with a `temp` pointer, incrementing a `size` counter for each node visited, until `temp` reaches `null`. This gives the total number of nodes in the list.
- **Second pass — walk to the middle:** starting `current` back at `head`, advance it `size / 2` times.
  - For an **odd** total size (e.g., `5` nodes), `size / 2 = 2` (integer division), which correctly lands on the 3rd node (0-indexed position `2`) — the true middle.
  - For an **even** total size (e.g., `6` nodes), `size / 2 = 3`, which lands on the 4th node — the **second** of the two middle nodes, exactly as the problem requires.
- Return `current`, now pointing at the correct middle node.

---

## Code

```java
class Solution {
    public ListNode middleNode(ListNode head) {
        int size = 0;
        ListNode temp = head;
        while (temp != null) {
            size++;
            temp = temp.next;
        }
        ListNode current = head;
        for (int i = 1; i <= size / 2; i++) {
            current = current.next;
        }
        return current;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5,6]
```

**First pass — count size:**

| temp | size after |
|------|------------|
|node1|1|
|node2|2|
|node3|3|
|node4|4|
|node5|5|
|node6|6|

`temp` becomes `null` after node6 → loop exits. `size = 6`

**Second pass — walk `size / 2 = 3` steps from `head`:**

| i | current before | current after (current = current.next) |
|---|--------------------|----------------------------------------------|
|1|node1|node2|
|2|node2|node3|
|3|node3|node4|

Loop ends (`i` exceeds `3`). `current = node4`

**Return:** `node4`, representing the sublist `[4,5,6]`

Output:

```text
[4,5,6]
```

This matches the expected output — with two middle nodes (`3` and `4`), the second one (`4`) is correctly returned.

---

## Why This Approach Works

Once the exact `size` of the list is known, the middle index can be computed directly using integer division. Walking exactly `size / 2` steps from the head always lands on the correct node: for odd-length lists, this is the unambiguous single middle node; for even-length lists, integer division naturally rounds this index toward the **later** of the two middle positions, which is exactly the "second middle node" the problem asks for — no special-case branching between odd and even lengths is needed.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — two full passes through the list (counting, then walking)
- **Space Complexity:** `O(1)` — only a constant number of pointer/counter variables are used

---

## Key Insight

While this two-pass approach is simple and correct, the more commonly taught approach for this exact problem is the **fast and slow pointer** technique (the same core idea as Floyd's Cycle Detection): advancing `slow` one step and `fast` two steps simultaneously in a **single pass** — when `fast` reaches the end, `slow` is naturally sitting at the middle. That approach achieves the same result without needing to know the list's length upfront, avoiding the two separate traversals used here.

---

## Concepts Used

- Linked List
- Two-Pass Traversal
- Integer Division for Midpoint Calculation
- Fast & Slow Pointers (alternative single-pass approach)