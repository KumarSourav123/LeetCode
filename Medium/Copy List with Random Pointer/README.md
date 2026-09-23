# LeetCode 138 - Copy List with Random Pointer

## Problem Statement

A linked list of length `n` is given such that each node contains an additional random pointer, which could point to any node in the list, or `null`.

Construct a **deep copy** of the list. The deep copy should consist of exactly `n` **brand new** nodes, where each new node has its value set to the value of its corresponding original node. Both the `next` and `random` pointer of the new nodes should point to new nodes in the copied list such that the pointers in the original list and copied list represent the same list state. **None of the pointers in the new list should point to nodes in the original list.**

For example, if there are two nodes `X` and `Y` in the original list, where `X.random --> Y`, then for the corresponding two nodes `x` and `y` in the copied list, `x.random --> y`.

Return *the head of the copied linked list*.

The linked list is represented in the input/output as a list of `n` nodes. Each node is represented as a pair of `[val, random_index]` where:

- `val`: an integer representing `Node.val`
- `random_index`: the index of the node (range from `0` to `n-1`) that the `random` pointer points to, or `null` if it does not point to any node.

Your code will **only** be given the `head` of the original linked list.

---

## Example 1:
```
Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
```

## Example 2:
```
Input: head = [[1,1],[2,1]]
Output: [[1,1],[2,1]]
```

## Example 3:
```
Input: head = [[3,null],[3,0],[3,null]]
Output: [[3,null],[3,0],[3,null]]
```

## Constraints:
- `0 <= n <= 1000`
- `-10^4 <= Node.val <= 10^4`
- `Node.random` is `null` or is pointing to some node in the linked list.

---

## Approach

This solution uses the classic **three-pass weaving technique**, avoiding any hash map: first it interleaves a copy node right after each original node, then it uses that interleaving to set each copy's `random` pointer in terms of the original's `random` pointer, and finally it un-weaves the two lists back apart.

### Idea

- **Edge case:** if `head` is `null`, there's nothing to copy — return `head` (i.e. `null`) directly.
- **Pass 1 — interleave copies:** walk `temp` through the original list. For each original node, create `newNode` with the same value, insert it **right after** its original (`newNode.next = temp.next; temp.next = newNode;`), then advance `temp` to the node **after** the newly-inserted copy (`temp = newNode.next`) — which is the next original node. After this pass, the list looks like `orig1 -> copy1 -> orig2 -> copy2 -> ... -> origN -> copyN -> null`.
- **Pass 2 — wire up random pointers using the interleaving:** walk `pointer` through the (now-doubled) list, but only ever landing on *original* nodes (`pointer = pointer.next.next` skips over each copy). For each original node:
  - `extra = pointer.random` — the original node this one's random pointer targets (or `null`).
  - If `extra` is `null`, the copy's random pointer is also `null`: `pointer.next.random = null`.
  - Otherwise, since every original node `X` is immediately followed by its own copy `x`, the copy of `extra` is simply `extra.next` — so `pointer.next.random = extra.next` correctly points the current copy's random pointer at the **copy** of the original's random target, never at an original node.
- **Pass 3 — separate the two interleaved lists:** using a `dummy` head and a `rest` pointer for the copy list, and `current` walking the original list:
  - `rest.next = current.next` — attach the next copy node to the growing copy list.
  - `rest = rest.next` — advance the copy-list tail.
  - `current.next = rest.next` — restore the original list's own `next` pointer to skip over the copy node, repairing it back to its original structure.
  - `current = current.next` — advance to the next original node.
- Return `dummy.next` — the head of the fully separated, independent copy list.

---

## Code

```java
class Solution {
    public Node copyRandomList(Node head) {

        if(head == null){
            return head;
        }

        Node temp = head;

        while(temp != null){
            Node newNode = new Node(temp.val);
            newNode.next = temp.next;
            temp.next = newNode;
            temp = newNode.next;
        }

        Node pointer = head;

        while(pointer != null){
            Node extra = pointer.random;
            if(extra == null){
                pointer.next.random = null;
            }else{
                pointer.next.random = extra.next;
            }
            pointer = pointer.next.next;
        }

        Node dummy = new Node(0);
        Node current = head;
        Node rest = dummy;

        while(current != null){
            rest.next = current.next;
            rest = rest.next;
            current.next = rest.next;
            current = current.next;
        }

        return dummy.next;
    }
}
```

---

## Dry Run

### Input

```text
head = [[1,1],[2,1]]

A(val=1, random -> B) -> B(val=2, random -> B, i.e. itself) -> null
```

`head != null` → proceed.

### Phase 1 — Interleave copies

