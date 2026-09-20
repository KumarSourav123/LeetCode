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

This solution uses the **slow/fast pointer** technique with a deliberate head start for `fast`: instead of starting both pointers at `head`, `fast` is launched two nodes ahead from the beginning (`head.next.next`). This offset means `slow` naturally ends up parked one node *before* the middle once the loop finishes, so the middle node can be unlinked directly via `slow.next = slow.next.next` — no separate trailing/dummy pointer needed.

### Idea

- **Edge case:** if the list is empty or has only one node, deleting the middle leaves nothing behind, so return `null` directly.
- Start `slow = head` and `fast = head.next.next` — `fast` begins two steps ahead of `slow` from the very first comparison.
- **Loop** `while(fast != null && fast.next != null)`:
  - `slow = slow.next` — advance `slow` one step.
  - `fast = fast.next.next` — advance `fast` two steps.
- Because `fast` starts two nodes ahead and both then advance at the usual 1x/2x rates, the gap between `slow` and the true middle node stays exactly one node throughout — so when the loop stops, `slow` is sitting one node **before** the middle, rather than on it.
- **Unlink the middle node directly:** `slow.next = slow.next.next` skips over `slow.next` (the middle node), removing it from the list — no extra pointer was needed to track the predecessor, since the head-start baked that offset in from the start.
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
        ListNode fast = head.next.next;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        slow.next = slow.next.next;

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

`head != null` and `head.next != null` → proceed. `slow = n0`, `fast = head.next.next = n2` (already two steps ahead)

**Iteration 1:** `fast(n2) != null` and `fast.next(n3) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|slow = n1|—|
|fast = fast.next.next|fast = n4|—|

**Iteration 2:** `fast(n4) != null` and `fast.next(n5) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|slow = n2|—|
|fast = fast.next.next|fast = n6|—|

**Loop check:** `fast(n6) != null` → true, but `fast.next(n6.next)` is `null` → loop condition fails, loop exits.

State: `slow = n2` (the node just before the middle node, n3)

**Unlink the middle node:** `slow.next = slow.next.next` → `n2.next = n3.next = n4`, so `n2.next = n4`, removing `n3` (value 7)

**Return:** `head` (n0), traversing: `1 -> 3 -> 4 -> 1 -> 2 -> 6 -> null`

Output:

```text
[1,3,4,1,2,6]
```

This matches the expected output.

---

## Why This Approach Works

In the standard slow/fast midpoint pattern (both starting at `head`), `slow` ends up exactly *on* the middle node when `fast` runs out of room. Here, `fast` is deliberately launched two nodes ahead of where it would normally start — which is equivalent to running the loop one extra "virtual" iteration's worth of head start for `fast` alone, without `slow` getting that same head start. The effect is that `slow` consistently lags one node behind where it would have landed in the standard version — i.e., one node before the true middle — for every possible list length.

That one-node offset is exactly what's needed to delete the middle node in a singly linked list, since deletion requires access to the **predecessor** of the target node. By baking the offset into `fast`'s starting position, the predecessor falls directly out of the same single pass that finds the middle — no second traversal, and no extra pointer needed to separately track "one step behind."

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the list; `fast` advances two nodes per iteration, so the loop runs roughly `n/2` times
- **Space Complexity:** `O(1)` — only two pointer variables are used, no new nodes or auxiliary data structures

---

## Key Insight

Rather than tracking a separate trailing/predecessor pointer during a slow/fast traversal, deliberately offsetting where the fast pointer *starts* can shift what the slow pointer lands on by exactly the amount needed — here, turning "find the middle" into "find the node right before the middle" for free, since deletion only ever needs the predecessor, never the target node itself.

---

## Concepts Used

- Linked List
- Two Pointers (Slow & Fast, with an Offset Start)
- Node Deletion
- Constant Space Algorithm