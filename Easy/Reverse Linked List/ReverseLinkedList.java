class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next;}

}


class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode current = head;
        ListNode before = null;
        while(current != null){
            ListNode after = current.next;
            current.next = before;
            before = current;
            current = after;
        } 
        return before;
    }
}