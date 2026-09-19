# LeetCode 141 - Linked List Cycle

## Problem Statement

Given `head`, the head of a linked list, determine if the linked list has a cycle in it.

There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the `next` pointer. Internally, `pos` is used to denote the index of the node that the tail's `next` pointer is connected to. Note that `pos` is not passed as a parameter.

Return `true` *if there is a cycle in the linked list*. Otherwise, return `false`.

---

## Example 1:
```
Input: head = [3,2,0,-4], pos = 1
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).
```

## Example 2:
```
Input: head = [1,2], pos = 0
Output: true
Explanation: There is a cycle in the linked list, where the tail connects to the 0th node.
```

## Example 3:
```
Input: head = [1], pos = -1
Output: false
Explanation: There is no cycle in the linked list.
```

## Constraints:
- The number of the nodes in the list is in the range `[0, 10^4]`.
- `-10^5 <= Node.val <= 10^5`
- `pos` is `-1` or a valid index in the linked-list.

**Follow up:** Can you solve it using `O(1)` (i.e. constant) memory?

---

## Approach

This solution uses **Floyd's Cycle Detection** (the "tortoise and hare" technique): `slow` advances one node at a time while `fast` advances two nodes at a time. If there's no cycle, `fast` reaches the end of the list first. If there **is** a cycle, `fast` will eventually lap `slow` from behind and the two pointers will land on the exact same node.

### Idea

- **Edge case:** if `head` is `null`, there are no nodes at all, so there can't be a cycle — return `false` immediately.
- Start both `slow` and `fast` at `head`.
- **Loop** `while(fast != null && fast.next != null)` — this condition guards against a `NullPointerException` on `fast.next.next` and also naturally handles the "no cycle" case, since a `fast` that keeps advancing two-at-a-time down an acyclic list will eventually hit `null`.
  - `slow = slow.next` — advance `slow` one step.
  - `fast = fast.next.next` — advance `fast` two steps.
  - **Check for a meeting:** `if(slow == fast)`, the two pointers have landed on the same node — this can only happen if there's a cycle (in an acyclic list, `fast` is always strictly ahead of `slow` and they could never coincide), so return `true` immediately.
- If the loop exits normally (`fast` or `fast.next` became `null`), the list has a genuine end, so there's no cycle — return `false`.

---

## Code

```java
public class Solution {
    public boolean hasCycle(ListNode head) {

        if(head == null){
            return false;
        }

        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null){

            slow = slow.next;
            fast = fast.next.next;

            if(slow == fast){
                return true;
            }

        }

        return false;
    }
}
```

---

## Dry Run

### Input

```text
head = [3,2,0,-4], pos = 1

n1(3) -> n2(2) -> n3(0) -> n4(-4) -> back to n2 (cycle at index 1)
```

`head != null` → proceed. `slow = n1`, `fast = n1`

**Iteration 1:** `fast(n1) != null` and `fast.next(n2) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n1.next = n2|slow = n2|
|fast = fast.next.next|n1.next.next = n3|fast = n3|
|slow == fast?|n2 vs n3|no|

**Iteration 2:** `fast(n3) != null` and `fast.next(n4) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n2.next = n3|slow = n3|
|fast = fast.next.next|n3.next.next = n4.next = n2 (cycle)|fast = n2|
|slow == fast?|n3 vs n2|no|

**Iteration 3:** `fast(n2) != null` and `fast.next(n3) != null` → loop runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n3.next = n4|slow = n4|
|fast = fast.next.next|n2.next.next = n3.next = n4|fast = n4|
|slow == fast?|n4 vs n4|**yes** → `return true`|

**Return:** `true`

Output:

```text
true
```

This matches the expected output — the list does contain a cycle.

---

## Why This Approach Works

In an acyclic list, `fast` always stays strictly ahead of `slow` (it moves twice as far every iteration), so the two can never land on the same node — `fast` simply reaches `null` first, which is exactly what makes the loop terminate and fall through to `return false`.

If a cycle exists, both pointers eventually enter it and can never leave (there's no `null` to escape to). Once both are inside the cycle, `fast` is closing the gap on `slow` by exactly one node per iteration (it gains 2 steps while `slow` gains 1, net 1 step of relative closing distance each round) — since the cycle has a finite length, that gap is guaranteed to shrink to zero within at most one full lap of the cycle, at which point `slow == fast` and a cycle is correctly reported.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — in the no-cycle case, `fast` traverses the list once at double speed; in the cycle case, `slow` takes at most one full lap of the cycle after entering it before `fast` catches up, which is still bounded by the total number of nodes
- **Space Complexity:** `O(1)` — only two pointer variables are used, no new nodes, visited-set, or auxiliary data structures, satisfying the follow-up's constant-memory requirement

---

## Key Insight

Floyd's Cycle Detection turns "does this list loop back on itself?" into a pure relative-speed argument: two pointers moving at different speeds through a cycle are mathematically guaranteed to meet, the same way two runners on a circular track at different paces eventually lap each other — no extra memory (like a hash set of visited nodes) is needed to detect the cycle, which is what makes this the standard `O(1)`-space solution.

---

## Concepts Used

- Linked List
- Two Pointers (Slow & Fast)
- Floyd's Cycle Detection (Tortoise and Hare)
- Constant Space Algorithm