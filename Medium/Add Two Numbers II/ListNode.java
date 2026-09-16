
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head1 = reverse(l1);
        ListNode head2 = reverse(l2);
        int carry = 0;
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while(head1 != null  || head2 != null){
            int sum = carry;
            if(head1 != null){
                sum = head1.val + sum;
            }
            if(head2 != null){
                sum = head2.val + sum;
            }
            
            ListNode node = new ListNode(sum % 10);
            carry = sum/10;

            current.next = node;
            current = node;

            if(head1 != null){
                head1 = head1.next;
            }
            if(head2 != null){
                head2 = head2.next;
            }
        }

        if(carry != 0){
            ListNode node = new ListNode(carry);
            current.next = node;
            current = node;
        }

        ListNode head = reverse(dummy.next);

        return head;
    }
    private ListNode reverse(ListNode head){
        ListNode current = head;
        ListNode prev = null;

        while(current != null){
            ListNode after = current.next;
            current.next = prev;
            prev = current;
            current = after;
        }

        return prev;
    }
}
