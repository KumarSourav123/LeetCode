# LeetCode 2 - Add Two Numbers

## Problem Statement

You are given two **non-empty** linked lists representing two non-negative integers. The digits are stored in **reverse order**, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number `0` itself.

---

## Example 1:
```
Input: l1 = [2,4,3], l2 = [5,6,4]
Output: [7,0,8]
Explanation: 342 + 465 = 807.
```

## Example 2:
```
Input: l1 = [0], l2 = [0]
Output: [0]
```

## Example 3:
```
Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
Output: [8,9,9,9,0,0,0,1]
```

## Constraints:
- The number of nodes in each linked list is in the range `[1, 100]`.
- `0 <= Node.val <= 9`
- It is guaranteed that the list represents a number that does not have leading zeros.

---

## Approach

This solution simulates **elementary school-style digit-by-digit addition**, using a `dummy` head node to simplify building the result list, and tracking a running `carry` across positions.

### Idea

- Create a `dummy` node (value `0`, not part of the actual answer) with a `current` pointer starting there — this avoids needing special-case logic for attaching the very first real node of the result.
- Use `temp1` and `temp2` to walk through `l1` and `l2` respectively, and `carry` to hold any overflow digit from the previous addition.
- While either `temp1` or `temp2` is still non-`null` (there are still digits left in at least one list):
  - Start `sum` at the current `carry`.
  - If `temp1` isn't `null`, add its value to `sum`, then advance `temp1`.
  - If `temp2` isn't `null`, add its value to `sum`, then advance `temp2`.
  - Create a new node holding `sum % 10` (the digit to keep), and update `carry = sum / 10` (the digit to carry forward, `0` or `1`).
  - Attach this new node to `current.next`, then advance `current` to this new node.
- After the loop, if `carry` is still non-zero (meaning the final addition produced an extra digit, like `9+9=18`), append one more node holding just the `carry` value.
- Return `dummy.next` — the actual head of the result list, skipping the placeholder `dummy` node.

---

## Code

```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        ListNode temp1 = l1;
        ListNode temp2 = l2;
        int carry = 0;

        while (temp1 != null || temp2 != null) {
            int sum = carry;
            if (temp1 != null) {
                sum = sum + temp1.val;
            }
            if (temp2 != null) {
                sum = sum + temp2.val;
            }

            ListNode node = new ListNode(sum % 10);
            carry = sum / 10;

            current.next = node;
            current = node;

            if (temp1 != null) {
                temp1 = temp1.next;
            }
            if (temp2 != null) {
                temp2 = temp2.next;
            }
        }
        if (carry != 0) {
            ListNode node = new ListNode(carry);
            current.next = node;
        }
        return dummy.next;
    }
}
```

---

## Dry Run

### Input

```text
l1 = [2,4,3], l2 = [5,6,4]   (representing 342 + 465)
```

**Initial:** `dummy = node(0)`, `current = dummy`, `temp1 = l1's head (2)`, `temp2 = l2's head (5)`, `carry = 0`

| temp1.val | temp2.val | sum = carry+temp1+temp2 | new node val (sum%10) | carry (sum/10) | current advances to |
|-----------|-----------|-------------------------------|----------------------------|--------------------|--------------------------|
|2|5|0+2+5 = 7|7|0|node(7)|
|4|6|0+4+6 = 10|0|1|node(0)|
|3|4|1+3+4 = 8|8|0|node(8)|

After the third iteration, `temp1 = null` and `temp2 = null` (both lists exhausted) → loop exits.

**Final carry check:** `carry = 0` → no extra node appended.

**Return:** `dummy.next`, traversing: `7 -> 0 -> 8 -> null`

Output:

```text
[7,0,8]
```

This matches the expected output (`342 + 465 = 807`, stored in reverse digit order as `[7,0,8]`).

---

## Why This Approach Works

Since both numbers are stored in **reverse order** (least significant digit first), adding them digit-by-digit from the head of each list naturally aligns with how addition works by hand — starting from the ones place and carrying forward into higher place values, exactly like the `carry` variable does here.

Using a `dummy` head node sidesteps the usual special-case handling needed for "attaching the first node" versus "attaching subsequent nodes" — every new digit node is attached the same way (`current.next = node`), regardless of whether it's the first result digit or the last.

---

## Complexity Analysis

- **Time Complexity:** `O(max(m, n))`, where `m` and `n` are the lengths of `l1` and `l2` — the loop runs once per digit position, up to the length of the longer list (plus possibly one extra step for a final carry)
- **Space Complexity:** `O(max(m, n))` — for the newly created result list (not counting the input lists)

---

## Key Insight

The `dummy` head node pattern is a common linked-list-building technique: by starting with a placeholder node and always attaching new nodes via `current.next`, the code avoids ever needing an `if (this is the first node)` branch — the real head of the result is simply whatever `dummy.next` ends up pointing to once construction is complete.

---

## Concepts Used

- Linked List
- Simulation
- Carry Propagation
- Dummy Head Node Pattern