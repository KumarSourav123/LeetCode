# LeetCode 145 - Binary Tree Postorder Traversal

## Problem Statement

Given the `root` of a binary tree, return *the postorder traversal of its nodes' values*.

---

## Example 1:
```
Input: root = [1,null,2,3]
Output: [3,2,1]
Explanation: Node 1 has a right child 2, and node 2 has a left child 3.
```

## Example 2:
```
Input: root = [1,2,3,4,5,null,8,null,null,6,7,9]
Output: [4,6,7,5,2,9,8,3,1]
```

## Example 3:
```
Input: root = []
Output: []
```

## Example 4:
```
Input: root = [1]
Output: [1]
```

## Constraints:
- The number of the nodes in the tree is in the range `[0, 100]`.
- `-100 <= Node.val <= 100`

**Follow up:** Recursive solution is trivial, could you do it iteratively?

---

## Approach

This solution uses a straightforward **recursive depth-first traversal**. Postorder means visiting the **left subtree first, then the right subtree, and the node itself last**, so each node's value is added to a shared list only *after* both of its children have been fully processed.

### Idea

- Keep a shared `list` as a field of the class. It collects the visited values in order.
- **Base case:** if `root == null`, there's nothing to visit — return `list` as-is.
- **Recursive case**, in this exact order:
  - `postorderTraversal(root.left)` — fully process the left subtree.
  - `postorderTraversal(root.right)` — fully process the right subtree.
  - `list.add(root.val)` — only now record the current node's value.
- Finally `return list`. The return value of the *inner* recursive calls is ignored; only the outermost call's return value is what the caller sees, but since every call appends to the same shared `list`, it ends up holding the complete traversal.

---

## Code

```java
class Solution {

    ArrayList<Integer> list = new ArrayList<>();

    public List<Integer> postorderTraversal(TreeNode root) {
        if(root == null){
            return list;
        }

        postorderTraversal(root.left);
        postorderTraversal(root.right);
        list.add(root.val);

        return list;
    }
}
```

---

## Dry Run

### Input (Example 1)

```text
root = [1, null, 2, 3]

    1
     \
      2
     /
    3
```

`list = []`

Call by call (indentation shows the recursion depth):

```text
postorderTraversal(1)
    postorderTraversal(1.left = null)   -> base case, returns
    postorderTraversal(1.right = 2)
        postorderTraversal(2.left = 3)
            postorderTraversal(3.left = null)    -> base case, returns
            postorderTraversal(3.right = null)   -> base case, returns
            list.add(3)                           -> list = [3]
        postorderTraversal(2.right = null)       -> base case, returns
        list.add(2)                               -> list = [3, 2]
    list.add(1)                                   -> list = [3, 2, 1]
```

| Step | Node being finished | Action | list after |
|------|----------------------|--------|------------|
|1|3 (both children `null`)|`list.add(3)`|`[3]`|
|2|2 (left subtree done, right is `null`)|`list.add(2)`|`[3, 2]`|
|3|1 (left is `null`, right subtree done)|`list.add(1)`|`[3, 2, 1]`|

**Return:** `list = [3, 2, 1]`

Output:

```text
[3,2,1]
```

This matches the expected output.

---

## Why This Approach Works

Postorder is defined recursively: *traverse the left subtree, traverse the right subtree, then visit the node*. The code mirrors that definition line for line, so correctness follows directly from the definition.

The recursion also guarantees that a node's value is appended only after every value in both of its subtrees has been appended. That's why a parent always appears **after** its children in the result (`3` before `2`, `2` before `1` in the example above).

The base case (`root == null`) is what stops the recursion at the leaves' missing children, and it makes empty trees (Example 3) work naturally, since the very first call returns the empty list immediately.

One thing to be aware of: because `list` is an **instance field** rather than a local variable, it accumulates across calls on the *same* `Solution` object. That's fine when each test gets a fresh `Solution` instance (as LeetCode does), but calling `postorderTraversal` on the same instance for two different trees would mix their values together.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — every node is visited exactly once, and each visit does constant work
- **Space Complexity:** `O(h)` for the recursion call stack, where `h` is the height of the tree (`O(log n)` for a balanced tree, up to `O(n)` for a completely skewed one), plus `O(n)` for the output list itself

---

## Key Insight

Recursive tree traversals are all the same skeleton with the "visit the node" line moved: before the two recursive calls gives **preorder**, between them gives **inorder**, and after them (as here) gives **postorder**. For the iterative follow-up, the recursion can be replaced with an explicit stack — for example by doing a modified preorder (node, right, left) and reversing the result, or by tracking the last visited node so a parent is only popped once both children are done.

---

## Concepts Used

- Binary Tree
- Depth-First Search (DFS)
- Recursion
- Postorder Traversal