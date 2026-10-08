# LeetCode 94 - Binary Tree Inorder Traversal

## Problem Statement

Given the `root` of a binary tree, return *the inorder traversal of its nodes' values*.

---

## Example 1:
```
Input: root = [1,null,2,3]
Output: [1,3,2]
Explanation: Node 1 has a right child 2, and node 2 has a left child 3.
```

## Example 2:
```
Input: root = [1,2,3,4,5,null,8,null,null,6,7,9]
Output: [4,2,6,5,7,1,3,9,8]
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
- The number of nodes in the tree is in the range `[0, 100]`.
- `-100 <= Node.val <= 100`

**Follow up:** Recursive solution is trivial, could you do it iteratively?

---

## Approach

This solution uses a straightforward **recursive depth-first traversal**. Inorder means visiting the **left subtree first, then the node itself, then the right subtree**, so each node's value lands in the shared list *between* its left and right subtrees.

### Idea

- Keep a shared `list` as a field of the class. It collects the visited values in order.
- **Base case:** if `root == null`, there's nothing to visit — return `list` as-is.
- **Recursive case**, in this exact order:
  - `inorderTraversal(root.left)` — fully process the left subtree first.
  - `list.add(root.val)` — record the current node's value **in the middle**.
  - `inorderTraversal(root.right)` — then fully process the right subtree.
- Finally `return list`. The return values of the inner recursive calls are ignored; every call appends to the same shared `list`, so by the time the outermost call returns, `list` holds the complete traversal.

---

## Code

```java
class Solution {

    ArrayList<Integer> list = new ArrayList<>();

    public List<Integer> inorderTraversal(TreeNode root) {

        if(root == null){
            return list;
        }

        inorderTraversal(root.left);
        list.add(root.val);
        inorderTraversal(root.right);

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
inorderTraversal(1)
    inorderTraversal(1.left = null)              -> base case, returns
    list.add(1)                                  -> list = [1]
    inorderTraversal(1.right = 2)
        inorderTraversal(2.left = 3)
            inorderTraversal(3.left = null)      -> base case, returns
            list.add(3)                          -> list = [1, 3]
            inorderTraversal(3.right = null)     -> base case, returns
        list.add(2)                              -> list = [1, 3, 2]
        inorderTraversal(2.right = null)         -> base case, returns
```

| Step | Node being visited | Action | list after |
|------|---------------------|--------|------------|
|1|1 (left subtree is empty)|`list.add(1)`|`[1]`|
|2|3 (left subtree is empty)|`list.add(3)`|`[1, 3]`|
|3|2 (left subtree `{3}` already done)|`list.add(2)`|`[1, 3, 2]`|

**Return:** `list = [1, 3, 2]`

Output:

```text
[1,3,2]
```

This matches the expected output.

---

## Why This Approach Works

Inorder is defined recursively: *traverse the left subtree, visit the node, traverse the right subtree*. The code mirrors that definition line for line, so correctness follows directly from the definition.

Placing `list.add(root.val)` **between** the two recursive calls guarantees that everything in a node's left subtree is recorded before the node, and everything in its right subtree is recorded after it. Notice how in the example, node `2` has left child `3`, so `3` appears before `2` in the output even though `2` is the one reached first from the root.

The base case (`root == null`) stops the recursion at missing children, and it makes an empty tree (Example 3) work naturally, since the very first call returns the empty list immediately.

One thing to be aware of: because `list` is an **instance field** rather than a local variable, it accumulates across calls on the *same* `Solution` object. That's fine when each test gets a fresh `Solution` instance (as LeetCode does), but reusing one instance for two different trees would mix their values together.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — every node is visited exactly once, and each visit does constant work
- **Space Complexity:** `O(h)` for the recursion call stack, where `h` is the height of the tree (`O(log n)` for a balanced tree, up to `O(n)` for a completely skewed one), plus `O(n)` for the output list itself

---

## Key Insight

Inorder is the traversal with special meaning for **binary search trees**: on a valid BST, an inorder traversal visits the values in **sorted ascending order**, which is why it shows up in so many BST problems (validating a BST, finding the kth smallest element, and so on). For the iterative follow-up, use an explicit stack: keep walking left and pushing nodes until there's no left child, then pop a node, record it, and move to its right child, repeating until both the stack and the current pointer are empty.

---

## Concepts Used

- Binary Tree
- Depth-First Search (DFS)
- Recursion
- Inorder Traversal