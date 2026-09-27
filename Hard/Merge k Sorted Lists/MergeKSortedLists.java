import java.util.*;

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

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