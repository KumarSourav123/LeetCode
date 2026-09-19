# LeetCode 876 - Middle of the Linked List

## Problem Statement

Given the `head` of a singly linked list, return *the middle node of the linked list*.

If there are two middle nodes, return **the second middle** node.

---

## Example 1:
```
Input: head = [1,2,3,4,5]
Output: [3,4,5]
Explanation: The middle node of the list is node 3.
```

## Example 2:
```
Input: head = [1,2,3,4,5,6]
Output: [4,5,6]
Explanation: Since the list has two middle nodes with values 3 and 4, we return the second one.
```

## Constraints:
- The number of nodes in the list is in the range `[1, 100]`.
- `1 <= Node.val <= 100`

---

## Approach

This solution uses the standard **slow/fast pointer** technique: `slow` advances one node at a time while `fast` advances two nodes at a time, so by the time `fast` reaches the end of the list, `slow` has covered exactly half the distance and is sitting on the middle node.

### Idea

- Start both `slow` and `fast` at `head`.
- **Loop** `while(fast != null && fast.next != null)`:
  - `slow = slow.next` — advance `slow` one step.
  - `fast = fast.next.next` — advance `fast` two steps.
- The loop condition is what naturally produces the "second middle node" behavior for even-length lists: it only keeps running as long as `fast` has **two** more nodes to consume. For an even-length list, `fast` runs out of a full pair of steps exactly when `slow` has reached the *second* of the two middle nodes, at which point the loop stops.
- Once the loop exits (`fast` is `null`, or `fast.next` is `null`), `slow` is sitting on the correct middle node.
- Return `slow`.

---

## Code

```java
class Solution {
    public ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5,6]   (n1 -> n2 -> n3 -> n4 -> n5 -> n6 -> null)
```

`slow = n1`, `fast = n1`

**Iteration 1:** `fast(n1) != null` and `fast.next(n2) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n1.next = n2|slow = n2|
|fast = fast.next.next|n1.next.next = n3|fast = n3|

**Iteration 2:** `fast(n3) != null` and `fast.next(n4) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n2.next = n3|slow = n3|
|fast = fast.next.next|n3.next.next = n5|fast = n5|

**Iteration 3:** `fast(n5) != null` and `fast.next(n6) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n3.next = n4|slow = n4|
|fast = fast.next.next|n5.next.next = n6.next = null|fast = null|

**Loop check:** `fast == null` → loop exits.

**Return:** `slow` (n4), traversing: `4 -> 5 -> 6 -> null`

Output:

```text
[4,5,6]
```

This matches the expected output — since the list has two middle nodes (values 3 and 4), the second one (4) is correctly returned.

---

## Why This Approach Works

Since `fast` always moves exactly twice as fast as `slow`, whenever `fast` finishes traversing the list, `slow` has covered exactly half the distance. The precise way the loop condition is written (`fast != null && fast.next != null`) is what determines *which* middle node gets returned when the list has two: the loop keeps going as long as there are two more nodes left for `fast` to consume, so on an even-length list it runs one extra half-step compared to what would leave `slow` on the first middle node — landing it on the second middle node instead, exactly as the problem requires.

For an odd-length list, `fast` naturally lands exactly on the last node (`fast.next == null` stops the loop), and `slow` has landed precisely on the single true middle node.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — `fast` traverses the list once (two nodes per iteration), so the loop runs roughly `n/2` times
- **Space Complexity:** `O(1)` — only two pointer variables are used, no new nodes or auxiliary data structures

---

## Key Insight

The slow/fast (or "tortoise and hare") pointer pattern is the standard way to find a linked list's midpoint in a single pass without first counting its length — and the exact loop condition chosen (`fast != null && fast.next != null` vs. a stricter variant) is what decides, without any extra branching, whether the first or second of two middle nodes is returned on even-length lists.

---

## Concepts Used

- Linked List
- Two Pointers (Slow & Fast)
- Single-Pass Traversal
- Constant Space Algorithm