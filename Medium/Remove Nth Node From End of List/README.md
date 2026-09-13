# LeetCode 19 - Remove Nth Node From End of List

## Problem Statement

Given the `head` of a linked list, remove the `n`th node from the end of the list and return its head.

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

## Follow up:
Could you do this in one pass?

---

## Approach

This solution uses a **two-pass counting technique**: first measuring the total list length, then converting "nth from the end" into a straightforward "kth from the start" position to remove.

### Idea

- **Edge case first:** if `head.next == null`, the list has exactly one node, and since the constraints guarantee `n` is always valid, removing that single node means the whole list becomes empty — set `head = null` and return immediately.
- **First pass — measure the list:** traverse with a `temp` pointer, counting the total number of nodes into `size`.
- **Convert to a forward position:** the node to remove, counted from the end, is at forward position `size - n` (0-indexed) from the head. Update `size = size - n` to represent this forward step count.
- **Second pass — walk to the target node:** starting `current` at `head` and `prev` at `null`, advance both together for `size` steps: at each step, set `prev = current` before moving `current = current.next`. After the loop, `current` points to the node that needs to be removed, and `prev` points to the node just before it.
- **Perform the removal:**
  - If `current == head` (the node to remove happens to be the head itself — this occurs when `size` ended up being `0`), update `head = current.next` to skip it.
  - Otherwise, if `prev` is not `null`, relink around `current`: `prev.next = current.next`, then clear `current.next = null` for cleanliness (detaching the removed node fully).
- Return `head`.

---

## Code

```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head.next == null) {
            head = null;
            return head;
        }

        ListNode temp = head;
        int size = 0;
        while (temp != null) {
            size++;
            temp = temp.next;
        }

        size = size - n;

        ListNode current = head;
        ListNode prev = null;

        for (int i = 1; i <= size; i++) {
            prev = current;
            current = current.next;
        }

        if (current == head) {
            head = current.next;
        }

        if (prev != null) {
            prev.next = current.next;
            current.next = null;
        } else {
            current.next = null;
        }

        return head;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5], n = 2
```

`head.next != null` (node2 exists) → skip early-return case.

**First pass — measure size:**

Traversing `1 -> 2 -> 3 -> 4 -> 5 -> null` counts `size = 5`.

**Convert to forward position:** `size = size - n = 5 - 2 = 3`

**Second pass — walk 3 steps from head:**

`current = node1`, `prev = null`

| i | prev = current | current = current.next |
|---|----------------------|------------------------------|
|1|prev = node1|current = node2|
|2|prev = node2|current = node3|
|3|prev = node3|current = node4|

Loop ends. `current = node4` (the node to remove), `prev = node3`

**Removal:**

`current == head`? `node4 == node1`? No → skip head-reassignment case.

`prev != null`? Yes → `prev.next = current.next` → `node3.next = node5` (skipping node4). `current.next = null` (detaching node4 fully).

**Return:** `head` (node1), traversing: `1 -> 2 -> 3 -> 5 -> null`

Output:

```text
[1,2,3,5]
```

This matches the expected output — the 2nd node from the end (`4`) was correctly removed.

---

## Why This Approach Works

Knowing the total list length converts the awkward "count from the end" requirement into a simple, direct "count from the start" position — specifically, the node to remove sits at forward index `size - n` (0-indexed) from the head, since there are exactly `n` nodes after it (including itself) when counted from the tail.

Tracking `prev` alongside `current` during the walk ensures that once `current` reaches the target node, there's already a reference to the node right before it, ready to be relinked — without needing a separate lookup or a third traversal.

---

## Complexity Analysis

- **Time Complexity:** `O(n)`, more precisely `O(sz)` — two passes through the list, each proportional to its length
- **Space Complexity:** `O(1)` — only a constant number of pointer/counter variables are used

---

## Follow-up Considerations

The follow-up asks whether this can be done in **one pass** — this two-pass approach doesn't satisfy that, but a well-known **one-pass technique using two pointers with a gap of `n`** does: advance a `fast` pointer `n` steps ahead first, then move both `slow` and `fast` together until `fast` reaches the end. At that point, `slow` sits exactly at the node before the one that needs removal — achieving the same result without first needing to know the list's total length.

---

## Key Insight

While measuring the list length first is intuitive and easy to reason about, it requires two full traversals. The more elegant `O(n)`-single-pass solution recognizes that maintaining a fixed **gap of `n` nodes** between two pointers achieves the same "n-th from the end" positioning implicitly, without ever needing to know the list's total size upfront.

---

## Concepts Used

- Linked List
- Two-Pass Traversal
- Node Removal via Relinking
- Two Pointers with a Gap (alternative one-pass approach)