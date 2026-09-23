# LeetCode 148 - Sort List

## Problem Statement

Given the `head` of a linked list, return *the list after sorting it in* **ascending order**.

---

## Example 1:
```
Input: head = [4,2,1,3]
Output: [1,2,3,4]
```

## Example 2:
```
Input: head = [-1,5,3,4,0]
Output: [-1,0,3,4,5]
```

## Example 3:
```
Input: head = []
Output: []
```

## Constraints:
- The number of nodes in the list is in the range `[0, 5 * 10^4]`.
- `-10^5 <= Node.val <= 10^5`

**Follow up:** Can you sort the linked list in `O(n log n)` time and `O(1)` memory (i.e. constant space)?

---

## Approach

This solution implements **merge sort directly on the linked list**: recursively split the list in half using a slow/fast pointer to find the midpoint, sort each half recursively, then merge the two sorted halves back together.

### Idea

- **`sortList` (the main recursive function):**
  - **Base case:** if the list is empty or has only one node, it's already sorted — return `head` as-is.
  - **Find the midpoint** with the `middle` helper.
  - **Split the list in two:** `leftHead = head` (the front half), `rightHead = middle.next` (the back half), then `middle.next = null` severs the link between them, turning one list into two independent lists.
  - **Recurse on each half:** `leftHead = sortList(leftHead)` and `rightHead = sortList(rightHead)` sort each half independently — this is where the recursion bottoms out at single-node (already-sorted) lists.
  - **Merge the two sorted halves** with the `mergeFinal` helper and return the result.
- **`middle` helper** — locates the midpoint using a slow/fast pointer, but with `fast` starting one step ahead (`fast = head.next`) so that on an even-length list, `slow` lands on the **first** of the two middle nodes rather than the second. This specific choice is what makes the split "balanced" (e.g. a 4-node list splits into 2 + 2, not 1 + 3), which is what merge sort needs for correctness and its `O(n log n)` guarantee.
- **`mergeFinal` helper** — a standard two-pointer merge of two already-sorted lists using a dummy head and a `temp` tail pointer:
  - While both `first` and `second` are non-null, whichever has the smaller-or-equal value gets relinked (`temp.next = first` or `second`) and its own pointer advances.
  - Once one list is exhausted, whichever list still has nodes left is attached in a single step (`temp.next = first` or `temp.next = second`), since it's already sorted.
  - Return `dummy.next`, the head of the merged, sorted list.

---

## Code

```java
class Solution {
    public ListNode sortList(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }

        ListNode middle = middle(head);

        ListNode leftHead = head;
        ListNode rightHead = middle.next;
        middle.next = null;

        leftHead = sortList(leftHead);
        rightHead = sortList(rightHead);

        return mergeFinal(leftHead, rightHead);
    }

    private ListNode middle(ListNode head){
        ListNode slow = head;
        ListNode fast = head.next;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private ListNode mergeFinal(ListNode first , ListNode second){
        ListNode dummy = new ListNode(0);
        ListNode temp = dummy;

        while(first != null && second != null){
            if(first.val <= second.val){
                temp.next = first;
                temp = first;
                first = first.next;
            }else{
                temp.next = second;
                temp = second;
                second = second.next;
            }
        }

        if(first != null){
            temp.next = first;
        }else{
            temp.next = second;
        }

        return dummy.next;
    }
}
```

---

## Dry Run

### Input

```text
head = [4,2,1,3]   (n0(4) -> n1(2) -> n2(1) -> n3(3) -> null)
```

### Top-level call: sortList(n0..n3)

`head.next != null` → proceed.

**Find the middle:** `slow = n0`, `fast = head.next = n1`

| Iteration | Check | slow | fast |
|-----------|-------|------|------|
|1|`fast(n1)!=null`, `fast.next(n2)!=null` → runs|n1|n3|
|2|`fast(n3)!=null`, `fast.next(n3.next)==null` → exits|n1|n3|

`middle = n1`

**Split:** `leftHead = n0`, `rightHead = middle.next = n2`, then `middle.next = null` → `n1.next = null`

Two independent lists: **Left** = `n0(4) -> n1(2) -> null`, **Right** = `n2(1) -> n3(3) -> null`

### Recursive call: sortList(Left = 4,2)

`head.next != null` → proceed. **Find the middle:** `slow = n0`, `fast = n0.next = n1`. `fast.next` is `null` (already cut) → loop doesn't run. `middle = n0`.

**Split:** `leftHead = n0`, `rightHead = middle.next = n1`, then `n0.next = null`

Sub-lists: `n0(4) -> null` and `n1(2) -> null` — both single nodes, so both recursive calls hit the **base case** and return immediately: `sortList(n0) = n0`, `sortList(n1) = n1`.

