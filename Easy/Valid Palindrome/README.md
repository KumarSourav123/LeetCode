# LeetCode 125 - Valid Palindrome

## Problem Statement

A phrase is a **palindrome** if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.

Given a string `s`, return `true` if it is a **palindrome**, or `false` otherwise.

---

## Example 1:
```
Input: s = "A man, a plan, a canal: Panama"
Output: true
Explanation: "amanaplanacanalpanama" is a palindrome.
```

## Example 2:
```
Input: s = "race a car"
Output: false
Explanation: "raceacar" is not a palindrome.
```

## Example 3:
```
Input: s = " "
Output: true
Explanation: s is an empty string "" after removing non-alphanumeric characters. Since an empty string reads the same forward and backward, it is a palindrome.
```

## Constraints:
- `1 <= s.length <= 2 * 10^5`
- `s` consists only of printable ASCII characters.

---

## Approach

This solution first **normalizes** the string (lowercasing and stripping non-alphanumeric characters), then uses **two pointers** moving inward from both ends to check for symmetry.

### Idea

- Convert the entire string to lowercase using `toLowerCase()`.
- Use a regular expression, `replaceAll("[^a-z0-9]", "")`, to strip out every character that isn't a lowercase letter or digit — this removes spaces, punctuation, and any other non-alphanumeric characters in one step.
- With the cleaned string in hand, iterate `i` from `0` up to (but not including) the middle of the string (`s.length() / 2`).
  - At each step, compare the character at position `i` (from the front) with the character at position `s.length() - 1 - i` (the mirrored position from the back).
  - If any pair of characters doesn't match, the string isn't a palindrome, so return `false` immediately.
- If the loop completes without finding any mismatch, the string reads the same forward and backward, so return `true`.

---

## Code

```java
class Solution {
    public boolean isPalindrome(String s) {

        s = s.toLowerCase().replaceAll("[^a-z0-9]", "");

        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }
}
```

---

## Dry Run

### Input

```text
s = "race a car"
```

**Step 1: Normalize the string**

`s.toLowerCase()` → `"race a car"` (already all lowercase)

`replaceAll("[^a-z0-9]", "")` → removes the two spaces → `"raceacar"`

**Step 2: Two-pointer comparison** (`length = 8`, loop runs while `i < 4`)

| i | s.charAt(i) | s.charAt(length-1-i) | Match? |
|---|--------------|---------------------------|--------|
|0|'r' (index 0)|'r' (index 7)|Yes|
|1|'a' (index 1)|'a' (index 6)|Yes|
|2|'c' (index 2)|'c' (index 5)|Yes|
|3|'e' (index 3)|'a' (index 4)|**No** → return `false`|

Output:

```text
false
```

This matches the expected output, since `"raceacar"` is not a palindrome (`e` at index 3 doesn't match `a` at index 4).

---

## Why This Approach Works

Normalizing the string first (lowercasing and stripping non-alphanumeric characters via regex) reduces the problem to a plain character-by-character palindrome check, since the problem's definition of "palindrome" only cares about letters and digits, ignoring case and punctuation entirely.

Comparing characters from both ends moving inward is a standard and efficient way to verify a palindrome: if every mirrored pair of characters matches all the way to the middle, the string must read identically in both directions.

---

## Complexity Analysis

- **Time Complexity:** `O(n)` — the regex cleanup and lowercase conversion take `O(n)`, and the two-pointer scan takes `O(n)` as well
- **Space Complexity:** `O(n)` — since `replaceAll` and `toLowerCase` both create new string objects rather than modifying `s` in place

---

## Key Insight

Using a regular expression to strip non-alphanumeric characters in a single pass avoids manually writing character-filtering logic, and combining it with the classic two-pointer palindrome check keeps the overall solution concise while still being linear in time.

---

## Concepts Used

- String
- Two Pointers
- Regular Expressions
- Case Normalization
- Character Comparison