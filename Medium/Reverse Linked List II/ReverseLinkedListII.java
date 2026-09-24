class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {

        if(head == null || head.next == null){
            return head;
        }
        
        ListNode first = head;
        ListNode low = head;
        ListNode high = head;
        ListNode temp = head;
        ListNode second = null;

        int size = 0;

        while(temp != null){
            size++;
            if(size == left-1 && size != 1){
                first = temp;
            }
            if(size == left){
                low = temp;
            }
            if(size == right){
                high = temp;
            }
            temp = temp.next;
        }

        if(size != right){
            second = high.next;
        }else{
            second = high.next;
        }

        ListNode current = low;
        ListNode prev = null;

        while(current != second){
            ListNode after = current.next;
            current.next = prev;
            prev = current;
            current = after;
        }

        low.next = second;
        ListNode extra = null;

        if(first != low){
            first.next = high;
            extra = head;
        }else{
            extra = high;
        }

        return extra;
    }
}