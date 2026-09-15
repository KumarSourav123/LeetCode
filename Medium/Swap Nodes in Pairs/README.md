# LeetCode 24 - Swap Nodes in Pairs

## Problem Statement

Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed).

---

## Example 1:
```
Input: head = [1,2,3,4]
Output: [2,1,4,3]
```

## Example 2:
```
Input: head = []
Output: []
```

## Example 3:
```
Input: head = [1]
Output: [1]
```

## Example 4:
```
Input: head = [1,2,3]
Output: [2,1,3]
```

## Constraints:
- The number of nodes in the list is in the range `[0, 100]`.
- `0 <= Node.val <= 100`

---

## Approach

This solution swaps each adjacent pair **in place** (by relinking `next` pointers, not swapping values), using a `slow`/`fast` pointer pair to process two nodes at a time while stitching each swapped pair onto the end of the result being built.

### Idea

- **Edge case:** if the list is empty or has only one node, there's nothing to swap — return `head` as-is.
- Save `extra = head.next` — this will become the **new head** of the list once the first pair is swapped (since the second node always ends up first within its pair).
- Initialize `slow = head` (the first node of the current pair) and `fast = head.next` (the second node of the current pair).
- While both `fast.next` and `fast.next.next` are non-`null` (there's a complete next pair to process):
  - Save `dummy = slow` — remember the current pair's first node, since it will become the **tail** of the swapped pair and needs to link forward to whatever comes next.
  - `slow.next = fast.next.next` — pre-link the current pair's first node ahead to the **start of the pair after next** (skipping over the upcoming pair, to be corrected once that pair is also swapped).
  - `slow = fast.next` — advance `slow` to become the first node of the **next** pair (to be processed in the following iteration).
  - `fast.next = dummy` — complete the swap for the current pair: the second node (`fast`) now points back to the first node (`dummy`), reversing their order.
  - `fast = slow.next` — advance `fast` to become the second node of the next pair.
- Once the loop ends (fewer than two nodes remain to process), handle the leftover:
  - If `fast.next == null`, there's a single leftover node (`fast`) at the very end with no pair partner — attach it after the last swapped pair (`fast.next = slow`) and terminate the list (`slow.next = null`).
  - Otherwise, exactly one more pair remains unswapped — perform the same relinking as inside the loop (`slow.next = fast.next`, `fast.next = slow`) to swap this final pair.
- Return `extra`, the new head of the fully swapped list.

---

## Code

```java
class Solution {
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode extra = head.next;
        ListNode slow = head;
        ListNode fast = head.next;

        while (fast.next != null && fast.next.next != null) {
            ListNode dummy = slow;
            slow.next = fast.next.next;
            slow = fast.next;
            fast.next = dummy;
            fast = slow.next;
        }
        if (fast.next == null) {
            fast.next = slow;
            slow.next = null;
        } else {
            slow.next = fast.next;
            fast.next = slow;
        }

        return extra;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4]   (n1 -> n2 -> n3 -> n4 -> null)
```

`head != null`, `head.next != null` → proceed. `extra = n2` (saved as new head), `slow = n1`, `fast = n2`

**Loop check:** `fast.next (n3) != null` and `fast.next.next (n4) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|dummy = slow|dummy = n1|—|
|slow.next = fast.next.next|n1.next = n3.next = n4|n1 now points to n4|
|slow = fast.next|slow = n2.next = n3|slow = n3|
|fast.next = dummy|n2.next = n1|n2 now points to n1|
|fast = slow.next|fast = n3.next = n4|fast = n4|

State so far: `n2 -> n1 -> n4`, with `slow = n3` (dangling, to be attached next), `fast = n4`

**Loop check:** `fast.next (n4.next = null)` → condition fails → loop exits.

**Leftover handling:** `fast.next == null`? Yes (`fast = n4`, `n4.next = null`)

| Step | Action | Result |
|------|--------|--------|
|fast.next = slow|n4.next = n3|n4 now points to n3|
|slow.next = null|n3.next = null|n3 terminates the list|

**Return:** `extra` (n2), traversing: `2 -> 1 -> 4 -> 3 -> null`

Output:

```text
[2,1,4,3]
```

This matches the expected output.

---

## Why This Approach Works

Each iteration of the loop fully swaps one pair of nodes and correctly links that swapped pair's tail (`fast`, the original first node of the pair, now in second position) forward to the start of the *next* pair to be processed — building the result chain incrementally, one swapped pair at a time.

The final `if/else` handles the two possible leftover situations after the loop: either exactly one unpaired node remains (odd-length list), which simply gets appended after the last swapped pair without swapping, or exactly one more full pair remains that the loop's `&&` condition caused it to stop just short of processing, which then gets swapped using the same logic as inside the loop.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the list, processing two nodes per iteration
- **Space Complexity:** `O(1)` — only a constant number of pointer variables are used, and no node values are ever modified (only `next` pointers)

---

## Key Insight

Since the problem explicitly forbids swapping node **values** (only the nodes themselves may be rearranged), this solution carefully relinks `next` pointers to physically reorder the nodes — a more involved but value-preserving alternative to the simpler (but disallowed) approach of just swapping `val` fields between adjacent nodes.

---

## Concepts Used

- Linked List
- In-Place Node Swapping
- Two Pointers
- Pointer Relinking (not value swapping)