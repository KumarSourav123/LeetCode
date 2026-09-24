class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode rotateRight(ListNode head, int k) {

        if(head == null || head.next == null){
            return head;
        }

        ListNode temp = head;
        int size = 0;

        while(temp != null){
            temp = temp.next;
            size++;
        }
        
        ListNode fast = head;
        int val = k % size;

        for(int i = 1 ; i <= val ; i++){
            fast = fast.next;
            
        }

        if(fast == null || fast == head){
            return head;
        }

        ListNode slow = head;
        while(fast.next != null){
            slow = slow.next;
            fast = fast.next;            
        }

        ListNode extra = slow.next;
        slow.next = null;
        fast.next = head;

        return extra;

    }
}