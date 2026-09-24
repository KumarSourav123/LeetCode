class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public void reorderList(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode current = slow;
        ListNode prev = null;

        while(current != null){
            ListNode after = current.next;
            current.next = prev;
            prev = current;
            current = after;
        }

        ListNode first = head;
        ListNode last = prev;

        while(last.next != null){
            ListNode dummy1 = first.next;
            ListNode dummy2 = last.next;
            first.next = last;
            first = dummy1;
            if(last.next == null){
                last.next = null;
            }else{
                last.next = first;
            }           
            last = dummy2;
        }

    }
}