# LeetCode 141 - Linked List Cycle

## Problem Statement

Given `head`, the head of a linked list, determine if the linked list has a cycle in it.

There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the `next` pointer. Internally, `pos` is used to denote the index of the node that tail's `next` pointer is connected to. **Note that** `pos` **is not passed as a parameter**.

Return `true` *if there is a cycle in the linked list*. Otherwise, return `false`.

---

## Example 1:
```
Input: head = [3,2,0,-4], pos = 1
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).
```

## Example 2:
```
Input: head = [1,2], pos = 0
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 0th node.
```

## Example 3:
```
Input: head = [1], pos = -1
Output: false
Explanation: There is no cycle in the linked list.
```

## Constraints:
- The number of nodes in the list is in the range `[0, 10^4]`.
- `-10^5 <= Node.val <= 10^5`
- `pos` is `-1` or a valid index in the linked-list.

## Follow up:
Can you solve it using `O(1)` (i.e. constant) memory?

---

## Approach

This solution uses **Floyd's Cycle Detection Algorithm** (the "tortoise and hare" technique), using two pointers moving at different speeds through the list.

### Idea

- Handle the trivial empty-list case: if `head == null`, there's obviously no cycle — return `false`.
- Initialize two pointers, both starting at `head`:
  - `slow` — advances **one** node at a time.
  - `fast` — advances **two** nodes at a time.
- While both `fast.next` and `fast.next.next` are non-`null` (ensuring `fast` never tries to step past the end of the list):
  - Advance `slow` by one node (`slow = slow.next`).
  - Advance `fast` by two nodes (`fast = fast.next.next`).
  - If `slow == fast` at any point, the two pointers have collided — this can only happen if the list contains a cycle (since in an acyclic list, `fast` would simply reach the end and the loop would terminate first). Return `true`.
- If the loop ends because `fast` (or `fast.next`) reaches `null` without ever colliding with `slow`, the list has no cycle — return `false`.

---

## Code

```java
public class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2], pos = 0   (node2.next connects back to node1, forming a cycle)
```

`head != null` → proceed. `slow = node1`, `fast = node1`

**Iteration 1:** `fast.next = node2` (not null) && `fast.next.next = node2.next = node1` (not null, it's the cycle-back link) → loop condition holds

| Step | Value |
|------|-------|
|slow = slow.next|slow = node2|
|fast = fast.next.next|fast = node1 (via node2 → node1)|
|slow == fast?|node2 == node1? No|

**Iteration 2:** `fast.next = node2` (not null) && `fast.next.next = node1` (not null) → loop condition holds

| Step | Value |
|------|-------|
|slow = slow.next|slow = node1 (via node2 → node1)|
|fast = fast.next.next|fast = node1 (via node2 → node1)|
|slow == fast?|node1 == node1? **Yes → return true**|

Output:

```text
true
```

This matches the expected output.

---

## Why This Approach Works

If a linked list has no cycle, `fast` (moving twice as fast as `slow`) will always reach the end of the list (`null`) first, causing the loop to terminate naturally without ever finding a collision.

If the list **does** have a cycle, once both pointers enter the cycle, `fast` gains on `slow` by exactly one extra step per iteration (since it moves two steps to `slow`'s one). Because the cycle has a fixed, finite length, `fast` is guaranteed to eventually "lap" `slow` and land on the exact same node — this collision is mathematically guaranteed to happen within at most one full loop around the cycle, making the detection both correct and efficient.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — in the worst case, `fast` travels through the list (and possibly one extra lap around a cycle) a number of times proportional to the list length
- **Space Complexity:** `O(1)` — only two pointer variables are used, satisfying the follow-up's constant-memory requirement

---

## Follow-up Considerations

The follow-up asks for an `O(1)` memory solution — this Floyd's Cycle Detection approach already satisfies that, using only two pointers regardless of list size. This is the key advantage over an alternative approach using a `HashSet` to track visited nodes, which would work correctly but require `O(n)` extra space.

---

## Key Insight

The "tortoise and hare" technique is a classic space-efficient alternative to tracking visited nodes in a `HashSet`: by exploiting the fact that a faster pointer must eventually catch up to a slower one if they're both looping within a bounded cycle, cycle detection can be achieved with just two extra pointers instead of any auxiliary data structure.

---

## Concepts Used

- Linked List
- Two Pointers (Fast & Slow)
- Floyd's Cycle Detection Algorithm
- Constant Space Algorithm