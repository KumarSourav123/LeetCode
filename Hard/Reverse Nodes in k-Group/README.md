# LeetCode 25 - Reverse Nodes in k-Group

## Problem Statement

Given the `head` of a linked list, reverse the nodes of the list `k` at a time, and return *the modified list*.

`k` is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of `k` then left-out nodes, in the end, should remain as it is.

You may not alter the values in the list's nodes, only nodes themselves may be changed.

---

## Example 1:
```
Input: head = [1,2,3,4,5], k = 2
Output: [2,1,4,3,5]
```

## Example 2:
```
Input: head = [1,2,3,4,5], k = 3
Output: [3,2,1,4,5]
```

## Constraints:
- The number of nodes in the list is `n`.
- `1 <= k <= n <= 5000`
- `0 <= Node.val <= 1000`

**Follow-up:** Can you solve the problem in `O(1)` extra memory space?

---

## Approach

This solution processes the list **one group of `k` nodes at a time**: for each group, it first checks whether a full group of `k` nodes actually exists (using a helper to walk ahead `k-1` steps), reverses that group in place if so, then stitches the newly-reversed group onto whatever came before it — repeating until fewer than `k` nodes remain, which are left untouched as required.

### Idea

- **`reverseKGroup` (the main function):**
  - `temp` starts at `head` and marks the beginning of the *current* group being considered. `nextNode` will hold where the *next* group starts. `prev` tracks the **tail** of the *previously reversed* group, so it knows where to attach the next reversed group.
  - **Loop** while `temp != null`:
    - **Find the `k`th node from `temp`** using the `getKthNode` helper. If it returns `null`, fewer than `k` nodes remain from `temp` onward — not enough for a full group.
      - If `prev != null` (at least one group has already been reversed), attach the leftover nodes as-is: `prev.next = temp;` then `break` out of the loop entirely, leaving these final nodes untouched.
      - (If `prev == null` at this point — meaning even the very first group can't be completed — the loop would fall through without an explicit early exit; per the problem's constraints, `k` is always `<= n`, so this situation can never actually arise on valid input.)
    - **If a full group of `k` nodes was found** (`kthNode`): save `nextNode = kthNode.next` (where the *next* group will begin) before anything gets rewired, then cut this group off from the rest of the list: `kthNode.next = null`. This isolates the current `k`-node group as its own standalone mini-list.
    - **Reverse the isolated group** with the `reverse` helper — this rewires the group's internal `.next` pointers in place (though the call's return value isn't captured; the mutation to the nodes themselves is what matters).
    - **Reattach the reversed group to the rest of the list:**
      - If this was the **very first** group (`temp == head`), the new overall head of the list is now `kthNode` (the old last node of the group, now its first node after reversal): `head = kthNode`.
      - Otherwise, link the previous group's tail forward to this group's new head: `prev.next = kthNode`.
    - **Advance for the next iteration:** `prev = temp` (the just-reversed group's *original* first node is now its tail, so it's the new "previous tail" for next time), and `temp = nextNode` (move on to where the next group begins).
  - Return `head`.
