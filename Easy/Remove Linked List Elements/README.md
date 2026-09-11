# LeetCode 203 - Remove Linked List Elements

## Problem Statement

Given the `head` of a linked list and an integer `val`, remove all the nodes of the linked list that has `Node.val == val`, and return *the new head*.

---

## Example 1:
```
Input: head = [1,2,6,3,4,5,6], val = 6
Output: [1,2,3,4,5]
```

## Example 2:
```
Input: head = [], val = 1
Output: []
```

## Example 3:
```
Input: head = [7,7,7,7], val = 7
Output: []
```

## Constraints:
- The number of nodes in the list is in the range `[0, 10^4]`.
- `1 <= Node.val <= 50`
- `0 <= val <= 50`

---

## Approach

This solution handles removing matching nodes at the **head** separately from removing matching nodes **elsewhere** in the list, since removing the head requires reassigning the `head` reference itself, while removing an interior node only requires relinking `next` pointers.

### Idea

- **Handle leading matches first:** while `head` is not `null` and `head.val == val`, advance `head = head.next` — this strips away any run of matching nodes right at the start of the list (including handling the case where the *entire* list consists of matching values).
- **Empty list check:** if `head` becomes `null` after that loop (either the list was originally empty, or every node matched `val`), return `head` (which is `null`) immediately.
- **Remove matches from the rest of the list:** starting a `current` pointer at the (now guaranteed non-matching) `head`, walk through the list:
  - If `current.next.val == val`, the next node is a match — **skip over it** by relinking `current.next = current.next.next`, effectively removing it from the list. Note `current` does **not** advance in this case, since the *new* `current.next` might itself also need checking.
  - Otherwise, the next node doesn't match — simply advance `current = current.next` to continue scanning forward.
  - Continue until `current.next == null` (reached the end of the list).
- Return the (possibly updated) `head`.

---

## Code

```java
class Solution {
    public ListNode removeElements(ListNode head, int val) {

        while (head != null && head.val == val) {
            head = head.next;
        }
        if (head == null) {
            return head;
        }

        ListNode current = head;
        while (current.next != null) {
            if (current.next.val == val) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
        return head;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,6,3,4,5,6], val = 6
```

**Step 1: Strip leading matches.** `head.val = 1 == 6`? No → loop doesn't execute, `head` stays at node `1`.

**Step 2: Empty check.** `head == null`? No → continue.

**Step 3: Scan and remove interior matches.** `current = head` (node `1`)

| current | current.next | current.next.val == 6? | Action |
|---------|----------------|------------------------------|--------|
|1|2|No|current = 2|
|2|6 (1st)|**Yes**|current.next = 6's next (node 3) → node `6` removed|
|2|3|No|current = 3|
|3|4|No|current = 4|
|4|5|No|current = 5|
|5|6 (2nd)|**Yes**|current.next = 6's next (null) → node `6` removed|

**Loop check:** `current.next == null` (current is now node `5`, and its next is `null`) → loop exits.

**Return:** `head` (node `1`), traversing: `1 -> 2 -> 3 -> 4 -> 5 -> null`

Output:

```text
[1,2,3,4,5]
```

This matches the expected output — both occurrences of `6` were correctly removed.

---

## Why This Approach Works

Separating the "leading matches" case from the "interior matches" case is necessary because removing the very first node(s) requires changing what `head` itself points to, whereas removing any other node only requires relinking the `next` pointer of the node **before** it — a `current` pointer walking the list can only ever look ahead and relink `current.next`, so it can never directly remove the node it currently sits on (which is why the head must be handled separately, before `current` even starts).

Not advancing `current` after a removal is essential: since the node that took the removed node's place might *also* match `val` (as could happen with several duplicates in a row), staying at the same `current` and re-checking `current.next` ensures every consecutive run of matches gets fully cleared, not just every other one.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — a single pass through the list (plus the initial leading-match strip, which together still only visit each node a constant number of times)
- **Space Complexity:** `O(1)` — only a constant number of pointer variables are used

---

## Key Insight

The core insight for any "remove nodes from a linked list" problem is that removal is really a **relinking** operation performed by the node *before* the one being removed — this is why walking with a `current` pointer that inspects `current.next` (rather than inspecting `current` itself) is the natural pattern, and why the head of the list needs special handling since there's no "node before" it to perform the relink.

---

## Concepts Used

- Linked List
- Node Removal via Relinking
- Head Pointer Handling
- Iterative Traversal