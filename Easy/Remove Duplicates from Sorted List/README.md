# LeetCode 83 - Remove Duplicates from Sorted List

## Problem Statement

Given the `head` of a sorted linked list, delete all duplicates such that each element appears only once. Return *the linked list sorted as well*.

---

## Example 1:
```
Input: head = [1,1,2]
Output: [1,2]
```

## Example 2:
```
Input: head = [1,1,2,3,3]
Output: [1,2,3]
```

## Constraints:
- The number of nodes in the list is in the range `[0, 300]`.
- `-100 <= Node.val <= 100`
- The list is guaranteed to be sorted in ascending order.

---

## Approach

This solution takes advantage of the fact that the list is already **sorted**, meaning any duplicate values must always sit **directly adjacent** to each other — so a single pointer scanning forward and comparing each node to its immediate neighbor is sufficient.

### Idea

- Use a `current` pointer starting at `head`.
- While `current` and `current.next` are both non-`null`:
  - If `current.val == current.next.val`, the next node is a duplicate of the current one — **skip over it** by relinking `current.next = current.next.next`. Note that `current` does **not** advance here, since the new `current.next` (the node after the removed duplicate) might *also* be a duplicate of `current.val` (e.g., three or more repeated values in a row).
  - Otherwise, the next node has a different value — simply advance `current = current.next` to continue scanning forward.
- Once `current.next` becomes `null` (reaching the end of the list), all duplicates have been removed. Return `head` — which never needed to change, since duplicates can never appear at the very first position without also appearing right after it (and being caught by the comparison loop).

---

## Code

```java
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        ListNode current = head;
        while (current != null && current.next != null) {
            if (current.val == current.next.val) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
        return head;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,1,2,3,3]   (node1 -> node2 -> node3 -> node4 -> node5 -> null)
```

**Initial:** `current = node1` (val `1`)

| current (val) | current.next (val) | Equal? | Action |
|-----------------|------------------------|--------|--------|
|node1 (1)|node2 (1)|Yes|current.next = node2's next = node3 → node2 removed|
|node1 (1)|node3 (2)|No|current = node3|
|node3 (2)|node4 (3)|No|current = node4|
|node4 (3)|node5 (3)|Yes|current.next = node5's next = null → node5 removed|

**Loop check:** `current = node4`, `current.next = null` → loop exits.

**Return:** `head` (node1), traversing: `1 -> 2 -> 3 -> null`

Output:

```text
[1,2,3]
```

This matches the expected output — both duplicate values (`1` and `3`) were correctly reduced to a single occurrence each.

---

## Why This Approach Works

Because the list is sorted, any two nodes with the same value must be **consecutive** — there's no way for a duplicate to appear later in the list without first passing through every value in between, which the sorted order rules out. This guarantees that a simple adjacent-node comparison is sufficient to catch every duplicate, without needing a `HashSet` or any other tracking structure to remember previously seen values.

Not advancing `current` immediately after removing a duplicate is what correctly handles runs of **three or more** identical values in a row — since after removing one duplicate, the next node might still match `current.val`, and re-checking from the same `current` position catches that case.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the list
- **Space Complexity:** `O(1)` — only a constant number of pointer variables are used

---

## Key Insight

This problem is a simpler special case of the general "remove nodes matching a condition" pattern (as seen in Remove Linked List Elements): because the list's sorted property guarantees duplicates are always adjacent, the removal condition can be checked directly against `current.next` without needing to track or compare against any value seen earlier in the traversal.

---

## Concepts Used

- Linked List
- Sorted List Properties
- Node Removal via Relinking
- Iterative Traversal