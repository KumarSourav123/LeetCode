# LeetCode 61 - Rotate List

## Problem Statement

Given the `head` of a linked list, rotate the list to the right by `k` places.

A right rotation by one place means the last node becomes the first node.

---

## Example 1

Input: `head = [1,2,3,4,5], k = 2`

Output: `[4,5,1,2,3]`

Explanation:

Original list:

`1 → 2 → 3 → 4 → 5 → null`

After rotate 1:

`5 → 1 → 2 → 3 → 4 → null`

After rotate 2:

`4 → 5 → 1 → 2 → 3 → null`

---

## Example 2

Input: `head = [0,1,2], k = 4`

Output: `[2,0,1]`

Explanation:

The list contains 3 nodes, so rotating by 4 is the same as rotating by:

`4 % 3 = 1`

Therefore:

`0 → 1 → 2`

After 1 rotation:

`2 → 0 → 1`

---

## Constraints

- The number of nodes in the list is in the range `[0, 500]`.
- `-100 <= Node.val <= 100`
- `0 <= k <= 2 * 10^9`

---

## Approach

The main idea is to find the point where the list should be rotated.

For example:

`1 → 2 → 3 → 4 → 5 → null`

For `k = 2`, the last 2 nodes should move to the front:

`4 → 5 → 1 → 2 → 3 → null`

First, we find the size of the linked list.

Then we calculate:

`k = k % size`

This is important because rotating a list of size `5` by `5` positions gives the same list.

After finding the effective value of `k`, we find the node that will become the last node of the rotated list.

Its next node becomes the new head.

---

## Idea

1. If the list is empty or contains only one node, return `head`.
2. If `k == 0`, return `head`.
3. Find the size of the linked list.
4. Calculate `k = k % size`.
5. If `k == 0`, return `head`.
6. Find the node at position `size - k`.
7. The next node will become the new head.
8. Break the connection between the old part and the new head.
9. Find the old tail.
10. Connect the old tail to the original head.
11. Return the new head.

---

## Code

    class Solution {
        public ListNode rotateRight(ListNode head, int k) {

            if (head == null || head.next == null) {
                return head;
            }

            if (k == 0) {
                return head;
            }

            ListNode temp = head;
            int size = 0;

            // Find size of linked list
            while (temp != null) {
                size++;
                temp = temp.next;
            }

            k = k % size;

            if (k == 0) {
                return head;
            }

            // Find the node just before the new head
            int val = size - k;

            ListNode current = head;

            for (int i = 1; i < val; i++) {
                current = current.next;
            }

            // New head
            ListNode pointer = current.next;

            // Break the list
            current.next = null;

            // Find the old tail
            ListNode dummy = pointer;

            while (dummy.next != null) {
                dummy = dummy.next;
            }

            // Connect old tail to old head
            dummy.next = head;

            return pointer;
        }
    }

---

## Detailed Dry Run

### Input

`head = [1,2,3,4,5]`

`k = 2`

Initial list:

`1 → 2 → 3 → 4 → 5 → null`

### Step 1: Find Size

We traverse the complete list.

`temp = 1 → size = 1`

`temp = 2 → size = 2`

`temp = 3 → size = 3`

`temp = 4 → size = 4`

`temp = 5 → size = 5`

`temp = null → stop`

Therefore:

`size = 5`

---

### Step 2: Calculate Effective Rotation

`k = k % size`

`k = 2 % 5`

`k = 2`

So we need to rotate the list by 2 positions.

---

### Step 3: Find the New Tail

We calculate:

`val = size - k`

Therefore:

`val = 5 - 2`

`val = 3`

We need to reach the 3rd node.

`1 → 2 → 3 → 4 → 5`

After the loop:

`current = 3`

---

### Step 4: Find the New Head

We execute:

`pointer = current.next`

Since `current` is pointing to `3`:

`pointer = 4`

So `4` will become the new head.

---

### Step 5: Break the Connection

We execute:

`current.next = null`

Since `current = 3`, the list becomes:

`1 → 2 → 3 → null`

`4 → 5 → null`

---

### Step 6: Find the Old Tail

We start:

`dummy = pointer`

Therefore:

`dummy = 4`

Move `dummy` until the last node:

`dummy = 4`

`dummy = 5`

Now:

`dummy = 5`

So `5` is the old tail.

---

### Step 7: Connect Old Tail to Old Head

We execute:

`dummy.next = head`

Therefore:

`5 → 1`

The complete list becomes:

`4 → 5 → 1 → 2 → 3 → null`

---

### Final Output

`[4,5,1,2,3]`

---

## Why This Approach Works

When we rotate a list to the right by `k` positions, the last `k` nodes move to the front.

For:

`1 → 2 → 3 → 4 → 5`

and:

`k = 2`

the last two nodes are:

`4 → 5`

The remaining nodes are:

`1 → 2 → 3`

After reconnecting them:

`4 → 5 → 1 → 2 → 3`

The node at position `size - k` becomes the new tail.

Its next node becomes the new head.

---

## Complexity Analysis

### Time Complexity

`O(n)`

We traverse the linked list a few times, but each traversal takes `O(n)` time.

Therefore, the overall time complexity is:

`O(n)`

### Space Complexity

`O(1)`

We only use a few pointers and do not create any extra data structure.

---

## Key Insight

The two most important calculations are:

`k = k % size`

and:

`val = size - k`

For example:

`size = 5`

`k = 2`

Therefore:

`size - k = 3`

So node `3` becomes the new tail and node `4` becomes the new head.

Original:

`1 → 2 → 3 → 4 → 5`

After rotation:

`4 → 5 → 1 → 2 → 3`

---

## Concepts Used

- Singly Linked List
- Linked List Traversal
- Pointers
- In-place Linked List Manipulation
- Modulo Operator
- List Rotation
- Breaking and Reconnecting Nodes
- `O(n)` Time Complexity
- `O(1)` Space Complexity