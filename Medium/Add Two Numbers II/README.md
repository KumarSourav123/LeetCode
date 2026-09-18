# LeetCode 445 - Add Two Numbers II

## Problem Statement

You are given two **non-empty** linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

---

## Example 1:
```
Input: l1 = [7,2,4,3], l2 = [5,6,4]
Output: [7,8,0,7]
```

## Example 2:
```
Input: l1 = [2,4,3], l2 = [5,6,4]
Output: [8,0,7]
```

## Example 3:
```
Input: l1 = [0], l2 = [0]
Output: [0]
```

## Constraints:
- The number of nodes in each linked list is in the range `[1, 100]`.
- `0 <= Node.val <= 9`
- It is guaranteed that the list represents a number that does not have leading zeros.

**Follow up:** Could you solve it without reversing the input lists?

---

## Approach

This solution sidesteps the "most significant digit first" layout by **reversing both input lists first**, so addition can proceed digit-by-digit from the least significant end exactly like adding numbers on paper, then reversing the result at the end to restore most-significant-first order.

### Idea

- **Reverse both inputs:** `head1 = reverse(l1)` and `head2 = reverse(l2)` flip each list so its first node is now the least significant digit.
- Set up `carry = 0` and a dummy head (`dummy`) with `current` pointing at it, so digits of the result can be appended one at a time without special-casing the first node.
- **Main loop**, while either `head1` or `head2` is non-null:
  - Start `sum = carry`, then add `head1.val` if `head1` is non-null, and `head2.val` if `head2` is non-null. This naturally handles lists of unequal length — once one list runs out, its digits just stop contributing.
  - Create a new node holding `sum % 10` (the digit to keep) and update `carry = sum / 10` (what rolls over to the next column).
  - Append the new node: `current.next = node; current = node;`.
  - Advance whichever of `head1` / `head2` is still non-null.
- **After the loop:** if `carry != 0`, one more digit is left over (e.g. `9 + 9 = 18` on the last column), so append a final node holding `carry`.
- The result built in `dummy.next` is least-significant-first (since it was built by walking the reversed inputs). Reverse it once more — `reverse(dummy.next)` — to put it back into the required most-significant-first order, and return that as `head`.
- A private `reverse(ListNode head)` helper does the standard iterative in-place reversal (`current`/`prev`/`after`), reused for both the inputs and the final output.

---

## Code

```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head1 = reverse(l1);
        ListNode head2 = reverse(l2);
        int carry = 0;
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while(head1 != null || head2 != null){
            int sum = carry;
            if(head1 != null){
                sum = head1.val + sum;
            }
            if(head2 != null){
                sum = head2.val + sum;
            }

            ListNode node = new ListNode(sum % 10);
            carry = sum/10;

            current.next = node;
            current = node;

            if(head1 != null){
                head1 = head1.next;
            }
            if(head2 != null){
                head2 = head2.next;
            }
        }

        if(carry != 0){
            ListNode node = new ListNode(carry);
            current.next = node;
            current = node;
        }

        ListNode head = reverse(dummy.next);

        return head;
    }

    private ListNode reverse(ListNode head){
        ListNode current = head;
        ListNode prev = null;

        while(current != null){
            ListNode after = current.next;
            current.next = prev;
            prev = current;
            current = after;
        }

        return prev;
    }
}
```

---

## Dry Run

### Input

```text
l1 = [7,2,4,3]   (n1 -> n2 -> n4 -> n3, representing 7243)
l2 = [5,6,4]     (m5 -> m6 -> m4, representing 564)
```

### Phase 1 — Reverse both inputs

`head1 = reverse(l1)` → chain becomes `n3 -> n4 -> n2 -> n1` (least significant digit first: 3, 4, 2, 7)
`head2 = reverse(l2)` → chain becomes `m4 -> m6 -> m5` (least significant digit first: 4, 6, 5)

### Phase 2 — Main addition loop

`carry = 0`, `dummy = d0`, `current = d0`

