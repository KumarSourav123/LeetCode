# LeetCode 2095 - Delete the Middle Node of a Linked List

## Problem Statement

You are given the `head` of a linked list. Delete the **middle** node, and return *the `head` of the modified linked list*.

The middle node of a linked list of size `n` is the `⌊n / 2⌋th` node from the **start** using **0-based indexing**, where `⌊x⌋` denotes the largest integer less than or equal to `x`.

- For `n` = `1`, `2`, `3`, `4`, and `5`, the middle nodes are `0`, `1`, `1`, `2`, and `2`, respectively.

---

## Example 1:
```
Input: head = [1,3,4,7,1,2,6]
Output: [1,3,4,1,2,6]
Explanation: For n = 7, node 3 with value 7 is the middle node, which is marked in red.
We return the new list after removing this node.
```

## Example 2:
```
Input: head = [1,2,3,4]
Output: [1,2,4]
Explanation: For n = 4, node 2 with value 3 is the middle node, which is marked in red.
```

## Example 3:
```
Input: head = [2,1]
Output: [2]
Explanation: For n = 2, node 1 with value 1 is the middle node.
Node 0 with value 2 is the only node remaining after removing node 1.
```

## Constraints:
- The number of nodes in the list is in the range `[1, 10^5]`.
- `1 <= Node.val <= 10^5`

---

## Approach

This solution uses the **slow/fast pointer** technique with one twist: an extra `dummy` pointer trails one step *behind* `slow` throughout the traversal, so it's already sitting on the node just before the middle by the time the middle is found — exactly what's needed to unlink it.

### Idea

- **Edge case:** if the list is empty or has only one node, deleting the middle leaves nothing behind, so return `null` directly.
- Start `slow = head`, `fast = head`, and `dummy = null` (it will be assigned before `slow` ever moves).
- **Loop** `while(fast != null && fast.next != null)`:
  - `dummy = slow` — before advancing `slow`, save its *current* position. This means `dummy` always trails exactly one step behind wherever `slow` is about to move to.
  - `slow = slow.next` — advance `slow` one step.
  - `fast = fast.next.next` — advance `fast` two steps.
- By the same slow/fast logic used to find a list's midpoint, the loop stops with `slow` sitting on the middle node — and, because of how `dummy` was updated just before each `slow` move, `dummy` is left sitting exactly one node **before** `slow`.
- **Unlink the middle node:** `dummy.next = slow.next` skips over `slow` (the middle node), removing it from the list.
- Return `head` (unchanged, since only an interior node was removed).

---

## Code

```java
class Solution {
    public ListNode deleteMiddle(ListNode head) {
        if(head == null || head.next == null ){
            return null;
        }

        ListNode slow = head;
        ListNode fast = head;
        ListNode dummy = null;

        while(fast != null && fast.next != null){
            dummy = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        dummy.next = slow.next;

        return head;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,3,4,7,1,2,6]   (n0(1) -> n1(3) -> n2(4) -> n3(7) -> n4(1) -> n5(2) -> n6(6) -> null)
```

`head != null` and `head.next != null` → proceed. `slow = n0`, `fast = n0`, `dummy = null`

**Iteration 1:** `fast(n0) != null` and `fast.next(n1) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|dummy = slow|dummy = n0|—|
|slow = slow.next|slow = n1|—|
|fast = fast.next.next|fast = n2|—|

**Iteration 2:** `fast(n2) != null` and `fast.next(n3) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|dummy = slow|dummy = n1|—|
|slow = slow.next|slow = n2|—|
|fast = fast.next.next|fast = n4|—|

**Iteration 3:** `fast(n4) != null` and `fast.next(n5) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|dummy = slow|dummy = n2|—|
|slow = slow.next|slow = n3|—|
|fast = fast.next.next|fast = n6|—|

**Iteration 4 check:** `fast(n6) != null` → true, but `fast.next(n6.next)` is `null` → loop condition fails, loop exits.

State: `dummy = n2`, `slow = n3` (the middle node, value 7)

**Unlink the middle node:** `dummy.next = slow.next` → `n2.next = n3.next = n4`, so `n2.next = n4`, removing `n3`

**Return:** `head` (n0), traversing: `1 -> 3 -> 4 -> 1 -> 2 -> 6 -> null`

Output:

```text
[1,3,4,1,2,6]
```

This matches the expected output.

---

## Why This Approach Works

The standard slow/fast pattern (`slow` moves 1, `fast` moves 2) guarantees `slow` lands on the middle node once `fast` reaches or passes the end — this is the same relationship used to find a list's midpoint in a single pass. The extra `dummy` pointer piggybacks on that same traversal: since it's always assigned to `slow`'s value *before* `slow` advances, it's perpetually one step behind — so the moment `slow` reaches the middle, `dummy` is already correctly positioned at the node just before it, with no second pass needed to re-find that predecessor.

Deleting a node from a singly linked list requires access to its **predecessor** (to rewire `.next`), which is exactly the gap that a single slow/fast pass alone wouldn't naturally provide — `dummy` is what closes that gap for free during the same traversal.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the list; `fast` advances two nodes per iteration, so the loop runs roughly `n/2` times
- **Space Complexity:** `O(1)` — only three pointer variables are used, no new nodes or auxiliary data structures

---

## Key Insight

Whenever a linked list problem needs both "the target node" and "the node right before it" (as almost any deletion does), a slow/fast traversal can be adapted to track both simultaneously by maintaining a trailing pointer that's updated to the previous position *before* the main pointer advances — avoiding the need for a separate lookup or a second pass to re-find the predecessor.

---

## Concepts Used

- Linked List
- Two Pointers (Slow & Fast)
- Trailing/Predecessor Pointer Tracking
- Node Deletion
- Constant Space Algorithm