# LeetCode 143 - Reorder List

## Problem Statement

You are given the head of a singly linked-list. The list can be represented as:

```
L0 → L1 → … → Ln-1 → Ln
```

Reorder the list to be on the following form:

```
L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → …
```

You may not modify the values in the list's nodes. Only nodes themselves may be changed.

---

## Example 1:
```
Input: head = [1,2,3,4]
Output: [1,4,2,3]
```

## Example 2:
```
Input: head = [1,2,3,5]
Output: [1,5,2,4,3]
```

## Constraints:
- The number of nodes in the list is in the range `[1, 5 * 10^4]`.
- `1 <= Node.val <= 1000`

---

## Approach

This solution breaks the problem into **three classic linked list sub-routines executed one after another**: find the middle with slow/fast pointers, reverse the list starting from the middle, then weave the front half and the reversed back half together node by node.

### Idea

- **Find the middle:** move `slow` one step and `fast` two steps at a time. When `fast` runs off the end (`fast == null` or `fast.next == null`), `slow` is sitting at the middle node — the start of the second half.
- **Reverse from the middle:** starting at `current = slow` with `prev = null`, walk forward flipping each `next` pointer backwards (`after = current.next; current.next = prev; prev = current; current = after;`). When the walk ends, `prev` is the **tail of the original list**, now the head of the reversed second half.
- After this step there are two chains that both end at the middle node: the front chain `head → … → mid → null` and the reversed chain `prev → … → mid → null`. They physically share the middle node, which is exactly what makes the weave terminate cleanly.
- **Weave the two halves:** set `first = head` (front chain) and `last = prev` (reversed chain). While `last.next != null`:
  - `dummy1 = first.next` and `dummy2 = last.next` — save both "next up" nodes before any pointer is overwritten.
  - `first.next = last` — attach the current back node right after the current front node.
  - `first = dummy1` — advance the front pointer to the node saved earlier.
  - `last.next = first` — attach the next front node right after the current back node, completing the alternation.
  - `last = dummy2` — advance the back pointer.
- The loop condition `last.next != null` stops the weave once the back pointer reaches the shared middle node, whose `next` was set to `null` during the reversal. Nothing is returned — the reordering happens in place on `head`.

---

## Code

```java
class Solution {
    public void reorderList(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode current = slow;
        ListNode prev = null;

        while(current != null){
            ListNode after = current.next;
            current.next = prev;
            prev = current;
            current = after;
        }

        ListNode first = head;
        ListNode last = prev;

        while(last.next != null){
            ListNode dummy1 = first.next;
            ListNode dummy2 = last.next;
            first.next = last;
            first = dummy1;
            if(last.next == null){
                last.next = null;
            }else{
                last.next = first;
            }
            last = dummy2;
        }
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5]   (n1 -> n2 -> n3 -> n4 -> n5 -> null)
```

### Phase 1 — Find the middle

`slow = n1`, `fast = n1`

| Iteration | Check | slow | fast |
|-----------|-------|------|------|
|1|`fast(n1) != null`, `fast.next(n2) != null` → runs|n2|n3|
|2|`fast(n3) != null`, `fast.next(n4) != null` → runs|n3|n5|
|3|`fast(n5) != null`, `fast.next(n5.next) == null` → exits|n3|n5|

State: `slow = n3` (the middle node)

### Phase 2 — Reverse from the middle

`current = n3`, `prev = null`

| Step | after | Action | prev | current |
|------|-------|--------|------|---------|
|1|n4|`n3.next = null`|n3|n4|
|2|n5|`n4.next = n3`|n4|n5|
|3|null|`n5.next = n4`|n5|null|

`current == null` → loop exits. `prev = n5`

State: front chain `n1 -> n2 -> n3 -> null`, reversed chain `n5 -> n4 -> n3 -> null` (both ending at the shared node n3)

### Phase 3 — Weave the halves

`first = n1`, `last = n5`

**Iteration 1:** `last.next (n5.next = n4) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|dummy1 = first.next|dummy1 = n2|front's next node saved|
|dummy2 = last.next|dummy2 = n4|back's next node saved|
|first.next = last|n1.next = n5|n1 now points to n5|
|first = dummy1|first = n2|—|
|last.next = first|n5.next = n2|n5 now points to n2|
|last = dummy2|last = n4|—|

State: `n1 -> n5 -> n2 -> n3 -> null`, remaining back chain `n4 -> n3 -> null`

**Iteration 2:** `last.next (n4.next = n3) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|dummy1 = first.next|dummy1 = n3|—|
|dummy2 = last.next|dummy2 = n3|—|
|first.next = last|n2.next = n4|n2 now points to n4|
|first = dummy1|first = n3|—|
|last.next = first|n4.next = n3|unchanged, already correct|
|last = dummy2|last = n3|—|

State: `n1 -> n5 -> n2 -> n4 -> n3 -> null`

**Loop check:** `last.next (n3.next)` is `null` (set during the reversal) → loop exits.

**Return:** nothing — `head` has been reordered in place. Traversing: `1 -> 5 -> 2 -> 4 -> 3 -> null`

Output:

```text
[1,5,2,4,3]
```

This matches the expected output.

---

## Why This Approach Works

The target ordering `L0 → Ln → L1 → Ln-1 → …` is just the front half and the back half interleaved, with the back half taken in reverse. So producing it only requires being able to walk both halves forward at the same time — and reversing the second half is precisely what turns a backwards walk into a forwards one, without needing an array or any back-pointers.

Letting the two chains **share the middle node** rather than cleanly splitting them is what makes the weave self-terminating. Since the reversal sets the middle node's `next` to `null`, the middle node is the only node in the back chain with a null `next`, so `while(last.next != null)` fires exactly the right number of times for both odd and even lengths, and the middle node ends up naturally in the final position with a `null` tail.

Saving `dummy1` and `dummy2` at the top of each weave iteration is essential — both `first.next` and `last.next` are overwritten inside the body, so without capturing them first, the forward references to the rest of each chain would be lost.

One small note: the `if(last.next == null){ last.next = null; }` branch is unreachable. The loop only runs when `last.next != null`, and `last.next` isn't modified between the check and that `if`, so control always takes the `else`. It's harmless, but the `if/else` can be collapsed to just `last.next = first;`.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — three sequential passes (finding the middle, reversing, weaving), each visiting at most `n` nodes, so `O(n) + O(n) + O(n) = O(n)`
- **Space Complexity:** `O(1)` — only a fixed set of pointer variables is used; the reversal and the weave both rewire existing nodes in place, with no new nodes or auxiliary data structures

---

## Key Insight

The whole problem collapses into three well-known linked list patterns chained together: **slow/fast to find the middle**, **iterative reversal**, and **merging two lists by alternating nodes**. Recognizing this decomposition is more valuable than any clever one-pass trick — and the shared middle node acting as the natural stopping marker is the detail that makes the merge step fall out cleanly without special-casing odd versus even lengths.

---

## Concepts Used

- Linked List
- Two Pointers (Slow & Fast)
- In-Place Linked List Reversal
- Merging / Interleaving Two Lists
- Constant Space Algorithm