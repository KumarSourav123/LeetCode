class Node{
    String data;
    Node back;
    Node forward;

    Node(String data){
        this.data = data;
        this.forward = null;
        this.back = null;
    }
}

class BrowserHistory {

    Node current = null;

    public BrowserHistory(String homepage) {
        Node newNode = new Node(homepage);
        current = newNode;        
    }
    
    public void visit(String url) {
        Node newNode = new Node(url);
        newNode.back = current;
        current.forward = newNode;
        current = newNode;
    }
    
    public String back(int steps) {
        while(steps > 0 && current.back != null){
            current = current.back;
            steps--;
        }

        return current.data;
    }
    
    public String forward(int steps) {
        while(steps > 0 && current.forward != null){           
            current = current.forward;
            steps--;           
        }

        return current.data;
    }
}

/**
 * Your BrowserHistory object will be instantiated and called as such:
 * BrowserHistory obj = new BrowserHistory(homepage);
 * obj.visit(url);
 * String param_2 = obj.back(steps);
 * String param_3 = obj.forward(steps);
 */