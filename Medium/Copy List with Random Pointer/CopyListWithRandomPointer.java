
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}


class Solution {
    public Node copyRandomList(Node head) {

        if(head == null){
            return head;
        }

        Node temp = head;

        while(temp != null){
            Node newNode = new Node(temp.val);
            newNode.next = temp.next;
            temp.next = newNode;
            temp = temp.next.next;
        }

        Node pointer = head;

        while(pointer != null){
            Node extra = pointer.random;
            if(extra == null){
                pointer.next.random = null;
            }else{
                pointer.next.random = extra.next;
            }
            pointer = pointer.next.next;
        }

        Node dummy = new Node(0);
        Node current = head;
        Node rest = dummy;

        while(current != null){ 
            rest.next = current.next; 
            rest = rest.next;          
            current.next = rest.next;
            current = current.next;
        }

        return dummy.next;

    }
}