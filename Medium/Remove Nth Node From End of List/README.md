# LeetCode 19 - Remove Nth Node From End of List

## Problem Statement

Given the `head` of a linked list, remove the `nth` node from the end of the list and return its head.

---

## Example 1:
```
Input: head = [1,2,3,4,5], n = 2
Output: [1,2,3,5]
```

## Example 2:
```
Input: head = [1], n = 1
Output: []
```

## Example 3:
```
Input: head = [1,2], n = 1
Output: [1]
```

## Constraints:
- The number of nodes in the list is `sz`.
- `1 <= sz <= 30`
- `0 <= Node.val <= 100`
- `1 <= n <= sz`

**Follow up:** Could you do this in one pass?

---

## Approach

This solution uses the classic **two-pointer gap technique**: push `fast` exactly `n` nodes ahead of `slow` first, then advance both together — by the time `fast` reaches the end, `slow` is sitting right before the node that needs to be removed.

### Idea

- Start both `fast` and `slow` at `head`.
- **Open the gap:** run a `for` loop `n` times, moving `fast = fast.next` each iteration. After this, `fast` is exactly `n` nodes ahead of `slow`.
- **Handle the head-removal edge case:** if `fast` is now `null`, that means `n` equals the total list length — the node to remove is the **head itself**. In that case, return `head.next` directly (skipping the old head) without touching `slow`/`fast` any further.
- **Advance together:** otherwise, `while(fast.next != null)`, move both `fast = fast.next` and `slow = slow.next` one step at a time. Because the `n`-node gap between them stays constant, when `fast` reaches the **last** node of the list (`fast.next == null`), `slow` has landed exactly on the node **just before** the one to be removed.
- **Remove the target node:** `slow.next = slow.next.next` skips over `slow.next` (the nth-from-end node), unlinking it from the list.
- Return `head` (unchanged, since only an interior/tail node was removed).

---

## Code

```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {

        ListNode fast = head;
        ListNode slow = head;

        for(int i = 1 ; i <= n ; i++){
            fast = fast.next;
        }

        if(fast == null){
            return head.next;
        }

        while(fast.next != null){
            fast = fast.next;
            slow = slow.next;
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
head = [1,2,3,4,5]   (n1 -> n2 -> n3 -> n4 -> n5 -> null)
n = 2
```

`fast = n1`, `slow = n1`

### Phase 1 — Open the gap

| i | Condition (`i <= n`) | Action | fast |
|---|------------------------|--------|------|
|1|1 <= 2 → true|`fast = fast.next`|n2|
|2|2 <= 2 → true|`fast = fast.next`|n3|
|3|3 <= 2 → false|loop exits|—|

State: `fast = n3`, `slow = n1` (gap of 2 nodes between them)

### Phase 2 — Head-removal check

`fast == null`? `fast = n3`, not null → skip, continue to the main loop.

### Phase 3 — Advance together

**Iteration 1:** `fast.next (n3.next = n4) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|fast = fast.next|fast = n4|—|
|slow = slow.next|slow = n2|—|

**Iteration 2:** `fast.next (n4.next = n5) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|fast = fast.next|fast = n5|—|
|slow = slow.next|slow = n3|—|

**Loop check:** `fast.next (n5.next)` is `null` → loop exits.

State: `fast = n5` (the last node), `slow = n3` (the node just before the target)

### Phase 4 — Remove the target node

`slow.next = slow.next.next` → `slow.next` is `n4`, and `n4.next` is `n5`, so `n3.next = n5` — `n4` is unlinked.

**Return:** `head` (n1), traversing: `1 -> 2 -> 3 -> 5 -> null`

Output:

```text
[1,2,3,5]
```

This matches the expected output (the node with value 4, the 2nd from the end, was removed).

---

## Why This Approach Works

The key relationship this approach relies on is: if `fast` is `n` nodes ahead of `slow`, then the moment `fast` reaches the **last** node of the list, `slow` must be exactly `n` nodes behind the last node — which is precisely one node before the nth-from-end node. Maintaining a fixed gap while advancing both pointers together is what makes this land correctly without ever needing to know the list's total length up front.

The `if(fast == null)` check after opening the gap is what makes the head-removal case safe: if `n` equals the list's length, `fast` runs completely off the end while opening the gap (before the main loop even starts), and there is no "node before the head" to land `slow` on — so that case is handled separately by simply returning `head.next`.

---

## Complexity Analysis

- **Time Complexity:** `O(sz)` — a single pass to open the `n`-node gap plus a second pass advancing both pointers together, each bounded by the list length, so `O(n) + O(sz - n) = O(sz)` overall. This satisfies the one-pass follow-up in spirit, since the list is only ever traversed once end-to-end (the gap-opening and the joint advance together cover each node exactly once).
- **Space Complexity:** `O(1)` — only a fixed set of pointer variables is used, no new nodes or auxiliary data structures

---

## Key Insight

Maintaining a **fixed-size gap** between two pointers is a reusable pattern any time a linked list problem is phrased in terms of distance "from the end" — since a singly linked list can't be walked backward, converting "n from the end" into "n ahead of another pointer, both moving together" turns an otherwise two-pass problem (count the list, then walk to `length - n`) into a single traversal.

---

## Concepts Used

- Linked List
- Two Pointers (Fixed Gap)
- Edge Case Handling (Removing the Head)
- Constant Space Algorithm