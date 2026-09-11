# LeetCode 237 - Delete Node in a Linked List

## Problem Statement

There is a singly-linked list `head` and we want to delete a node `node` in it.

You are given the node to be deleted `node`. You will **not be given access** to the first node of `head`.

All the values of the linked list are **unique**, and it is guaranteed that the given node `node` is **not the last node** in the linked list.

Delete the given node. Note that by deleting the node, we do not mean removing it from memory. We mean:

- The value of the given node should not exist in the linked list.
- The number of nodes in the linked list should decrease by one.
- All the values before `node` should be in the same order.
- All the values after `node` should be in the same order.

**Custom testing:**
- For the input, you should provide the entire linked list `head` and the node to be given `node`. `node` should not be the last node of the list and should be an actual node in the list.
- We will build the linked list and pass the node to your function.
- The output will be the entire list after calling your function.

---

## Example 1:
```
Input: head = [4,5,1,9], node = 5
Output: [4,1,9]
Explanation: You are given the second node with value 5, the linked list should become 4 -> 1 -> 9 after calling your function.
```

## Example 2:
```
Input: head = [4,5,1,9], node = 1
Output: [4,5,9]
Explanation: You are given the third node with value 1, the linked list should become 4 -> 5 -> 9 after calling your function.
```

## Constraints:
- The number of the nodes in the given list is in the range `[2, 1000]`.
- `-1000 <= Node.val <= 1000`
- The value of each node in the list is unique.
- The `node` to be deleted is in the list and is not a tail node.

---

## Approach

This solution uses a clever trick since **there's no access to the list's head or the node before `node`** — instead of removing `node` itself, it **overwrites `node` with the values of the next node**, and then removes that next node instead.

### Idea

- Since we can't reach backward to relink the previous node's `next` pointer (we don't have a reference to it), the only nodes reachable from `node` are `node` itself and everything after it.
- **Copy the next node's value into the current node:** `node.val = node.next.val` — this effectively makes `node` "become" a copy of the node that was supposed to be removed... but in reverse: it actually makes the current node take on the identity of its successor.
- **Skip over the (now-duplicated) next node:** `node.next = node.next.next` — this removes the *next* node from the list, since its value has already been copied into `node`.
- The net effect: from the perspective of anyone still holding a reference to the original `node`, it now appears as though `node` was deleted — its value has changed to match what used to be the next node, and the list has correctly shrunk by one node.

---

## Code

```java
class Solution {
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
}
```

---

## Dry Run

### Input

```text
head = [4,5,1,9], node = the node with value 5
```

Original list: `node4(4) -> node5(5) -> node1(1) -> node9(9) -> null`

`node` refers to `node5`.

**Step 1:** `node.val = node.next.val` → `node5.val = node1.val = 1` → `node5` now holds the value `1`

**Step 2:** `node.next = node.next.next` → `node5.next = node1.next = node9` → `node1` is skipped/removed from the chain

**Resulting list** (traversing from `node4`, which was never touched): `node4(4) -> node5(now val=1) -> node9(9) -> null`

Output:

```text
[4,1,9]
```

This matches the expected output — the original value `5` no longer exists in the list, and the list correctly shrank from 4 nodes to 3.

---

## Why This Approach Works

Since the function only receives `node` itself (with no way to reach the node before it), a normal deletion — which requires relinking the *previous* node's `next` pointer — isn't directly possible.

The workaround exploits the fact that "deleting `node`" is behaviorally equivalent to "renaming `node` to be its successor's value, then deleting the successor instead" — since all values are unique, overwriting `node.val` with `node.next.val` effectively removes the original value `node.val` from the list (it no longer exists anywhere), while the actual node object that gets unlinked (`node.next`) is one we *do* have direct access to via `node.next.next`.

---

## Complexity Analysis

- **Time Complexity:** `O(1)` — only a constant number of operations are performed, regardless of list size
- **Space Complexity:** `O(1)` — no extra data structures are used

---

## Key Insight

This is a well-known linked list trick: when you can't access the node *before* the one you want to remove, you can instead **overwrite the target node's data with its successor's data**, then unlink the successor — achieving the same observable result (the target's original value is gone, and the list is one node shorter) without ever needing a "previous" pointer at all. This only works because the target node is guaranteed not to be the last node (so a `next` always exists) and because all values in the list are unique.

---

## Concepts Used

- Linked List
- Node Value Overwriting
- Constant Time Deletion Trick