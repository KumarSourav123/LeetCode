# LeetCode 1472 - Design Browser History

## Problem Statement

You have a browser of one tab where you start on the `homepage` and you can visit another `url`, get back in the history a number of `steps` or move forward in the history a number of `steps`.

Implement the `BrowserHistory` class:

- `BrowserHistory(string homepage)` Initializes the object with the `homepage` of the browser.
- `void visit(string url)` Visits `url` from the current page. It clears up all the forward history.
- `string back(int steps)` Move `steps` back in history. If you can only return `x` steps in the history and `steps > x`, you will return only `x` steps. Return the current `url` after moving back in history **at most** `steps`.
- `string forward(int steps)` Move `steps` forward in history. If you can only forward `x` steps in the history and `steps > x`, you will forward only `x` steps. Return the current `url` after forwarding in history **at most** `steps`.

---

## Example:
```
Input:
["BrowserHistory","visit","visit","visit","back","back","forward","visit","forward","back","back"]
[["leetcode.com"],["google.com"],["facebook.com"],["youtube.com"],[1],[1],[1],["linkedin.com"],[2],[2],[7]]
Output:
[null,null,null,null,"facebook.com","google.com","facebook.com",null,"linkedin.com","google.com","leetcode.com"]

Explanation:
BrowserHistory browserHistory = new BrowserHistory("leetcode.com");
browserHistory.visit("google.com");       // You are in "leetcode.com". Visit "google.com"
browserHistory.visit("facebook.com");     // You are in "google.com". Visit "facebook.com"
browserHistory.visit("youtube.com");      // You are in "facebook.com". Visit "youtube.com"
browserHistory.back(1);                   // You are in "youtube.com", move back to "facebook.com" return "facebook.com"
browserHistory.back(1);                   // You are in "facebook.com", move back to "google.com" return "google.com"
browserHistory.forward(1);                // You are in "google.com", move forward to "facebook.com" return "facebook.com"
browserHistory.visit("linkedin.com");     // You are in "facebook.com". Visit "linkedin.com"
browserHistory.forward(2);                // You are in "linkedin.com", you cannot move forward any steps.
browserHistory.back(2);                   // You are in "linkedin.com", move back two steps to "facebook.com" then to "google.com". return "google.com"
browserHistory.back(7);                   // You are in "google.com", you can move back only one step to "leetcode.com". return "leetcode.com"
```

## Constraints:
- `1 <= homepage.length <= 20`
- `1 <= url.length <= 20`
- `1 <= steps <= 100`
- `homepage` and `url` consist of `'.'` or lower case English letters.
- At most `5000` calls will be made to `visit`, `back`, and `forward`.

---

## Approach

This solution models the browser's history as a **doubly linked list**, where each visited page is a node with a `back` pointer (the previous page) and a `forward` pointer (the next page), and a single `current` reference always points at "the page you're on right now." Navigating back/forward is just walking this list; visiting a new page grows it (and implicitly discards anything ahead).

### Idea

- **`Node` class:** holds the page's `data` (the URL string) plus `back` and `forward` pointers, both initialized to `null`.
- **Constructor `BrowserHistory(homepage)`:** creates a single node for `homepage` and sets `current` to it — this is the very first, and so far only, page in the history.
- **`visit(url)`:** creates a new node for `url`, links it *backward* to wherever you currently are (`newNode.back = current`), links the current page *forward* to the new node (`current.forward = newNode`), then moves `current` to the new node. Critically, this **overwrites** whatever `current.forward` pointed to before — so any "forward" history that existed from the old `current` is silently discarded, exactly satisfying the "visiting clears forward history" requirement, without needing to explicitly walk and delete those now-unreachable nodes.
- **`back(steps)`:** repeatedly moves `current = current.back` as long as there are `steps` remaining **and** there's actually a previous page to move to (`current.back != null`). The loop naturally stops early if the start of history is reached before `steps` runs out, which is what caps the movement at "at most `steps`" rather than requiring exactly `steps`. Returns `current.data` after moving.
- **`forward(steps)`:** the mirror image of `back` — repeatedly moves `current = current.forward` while `steps > 0` and `current.forward != null`, then returns `current.data`.

---

## Code

```java
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
```

---

## Dry Run

### Input

```text
BrowserHistory("leetcode.com")
visit("google.com"); visit("facebook.com"); visit("youtube.com")
back(1); back(1); forward(1)
visit("linkedin.com")
forward(2); back(2); back(7)
```

Using short names: `L` = leetcode.com, `G` = google.com, `F` = facebook.com, `Y` = youtube.com, `Li` = linkedin.com

