# LeetCode 445 - Add Two Numbers II

## Problem Statement

You are given two non-empty linked lists representing two non-negative integers.

The most significant digit comes first, and each node contains a single digit.

Add the two numbers and return the sum as a linked list.

You may not modify the input lists. In other words, you should not reverse the original lists directly.

---

## Example 1

Input:

`l1 = [7,2,4,3]`

`l2 = [5,6,4]`

Output:

`[7,8,0,7]`

Explanation:

The linked lists represent:

`7243 + 564 = 7807`

Therefore, the result is:

`7 → 8 → 0 → 7`

---

## Example 2

Input:

`l1 = [2,4,3]`

`l2 = [5,6,4]`

Output:

`[8,0,7]`

Explanation:

`243 + 564 = 807`

Therefore:

`8 → 0 → 7`

---

## Example 3

Input:

`l1 = [0]`

`l2 = [0]`

Output:

`[0]`

---

## Constraints

- The number of nodes in each linked list is in the range `[1, 100]`.
- `0 <= Node.val <= 9`
- It is guaranteed that the list represents a number that does not have leading zeros.
- The linked lists represent non-negative integers.

---

## Approach

Since the digits are given in forward order, normal addition is difficult because addition starts from the least significant digit.

For example:

`7 → 2 → 4 → 3`

represents:

`7243`

But addition should start from:

`3`

So the easiest approach is:

1. Reverse the first linked list.
2. Reverse the second linked list.
3. Perform normal addition from left to right.
4. Store each resulting digit in a new linked list.
5. If a carry remains after the loop, add one more node.
6. Reverse the result list.
7. Return the reversed result.

This allows us to perform addition exactly like normal mathematical addition.

---

## Idea

Suppose:

`l1 = 7 → 2 → 4 → 3`

`l2 = 5 → 6 → 4`

First reverse both:

`l1 = 3 → 4 → 2 → 7`

`l2 = 4 → 6 → 5`

Now we can start addition from the least significant digit.

The addition becomes:

`3 + 4 = 7`

`4 + 6 = 10`

Write `0` and carry `1`.

`2 + 5 + 1 = 8`

Then:

`7 + 0 + 0 = 7`

Result before reversing:

`7 → 0 → 8 → 7`

Reverse it:

`7 → 8 → 0 → 7`

Final answer:

`[7,8,0,7]`

---

## Code

    class Solution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

            ListNode head1 = reverse(l1);
            ListNode head2 = reverse(l2);

            int carry = 0;

            ListNode dummy = new ListNode(0);
            ListNode current = dummy;

            while (head1 != null || head2 != null) {

                int sum = carry;

                if (head1 != null) {
                    sum = sum + head1.val;
                }

                if (head2 != null) {
                    sum = sum + head2.val;
                }

                ListNode node = new ListNode(sum % 10);
                carry = sum / 10;

                current.next = node;
                current = node;

                if (head1 != null) {
                    head1 = head1.next;
                }

                if (head2 != null) {
                    head2 = head2.next;
                }
            }

            if (carry != 0) {
                ListNode node = new ListNode(carry);
                current.next = node;
                current = node;
            }

            ListNode head = reverse(dummy.next);

            return head;
        }

        private ListNode reverse(ListNode head) {

            ListNode current = head;
            ListNode prev = null;

            while (current != null) {

                ListNode after = current.next;
                current.next = prev;
                prev = current;
                current = after;
            }

            return prev;
        }
    }

---

## Detailed Dry Run

### Input

`l1 = [7,2,4,3]`

`l2 = [5,6,4]`

The lists are:

`l1: 7 → 2 → 4 → 3`

`l2: 5 → 6 → 4`

They represent:

`7243 + 564`

---

## Step 1: Reverse Both Lists

We call:

    ListNode head1 = reverse(l1);
    ListNode head2 = reverse(l2);

After reversing:

`head1: 3 → 4 → 2 → 7`

`head2: 4 → 6 → 5`

Now the least significant digits are at the beginning.

---

## Step 2: Initialize Carry

We start with:

    int carry = 0;

And create a dummy node:

    ListNode dummy = new ListNode(0);
    ListNode current = dummy;

The dummy node makes it easier to build the result list.

---

## Step 3: First Addition

Current digits:

`3` and `4`

Carry:

`0`

Therefore:

`sum = 3 + 4 + 0`

`sum = 7`

Create:

`7`

Since:

`7 % 10 = 7`

The carry is:

`7 / 10 = 0`

Result so far:

`7`

---

## Step 4: Second Addition

Current digits:

`4` and `6`

Carry:

`0`

Therefore:

`sum = 4 + 6 + 0`

`sum = 10`

Digit:

`10 % 10 = 0`

Carry:

`10 / 10 = 1`

So we store:

`0`

Result:

`7 → 0`

Carry:

`1`

---

## Step 5: Third Addition

Current digits:

`2` and `5`

Carry:

`1`

Therefore:

`sum = 2 + 5 + 1`

`sum = 8`

Digit:

`8 % 10 = 8`

