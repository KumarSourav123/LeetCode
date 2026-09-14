
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode swapPairs(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }

        ListNode extra = head.next;
        ListNode slow = head;
        ListNode fast = head.next;

        while(fast.next != null && fast.next.next != null){
            ListNode dummy = slow;
            slow.next = fast.next.next;
            slow = fast.next;
            fast.next = dummy;
            fast = slow.next;        
        }
        if(fast.next == null){
            fast.next = slow;
            slow.next = null;
        }else{
            slow.next = fast.next;
            fast.next = slow;
        }

        return extra;
    }
}