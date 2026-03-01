# Strings

> Problems involving string manipulation, comparison, and pattern matching.

---

## 🧠 String Fundamentals in Java

### Key properties:
- **Immutable** — can't modify characters in place. `s1 = ","` reassigns the reference, not the object.
- **`String` vs `char`:** `"A"` is a String (double quotes), `'A'` is a char (single quotes)
- **`StringBuilder`** — mutable string builder, use for building strings in loops (avoids O(n²) concatenation)

### Common operations:

| Operation | Method | Time |
|-----------|--------|------|
| Length | `s.length()` | O(1) |
| Char at index | `s.charAt(i)` | O(1) |
| Substring | `s.substring(i, j)` | O(j-i) |
| To char array | `s.toCharArray()` | O(n) |
| Compare | `s.equals(t)` | O(n) |

### String ↔ Number patterns:
```java
// Right-to-left digit processing (Add Binary, Plus One)
int sum = carry;
sum += a.charAt(i--) - '0';
digit = sum % base;    // current digit
carry = sum / base;    // carry to next position
```

---

## 🧩 Problems Solved

### 67. Add Binary (Easy) ✅
- **Approach 1:** Pad shorter string, explicit if-else for each binary addition case
- **Approach 2 (Optimal):** Right-to-left with `sum % 2` (digit) and `sum / 2` (carry)
- **Key insight:** Same pattern as Plus One — no need to pad, `i >= 0 || j >= 0` handles different lengths
- **Time:** O(max(m,n)) | **Space:** O(max(m,n))
- 📄 [AddBinary.java](./AddBinary.java)

### 28. Find the Index of the First Occurrence (Easy) ✅
- **Approach 1:** `indexOf()` — one-liner
- **Approach 2:** Manual sliding window — `substring(i, i+m).equals(needle)`
- **Key:** Loop bound is `i <= n - m` (not `i < n`)
- **Time:** O(n×m) | **Space:** O(m)
- 📄 [FindFirstOccurrence.java](./FindFirstOccurrence.java)

### 14. Longest Common Prefix (Easy) ✅
- **Approach:** Start with shortest string, check `startsWith()` against all, trim from end if not
- **Gotcha:** Use `.isEmpty()` not `!= ""` for string comparison
- **Time:** O(n×m²) | **Space:** O(1)
- 📄 [LongestCommonPrefix.java](./LongestCommonPrefix.java)

### 344. Reverse String (Easy) ✅
- **Approach:** Two pointers — swap from both ends inward
- **Key:** `char[]` uses `s[i]` (mutable), `String` uses `charAt(i)` (immutable)
- **Time:** O(n) | **Space:** O(1)
- 📄 [ReverseString.java](./ReverseString.java)

### Merge Close Characters (Medium) ✅
- **Approach:** Simulation — `StringBuilder` + restart-scan loop
- **Key insight:** After each merge (delete right char), indices shift → must restart scan from index 0
- **Pattern:** `!merged` flag in outer loop short-circuits scan, `while` restarts from beginning
- **Bug hit:** `new StringBuilder()` (empty) instead of `new StringBuilder(s)` — loop never ran since `sb.length() == 0`
- **Time:** O(n³) worst case, n ≤ 100 | **Space:** O(n)
- 📄 [MergeCloseCharacters.java](./MergeCloseCharacters.java)

### Trim Trailing Vowels (Easy) ✅ — Weekly Contest
- **Approach 1 (Submitted):** Reverse scan with boolean flag + `StringBuilder.reverse()` — verbose
- **Approach 2 (Optimal):** Pointer from end — find last non-vowel, `substring(0, i+1)`
- **Takeaway:** "Remove trailing X" → pointer from the end, not "iterate and rebuild"
- **Time:** O(n) | **Space:** O(1)
- 📄 [TrimTrailingVowels.java](./TrimTrailingVowels.java)

### 151. Reverse Words in a String (Medium) ✅
- **Approach 1 (First attempt):** Manual char-by-char split into word list + reverse — failed on multiple/leading/trailing spaces (empty strings sneak into list)
- **Approach 2 (Optimal):** `trim()` + `split("\\s+")` + reverse loop
- **Key insight:** `trim()` + `split("\\s+")` handles all whitespace edge cases in one line
- **Time:** O(n) | **Space:** O(n)
- 📄 [ReverseWords.java](./ReverseWords.java)

### 557. Reverse Words in a String III (Easy) ✅
- **Approach:** Split → two-pointer swap on `char[]` per word → rejoin
- **Syntax bugs hit:** `"//s+"` (wrong regex), `.charAt()` on `char[]` (use `str[j]`), `sb.append(s)` instead of reversed word
- **Key:** `sb.append(char[])` works directly — no need for `new String()`
- **Time:** O(n) | **Space:** O(n)
- 📄 [ReverseWordsIII.java](./ReverseWordsIII.java)