**Merge:** `mergeFinal(n0, n1)` — `first = n0(4)`, `second = n1(2)`

| Step | Check | Action | Result |
|------|-------|--------|--------|
|1|`4 <= 2`? no|`temp.next = n1; temp = n1; second = null`|d -> n1|
|loop exits|`second == null`|—|—|
|leftover|`first(n0) != null`|`temp.next = n0`|d -> n1 -> n0|

Return `dummy.next = n1`: **`2 -> 4 -> null`**

### Recursive call: sortList(Right = 1,3)

`head.next != null` → proceed. **Find the middle:** `slow = n2`, `fast = n2.next = n3`. `fast.next` is `null` (list end) → loop doesn't run. `middle = n2`.

**Split:** `leftHead = n2`, `rightHead = middle.next = n3`, then `n2.next = null`

Sub-lists: `n2(1) -> null` and `n3(3) -> null` — both base cases: `sortList(n2) = n2`, `sortList(n3) = n3`.

**Merge:** `mergeFinal(n2, n3)` — `first = n2(1)`, `second = n3(3)`

| Step | Check | Action | Result |
|------|-------|--------|--------|
|1|`1 <= 3`? yes|`temp.next = n2; temp = n2; first = null`|d2 -> n2|
|loop exits|`first == null`|—|—|
|leftover|`second(n3) != null`, else branch|`temp.next = n3`|d2 -> n2 -> n3|

Return `dummy.next = n2`: **`1 -> 3 -> null`**

### Back to the top level: mergeFinal(Left = 2→4, Right = 1→3)

`first = n1(2)`, `second = n2(1)`

| Step | Check | Action | Result so far |
|------|-------|--------|----------------|
|1|`2 <= 1`? no|`temp.next = n2 (1); temp = n2; second = n3 (3)`|d -> n2|
|2|`2 <= 3`? yes|`temp.next = n1 (2); temp = n1; first = n0 (4)`|d -> n2 -> n1|
|3|`4 <= 3`? no|`temp.next = n3 (3); temp = n3; second = null`|d -> n2 -> n1 -> n3|

**Loop exits:** `second == null`. **Leftover:** `first(n0) != null` → `temp.next = n0`

Final chain: `d -> n2(1) -> n1(2) -> n3(3) -> n0(4) -> null`

**Return:** `dummy.next` (n2), traversing: `1 -> 2 -> 3 -> 4 -> null`

Output:

```text
[1,2,3,4]
```

This matches the expected output.

---

## Why This Approach Works

Merge sort's correctness doesn't depend on the underlying data structure — it only needs the ability to (1) split a sequence into two roughly equal halves, (2) recursively sort each half, and (3) merge two sorted sequences back together in order. A linked list supports all three operations directly: the midpoint can be found with a slow/fast pointer in one pass (no random access needed), splitting is just cutting a `.next` link, and merging is the same two-pointer technique used to merge any two sorted sequences.

Starting `fast` one step ahead in the `middle` helper (`fast = head.next` rather than `fast = head`) is a deliberate choice: it makes `slow` land on the **first** of two middle nodes for even-length lists, which guarantees the two halves are as close to equal size as possible on every split — this even splitting is exactly what gives merge sort its `O(n log n)` time bound; an unbalanced split (e.g. always 1 node vs. the rest) would degrade it toward `O(n^2)`.

---

## Complexity Analysis

- **Time Complexity:** `O(n log n)` — the list is split in half at each recursive level (`log n` levels), and merging all the sub-lists back together at each level costs `O(n)` total work per level, giving `O(n log n)` overall — matching the follow-up's target
- **Space Complexity:** `O(log n)` for the recursion call stack (one frame per level of splitting); no new nodes are created and no auxiliary arrays or hash structures are used, so aside from the recursion stack this is effectively in-place. (Note: this does not satisfy the follow-up's stricter `O(1)` memory bound, which would require an iterative bottom-up merge sort instead of recursion.)

---

## Key Insight

Merge sort translates naturally onto linked lists because its two "expensive" operations on arrays — splitting (normally requiring index arithmetic) and merging (normally requiring a second array) — become simple pointer operations instead: cutting a `.next` link to split, and relinking existing nodes (rather than copying values) to merge. The one subtlety worth internalizing is the "which pointer starts ahead" detail in the midpoint-finding helper, since it's what decides which of the two middle nodes an even-length list's front half ends on — and getting that consistently balanced is what preserves the `O(n log n)` guarantee.

---

## Concepts Used

- Linked List
- Merge Sort
- Divide and Conquer / Recursion
- Two Pointers (Slow & Fast)
- Two-Pointer Merge of Sorted Lists