Carry:

`8 / 10 = 0`

Result:

`7 → 0 → 8`

---

## Step 6: Fourth Addition

Now:

`head2 == null`

because the second number has no more digits.

But `head1` still contains:

`7`

So:

`sum = 7 + 0`

`sum = 7`

Digit:

`7 % 10 = 7`

Carry:

`7 / 10 = 0`

Result:

`7 → 0 → 8 → 7`

---

## Step 7: Check Carry

We check:

    if (carry != 0)

Here:

`carry = 0`

So no extra node is needed.

The result currently is:

`7 → 0 → 8 → 7`

But this is reversed because we performed addition from the least significant digit.

---

## Step 8: Reverse the Result

We execute:

    ListNode head = reverse(dummy.next);

Before reversing:

`7 → 0 → 8 → 7`

After reversing:

`7 → 8 → 0 → 7`

Therefore:

`[7,8,0,7]`

---

## Dry Run Table

| Step | Digit from L1 | Digit from L2 | Carry In | Sum | Result Digit | Carry Out |
|------|---------------|---------------|----------|-----|--------------|-----------|
| 1 | 3 | 4 | 0 | 7 | 7 | 0 |
| 2 | 4 | 6 | 0 | 10 | 0 | 1 |
| 3 | 2 | 5 | 1 | 8 | 8 | 0 |
| 4 | 7 | 0 | 0 | 7 | 7 | 0 |

Result before final reverse:

`7 → 0 → 8 → 7`

Result after final reverse:

`7 → 8 → 0 → 7`

---

## Why This Approach Works

Normal addition always starts from the least significant digit.

For:

`7243 + 564`

we actually calculate from right to left:

`3 + 4`

then:

`4 + 6`

then:

`2 + 5`

then:

`7 + 0`

But the linked lists store digits from left to right:

`7 → 2 → 4 → 3`

Therefore, reversing both lists allows us to process the digits from the least significant digit to the most significant digit.

After performing the addition, the result is also reversed, so we reverse it one final time to restore the correct order.

---

## Important Part of the Code

The following two lines handle the current digits:

    if (head1 != null) {
        sum = sum + head1.val;
    }

    if (head2 != null) {
        sum = sum + head2.val;
    }

This is important because the two linked lists may have different lengths.

For example:

`l1 = 7 → 2 → 4 → 3`

`l2 = 5 → 6 → 4`

After processing `3`, `4`, and `2`, the second list becomes `null`.

But the first list still contains `7`.

Therefore, the condition:

    while (head1 != null || head2 != null)

allows us to continue processing the remaining nodes.

---

## Understanding `% 10` and `/ 10`

For every addition:

    digit = sum % 10;
    carry = sum / 10;

For example:

`4 + 6 = 10`

Then:

`10 % 10 = 0`

So the digit we store is:

`0`

And:

`10 / 10 = 1`

So the carry is:

`1`

Another example:

`8 + 7 + 1 = 16`

Then:

`16 % 10 = 6`

and:

`16 / 10 = 1`

So we store `6` and carry `1`.

---

## Why We Use a Dummy Node

We create:

    ListNode dummy = new ListNode(0);

Then:

    ListNode current = dummy;

The dummy node is not part of the answer.

It simply gives us a starting point so that every new result node can be attached using:

    current.next = node;
    current = node;

At the end:

    dummy.next

points to the actual result list.

---

## Why We Reverse the Result

During addition, we start from the least significant digit.

Therefore, the result is initially created in reverse order.

For example:

`7243 + 564`

gives:

`7 → 0 → 8 → 7`

But the correct answer should be:

`7 → 8 → 0 → 7`

So we perform:

    ListNode head = reverse(dummy.next);

This restores the correct forward order.

---

## Complexity Analysis

### Time Complexity

`O(n + m)`

Where:

- `n` = number of nodes in `l1`
- `m` = number of nodes in `l2`

We reverse both lists, traverse both lists for addition, and reverse the result.

Therefore, the total time complexity is:

`O(n + m)`

### Space Complexity

`O(n + m)`

We create a new linked list for the result.

The extra result list can contain at most `max(n, m) + 1` nodes.

Apart from the result list, only a constant number of pointers are used.

---

## Key Insight

The main trick is:

`Reverse → Add → Reverse`

Because the linked lists store digits in forward order:

`7 → 2 → 4 → 3`

but addition needs to start from the end:

`3 → 4 → 2 → 7`

So we reverse the input lists, perform normal addition with carry, and reverse the result again.

The three important operations are:

    head1 = reverse(l1);
    head2 = reverse(l2);

    // Perform addition

    head = reverse(dummy.next);

This turns the linked-list problem into the same process as normal decimal addition.

---

## Concepts Used

- Singly Linked List
- Linked List Reversal
- Traversal
- Dummy Node
- Carry Handling
- Modulo Operator `%`
- Integer Division `/`
- Handling Linked Lists of Different Lengths
- In-place Reversal
- `O(n + m)` Time Complexity
- `O(n + m)` Space Complexity