| Call | Effect on the chain | current after | Returned |
|------|----------------------|----------------|----------|
|`new BrowserHistory("leetcode.com")`|create `L`|`L`|—|
|`visit("google.com")`|create `G`; `G.back=L`; `L.forward=G`|`G`|—|
|`visit("facebook.com")`|create `F`; `F.back=G`; `G.forward=F`|`F`|—|
|`visit("youtube.com")`|create `Y`; `Y.back=F`; `F.forward=Y`|`Y`|—|

Chain so far: `L <-> G <-> F <-> Y`

| Call | Loop trace | current after | Returned |
|------|------------|----------------|----------|
|`back(1)`|`steps=1>0`, `Y.back=F≠null` → `current=F`, `steps=0`|`F`|`"facebook.com"`|
|`back(1)`|`steps=1>0`, `F.back=G≠null` → `current=G`, `steps=0`|`G`|`"google.com"`|
|`forward(1)`|`steps=1>0`, `G.forward=F≠null` → `current=F`, `steps=0`|`F`|`"facebook.com"`|

| Call | Effect on the chain | current after | Returned |
|------|----------------------|----------------|----------|
|`visit("linkedin.com")`|create `Li`; `Li.back=F`; `F.forward=Li` **(overwrites `F.forward`, which was `Y` — `Y` is now unreachable, discarding the forward history)**|`Li`|—|

Chain now: `L <-> G <-> F <-> Li` (with `Y` orphaned, no longer part of the reachable chain)

| Call | Loop trace | current after | Returned |
|------|------------|----------------|----------|
|`forward(2)`|`steps=2>0` but `Li.forward=null` → condition false, loop doesn't run at all|`Li`|`"linkedin.com"`|
|`back(2)`|iter1: `steps=2>0`, `Li.back=F≠null` → `current=F`, `steps=1`. iter2: `steps=1>0`, `F.back=G≠null` → `current=G`, `steps=0`|`G`|`"google.com"`|
|`back(7)`|iter1: `steps=7>0`, `G.back=L≠null` → `current=L`, `steps=6`. iter2: `steps=6>0`, but `L.back=null` → condition false, loop exits|`L`|`"leetcode.com"`|

**Full output sequence:**

```text
[null,null,null,null,"facebook.com","google.com","facebook.com",null,"linkedin.com","google.com","leetcode.com"]
```

This matches the expected output.

---

## Why This Approach Works

A browser's history is naturally a linear sequence with a single "current position" that can move in either direction — which is exactly what a doubly linked list with a movable `current` pointer represents. Each node only ever needs to know its immediate neighbors (`back` and `forward`), so navigating any number of steps is just repeated single-hop traversal, bounded by however many hops actually exist.

The forward-history-clearing behavior falls out naturally from how `visit` is written, without any explicit cleanup logic: `current.forward = newNode` simply **replaces** the old forward link. Any node that used to be reachable via that old link becomes unreachable from `current` onward (nothing in the chain points to it anymore), which is behaviorally identical to having deleted it — Java's garbage collector reclaims it, but from the algorithm's perspective it's already gone.

The `steps > 0 && current.back/forward != null` loop condition in both `back` and `forward` is what correctly implements the "at most `steps`" requirement: rather than requiring exactly `steps` valid moves (which could run off the end of the history), the loop simply stops whichever comes first — running out of `steps`, or running out of history to move through.

---

## Complexity Analysis

- **Time Complexity:**
  - `BrowserHistory(homepage)`: `O(1)` — creates a single node.
  - `visit(url)`: `O(1)` — creates one node and relinks a constant number of pointers.
  - `back(steps)` / `forward(steps)`: `O(steps)` in the worst case, since each step is one pointer hop; bounded above by the total number of pages visited so far in that direction.
- **Space Complexity:** `O(n)` overall, where `n` is the total number of pages ever visited (each `visit` call allocates one new node); each individual operation itself only uses `O(1)` extra space beyond the node it may create.

---

## Key Insight

Modeling "current position within a bidirectional history" as a doubly linked list with a single movable pointer is a clean fit because it mirrors the real-world behavior exactly: moving back and forward is just following existing links, and the "new visit invalidates forward history" rule is handled implicitly by simply overwriting a pointer, rather than needing to explicitly track or prune a separate forward-history list.

---

## Concepts Used

- Doubly Linked List
- Design (Object-Oriented State Machine)
- Pointer Manipulation
- Implicit Garbage / Unreachable Node Discarding