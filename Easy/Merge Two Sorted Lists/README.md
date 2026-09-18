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

This solution builds a brand-new merged list one node at a time, using a **dummy head** to avoid special-casing the first node, and comparing the current front of each list to decide which value gets appended next — then appends whatever's left over once one list runs dry.

### Idea

- Set up `dummy = new ListNode(0)` and `current = dummy`. `dummy` never holds real data; it just gives `current.next` somewhere to point on the very first append, so no separate "is this the first node?" check is needed.
- **Main loop**, while both `list1` and `list2` are non-null:
  - Compare `list1.val` and `list2.val`. Whichever is smaller-or-equal wins: create a **new** node copying that value, append it (`current.next = node; current = node;`), and advance that list's pointer (`list1 = list1.next` or `list2 = list2.next`).
  - Using `<=` for the `list1.val <= list2.val` check means that on ties, `list1`'s node is taken first — this is what keeps the merge stable/consistent, though either list winning a tie would still produce a valid non-decreasing sequence.
- The main loop stops as soon as **either** list is exhausted, since the `&&` condition needs both to be non-null.
- **Drain whichever list still has nodes left:** two follow-up `while` loops, `while(list1 != null)` and `while(list2 != null)`, each doing the same "create a new node, append, advance" pattern. At most one of these two loops actually does any work, since the main loop only stopped because one list ran out.
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
                ListNode node = new ListNode(list1.val);
                current.next = node;
                current = node;
                list1 = list1.next;
            }else{
                ListNode node = new ListNode(list2.val);
                current.next = node;
                current = node;
                list2 = list2.next;
            }

        }

        while(list1 != null){
            ListNode node = new ListNode(list1.val);
            current.next = node;
            current = node;
            list1 = list1.next;
        }
        while(list2 != null){
            ListNode node = new ListNode(list2.val);
            current.next = node;
            current = node;
            list2 = list2.next;
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

### Phase 1 — Main comparison loop

**Iteration 1:** `list1(a1) != null` and `list2(b1) != null` → runs. `list1.val(1) <= list2.val(1)` → true

| Step | Action | Result |
|------|--------|--------|
|create node|`node = new ListNode(1)` from list1|call it `n1`|
|append|`current.next = n1; current = n1`|d0 -> n1(1)|
|advance|`list1 = a2`|—|

**Iteration 2:** `list1(a2) != null` and `list2(b1) != null` → runs. `list1.val(2) <= list2.val(1)` → false

| Step | Action | Result |
|------|--------|--------|
|create node|`node = new ListNode(1)` from list2|call it `n2`|
|append|`current.next = n2; current = n2`|d0 -> n1(1) -> n2(1)|
|advance|`list2 = b3`|—|

**Iteration 3:** `list1(a2) != null` and `list2(b3) != null` → runs. `list1.val(2) <= list2.val(3)` → true

| Step | Action | Result |
|------|--------|--------|
|create node|`node = new ListNode(2)` from list1|call it `n3`|
|append|`current.next = n3; current = n3`|... -> n3(2)|
|advance|`list1 = a4`|—|

**Iteration 4:** `list1(a4) != null` and `list2(b3) != null` → runs. `list1.val(4) <= list2.val(3)` → false

| Step | Action | Result |
|------|--------|--------|
|create node|`node = new ListNode(3)` from list2|call it `n4`|
|append|`current.next = n4; current = n4`|... -> n4(3)|
|advance|`list2 = b4`|—|

**Iteration 5:** `list1(a4) != null` and `list2(b4) != null` → runs. `list1.val(4) <= list2.val(4)` → true

| Step | Action | Result |
|------|--------|--------|
|create node|`node = new ListNode(4)` from list1|call it `n5`|
|append|`current.next = n5; current = n5`|... -> n5(4)|
|advance|`list1 = null` (a4 was the last node)|—|

**Loop check:** `list1 == null` → main loop exits.

State: merged so far `1 -> 1 -> 2 -> 3 -> 4`, remaining `list1 = null`, `list2 = [4]` (still pointing at `b4`)

### Phase 2 — Drain the remaining list

`while(list1 != null)`: `list1` is already `null` → doesn't run.

`while(list2 != null)`: `list2(b4) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|create node|`node = new ListNode(4)` from list2|call it `n6`|
|append|`current.next = n6; current = n6`|... -> n6(4)|
|advance|`list2 = null` (b4 was the last node)|—|

`list2 == null` → loop exits.

**Return:** `dummy.next` (n1), traversing: `1 -> 1 -> 2 -> 3 -> 4 -> 4 -> null`

Output:

```text
[1,1,2,3,4,4]
```

This matches the expected output.

---

## Why This Approach Works

Since both input lists are already individually sorted in non-decreasing order, the smallest remaining value across the whole merge is always sitting at the front of one of the two lists — never buried further in. So a simple "compare the two current fronts, take the smaller, advance that list" loop is guaranteed to emit values in overall non-decreasing order, one at a time, until one list is exhausted.

Once one list is empty, everything left in the other list is already sorted **and** every remaining value is `>=` everything already appended (since the exhausted list's last comparison already confirmed the other list's current front was `>=` it), so it can simply be copied over wholesale — which is exactly what the two drain loops do.

The dummy head is a standard trick for building a list from scratch: without it, the code would need an extra branch to handle attaching the very first node (since there's no "previous node" to link from before anything exists).

---

## Complexity Analysis

- **Time Complexity:** `O(m + n)` — where `m` and `n` are the lengths of `list1` and `list2`; the main loop and the two drain loops together visit every node of both lists exactly once
- **Space Complexity:** `O(m + n)` for the newly created output nodes, since this solution builds an entirely new list rather than reusing/re-linking the original nodes; excluding the output, only `O(1)` extra pointer variables are used

---

## Key Insight

Merging two already-sorted sequences only ever needs to look at their current fronts — this is the core idea behind the merge step of merge sort, applied directly to linked lists instead of arrays. The dummy-head pattern is what keeps the "build a list from nothing" logic uniform, so the very first append doesn't need special-case handling.

---

## Concepts Used

- Linked List
- Two Pointers
- Merge (as in Merge Sort)
- Dummy Head Node Technique