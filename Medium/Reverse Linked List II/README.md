# LeetCode 92 - Reverse Linked List II

## Problem Statement

Given the `head` of a singly linked list and two integers `left` and `right` where `left <= right`, reverse the nodes of the list from position `left` to position `right`, and return *the reversed list*.

---

## Example 1:
```
Input: head = [1,2,3,4,5], left = 2, right = 4
Output: [1,4,3,2,5]
```

## Example 2:
```
Input: head = [5], left = 1, right = 1
Output: [5]
```

## Constraints:
- The number of nodes in the list is `n`.
- `1 <= n <= 500`
- `-500 <= Node.val <= 500`
- `1 <= left <= right <= n`

**Follow up:** Could you do it in one pass?

---

## Approach

This solution makes a **first full pass to locate four key nodes** by index (the node just before the reversal window, the two ends of the window, and by extension the node just after it), then reverses only the window in a **second pass**, and finally reconnects the three pieces (untouched prefix, reversed window, untouched suffix).

### Idea

- **Edge case:** if the list is empty or has a single node, no reversal is possible/needed — return `head` as-is.
- Initialize `first = low = high = temp = head` and `second = null`, with a running `size` counter starting at 0. These will be pinned to specific positions as the list is walked.
- **First pass** — walk `temp` through the whole list, incrementing `size` each step:
  - When `size == left - 1` (and it isn't the very first node, i.e. `size != 1`), record `first = temp` — the node **just before** the reversal window, so its `.next` can be repointed later. When `left == 1` this case never fires, and `first` correctly stays at `head` itself (there is no "before" node).
  - When `size == left`, record `low = temp` — the **first node of the window** (it will become the tail of the reversed segment).
  - When `size == right`, record `high = temp` — the **last node of the window** (it will become the head of the reversed segment).
- After the pass, compute `second = high.next` — the node **just after** the window, which the reversal loop must stop before touching.
- **Second pass — reverse the window:** starting at `current = low` with `prev = null`, standard iterative reversal (`after = current.next; current.next = prev; prev = current; current = after;`), looping `while(current != second)` so it stops exactly at the boundary of the window without disturbing anything past it.
- **Reconnect:** `low.next = second` stitches the (now-tail) end of the reversed window back onto the untouched suffix.
- Then reattach the front: if `first != low` (i.e. the window didn't start at the head), `first.next = high` links the untouched prefix to the new head of the reversed window, and the original `head` is still the correct return value (`extra = head`). If `first == low` (the window starts at position 1), the reversed window's head `high` **is** the new head of the whole list, so `extra = high`.
- Return `extra`.

---

## Code

```java
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {

        if(head == null || head.next == null){
            return head;
        }

        ListNode first = head;
        ListNode low = head;
        ListNode high = head;
        ListNode temp = head;
        ListNode second = null;

        int size = 0;

        while(temp != null){
            size++;
            if(size == left-1 && size != 1){
                first = temp;
            }
            if(size == left){
                low = temp;
            }
            if(size == right){
                high = temp;
            }
            temp = temp.next;
        }

        if(size != right){
            second = high.next;
        }else{
            second = high.next;
        }

        ListNode current = low;
        ListNode prev = null;

        while(current != second){
            ListNode after = current.next;
            current.next = prev;
            prev = current;
            current = after;
        }

        low.next = second;
        ListNode extra = null;

        if(first != low){
            first.next = high;
            extra = head;
        }else{
            extra = high;
        }

        return extra;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5]   (n1 -> n2 -> n3 -> n4 -> n5 -> null)
left = 2, right = 4
```

`head != null` and `head.next != null` → proceed.
`first = n1`, `low = n1`, `high = n1`, `temp = n1`, `second = null`, `size = 0`

### Phase 1 — Locate the boundary nodes

| temp | size | size == left-1(1) && size != 1? | size == left(2)? | size == right(4)? | Updates |
|------|------|----------------------------------|-------------------|---------------------|---------|
|n1|1|1==1 but size==1 → false|no|no|—|
|n2|2|no|**yes**|no|`low = n2`|
|n3|3|no|no|no|—|
|n4|4|no|no|**yes**|`high = n4`|
|n5|5|no|no|no|—|

`temp` advances past `n5` to `null` → loop exits. `size = 5`

State: `first = n1` (never reassigned, still `head`), `low = n2`, `high = n4`

`second = high.next` → `second = n4.next = n5`

### Phase 2 — Reverse the window (low → second, exclusive)

`current = low(n2)`, `prev = null`

| Step | after | Action | prev | current |
|------|-------|--------|------|---------|
|1|n3|`n2.next = null`|n2|n3|
|2|n4|`n3.next = n2`|n3|n4|
|3|n5|`n4.next = n3`|n4|n5|

`current == second (n5)` → loop exits.

State: reversed window `n4 -> n3 -> n2 -> null`

### Phase 3 — Reconnect

`low.next = second` → `n2.next = n5` → window becomes `n4 -> n3 -> n2 -> n5`

`first != low` (`n1 != n2`) → true:
- `first.next = high` → `n1.next = n4`
- `extra = head` → `extra = n1`

**Return:** `extra` (n1), traversing: `1 -> 4 -> 3 -> 2 -> 5 -> null`

Output:

```text
[1,4,3,2,5]
```

This matches the expected output.

---

## Why This Approach Works

The list naturally splits into three segments relative to `[left, right]`: an untouched prefix, the window to reverse, and an untouched suffix. Locating `first` (last node of the prefix), `low`/`high` (the two ends of the window), and `second` (first node of the suffix) up front means the reversal itself only ever needs to loop over the window — `while(current != second)` guarantees it stops precisely at the suffix boundary without needing to count steps during the reversal.

The `first != low` check is what correctly handles the two shapes the problem can take: if `left > 1` there really is a "node before the window" (`first`) whose `.next` must be rewired to the new window head (`high`), and the original `head` is unaffected and still correct to return. If `left == 1`, the window starts at the head itself, so `first` and `low` end up being the same node, there is nothing before the window to rewire, and the reversed window's new head (`high`) becomes the new head of the whole list.

One thing worth flagging: the `if(size != right){ ... } else { ... }` block computes `second = high.next` in **both** branches — the branches are identical, so the condition has no effect and can be simplified to a single unconditional `second = high.next;`.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — one full pass to locate the boundary nodes, plus a second pass bounded by the window size (at most `n`), so `O(n) + O(right - left + 1) = O(n)` overall. (Note: since the first pass already walks the entire list, this doesn't satisfy the one-pass follow-up — a single combined pass that stops early once `right` is reached would be needed for that.)
- **Space Complexity:** `O(1)` — only a fixed set of pointer variables and a counter are used, no new nodes or auxiliary data structures

---

## Key Insight

Reversing a **sub-section** of a list, rather than the whole thing, is really the same in-place reversal pattern (`current`/`prev`/`after`) applied to a bounded window — the extra work is entirely in correctly identifying and preserving the four boundary references (`first`, `low`/`high`, `second`) so the window can be spliced back into the rest of the list afterward without losing the prefix or suffix.

---

## Concepts Used

- Linked List
- In-Place Restructuring
- Iterative Linked List Reversal
- Boundary/Pointer Tracking
- Constant Space Algorithm