`temp = A`

| Step | temp (before) | Action | Result |
|------|-----------------|--------|--------|
|1|A|create A'(val 1); `A'.next = A.next = B`; `A.next = A'`; `temp = A'.next`|list: `A -> A' -> B -> null`, temp = B|
|2|B|create B'(val 2); `B'.next = B.next = null`; `B.next = B'`; `temp = B'.next`|list: `A -> A' -> B -> B' -> null`, temp = null|

Loop exits when `temp == null`.

### Phase 2 — Wire up random pointers

`pointer = A` (head)

**Iteration 1:** `pointer = A`

| Step | Action | Result |
|------|--------|--------|
|extra = pointer.random|`extra = A.random = B`|—|
|extra == null?|no|—|
|set copy's random|`pointer.next.random = extra.next`|`A'.random = B.next = B'`|
|advance|`pointer = pointer.next.next`|`pointer = A'.next = B`|

**Iteration 2:** `pointer = B`

| Step | Action | Result |
|------|--------|--------|
|extra = pointer.random|`extra = B.random = B` (self)|—|
|extra == null?|no|—|
|set copy's random|`pointer.next.random = extra.next`|`B'.random = B.next = B'`|
|advance|`pointer = pointer.next.next`|`pointer = B'.next = null`|

Loop exits when `pointer == null`.

State: `A'.random = B'`, `B'.random = B'` (correctly mirrors the original: `A.random = B`, `B.random = B`)

### Phase 3 — Separate the lists

`dummy = d0`, `current = A`, `rest = d0`

**Iteration 1:** `current = A`

| Step | Action | Result |
|------|--------|--------|
|rest.next = current.next|`d0.next = A.next = A'`|—|
|rest = rest.next|`rest = A'`|—|
|current.next = rest.next|`A.next = A'.next = B`|`A.next = B` (original repaired)|
|current = current.next|`current = A.next = B`|—|

**Iteration 2:** `current = B`

| Step | Action | Result |
|------|--------|--------|
|rest.next = current.next|`A'.next = B.next = B'`|—|
|rest = rest.next|`rest = B'`|—|
|current.next = rest.next|`B.next = B'.next = null`|`B.next = null` (original repaired)|
|current = current.next|`current = B.next = null`|—|

Loop exits when `current == null`.

State: original list restored to `A -> B -> null`. Copy list: `d0 -> A' -> B' -> null`, with `A'.random = B'` and `B'.random = B'`.

**Return:** `dummy.next` (A'), a fully independent copy: `[1, random -> index1] -> [2, random -> index1]`

Output:

```text
[[1,1],[2,1]]
```

This matches the expected output.

---

## Why This Approach Works

The core obstacle in this problem is that `random` pointers can point **forward or backward** to any node, so naively copying node-by-node in a single pass would risk needing a random pointer's target before that target's copy has even been created. Interleaving a copy immediately after each original (Pass 1) sidesteps this entirely: it guarantees that for *any* original node `X`, its copy is always reachable as `X.next` — regardless of where `X` sits in the list or when it was processed.

That guarantee is exactly what makes Pass 2 correct without needing a hash map: to find "the copy of whatever `X.random` points to," it's enough to take `X.random.next`, since the interleaving structure ensures that's always the corresponding copy — even if `X.random` points to a node that appears earlier or later in the list.

Pass 3 is necessary because the problem explicitly requires the original list to be left in a valid, unmodified state (`current.next = rest.next` restores each original node's `next` pointer to skip the copy), and because the returned list must consist entirely of new nodes with no lingering references into the original list — which is exactly what fully separating the two interleaved chains accomplishes.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — three separate passes over the list, each visiting every node (or every original/copy pair) exactly once, so `O(n) + O(n) + O(n) = O(n)`
- **Space Complexity:** `O(1)` extra space (excluding the output copy itself, which is unavoidably `O(n)`) — no hash map or other auxiliary lookup structure is used; the interleaving trick itself serves as the mechanism for locating each node's copy

---

## Key Insight

When a data structure has pointers that can reference *any* other node arbitrarily (not just sequentially, as with `next`), a common trick to avoid extra lookup storage is to **temporarily co-locate** each original with its copy — here, by physically interleaving them in the same list — so that "find the copy of node X" becomes a simple, guaranteed-`O(1)` pointer hop (`X.next`) instead of a search or a hash map lookup.

---

## Concepts Used

- Linked List
- Deep Copy / Cloning
- Interleaving (Weave / Un-weave) Technique
- Random Pointer Resolution Without Auxiliary Storage
- Constant Auxiliary Space Algorithm