# 26. Remove Duplicates from Sorted Array

**Difficulty:** Easy  
**Topic:** Array, Two Pointers

---

## Problem Statement

Given an integer array `nums` sorted in **non-decreasing order**, remove the duplicates **in-place** such that each unique element appears only once.

The relative order of the elements should be kept the same.

Return the number of unique elements `k`.

The first `k` elements of `nums` should contain the unique elements. The remaining elements beyond `k` do not matter.

---

## Example 1

### Input

```text
nums = [1,1,2]
```

### Output

```text
2
```

### Modified Array

```text
[1,2,_]
```

---

## Example 2

### Input

```text
nums = [0,0,1,1,1,2,2,3,3,4]
```

### Output

```text
5
```

### Modified Array

```text
[0,1,2,3,4,_,_,_,_,_]
```

---

# Approach

Since the array is **already sorted**, all duplicate values appear next to each other.

Use **Two Pointers**:

- **Slow Pointer (`s`)**
  - Points to the last unique element.

- **Fast Pointer (`f`)**
  - Traverses the array searching for the next unique element.

Whenever a new unique element is found:

1. Move the slow pointer one step forward.
2. Place the new unique element at the slow pointer's position.

At the end, the first `s + 1` elements of the array are unique.

---

# Dry Run

### Input

```text
nums = [1,1,2,2,3]
```

Initially

```text
Index : 0 1 2 3 4
Array : 1 1 2 2 3
        S
          F
```

---

### Step 1

```text
nums[S] = 1
nums[F] = 1
```

Duplicate found.

Move only `F`.

```text
Index : 0 1 2 3 4
Array : 1 1 2 2 3
        S
            F
```

---

### Step 2

```text
nums[S] = 1
nums[F] = 2
```

New unique element.

Move `S`.

```text
Index : 0 1 2 3 4
Array : 1 1 2 2 3
          S
            F
```

Swap/Place the value.

```text
Array : 1 2 1 2 3
```

(Only the first `k` elements matter.)

---

### Step 3

```text
nums[S] = 2
nums[F] = 2
```

Duplicate.

Move `F`.

---

### Step 4

```text
nums[S] = 2
nums[F] = 3
```

New unique element.

Move `S`.

Place the value.

```text
Array : 1 2 3 2 1
```

The first three elements are now unique.

Return

```text
3
```

---

# Java Solution

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int s = 0;
        int f = 1;
        while(f < nums.length){
            if(nums[s] != nums[f]){
                s++;
                int temp = nums[s];
                nums[s] = nums[f];
                nums[f] = temp;
            }
            f++;
        }
        return s+1;
    }
}
```

---

# Complexity Analysis

### Time Complexity

```text
O(n)
```

The array is traversed only once.

---

### Space Complexity

```text
O(1)
```

No extra data structure is used.

---

# Key Learning

- The array is **sorted**, so duplicates are adjacent.
- Two pointers allow us to modify the array **in-place**.
- `slow` always points to the last unique element.
- `fast` scans the remaining array looking for the next unique value.
- Only the first `k` elements are important after the operation.

---

# Concepts Used

- Array
- Two Pointers
- In-place Modification

---

# LeetCode

- **Problem:** 26. Remove Duplicates from Sorted Array
- **Difficulty:** Easy
- **Pattern:** Two Pointers