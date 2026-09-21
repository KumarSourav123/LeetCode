# LeetCode 61 - Rotate List

## Problem Statement

Given the `head` of a linked list, rotate the list to the right by `k` places.

---

## Example 1:
```
Input: head = [1,2,3,4,5], k = 2
Output: [4,5,1,2,3]
```

## Example 2:
```
Input: head = [0,1,2], k = 4
Output: [2,0,1]
```

## Constraints:
- The number of nodes in the list is in the range `[0, 500]`.
- `-100 <= Node.val <= 100`
- `0 <= k <= 2 * 10^9`

---

## Approach

This solution finds the list's length up front, reduces `k` modulo that length to eliminate redundant full rotations, then locates the new **break point** using a `for` loop offset followed by a slow/fast walk to the tail — and finally splices the list into its rotated form by reconnecting three pointers.

### Idea

- **Edge case:** if the list is empty or has only one node, rotating it changes nothing — return `head` as-is.
- **First pass — measure the length:** walk `temp` through the whole list, counting nodes into `size`.
- **Reduce `k`:** `val = k % size` collapses away any full rotations (rotating a list of length `size` by `size` places returns it to its original form), leaving only the "effective" rotation amount, `0 <= val < size`.
- **Advance `fast` by `val` steps:** a `for` loop from `1` to `val` moves `fast` forward `val` nodes from `head`.
- **Handle the no-op case:** if `fast == head` (which happens exactly when `val == 0`, i.e. `k` was a multiple of the length), no rotation is actually needed — return `head` directly. (The `fast == null` check alongside it is a defensive guard; given `val < size`, `fast` can't actually run off the end here.)
- **Second pass — find the new break point:** with `slow` starting back at `head`, advance `slow` and `fast` together, one step each, `while(fast.next != null)`. Since `fast` already has a `val`-node head start, by the time `fast` reaches the **last** node of the list, `slow` has landed exactly `val` nodes short of the end — precisely the node that needs to become the **new tail** of the front segment.
- **Splice the list:**
  - `extra = slow.next` — save the node that will become the **new head** (the start of what was the last `val` nodes).
  - `slow.next = null` — cut the original list in two, ending the (soon-to-be) second half's front segment.
  - `fast.next = head` — connect the old tail (`fast`, still the last node of the original list) back to the original `head`, forming the new tail-to-front link.
- Return `extra` — the new head of the rotated list.

---

## Code

```java
class Solution {
    public ListNode rotateRight(ListNode head, int k) {

        if(head == null || head.next == null){
            return head;
        }

        ListNode temp = head;
        int size = 0;

        while(temp != null){
            temp = temp.next;
            size++;
        }

        ListNode fast = head;
        int val = k % size;

        for(int i = 1 ; i <= val ; i++){
            fast = fast.next;
        }

        if(fast == null || fast == head){
            return head;
        }

        ListNode slow = head;
        while(fast.next != null){
            slow = slow.next;
            fast = fast.next;
        }

        ListNode extra = slow.next;
        slow.next = null;
        fast.next = head;

        return extra;
    }
}
```

---

## Dry Run

### Input

```text
head = [1,2,3,4,5]   (n0(1) -> n1(2) -> n2(3) -> n3(4) -> n4(5) -> null)
k = 2
```

`head != null` and `head.next != null` → proceed.

### Phase 1 — Measure the length

`temp = n0`, `size = 0`

| Step | temp before | temp after | size |
|------|-------------|------------|------|
|1|n0|n1|1|
|2|n1|n2|2|
|3|n2|n3|3|
|4|n3|n4|4|
|5|n4|null|5|

Loop exits when `temp == null`. `size = 5`

### Phase 2 — Reduce k and advance fast

`fast = n0`, `val = k % size = 2 % 5 = 2`

| i | Condition (i <= 2) | Action | fast |
|---|----------------------|--------|------|
|1|true|`fast = fast.next`|n1|
|2|true|`fast = fast.next`|n2|
|3|false|loop exits|—|

`fast == null`? no. `fast == head`? `n2 != n0` → no. Continue.

### Phase 3 — Find the new break point

`slow = n0` (reset)

**Iteration 1:** `fast.next(n2.next = n3) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n0.next = n1|slow = n1|
|fast = fast.next|n2.next = n3|fast = n3|

**Iteration 2:** `fast.next(n3.next = n4) != null` → runs

| Step | Action | Result |
|------|--------|--------|
|slow = slow.next|n1.next = n2|slow = n2|
|fast = fast.next|n3.next = n4|fast = n4|

**Loop check:** `fast.next (n4.next)` is `null` → loop exits.

State: `slow = n2`, `fast = n4` (the original last node)

### Phase 4 — Splice

- `extra = slow.next` → `extra = n2.next = n3`
- `slow.next = null` → `n2.next = null` (front segment now ends at n2: `1 -> 2 -> 3` becomes `1 -> 2` ... wait, more precisely: `n0 -> n1 -> n2 -> null`)
- `fast.next = head` → `n4.next = n0` (old tail connects back to old head)

**Return:** `extra` (n3), traversing: `4 -> 5 -> 1 -> 2 -> 3 -> null`

Output:

```text
[4,5,1,2,3]
```

This matches the expected output.

---

## Why This Approach Works

Rotating a list right by `k` places is equivalent to: keep the last `k` nodes in place at the front, and move everything else behind them — which just means finding the node that's `k` nodes from the end (the new tail of the front part) and the node right after it (the new head), then relinking the ends. Reducing `k` modulo the list's length first is essential because rotating by exactly `size` places (or any multiple of it) returns the list to its original order, so only the remainder actually matters — without this reduction, the `for` loop advancing `fast` could run for an enormous, unnecessary number of steps given the constraint `k` can be up to `2 * 10^9`.

Giving `fast` a `val`-node head start before the synchronized walk is what makes `slow` land in exactly the right spot: since both pointers then move at the same rate, the gap between them stays fixed at `val` nodes, so when `fast` reaches the true last node, `slow` is necessarily sitting `val` nodes before it — exactly the break point needed.

The three-pointer splice (`extra`, cutting `slow.next`, and reconnecting `fast.next`) is what turns a single list into the rotated version without allocating any new nodes: it's really just relinking three existing connections — cutting one, and forming two others (the old tail to the old head, and returning the new head).

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — one pass to measure the length, a bounded pass (`val < n`) to advance `fast`, and a third pass (at most `n` steps) to find the break point; all three are linear in the list length, so the total remains `O(n)`
- **Space Complexity:** `O(1)` — only a fixed set of pointer variables and counters are used; the rotation is done by relinking existing nodes, not creating new ones

---

## Key Insight

Reducing `k` modulo the list's length is the detail that makes this approach robust against arbitrarily large `k` values — recognizing that rotating by the full length (or any multiple of it) is a no-op collapses the problem down to at most one lap around the list. From there, giving one pointer a head start equal to the effective rotation amount is the same "fixed gap" technique used to find the nth-from-end node, repurposed here to locate the exact splice point in a single synchronized pass.

---

## Concepts Used

- Linked List
- Two Pointers (Fixed Gap)
- Modulo Arithmetic to Normalize Rotation Count
- In-Place List Splicing
- Constant Space Algorithm