# LeetCode 234 - Palindrome Linked List

## Problem Statement

Given the `head` of a singly linked list, return `true` if it is a **palindrome** or `false` otherwise.

---

## Example 1:
```
Input: head = [1,2,2,1]
Output: true
```

## Example 2:
```
Input: head = [1,2]
Output: false
```

## Constraints:
- The number of nodes in the list is in the range `[1, 10^5]`.
- `0 <= Node.val <= 9`

## Follow up:
Could you do it in `O(n)` time and `O(1)` space?

---

## Approach

This solution combines three classic linked list techniques: the **fast/slow pointer** to find the middle, **in-place reversal** of the second half, and a final **two-pointer comparison** — all without using any extra array or stack.

### Idea

- **Step 1 — Find the middle using fast & slow pointers:** advance `slow` one node at a time and `fast` two nodes at a time. When `fast` reaches the end (`fast == null` or `fast.next == null`), `slow` sits at the start of the second half of the list.
- **Step 2 — Reverse the second half in place:** starting from `slow`, perform a standard iterative linked list reversal (tracking `prev`, `current`, and `after`) so that the second half of the list now points backward, with `prev` ending up as the head of this reversed second half.
- **Step 3 — Compare both halves:** use `first` starting at the original `head` and `second` starting at `prev` (the head of the reversed second half). Walk both pointers forward simultaneously, comparing `first.val` to `second.val` at each step:
  - If any pair of values doesn't match, the list isn't a palindrome — return `false` immediately.
  - If `second` reaches `null` (meaning the shorter reversed-half comparison has fully completed), every pair matched — return `true`.

Because the second half was physically reversed in place, comparing it against the first half front-to-back is equivalent to comparing the original list's front half against its back half in mirrored order — exactly what a palindrome check requires.

---

## Code

```java
class Solution {
    public boolean isPalindrome(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode prev = null;

        while(slow != null){
            ListNode after = slow.next;
            slow.next = prev;
            prev = slow;
            slow = after;
        }

        ListNode first = head;
        ListNode second = prev;

        while (second != null) {
            if (first.val != second.val) {
                return false;
            }
            first = first.next;
            second = second.next;
        }
        return true;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,2,1]   (n1 -> n2 -> n3 -> n4 -> null, values 1,2,2,1)
```

**Step 1: Find the middle**

`slow = n1`, `fast = n1`

| Iteration | slow advances to | fast advances to |
|-----------|----------------------|-----------------------|
|1|n2|n3|
|2|n3|null|

Loop exits (`fast == null`). `slow = n3` (start of second half: `n3 -> n4`)

**Step 2: Reverse the second half (starting at n3)**

`prev = null`

| slow (val) | after = slow.next | slow.next = prev | prev = slow | slow = after |
|-------------------|----------------------------|--------------------------|---------------------|-----------------------|
|n3 (2)|n4|n3.next = null|prev = n3|slow = n4|
|n4 (1)|null|n4.next = n3|prev = n4|slow = null|

Reversed second half: `n4(1) -> n3(2) -> null`. `prev = n4`

**Step 3: Compare first half vs. reversed second half**

`first = head (n1)`, `second = prev (n4)`

| first.val | second.val | Equal? | Action |
|-----------|------------|--------|--------|
|1 (n1)|1 (n4)|Yes|first = n2, second = n3|
|2 (n2)|2 (n3)|Yes|first = n3 (original forward link), second = n3.next = null (reversed during Step 2)|

`second == null` → loop exits.

Output:

```text
true
```

This matches the expected output.

---

## Why This Approach Works

The fast/slow pointer technique reliably locates the midpoint of the list in a single pass, without needing to know the list's length in advance. Reversing the second half in place (rather than copying it into an array) means the whole algorithm uses only a handful of pointer variables — no auxiliary data structure proportional to the list's size is ever needed.

Comparing `first` (walking forward from the original head) against `second` (walking forward from the head of the *reversed* second half) is mathematically equivalent to comparing the original list's elements symmetrically from both ends inward — which is exactly the definition of a palindrome.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — finding the middle, reversing the second half, and comparing both halves are each `O(n)`, giving `O(n)` total
- **Space Complexity:** `O(1)` — only a constant number of pointer variables are used, satisfying the follow-up's constant-space requirement

---

## Follow-up Considerations

The follow-up asks for `O(n)` time and `O(1)` space — this solution already achieves exactly that. A simpler but less space-efficient alternative would be to copy all values into an `ArrayList` or array first, then use two-pointer comparison from both ends — that approach is `O(n)` time but `O(n)` space, since it requires storing every value separately.

---

## Key Insight

This problem elegantly combines three fundamental linked list patterns into one solution: fast/slow pointers to find the midpoint without pre-counting the list, in-place reversal to avoid extra storage, and a final synchronized two-pointer walk to perform the actual comparison — together achieving the optimal `O(n)` time / `O(1)` space bound.

---

## Concepts Used

- Linked List
- Fast & Slow Pointers
- In-Place Reversal
- Two Pointers
- Constant Space Algorithm