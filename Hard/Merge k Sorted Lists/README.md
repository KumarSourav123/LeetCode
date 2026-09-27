# LeetCode 23 - Merge k Sorted Lists

## Problem Statement

You are given an array of `k` linked-lists `lists`, each linked-list is sorted in ascending order.

Merge all the linked-lists into one sorted linked-list and return it.

---

## Example 1:
```
Input: lists = [[1,4,5],[1,3,4],[2,6]]
Output: [1,1,2,3,4,4,5,6]
Explanation: The linked-lists are:
[
  1->4->5,
  1->3->4,
  2->6
]
merging them into one sorted linked list:
1->1->2->3->4->4->5->6
```

## Example 2:
```
Input: lists = []
Output: []
```

## Example 3:
```
Input: lists = [[]]
Output: []
```

## Constraints:
- `k == lists.length`
- `0 <= k <= 10^4`
- `0 <= lists[i].length <= 500`
- `-10^4 <= lists[i][j] <= 10^4`
- `lists[i]` is sorted in **ascending order**.
- The sum of `lists[i].length` will not exceed `10^4`.

---

## Approach

This solution uses a **min-heap (priority queue)** to always know, at any moment, which of the `k` lists currently has the smallest available "front" value — repeatedly pulling the overall smallest node, appending it to the result, and pushing that node's successor back into the heap so the next-smallest candidate from that same list becomes available.

### Idea

- Create `pq`, a `PriorityQueue<ListNode>` ordered by node value (`(a, b) -> a.val - b.val`), so `pq.poll()` always returns the node with the smallest `val` currently in the queue.
- **Seed the heap:** loop through `lists`, adding the head of every non-null list into `pq`. This gives the heap one representative "front" node from each list to start comparing.
- Set up `dummy` and `temp` for building the merged result, the standard pattern for constructing a list from scratch without special-casing the first node.
- **Main loop**, `while (!pq.isEmpty())`:
  - `minNode = pq.poll()` — removes and returns whichever node currently has the smallest value across *all* the lists represented in the heap.
  - **If `minNode` has a successor** (`minNode.next != null`), push that successor into the heap: `pq.add(minNode.next)`. This is what keeps that list's next candidate value available for future comparisons.
  - **Append `minNode` to the result:** `temp.next = minNode; temp = temp.next;`.
- Once the heap is empty, every node from every list has been extracted exactly once, in fully sorted order. Return `dummy.next`.

---

## Code

```java
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {

        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);

        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                pq.add(lists[i]);
            }
        }

        ListNode dummy = new ListNode(0);
        ListNode temp = dummy;

        while (!pq.isEmpty()) {
            ListNode minNode = pq.poll();
            if (minNode.next != null) {
                pq.add(minNode.next);
            }
            temp.next = minNode;
            temp = temp.next;
        }

        return dummy.next;
    }
}
```

---

## Dry Run

### Input

```text
lists = [[1,4,5],[1,3,4],[2,6]]

List A: a1(1) -> a4(4) -> a5(5) -> null
List B: b1(1) -> b3(3) -> b4(4) -> null
List C: c2(2) -> c6(6) -> null
```

**Seed the heap:** all three list heads are non-null, so `pq` starts holding `{a1(1), b1(1), c2(2)}`.

`dummy = d`, `temp = d`

*(Note: `a1` and `b1` tie in value; which one a Java `PriorityQueue` returns first among equal-valued entries isn't strictly guaranteed by the API. The trace below picks one consistent, plausible order — the exact tie-break doesn't change the final sorted sequence of values, only which physical node object happens to carry each `1` or `4`.)*

| Poll | minNode | minNode.next | Push? | Appended value | pq contents after |
|------|---------|---------------------|-------|---------------------|--------------------------|
|1|a1 (val 1)|a4|yes, push a4|1|{b1(1), c2(2), a4(4)}|
|2|b1 (val 1)|b3|yes, push b3|1|{c2(2), a4(4), b3(3)}|
|3|c2 (val 2)|c6|yes, push c6|2|{b3(3), a4(4), c6(6)}|
|4|b3 (val 3)|b4|yes, push b4|3|{a4(4), b4(4), c6(6)}|
|5|a4 (val 4)|a5|yes, push a5|4|{b4(4), a5(5), c6(6)}|
|6|b4 (val 4)|null|no|4|{a5(5), c6(6)}|
|7|a5 (val 5)|null|no|5|{c6(6)}|
|8|c6 (val 6)|null|no|6|{} (empty)|

**Loop check:** `pq.isEmpty()` → true → loop exits.

**Return:** `dummy.next` (a1), traversing: `1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6 -> null`

Output:

```text
[1,1,2,3,4,4,5,6]
```

This matches the expected output.

---

## Why This Approach Works

At any point during the merge, the smallest value not yet placed into the result must be the smallest among the *current fronts* of the `k` lists — since each individual list is already sorted, nothing further back in any list could possibly be smaller than that list's own current front. A min-heap is exactly the right structure for repeatedly asking "what's the smallest among these `k` (or fewer) candidates?" in `O(log k)` time per query, rather than scanning all `k` fronts linearly every time (which a naive repeated-comparison approach would require).

Pushing `minNode.next` back into the heap immediately after polling `minNode` is what keeps every list's next candidate available for future comparisons — the heap always contains at most one "front" node per list still in play, so it never has more than `k` elements at once, and it correctly reflects the current merge frontier at every step.

Because every node is pushed into the heap exactly once (either during the initial seeding, or as another node's `.next`) and popped exactly once, every node from every input list ends up in the final result exactly once, and the order they're popped in is guaranteed to be non-decreasing by the heap's ordering — producing a fully sorted merged list.

---

## Complexity Analysis

- **Time Complexity:** `O(N log k)` — where `N` is the total number of nodes across all lists and `k` is the number of lists; each of the `N` nodes is pushed and popped from the heap exactly once, and each heap operation costs `O(log k)` since the heap never holds more than `k` elements at a time
- **Space Complexity:** `O(k)` for the heap itself (holding at most one node per list at any time); the merged result reuses the existing nodes rather than copying values, so no additional space proportional to `N` is used beyond the heap and the fixed `dummy`/`temp` pointers

---

## Key Insight

Merging `k` sorted sequences is a direct generalization of merging 2 sorted sequences (LeetCode 21) — where the 2-list version only ever needs to compare 2 candidates at a time, the `k`-list version needs to efficiently find the minimum among up to `k` candidates repeatedly, which is precisely what a min-heap is built for. This "heap of frontier candidates" pattern generalizes well beyond linked lists too — it's the standard technique anytime multiple sorted streams need to be merged into one.

---

## Concepts Used

- Linked List
- Heap / Priority Queue
- K-Way Merge
- Dummy Head Node Technique