class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode temp = head;
        ListNode nextNode = null;
        ListNode prev = null;

        while(temp != null){
            ListNode kthNode = getKthNode(temp, k);
            if(kthNode == null){
                if(prev != null){
                    prev.next = temp;
                    break;
                }
            }

            nextNode = kthNode.next;
            kthNode.next = null;
            reverse(temp);
            if(temp == head){
                head = kthNode;
            }else{
                prev.next = kthNode;
            }

            prev = temp;
            temp = nextNode;
        }

        return head;
        
    }

    private ListNode getKthNode(ListNode temp, int k){

        k--;

        while(temp != null && k > 0){
            temp = temp.next;
            k--;
        }

        return temp;
    }

    private ListNode reverse(ListNode temp){

        ListNode prev = null;
        while(temp != null){
            ListNode after = temp.next;
            temp.next = prev;
            prev = temp;
            temp = after;
        }

        return prev;
    }

}