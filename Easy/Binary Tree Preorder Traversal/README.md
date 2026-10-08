# LeetCode 144 - Binary Tree Preorder Traversal

## Problem Statement

Given the `root` of a binary tree, return *the preorder traversal of its nodes' values*.

---

## Example 1:
```
Input: root = [1,null,2,3]
Output: [1,2,3]
Explanation: Node 1 has a right child 2, and node 2 has a left child 3.
```

## Example 2:
```
Input: root = [1,2,3,4,5,null,8,null,null,6,7,9]
Output: [1,2,4,5,6,7,3,8,9]
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

This solution uses a straightforward **recursive depth-first traversal**. Preorder means visiting the **node itself first, then its left subtree, then its right subtree**, so each node's value is added to a shared list *before* either of its children is explored.

### Idea

- Keep a shared `list` as a field of the class. It collects the visited values in order.
- **Base case:** if `root == null`, there's nothing to visit — return `list` as-is.
- **Recursive case**, in this exact order:
  - `list.add(root.val)` — record the current node's value **first**.
  - `preorderTraversal(root.left)` — then fully process the left subtree.
  - `preorderTraversal(root.right)` — then fully process the right subtree.
- Finally `return list`. The return values of the inner recursive calls are ignored; every call appends to the same shared `list`, so by the time the outermost call returns, `list` holds the complete traversal.

---

## Code

```java
class Solution {

    ArrayList<Integer> list = new ArrayList<>();

    public List<Integer> preorderTraversal(TreeNode root) {
        if(root == null){
            return list;
        }

        list.add(root.val);
        preorderTraversal(root.left);
        preorderTraversal(root.right);

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
preorderTraversal(1)
    list.add(1)                                  -> list = [1]
    preorderTraversal(1.left = null)             -> base case, returns
    preorderTraversal(1.right = 2)
        list.add(2)                              -> list = [1, 2]
        preorderTraversal(2.left = 3)
            list.add(3)                          -> list = [1, 2, 3]
            preorderTraversal(3.left = null)     -> base case, returns
            preorderTraversal(3.right = null)    -> base case, returns
        preorderTraversal(2.right = null)        -> base case, returns
```

| Step | Node being visited | Action | list after |
|------|---------------------|--------|------------|
|1|1|`list.add(1)`, then recurse left (`null`) and right|`[1]`|
|2|2|`list.add(2)`, then recurse left and right|`[1, 2]`|
|3|3|`list.add(3)`, both children are `null`|`[1, 2, 3]`|

**Return:** `list = [1, 2, 3]`

Output:

```text
[1,2,3]
```

This matches the expected output.

---

## Why This Approach Works

Preorder is defined recursively: *visit the node, traverse the left subtree, traverse the right subtree*. The code mirrors that definition line for line, so correctness follows directly from the definition.

Because `list.add(root.val)` runs **before** the recursive calls, a parent always appears **before** all of its descendants in the result (`1` before `2`, `2` before `3` above). Within a node, the entire left subtree is exhausted before the right subtree starts, which gives preorder's characteristic "go as deep along the left as possible first" order.

The base case (`root == null`) stops the recursion at missing children, and it makes an empty tree (Example 3) work naturally, since the very first call returns the empty list immediately.

One thing to be aware of: because `list` is an **instance field** rather than a local variable, it accumulates across calls on the *same* `Solution` object. That's fine when each test gets a fresh `Solution` instance (as LeetCode does), but reusing one instance for two different trees would mix their values together.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — every node is visited exactly once, and each visit does constant work
- **Space Complexity:** `O(h)` for the recursion call stack, where `h` is the height of the tree (`O(log n)` for a balanced tree, up to `O(n)` for a completely skewed one), plus `O(n)` for the output list itself

---

## Key Insight

The three recursive tree traversals share one skeleton and differ only in where the "visit the node" line goes: **before** the two recursive calls gives preorder (as here), **between** them gives inorder, and **after** them gives postorder. For the iterative follow-up, preorder is the easiest of the three to convert: push the root onto a stack, then repeatedly pop a node, record it, and push its **right** child followed by its **left** child (so the left one is popped first).

---

## Concepts Used

- Binary Tree
- Depth-First Search (DFS)
- Recursion
- Preorder Traversal