**Iteration 1:** `head1(n3) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|sum = carry + head1.val + head2.val|sum = 0 + 3 + 4|sum = 7|
|node = new node(sum % 10)|node.val = 7|carry = 7/10 = 0|
|append node|d0 -> 7|current = node(7)|
|advance|head1 = n4, head2 = m6|—|

**Iteration 2:** `head1(n4) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|sum = carry + head1.val + head2.val|sum = 0 + 4 + 6|sum = 10|
|node = new node(sum % 10)|node.val = 0|carry = 10/10 = 1|
|append node|d0 -> 7 -> 0|current = node(0)|
|advance|head1 = n2, head2 = m5|—|

**Iteration 3:** `head1(n2) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|sum = carry + head1.val + head2.val|sum = 1 + 2 + 5|sum = 8|
|node = new node(sum % 10)|node.val = 8|carry = 8/10 = 0|
|append node|d0 -> 7 -> 0 -> 8|current = node(8)|
|advance|head1 = n1, head2 = null (m5 exhausted)|—|

**Iteration 4:** `head1(n1) != null` → runs (head2 is null, its `.val` is skipped)

| Step | Action | Result |
|------|--------|--------|
|sum = carry + head1.val|sum = 0 + 7|sum = 7|
|node = new node(sum % 10)|node.val = 7|carry = 7/10 = 0|
|append node|d0 -> 7 -> 0 -> 8 -> 7|current = node(7)|
|advance|head1 = null|—|

**Loop check:** `head1 == null` and `head2 == null` → loop exits.

`carry == 0`, so no extra node is appended.

### Phase 3 — Reverse the result

`dummy.next` = `7 -> 0 -> 8 -> 7` → `reverse(dummy.next)` → `7 -> 8 -> 0 -> 7`

**Return:** `head` (the reversed result), traversing: `7 -> 8 -> 0 -> 7 -> null`

Output:

```text
[7,8,0,7]
```

This matches the expected output (7243 + 564 = 7807).

---

## Why This Approach Works

Addition is naturally a least-significant-digit-first operation — carries flow from the ones column outward — but the input lists are stored most-significant-first. Reversing both lists up front converts the problem into the same shape as elementary column addition: walk both chains in lockstep from the "ones" end, track a running carry, and produce one output digit per step, with unequal-length inputs and a possible final carry handled by simple null-checks rather than special cases.

Since the digits are produced ones-place-first, the result chain itself is temporarily in the wrong order; reversing it once more (`reverse(dummy.next)`) restores the most-significant-first order the problem requires. Using a dummy head to build the result avoids having to special-case attaching the very first digit.

Reusing a single `reverse` helper for both the two inputs and the final output keeps the logic in one place — it's the same in-place `current`/`prev`/`after` pattern applied three times.

Note this is **not** the follow-up solution: the problem also asks whether it can be solved without reversing the input lists (e.g. using stacks), which this approach doesn't attempt.

---

## Complexity Analysis

- **Time Complexity:** `O(m + n)` — where `m` and `n` are the lengths of `l1` and `l2`; each list is reversed once (`O(m)` and `O(n)`), the addition loop runs once per node of the longer list (`O(max(m,n))`), and the final reversal touches the output list (`O(max(m,n))`), so overall linear in the total input size
- **Space Complexity:** `O(max(m,n))` for the newly created output nodes (unavoidable, since a new list must be returned); beyond that, only `O(1)` extra pointer variables are used — the reversals themselves are in-place

---

## Key Insight

When a problem's natural operation (addition, comparison, etc.) wants to move in the opposite direction from how the data is laid out, **reversing first** can be simpler and more robust than trying to process the original order with recursion or an explicit stack. Reversing twice — once to align with the operation, once to restore the required output order — is a cheap `O(n)` trade that keeps the core logic (the addition loop) clean and easy to reason about.

---

## Concepts Used

- Linked List
- In-Place Linked List Reversal
- Simulated Column Addition with Carry
- Dummy Head Node Technique