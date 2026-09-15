# LeetCode 328 - Odd Even Linked List

## Problem Statement

Given the `head` of a singly linked list, group all the nodes with odd indices together followed by the nodes with even indices, and return *the reordered list*.

The **first** node is considered **odd**, and the **second** node is **even**, and so on.

Note that the relative order inside both the even and odd groups should remain as it was in the input.

You must solve the problem in `O(1)` extra space complexity and `O(n)` time complexity.

---

## Example 1:
```
Input: head = [1,2,3,4,5]
Output: [1,3,5,2,4]
```

## Example 2:
```
Input: head = [2,1,3,5,6,4,7]
Output: [2,3,6,7,1,5,4]
```

## Constraints:
- The number of nodes in the linked list is in the range `[0, 10^4]`.
- `-10^6 <= Node.val <= 10^6`

---

## Approach

This solution builds the **odd-indexed chain and even-indexed chain simultaneously in a single pass**, using two pointers that each skip forward by two nodes at a time, then splices the even chain onto the end of the odd chain.

### Idea

- **Edge case:** if the list is empty or has only one node, no reordering is needed — return `head` as-is.
- Initialize `slow` at `head` (the start of the **odd** chain, since the first node is odd-indexed) and `fast` at `head.next` (the start of the **even** chain, since the second node is even-indexed). Save `dummy = head.next` to remember where the even chain begins, since this reference will be needed later to reattach it.
- While both `slow.next` and `fast.next` are non-`null` (there are still pairs of nodes to relink):
  - `slow.next = fast.next` — link the current odd node forward to the *next* odd-indexed node (which sits right after the current even node).
  - `slow = slow.next` — advance `slow` to that next odd node.
  - `fast.next = slow.next` — link the current even node forward to the *next* even-indexed node (which sits right after the node `slow` just moved to).
  - `fast = fast.next` — advance `fast` to that next even node.
- Once the loop ends (one of the chains has been fully processed), the odd chain and even chain have each been internally relinked correctly, but they're still two separate chains. Attach the even chain to the end of the odd chain: `slow.next = dummy`.
- Return `head` — now the head of the fully reordered list.

---

## Code

```java
class Solution {
    public ListNode oddEvenList(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        ListNode dummy = head.next;

        while (slow.next != null && fast.next != null) {
            slow.next = fast.next;
            slow = slow.next;
            fast.next = slow.next;
            fast = fast.next;
        }

        slow.next = dummy;

        return head;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5]   (n1 -> n2 -> n3 -> n4 -> n5 -> null)
```

`head != null` and `head.next != null` → proceed. `slow = n1`, `fast = n2`, `dummy = n2`

**Iteration 1:** `slow.next(n2) != null` and `fast.next(n3) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow.next = fast.next|n1.next = n3|n1 now points to n3|
|slow = slow.next|slow = n3|—|
|fast.next = slow.next|n2.next = n4 (n3's original next)|n2 now points to n4|
|fast = fast.next|fast = n4|—|

State: odd chain `n1 -> n3`, even chain `n2 -> n4`

**Iteration 2:** `slow.next(n3.next = n4, still original) != null` and `fast.next(n4.next = n5, still original) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow.next = fast.next|n3.next = n5|n3 now points to n5|
|slow = slow.next|slow = n5|—|
|fast.next = slow.next|n4.next = null (n5's original next)|n4 now points to null|
|fast = fast.next|fast = null|—|

State: odd chain `n1 -> n3 -> n5`, even chain `n2 -> n4 -> null`

**Loop check:** `slow.next (n5.next)` is `null` → loop exits.

**Splice the chains:** `slow.next = dummy` → `n5.next = n2` (attaching the even chain to the end of the odd chain)

**Return:** `head` (n1), traversing: `1 -> 3 -> 5 -> 2 -> 4 -> null`

Output:

```text
[1,3,5,2,4]
```

This matches the expected output.

---

## Why This Approach Works

Since `slow` always represents "the current end of the odd chain being built" and `fast` always represents "the current end of the even chain being built," relinking `slow.next` to `fast.next` (the node two positions ahead) and vice versa correctly reconstructs both chains in their original relative order, interleaving the relinking one step at a time as the pointers advance together through the original list.

Saving `dummy = head.next` before any relinking happens is essential — once the loop finishes, there's no longer a direct way to find where the even chain starts (since all the `.next` pointers have been rewritten), so this reference must be captured upfront.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the list
- **Space Complexity:** `O(1)` — only a handful of pointer variables are used, no new nodes are created and no extra data structures are needed, satisfying the problem's constant-space requirement

---

## Key Insight

Rather than physically separating the list into two new lists (which would still be `O(n)` time but conceptually messier to track), this solution rewires the **existing** nodes' `next` pointers in place, advancing two "current tail" pointers (`slow` for odd, `fast` for even) in lockstep — a common in-place linked list restructuring pattern that avoids any extra memory allocation.

---

## Concepts Used

- Linked List
- In-Place Restructuring
- Two Pointers
- Constant Space Algorithm