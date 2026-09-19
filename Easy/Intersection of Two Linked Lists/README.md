# LeetCode 160 - Intersection of Two Linked Lists

## Problem Statement

Given the heads of two singly linked-lists `headA` and `headB`, return *the node at which the two lists intersect*. If the two linked lists have no intersection at all, return `null`.

The test cases are generated such that there are no cycles anywhere in the entire linked structure.

Note that the linked lists must retain their original structure after the function returns.

**Custom Judge:**

The inputs to the judge are given as follows (your program is **not** given these inputs):

- `intersectVal` - The value of the node where the intersection occurs. This is `0` if there is no intersected node.
- `listA` - The first linked list.
- `listB` - The second linked list.
- `skipA` - The number of nodes to skip ahead in `listA` (starting from the head) to get to the intersected node.
- `skipB` - The number of nodes to skip ahead in `listB` (starting from the head) to get to the intersected node.

The judge will then create the linked structure based on these inputs and pass the two heads, `headA` and `headB`, to your program. If you correctly return the intersected node, then your solution will be accepted.

---

## Example 1:
```
Input: intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5], skipA = 2, skipB = 3
Output: Intersected at '8'
```

## Example 2:
```
Input: intersectVal = 2, listA = [1,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
Output: Intersected at '2'
```

## Example 3:
```
Input: intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
Output: No intersection
```

## Constraints:
- The number of nodes of `listA` is in the `m`.
- The number of nodes of `listB` is in the `n`.
- `1 <= m, n <= 3 * 10^4`
- `1 <= Node.val <= 10^5`
- `0 <= skipA <= m`
- `0 <= skipB <= n`
- `skipA == 0` if `listA` and `listB` do not intersect.
- `skipB == 0` if `listA` and `listB` do not intersect.
- `intersectVal == 0` if `listA` and `listB` do not intersect.
- `intersectVal == listA[skipA] == listB[skipB]` if `listA` and `listB` intersect.

**Follow up:** Could you write a solution that runs in `O(m + n)` time and use only `O(1)` memory?

---

## Approach

This solution uses the classic **"switch heads" two-pointer technique**: walk two pointers down both lists in lockstep, and whenever one pointer reaches the end of its own list, redirect it to the *head of the other list*. This equalizes the distance each pointer has left to travel, so they're guaranteed to meet exactly at the intersection node (or both hit `null` together if there is none).

### Idea

- **Edge case:** if either `headA` or `headB` is `null`, there's no way they can intersect — return `null` immediately.
- Start `temp1 = headA` and `temp2 = headB`.
- **Main loop**, `while(temp1 != temp2)`:
  - Advance both one step: `temp1 = temp1.next` and `temp2 = temp2.next`.
  - Check right away if they now point to the same node (`temp1 == temp2`) — if so, that's the intersection node, so return `temp1` immediately.
  - If `temp1` fell off the end of its list (`temp1 == null`), redirect it to the **other** list's head: `temp1 = headB`.
  - Symmetrically, if `temp2` fell off the end (`temp2 == null`), redirect it: `temp2 = headA`.
- Because each pointer eventually traverses "its own list's unique portion + the other list's unique portion + the shared tail" — the same total distance for both, just in a different order — the two pointers are guaranteed to arrive at the intersection node (or `null`, if there is none) on the same step, which is what lets `temp1 != temp2` reliably terminate the loop.
- Return `temp1` after the loop as a fallback (covers the no-intersection case, where both pointers become `null` at the same time and the loop exits naturally).

---

## Code

```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if(headA == null || headB == null){
            return null;
        }

        ListNode temp1 = headA;
        ListNode temp2 = headB;

        while(temp1 != temp2){

            temp1 = temp1.next;
            temp2 = temp2.next;

            if(temp1 == temp2){
                return temp1;
            }

            if(temp1 == null){
                temp1 = headB;
            }
            if(temp2 == null){
                temp2 = headA;
            }

        }

        return temp1;
    }
}
```

---

## Dry Run

### Input

```text
listA = [1,9,1,2,4], listB = [3,2,4], intersectVal = 2, skipA = 3, skipB = 1

A: a1(1) -> a2(9) -> a3(1) -> c1(2) -> c2(4) -> null
B: b1(3) -> c1(2) -> c2(4) -> null   (c1, c2 are the SAME nodes as in A)
```

`headA != null` and `headB != null` → proceed. `temp1 = a1`, `temp2 = b1`

