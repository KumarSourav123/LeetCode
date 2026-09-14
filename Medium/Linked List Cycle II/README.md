# LeetCode 142 - Linked List Cycle II

## Problem Statement

Given the `head` of a linked list, return *the node where the cycle begins*. If there is no cycle, return `null`.

There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the `next` pointer. Internally, `pos` is used to denote the index of the node that tail's `next` pointer is connected to (**0-indexed**). It is `-1` if there is no cycle. **Note that** `pos` **is not passed as a parameter**.

**Do not modify** the linked list.

---

## Example 1:
```
Input: head = [3,2,0,-4], pos = 1
Output: tail connects to node index 1
Explanation: There is a cycle in the linked list, where tail connects to the second node.
```

## Example 2:
```
Input: head = [1,2], pos = 0
Output: tail connects to node index 0
Explanation: There is a cycle in the linked list, where tail connects to the first node.
```

## Example 3:
```
Input: head = [1], pos = -1
Output: no cycle
Explanation: There is no cycle in the linked list.
```

## Constraints:
- The number of nodes in the list is in the range `[0, 10^4]`.
- `-10^5 <= Node.val <= 10^5`
- `pos` is `-1` or a valid index in the linked-list.

## Follow up:
Can you solve it using `O(1)` (i.e. constant) memory?

---

## Approach

This solution extends **Floyd's Cycle Detection Algorithm** with a second phase that pinpoints the exact **starting node** of the cycle, using the mathematical relationship between the distances traveled by the two pointers.

### Idea

- **Edge cases first:**
  - If `head == null` or `head.next == null`, the list is too short to contain a cycle — return `null`.
  - If `head.next == head` or `head.next.next == head`, a cycle exists and loops back to `head` almost immediately — return `head` directly.
- **Phase 1 — Detect a cycle (tortoise and hare):** advance `slow` one step and `fast` two steps at a time. If they ever meet (`slow == fast`), a cycle exists.
- **Phase 2 — Find the cycle's start:** once a meeting point is found, reset `slow` back to `head`, but leave `fast` at the meeting point. Then advance **both** `slow` and `fast` one step at a time (not two, this time) until they meet again. The node where they meet is guaranteed to be the **start of the cycle** — this relies on a well-known mathematical property of Floyd's algorithm (explained below).
- If the main loop exits without `slow` and `fast` ever meeting (i.e., `fast` reaches `null`), there's no cycle — return `null`.

---

## Code

```java
public class Solution {
    public ListNode detectCycle(ListNode head) {

        if (head == null || head.next == null) {
            return null;
        }

        if (head.next == head || head.next.next == head) {
            return head;
        }

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }

        return null;
    }
}
```

---

## Dry Run

### Input

```text
head = [3,2,0,-4], pos = 1   (n1(3) -> n2(2) -> n3(0) -> n4(-4) -> back to n2)
```

`head.next == head`? No. `head.next.next == head`? No — proceed to the main algorithm.

**Phase 1: Detect the cycle**

`slow = n1`, `fast = n1`

| Iteration | slow advances to | fast advances to | slow == fast? |
|-----------|----------------------|-----------------------|---------------------|
|1|n2|n3|No|
|2|n3|n2 (via n4 → n2)|No|
|3|n4|n4 (via n2 → n3 → n4)|**Yes → meeting point found**|

**Phase 2: Find the cycle start**

`slow` reset to `head (n1)`, `fast` stays at the meeting point `n4`

| slow == fast? | slow = slow.next | fast = fast.next |
|---------------------|--------------------------|--------------------------|
|n1 == n4? No|slow = n2|fast = n2 (via n4 → n2)|

`slow == fast`? `n2 == n2`? **Yes** → loop exits, return `slow = n2`

Output:

```text
node with value 2 (index 1)
```

This matches the expected output — the cycle begins at node index `1`.

---

## Why This Approach Works

**Phase 1's correctness** follows the same reasoning as basic cycle detection: if a cycle exists, `fast` (moving twice as fast) must eventually lap `slow` within the cycle and they will collide at some point inside it.

**Phase 2's correctness** relies on a distance relationship: let `L` be the distance from `head` to the cycle's start, and let the meeting point in Phase 1 be `k` steps into the cycle from its start. It can be shown mathematically that the remaining distance from the meeting point back around to the cycle's start (following the cycle) is exactly `L` steps as well (modulo the cycle length). Therefore, if one pointer starts fresh at `head` and another starts at the meeting point, and both move **one step at a time**, they are guaranteed to arrive at the cycle's starting node **simultaneously**.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — both phases together traverse the list a bounded number of times proportional to its length
- **Space Complexity:** `O(1)` — only pointer variables are used, satisfying the follow-up's constant-memory requirement

---

## Follow-up Considerations

The follow-up asks for an `O(1)` memory solution — this two-phase Floyd's algorithm approach already satisfies that. The alternative of tracking visited nodes in a `HashSet` (returning the first node found to be already present) would also correctly find the cycle's start, but would require `O(n)` extra space instead.

---

## Key Insight

This problem showcases the full power of Floyd's Cycle Detection Algorithm beyond simply detecting *whether* a cycle exists: the mathematical relationship between the pre-cycle distance and the meeting-point-to-cycle-start distance allows the exact starting node to be pinpointed using only two pointers and no extra data structures.

---

## Concepts Used

- Linked List
- Two Pointers (Fast & Slow)
- Floyd's Cycle Detection Algorithm
- Constant Space Algorithm