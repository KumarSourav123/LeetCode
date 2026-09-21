# LeetCode 82 - Remove Duplicates from Sorted List II

## Problem Statement

You are given the `head` of a sorted linked list.

Delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list.

Return *the linked list sorted as well*.

---

## Example 1:
```
Input: head = [1,2,3,3,4,4,5]
Output: [1,2,5]
```

## Example 2:
```
Input: head = [1,1,1,2,3]
Output: [2,3]
```

## Constraints:
- The number of nodes in the list is in the range `[0, 300]`.
- `-100 <= Node.val <= 100`
- The list is guaranteed to be **sorted** in ascending order.

---

## Approach

This solution uses a **dummy head plus a `prev`/`current` pair**: `current` scans forward looking for runs of equal values, and whenever it finds one, an inner loop skips past the *entire* run before `prev.next` is rewired to jump straight over it — completely removing every node that had a duplicate, not just the extras.

### Idea

- **Edge case:** if the list is empty or has only one node, there can't be any duplicates, so return `head` as-is.
- Set up `dummy = new ListNode(0)` with `dummy.next = head`, so the head itself can be removed cleanly if it turns out to be part of a duplicate run (without a dummy, removing the very first node would need special-case handling).
- `prev = dummy`, `current = head`.
- **Main loop**, while `current != null`:
  - **Check if `current` starts a duplicate run:** if `current.next != null && current.val == current.next.val`, a run of two or more equal values begins here.
    - **Inner loop:** keep advancing `current = current.next` while the *next* node still matches the same value, so `current` ends up on the **last** node of the run.
    - **Skip the whole run:** `prev.next = current.next` rewires `prev` to point directly past every node in the run (including the one `prev` might have been about to point at), removing all of them at once.
    - Note that `prev` itself is **not** advanced here — it stays where it was, correctly pointing just before wherever the next kept (non-duplicate) node will turn out to be.
  - **Otherwise** (`current` is a genuinely unique value, at least so far): `prev = prev.next` — advance `prev` to keep it immediately behind `current`, since this node is being kept.
  - **Either way**, `current = current.next` advances the scan to the node right after whatever was just examined (the node after the run, or simply the next node).
- Return `dummy.next` — the real head of the (possibly modified) list, skipping the dummy node itself.

---

## Code

```java
class Solution {
    public ListNode deleteDuplicates(ListNode head) {

        if(head == null || head.next == null){
            return head;
        }

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prev = dummy;
        ListNode current = head;

        while(current != null){

            if(current.next != null && current.val == current.next.val){

                while(current.next != null && current.val == current.next.val){
                    current = current.next;
                }

                prev.next = current.next;

            }else{
                prev = prev.next;
            }

            current = current.next;
        }

        return dummy.next;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,1,1,2,3]   (n0(1) -> n1(1) -> n2(1) -> n3(2) -> n4(3) -> null)
```

`head != null` and `head.next != null` → proceed. `dummy = d0`, `d0.next = n0`. `prev = d0`, `current = n0`

**Iteration 1:** `current(n0) != null` → loop runs. `current.next(n1) != null` and `current.val(1) == current.next.val(1)` → true, a duplicate run starts

| Step | Action | Result |
|------|--------|--------|
|inner check|`current.next(n1)!=null`, `current.val(1)==n1.val(1)`|true → `current = n1`|
|inner check|`current.next(n2)!=null`, `current.val(1)==n2.val(1)`|true → `current = n2`|
|inner check|`current.next(n3)!=null`, `current.val(1)==n3.val(2)`|false → inner loop exits|
|skip the run|`prev.next = current.next`|`d0.next = n2.next = n3`|
|advance (outside if/else)|`current = current.next`|`current = n2.next = n3`|

State: `prev = d0` (unchanged), `d0.next = n3`, `current = n3`

**Iteration 2:** `current(n3) != null` → loop runs. `current.next(n4) != null` but `current.val(2) == current.next.val(3)`? false → not a duplicate

| Step | Action | Result |
|------|--------|--------|
|else branch|`prev = prev.next`|`prev = d0.next = n3`|
|advance (outside if/else)|`current = current.next`|`current = n3.next = n4`|

**Iteration 3:** `current(n4) != null` → loop runs. `current.next` is `null` → condition false → not a duplicate

| Step | Action | Result |
|------|--------|--------|
|else branch|`prev = prev.next`|`prev = n3.next = n4`|
|advance (outside if/else)|`current = current.next`|`current = n4.next = null`|

**Loop check:** `current == null` → loop exits.

**Return:** `dummy.next` (n3), traversing: `2 -> 3 -> null`

Output:

```text
[2,3]
```

This matches the expected output — all three `1`s were part of a duplicate run and removed entirely, leaving only the genuinely unique values `2` and `3`.

---

## Why This Approach Works

Because the list is guaranteed sorted, every duplicate of a given value is guaranteed to sit consecutively — so a duplicate run can always be found and fully consumed by looking only one node ahead at a time (`current.val == current.next.val`), with no need to look further afield or use extra storage to count occurrences.

The inner `while` loop is what makes the removal complete rather than partial: it walks `current` all the way to the **last** node of the run before `prev.next` is rewired, so `prev.next = current.next` jumps clean over every node in the run in one assignment — including the first one, which a naive "skip just the extras" approach would incorrectly leave behind.

Only advancing `prev` in the `else` branch is the key discipline that keeps it trailing correctly: `prev` should only ever point at a node that's confirmed unique and being kept, so it must **not** move forward when the current node turns out to be part of a run that's about to be deleted.

The dummy head is essential here specifically because the **very first** node(s) of the list could themselves be part of a duplicate run (as in this dry run, where `1` appears three times starting from the head) — without `dummy`, there would be no valid "previous" node to rewire when removing a run that starts at the original head.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — every node is visited a constant number of times total across the outer loop and the inner "consume the run" loop combined (the inner loop only ever advances `current` forward, never backward or repeatedly over the same node), so the total work is linear in the list length
- **Space Complexity:** `O(1)` — only a fixed set of pointer variables plus one dummy node are used; nodes are unlinked in place, not copied

---

## Key Insight

Distinguishing "remove the extra copies of a duplicated value" from "remove **every** copy of a duplicated value" (this problem's actual requirement) is the crux here — it's what makes the inner loop-then-skip pattern necessary instead of a simpler single-pass "skip if equal to previous" check. Pairing that with a dummy head is what lets the very first node(s) of the list be safely removed like any other, without special-casing the case where the duplicate run starts right at the head.

---

## Concepts Used

- Linked List
- Sorted List Traversal
- Nested Loop / Run-Length Detection
- Dummy Head Node Technique
- Predecessor (`prev`) Pointer Tracking
- Constant Space Algorithm