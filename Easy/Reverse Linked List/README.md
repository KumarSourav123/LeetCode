# LeetCode 206 - Reverse Linked List

## Problem Statement

Given the `head` of a singly linked list, reverse the list, and return *the reversed list*.

---

## Example 1:
```
Input: head = [1,2,3,4,5]
Output: [5,4,3,2,1]
```

## Example 2:
```
Input: head = [1,2]
Output: [2,1]
```

## Example 3:
```
Input: head = []
Output: []
```

## Constraints:
- The number of nodes in the list is the range `[0, 5000]`.
- `-5000 <= Node.val <= 5000`

## Follow up:
A linked list can be reversed either iteratively or recursively. Could you implement both?

---

## Approach

This solution reverses the list **iteratively**, walking through the list once while re-pointing each node's `next` reference to point backward instead of forward.

### Idea

- Maintain two pointers: `current` (starting at `head`, tracking the node currently being processed) and `before` (starting at `null`, tracking the new "previous" node in the reversed direction).
- While `current` is not `null`:
  - Save a reference to the next node **before** modifying anything: `after = current.next` — this prevents losing the rest of the list once `current.next` is reassigned.
  - Reverse the pointer: `current.next = before` — this makes the current node point backward to the previous node instead of forward.
  - Advance both pointers: `before = current` (the just-reversed node becomes the new "previous" for the next iteration), and `current = after` (move on to the node that was originally next).
- Once `current` becomes `null`, every node's `next` pointer has been reversed, and `before` now points to what was originally the **last** node — which is the new head of the reversed list. Return `before`.

---

## Code

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode current = head;
        ListNode before = null;
        while (current != null) {
            ListNode after = current.next;
            current.next = before;
            before = current;
            current = after;
        }
        return before;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2]   (node1 -> node2 -> null)
```

**Initial:** `current = node1`, `before = null`

**Iteration 1:**

| Step | Value |
|------|-------|
|after = current.next|node2|
|current.next = before|node1.next = null|
|before = current|before = node1|
|current = after|current = node2|

List so far: `node1 -> null` (detached), `before = node1`, `current = node2`

**Iteration 2:**

| Step | Value |
|------|-------|
|after = current.next|null (node2's original next)|
|current.next = before|node2.next = node1|
|before = current|before = node2|
|current = after|current = null|

Now `node2 -> node1 -> null`, `before = node2`, `current = null`

**Loop check:** `current == null` → loop exits.

**Return:** `before` = `node2`

Traversing from `node2`: `node2 -> node1 -> null`, which represents the list `[2, 1]`.

Output:

```text
[2, 1]
```

This matches the expected output.

---

## Why This Approach Works

Saving `after = current.next` **before** overwriting `current.next` is essential — without it, the reference to the rest of the original list would be lost the moment `current.next` is reassigned to point backward, making it impossible to continue traversing forward through the original list.

By the time the loop ends, every node's `next` pointer has been flipped to point to its original predecessor instead of its original successor, and `before` has advanced all the way to what was originally the last node — which is exactly the new head of the reversed list.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the list, visiting each node exactly once
- **Space Complexity:** `O(1)` — only a constant number of pointer variables are used (excluding the output list itself)

---

## Follow-up Considerations

The follow-up asks for both an **iterative** and a **recursive** implementation. This solution is the iterative version. A recursive version would instead recurse to the end of the list first, then reverse each `next` pointer while unwinding the call stack — for example:

```java
public ListNode reverseList(ListNode head) {
    if (head == null || head.next == null) {
        return head;
    }
    ListNode newHead = reverseList(head.next);
    head.next.next = head;
    head.next = null;
    return newHead;
}
```

The recursive version achieves the same `O(n)` time complexity but uses `O(n)` space due to the recursion call stack, whereas the iterative version uses only `O(1)` space.

---

## Key Insight

The core trick to any iterative linked list reversal is maintaining **three** references at each step conceptually (previous, current, and next) even though only two variables (`before` and `current`) are kept as loop state — the third (`after`) is recomputed fresh each iteration purely to preserve the forward link before it gets overwritten.

---

## Concepts Used

- Linked List
- Iterative Pointer Reversal
- Two/Three Pointer Technique
- Recursion (alternative approach)