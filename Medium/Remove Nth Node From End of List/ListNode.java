
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if(head.next == null){
            head = null;
            return head;
        }

        ListNode temp = head;
        int size = 0;
        while(temp != null){
            size++;
            temp = temp.next;
        }

        size = size-n;

        ListNode current = head;
        ListNode prev = null;

        for(int i = 1 ; i <= size ; i++){
            prev = current;
            current = current.next;
        }

        if(current == head){
            head = current.next;
        }

        if(prev != null){
            prev.next = current.next;
            current.next = null;
        }else{
            current.next = null;
        }
        
        return head;
    }
}
