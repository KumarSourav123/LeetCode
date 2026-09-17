
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while(list1 != null && list2 != null){  

            if(list1.val <= list2.val){
                ListNode node = new ListNode(list1.val);
                current.next = node;
                current = node;
                list1 = list1.next;
            }else{
                ListNode node = new ListNode(list2.val);
                current.next = node;
                current = node;
                list2 = list2.next;
            }

        }

        while(list1 != null){
            ListNode node = new ListNode(list1.val);
            current.next = node;
            current = node;
            list1 = list1.next;
        }
        while(list2 != null){
            ListNode node = new ListNode(list2.val);
            current.next = node;
            current = node;
            list2 = list2.next;
        }

        return dummy.next;

    }
}