- **`getKthNode(temp, k)` helper:** walks `k - 1` steps forward from `temp` (decrementing `k` first, then looping while there's still a node and steps remaining), returning the node reached — or `null` if the list runs out before `k` steps are completed, signaling an incomplete group.
- **`reverse(temp)` helper:** the standard iterative linked-list reversal (`prev`/`after`/`temp` pattern), reversing the isolated group's internal links in place.

---

## Code

```java
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode temp = head;
        ListNode nextNode = null;
        ListNode prev = null;

        while(temp != null){
            ListNode kthNode = getKthNode(temp, k);
            if(kthNode == null){
                if(prev != null){
                    prev.next = temp;
                    break;
                }
            }

            nextNode = kthNode.next;
            kthNode.next = null;
            reverse(temp);
            if(temp == head){
                head = kthNode;
            }else{
                prev.next = kthNode;
            }

            prev = temp;
            temp = nextNode;
        }

        return head;
    }

    private ListNode getKthNode(ListNode temp, int k){

        k--;
        while(temp != null && k > 0){
            temp = temp.next;
            k--;
        }

        return temp;
    }

    private ListNode reverse(ListNode temp){

        ListNode prev = null;
        while(temp != null){
            ListNode after = temp.next;
            temp.next = prev;
            prev = temp;
            temp = after;
        }

        return prev;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5]   (n1 -> n2 -> n3 -> n4 -> n5 -> null)
k = 2
```

`temp = n1`, `prev = null`

### Iteration 1 — first group (n1, n2)

`getKthNode(n1, 2)`: `k-- → 1`; loop: `temp(n1)!=null, k(1)>0` → `temp = n2, k-- → 0`; loop exits (`k=0`). Returns `n2`. So `kthNode = n2`.

`kthNode != null` → proceed (skip the incomplete-group branch).

| Step | Action | Result |
|------|--------|--------|
|save next group start|`nextNode = kthNode.next = n2.next`|`nextNode = n3`|
|cut the group off|`kthNode.next = null`|`n2.next = null` — group is now isolated: `n1 -> n2 -> null`|
|reverse the group|`reverse(n1)`|internally: `n1.next = null`, `n2.next = n1` → group becomes `n2 -> n1 -> null`|
|reattach: is this the first group?|`temp(n1) == head(n1)`? yes|`head = kthNode = n2`|
|advance|`prev = temp = n1`; `temp = nextNode = n3`|—|

State: `head = n2`. Structure so far: `n2 -> n1 -> null` (not yet linked to the rest). `prev = n1`, `temp = n3`.

### Iteration 2 — second group (n3, n4)

`getKthNode(n3, 2)`: `k-- → 1`; loop: `temp(n3)!=null, k(1)>0` → `temp = n4, k-- → 0`; exits. Returns `n4`. `kthNode = n4`.

`kthNode != null` → proceed.

| Step | Action | Result |
|------|--------|--------|
|save next group start|`nextNode = kthNode.next = n4.next`|`nextNode = n5`|
|cut the group off|`kthNode.next = null`|`n4.next = null` — group isolated: `n3 -> n4 -> null`|
|reverse the group|`reverse(n3)`|internally: `n3.next = null`, `n4.next = n3` → group becomes `n4 -> n3 -> null`|
|reattach: is this the first group?|`temp(n3) == head(n2)`? no|`else`: `prev.next = kthNode` → `n1.next = n4`|
|advance|`prev = temp = n3`; `temp = nextNode = n5`|—|

State: `head = n2`. Structure so far: `n2 -> n1 -> n4 -> n3 -> null`. `prev = n3`, `temp = n5`.

### Iteration 3 — leftover node (n5)

`getKthNode(n5, 2)`: `k-- → 1`; loop: `temp(n5)!=null, k(1)>0` → `temp = n5.next = null, k-- → 0`; loop re-checks: `temp == null` → exits. Returns `null`. `kthNode = null`.

`kthNode == null` → true → `prev(n3) != null`? yes → `prev.next = temp` → `n3.next = n5`, then `break`.

State: `head = n2`. Final structure: `n2 -> n1 -> n4 -> n3 -> n5 -> null`.

**Return:** `head` (n2), traversing: `2 -> 1 -> 4 -> 3 -> 5 -> null`

Output:

```text
[2,1,4,3,5]
```

This matches the expected output — the first two pairs `(1,2)` and `(3,4)` were each reversed in place, and the leftover single node `5` (fewer than `k=2` nodes remaining) was left untouched, exactly as the problem requires.

---

## Why This Approach Works

Processing the list group-by-group, where each group is temporarily **cut off** from the rest of the list before being reversed, is what keeps the reversal logic simple: the `reverse` helper only ever has to deal with a short, self-contained mini-list (bounded by `.next = null`), reusing the same standard iterative reversal pattern used for reversing a whole list.

The `getKthNode` check up front — walking exactly `k-1` steps ahead — is what distinguishes "a full group of `k` nodes exists starting here" from "fewer than `k` nodes remain." Checking this *before* committing to any cutting or reversing is essential, since the problem requires leftover partial groups to be preserved exactly as they were, unreversed.

Tracking `prev` as "the tail of the most recently completed, reversed group" is what allows each new reversed group to be correctly spliced onto the growing result: since a group's *original* first node becomes its *new* last node after reversal, `prev = temp` (the original first node) correctly captures the attachment point for whatever comes next. The special case for the very first group (`temp == head`) is needed because there's no earlier group's tail to link from — instead, the list's own `head` reference itself has to be updated to point at the new first node of the whole list.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — every node is visited a constant number of times total: once by `getKthNode`'s scan, once during its group's `reverse` call, and once as part of the outer loop's bookkeeping — so the total work across all groups is linear in the number of nodes
- **Space Complexity:** `O(1)` — only a fixed set of pointer variables is used across all three functions; every group is reversed in place by rewiring existing nodes, with no new nodes, recursion, or auxiliary data structures — satisfying the follow-up's constant-space requirement

---

## Key Insight

A clean way to solve "reverse in fixed-size chunks" problems is to reduce each chunk to the *already-solved* simpler problem — reversing a short, self-contained list — by physically detaching it from its neighbors first (`kthNode.next = null`) before reversing, and only *afterward* worrying about how to reattach it to the growing result via a tracked "previous group's tail" pointer. This separation of concerns (check group validity → isolate → reverse → reattach) keeps each individual step simple even though the overall problem involves several moving pieces.

---

## Concepts Used

- Linked List
- In-Place Reversal (applied per-group)
- Two/Three Pointer Coordination (`temp`, `prev`, `nextNode`)
- Helper Function Decomposition
- Constant Space Algorithm