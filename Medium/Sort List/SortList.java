
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode sortList(ListNode head) {       
        if(head == null || head.next == null){
            return head;
        }

        ListNode middle = middle(head);

        ListNode leftHead = head;
        ListNode rightHead = middle.next;
        middle.next = null;

        leftHead = sortList(leftHead);
        rightHead = sortList(rightHead);

        return mergeFinal(leftHead, rightHead);
        
    }

    private ListNode middle(ListNode head){
        ListNode slow = head;
        ListNode fast = head.next;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private ListNode mergeFinal(ListNode first , ListNode second){
        ListNode dummy = new ListNode(0);
        ListNode temp = dummy;

        while(first != null && second != null){
            if(first.val <= second.val){
                temp.next = first;
                temp = first;
                first = first.next;
            }else{
                temp.next = second;
                temp = second;
                second = second.next;
            }
        }

        if(first != null){
            temp.next = first;
        }else{
            temp.next = second;
        }

        return dummy.next;
    }
}
