# LeetCode 21 - Merge Two Sorted Lists

## Problem Statement

You are given the heads of two sorted linked lists `list1` and `list2`.

Merge the two lists into one **sorted** list. The list should be made by splicing together the nodes of the first two lists.

Return *the head of the merged linked list*.

---

## Example 1:
```
Input: list1 = [1,2,4], list2 = [1,3,4]
Output: [1,1,2,3,4,4]
```

## Example 2:
```
Input: list1 = [], list2 = []
Output: []
```

## Example 3:
```
Input: list1 = [], list2 = [0]
Output: [0]
```

## Constraints:
- The number of nodes in both lists is in the range `[0, 50]`.
- `-100 <= Node.val <= 100`
- Both `list1` and `list2` are sorted in **non-decreasing** order.

---

## Approach

This solution merges the two lists by **relinking the original nodes** rather than creating new ones: a dummy head anchors the result, and at each step the smaller of the two current fronts is directly re-pointed into the merged chain, with whatever's left over attached in one shot once one list runs dry.

### Idea

- Set up `dummy = new ListNode(0)` and `current = dummy`. `dummy` never holds real data; it just gives `current.next` somewhere to point on the very first attachment, so no separate "is this the first node?" check is needed.
- **Main loop**, while both `list1` and `list2` are non-null:
  - Compare `list1.val` and `list2.val`. Whichever is smaller-or-equal wins: `current.next = list1` (or `list2`) directly reuses that existing node — no `new ListNode(...)` copy is made — then `current = current.next` advances the tail of the merged chain, and that list's own pointer advances (`list1 = list1.next` or `list2 = list2.next`).
  - Using `<=` for `list1.val <= list2.val` means ties favor `list1`, which keeps the merge stable, though either choice would still produce a valid non-decreasing sequence.
- The main loop stops as soon as **either** list is exhausted, since the `&&` condition needs both to be non-null.
- **Attach whatever's left over:** rather than two follow-up loops, a single `if/else` checks which list still has nodes remaining (`list1 != null`) and attaches that entire remaining chain in one assignment — `current.next = list1` or `current.next = list2`. Since the remainder is already a valid sorted sub-list with correct internal links, nothing further needs to be walked or relinked node by node.
- Return `dummy.next` — the dummy node itself is skipped, and the real merged list starts right after it.

---

## Code

```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while(list1 != null && list2 != null){

            if(list1.val <= list2.val){
                current.next = list1;
                current = current.next;
                list1 = list1.next;
            }else{
                current.next = list2;
                current = current.next;
                list2 = list2.next;
            }

        }

        if(list1 != null){
            current.next = list1;
        }else{
            current.next = list2;
        }

        return dummy.next;
    }
}
```

---

## Dry Run

### Input

```text
list1 = [1,2,4]   (a1 -> a2 -> a4 -> null)
list2 = [1,3,4]   (b1 -> b3 -> b4 -> null)
```

`dummy = d0`, `current = d0`

### Main comparison loop

**Iteration 1:** `list1(a1) != null` and `list2(b1) != null` → runs. `list1.val(1) <= list2.val(1)` → true

| Step | Action | Result |
|------|--------|--------|
|attach|`current.next = a1`|d0 -> a1|
|advance current|`current = a1`|—|
|advance list1|`list1 = a1.next = a2`|—|

**Iteration 2:** `list1(a2) != null` and `list2(b1) != null` → runs. `list1.val(2) <= list2.val(1)` → false

| Step | Action | Result |
|------|--------|--------|
|attach|`current.next = b1`|... -> a1 -> b1|
|advance current|`current = b1`|—|
|advance list2|`list2 = b1.next = b3`|—|

**Iteration 3:** `list1(a2) != null` and `list2(b3) != null` → runs. `list1.val(2) <= list2.val(3)` → true

| Step | Action | Result |
|------|--------|--------|
|attach|`current.next = a2`|... -> b1 -> a2|
|advance current|`current = a2`|—|
|advance list1|`list1 = a2.next = a4`|—|

**Iteration 4:** `list1(a4) != null` and `list2(b3) != null` → runs. `list1.val(4) <= list2.val(3)` → false

| Step | Action | Result |
|------|--------|--------|
|attach|`current.next = b3`|... -> a2 -> b3|
|advance current|`current = b3`|—|
|advance list2|`list2 = b3.next = b4`|—|

**Iteration 5:** `list1(a4) != null` and `list2(b4) != null` → runs. `list1.val(4) <= list2.val(4)` → true

| Step | Action | Result |
|------|--------|--------|
|attach|`current.next = a4`|... -> b3 -> a4|
|advance current|`current = a4`|—|
|advance list1|`list1 = a4.next = null`|—|

**Loop check:** `list1 == null` → main loop exits.

State: merged chain so far `1 -> 1 -> 2 -> 3 -> 4`, remaining `list1 = null`, `list2 = [4]` (still `b4`)

### Attach the remainder

`if(list1 != null)` → `list1` is `null` → false → `else` branch: `current.next = list2` → `a4.next = b4`

**Return:** `dummy.next` (a1), traversing: `1 -> 1 -> 2 -> 3 -> 4 -> 4 -> null`

Output:

```text
[1,1,2,3,4,4]
```

This matches the expected output.

---

## Why This Approach Works

Since both input lists are already individually sorted in non-decreasing order, the smallest remaining value across the whole merge is always sitting at the front of one of the two lists. So a simple "compare the two current fronts, relink the smaller one into the result, advance that list" loop is guaranteed to build the merged chain in non-decreasing order.

Because the winning node (`list1` or `list2`) is relinked directly rather than copied, its `.next` pointer still points into the rest of its original list at that moment — which is exactly what makes the leftover-attachment step safe: once one list is exhausted, everything remaining in the other list is already correctly linked and already sorted (and every value in it is `>=` everything already merged in), so `current.next = list1` (or `list2`) can attach the *entire* remaining chain in a single assignment, with no need to walk and re-link it node by node.

The dummy head is the standard trick for building a list from scratch without special-casing the very first attachment.

---

## Complexity Analysis

- **Time Complexity:** `O(m + n)` — where `m` and `n` are the lengths of `list1` and `list2`; the main loop visits each node at most once before one list is exhausted, and the final attachment is a single `O(1)` pointer assignment
- **Space Complexity:** `O(1)` — this version reuses the existing nodes from both input lists rather than allocating new ones, so aside from the dummy node and a fixed set of pointer variables, no extra memory proportional to the input size is used

---

## Key Insight

Relinking existing nodes instead of copying their values into new ones is what drops the space complexity from `O(m + n)` down to `O(1)` — since a node's own `.next` pointer already encodes "the rest of my original list," the moment one input list is exhausted, its counterpart's remaining tail can be attached wholesale in one step rather than being walked node by node, because it's already a valid, correctly-linked, sorted chain.

---

## Concepts Used

- Linked List
- Two Pointers
- Merge (as in Merge Sort)
- In-Place Node Relinking
- Dummy Head Node Technique
- Constant Space Algorithm