**Iteration 1:** `temp1(a1) != temp2(b1)` → loop runs

| Step | Action | Result |
|------|--------|--------|
|advance|`temp1 = a1.next`|temp1 = a2|
|advance|`temp2 = b1.next`|temp2 = c1|
|equal check|`temp1(a2) == temp2(c1)`? no|—|
|temp1 null check|not null|—|
|temp2 null check|not null|—|

**Iteration 2:** `temp1(a2) != temp2(c1)` → loop runs

| Step | Action | Result |
|------|--------|--------|
|advance|`temp1 = a2.next`|temp1 = a3|
|advance|`temp2 = c1.next`|temp2 = c2|
|equal check|`temp1(a3) == temp2(c2)`? no|—|
|temp1 null check|not null|—|
|temp2 null check|not null|—|

**Iteration 3:** `temp1(a3) != temp2(c2)` → loop runs

| Step | Action | Result |
|------|--------|--------|
|advance|`temp1 = a3.next`|temp1 = c1|
|advance|`temp2 = c2.next`|temp2 = null|
|equal check|`temp1(c1) == temp2(null)`? no|—|
|temp1 null check|not null|—|
|temp2 null check|**null** → `temp2 = headA`|temp2 = a1|

**Iteration 4:** `temp1(c1) != temp2(a1)` → loop runs

| Step | Action | Result |
|------|--------|--------|
|advance|`temp1 = c1.next`|temp1 = c2|
|advance|`temp2 = a1.next`|temp2 = a2|
|equal check|`temp1(c2) == temp2(a2)`? no|—|
|temp1 null check|not null|—|
|temp2 null check|not null|—|

**Iteration 5:** `temp1(c2) != temp2(a2)` → loop runs

| Step | Action | Result |
|------|--------|--------|
|advance|`temp1 = c2.next`|temp1 = null|
|advance|`temp2 = a2.next`|temp2 = a3|
|equal check|`temp1(null) == temp2(a3)`? no|—|
|temp1 null check|**null** → `temp1 = headB`|temp1 = b1|
|temp2 null check|not null|—|

**Iteration 6:** `temp1(b1) != temp2(a3)` → loop runs

| Step | Action | Result |
|------|--------|--------|
|advance|`temp1 = b1.next`|temp1 = c1|
|advance|`temp2 = a3.next`|temp2 = c1|
|equal check|`temp1(c1) == temp2(c1)`? **yes** → `return temp1`|—|

**Return:** `c1`, the node with value `2`

Output:

```text
Intersected at '2'
```

This matches the expected output.

---

## Why This Approach Works

Let `pA` be the length of `listA`'s unique prefix (before the intersection) and `pB` be `listB`'s unique prefix, with `s` the length of the shared tail. Pointer `temp1` walks `pA + s` steps down `listA`, then gets redirected and walks `pB` more steps into `listB`'s unique prefix — a total of `pA + s + pB` steps to reach the intersection node. Pointer `temp2` walks `pB + s` steps down `listB`, then `pA` more steps into `listA`'s prefix — also `pB + s + pA` steps total. Since both totals are identical, the two pointers arrive at the intersection node on the exact same step, no matter how different `pA` and `pB` are individually — the head-switch is precisely what equalizes the otherwise-mismatched distances.

If the lists never intersect, both pointers eventually traverse `m + n` total nodes (their own list plus a full pass of the other) and hit `null` at the same step, at which point `temp1 == temp2 == null` and the loop exits normally, so `null` is correctly returned.

The `if(temp1 == temp2){ return temp1; }` check right after advancing both pointers is what allows the intersection node itself to be caught and returned in the same iteration it's reached, rather than needing an extra pass.

---

## Complexity Analysis

- **Time Complexity:** `O(m + n)` — in the worst case (no intersection, or intersection near the end), each pointer traverses at most its own list plus the other list once, so the total work is bounded by `m + n`
- **Space Complexity:** `O(1)` — only two pointer variables are used, no new nodes, counters, or auxiliary data structures, which satisfies the follow-up's constant-memory requirement

---

## Key Insight

When two lists share a common tail but have different-length unique prefixes, redirecting each pointer to the *other* list's head upon reaching its own end is a neat way to force both pointers to cover the same total distance — turning what looks like it needs a length-difference calculation (walk the longer list's extra nodes first, then move both together) into a single uniform loop with no upfront length counting at all.

---

## Concepts Used

- Linked List
- Two Pointers
- Pointer Redirection / List Switching
- Constant Space Algorithm