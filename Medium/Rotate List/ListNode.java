
public class ListNode {
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
        if(k == 0){
            return head;
        }
        if(head.next.next == null){
            if(k == 2){
                return head;
            }
            if(k == 1){
                ListNode extra = head.next;
                head.next = null;
                extra.next = head;
                head = extra;
                return head;
            }
        }

        ListNode temp = head;
        int size = 0;
        while(temp != null){
            size++;
            temp = temp.next;
        }

        if(k % size == 0){
            return head;
        }

        int rotate = k % size;
        int val = size-rotate;
        ListNode current = head;

        for(int i = 1 ; i < val ; i++){
            current = current.next;
        }

        ListNode dummy = current.next;
        ListNode pointer = current.next;
        current.next = null;
        rotate--;

        while(rotate > 0){
            dummy = dummy.next;
            rotate--;
        }
        dummy.next = head;

        return pointer;

    }
}
