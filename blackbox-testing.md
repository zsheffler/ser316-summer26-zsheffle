# Black Box Testing Report - Assignment 2

**Student Name:** Zachary Sheffler 
**ASU ID:** zsheffle  
**Date:** 06/03/2026

---

## Part 1: Equivalence Partitioning (EP)

Identify equivalence partitions for the `checkoutBook(Book book, Patron patron)` method based on the specification (JavaDoc).
Book, Partron
Create **multiple tables**, one per partition category (e.g., book state, patron state, renewal, limits, etc.).

Do **not** put everything into one table.

**Column Explanations:**
- **Partition ID**: Unique identifier (e.g., EP 1.1, EP 2.1)
- **State**: The specific state/value for this partition (e.g., "Unavailable", "Available")
- **Valid/Invalid**: Whether this partition represents valid or invalid input
- **Input Condition**: Precise condition that defines this partition
- **Expected Return**: What return code you expect
- **Expected Behavior**: What should happen

### Example EP Table: Book Availability

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1 | Unavailable (0 copies) | Invalid | availableCopies == 0 AND other conditions allow checkout | 2.0 | No copies to checkout |
| EP 1.2 | Available (1+ copies) | Valid | availableCopies > 0 AND other conditions allow checkout | Success | Book can be checked out |

**Example test cases:** `testBookAvailable()`, `testUnavailableBook()`

---

### Your EP Tables (add as many as needed)

Book
| Partition ID | State            | Valid/Invalid | Input Condition                              | Expected Return | Expected Behavior      |
|--------------|------------------|---------|----------------------------------------------------|-------------|----------------------------|
| EP 1.1 | Unavailable (0 copies) | Invalid | availableCopies == 0 AND OCAC                      | 2.0 Failure | No copies to checkout      |
| EP 1.2 | NULL book              | Invalid | other conditions allow checkout, OCAC              | 2.1 Failure | Book is null               |
| EP 1.3 | Available (1+ copies)  | Valid   | availableCopies > 0 AND OCAC                       | 0.0 Success | Book can be checked out    |
| EP 1.4 | Renewal                | Valid   | Partron already has book AND OCAC                  | 0.1 Success | Book can be checked out    |
| EP 1.5 | Some Overdue           | Valid   | Partron has 2 overdue books AND OCAC               | 1.1 Warning | Book can be checked out    |
| EP 1.6 | Many Overdue           | Valid   | Partron is withing 2 of max overdue books AND OCAC | 1.1 Warning | Book can be checked out    |
| EP 1.7 | Reference-only         | Valid   | Book is reference-only AND OCAC                    | 5.0 Failure | Book cannot be checked out |
Partron
| Partition ID | State            | Valid/Invalid | Input Condition       | Expected Return | Expected Behavior      |
|--------------|------------------|---------|-----------------------------|-------------|----------------------------|
| EP 2.1 | Suspended    | Valid   | Partron account is suspended AND OCAC | 3.0 Failure | Book cannot be checked out |
| EP 2.2 | Null         | Invalid | Partron account is NULL AND OCAC      | 3.1 Failure | Book cannot be checked out | 
| EP 2.2 | Max Checkout | Valid   | Partron has Max checkout AND OCAC     | 3.2 Failure | Book cannot be checked out | 
| EP 2.2 | Overdue      | Valid   | Partron has 3 overdue books AND OCAC  | 4.0 Failure | Book cannot be checked out |
| EP 2.3 | Tab          | Valid   | Partron owes more then $10 AND OCAC   | 4.1 Failure | Book cannot be ckecked out |

---

## Part 2: Boundary Value Analysis (BVA)

Important BVA cases may overlap with EP. That is OK. You can reference all relevant EP/BVA coverage in Part 3.

### Example BVA Table: Overdue Count (Threshold: 3)

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 1.1 | Below | overdueCount = 0 | Success (depends on other setup) | Below warning threshold |
| BVA 1.2 | Warning High | overdueCount = 2 | 1.0 | Just below reject threshold |
| BVA 1.3 | At | overdueCount = 3 | 4.0 | At rejection boundary |
| BVA 1.4 | Above | overdueCount = 4 | 4.0 | Above rejection boundary |

---

### Your BVA Tables (add more as needed)

| Test ID | Boundary     | Input Value | Expected Return  | Rationale                   |
|---------|--------------|-------------|------------------|-----------------------------|
| BVA 1.1 | Below        | overdueCount = 0 | 0.0 Success | Below warning threshold     |
| BVA 1.2 | Warning High | overdueCount = 2 | 1.0 Warning | Just below reject threshold |
| BVA 1.3 | At           | overdueCount = 3 | 4.0 Failure | At rejection boundary       |
| BVA 1.4 | Above        | overdueCount = 4 | 4.0 Failure | Above rejection boundary    |
| BVA 2.5 | Below        | tab = 0.0        | 0.0 Success | Below warning threshold     |
| BVA 2.6 | At           | tab = 10.0       | 4.1 Failure | At rejection boundary       |
| BVA 2.7 | Above        | tab = 14.0       | 4.1 Failure | Above rejection boundary    |

---

## Part 3: Test Cases Designed

List at least **20** test cases you designed based on your EP/BVA analysis.

Each test case should include:
- EP/BVA coverage
- specific inputs / setup
- expected return code
- expected **observable state changes** (if any)

> Do not test console output.

### Test Case Table
At least some of your tests should verify observable state changes, not just return values.

**Checkout0-3 Columns:** Mark each implementation as Pass (✓) or Fail (✗) for this test case. This helps you track which implementations have bugs and will be useful for Part 4 analysis.

| Test ID Name           | EP/BVA | Input Description                                                              | Expected Return | Expected State Changes                    | Checkout0 | Checkout1 | Checkout2 | Checkout3 |
|------------------------|--------|--------------------------------------------------------------------------------|-----------------|-------------------------------------------|-----------|-----------|-----------|-----------|
| T1 testUnavailableBook | EP 1.1 | Book unavailable (0 copies), eligible patron                                               | 2.0 | No state change                           | ✓         | ✓        | ✗         | ✓        |
| T2 testBookAvailable   | EP 1.3/BVA 2.5,1.1 | Book available (1+ copies), eligible patron, no warnings normal checkout       | 0.0 | Patron map updated; copies of book change | ✗         | ✗        | ✓         | ✓        |
| T3 nullBook            | EP 1.2 | Book is null, eligible partron, no checkout                                                | 2.1 | No State change                           |           |          |           |           |
| T4 renewingAbook       | EP 1.4 | Book precheckedout by partron, eligible patron, no warnings normal checkout                | 0.1 | Patron map updated; copies of book change |           |          |           |           |
| T5 someOverdueBooks    | EP 1.5 | Book available (1+ copies), eligible patron, warning partron has 1 overdue book            | 1.0 | Patron map updated; copies of book change |           |          |           |           |
| T6 nearMaxFaculty      | EP 1.6 | Book available (1+ copies), eligible patron, warning partron has many checkout books at 18 | 1.1 | Patron map updated; copies of book change |           |          |           |           |
| T7 nearMaxStudent      | EP 1.6 | Book available (1+ copies), eligible patron, warning partron has many checkout books at 8  | 1.1 | Patron map updated; copies of book change |           |          |           |           |
| T8 nearMaxPublic       | EP 1.6 | Book available (1+ copies), eligible patron, warning partron has many checkout books at 3  | 1.1 | Patron map updated; copies of book change |           |          |           |           |
| T9 nearMaxChild        | EP 1.6 | Book available (1+ copies), eligible patron, warning partron has many checkout books at 1  | 1.1 | Patron map updated; copies of book change |           |          |           |           |
| T10 nearMaxStaff       | EP 1.6 | Book available (1+ copies), eligible patron, warning partron has many checkout books at 13 | 1.1 | Patron map updated; copies of book change |           |          |           |           |
X| T11 nearMaxNull        | EP 1.6 | Book available (1+ copies), Null type patron, warning partron has many checkout books at 3 | 1.1 | Patron map updated; copies of book change |           |          |           |           |
| T12 referanceBook      | EP 1.7 | Book is Referance, eligible patron, no checkout                                            | 5.0 | No State change                           |           |          |           |           |
| T13 suspendedPatron    | EP 2.1 | Book available (1+ copies), Suspended patron, no checkout                                  | 3.0 | No State change                           |           |          |           |           |
| T14 nullPartron        | EP 2.2 | Book available (1+ copies), Null patron, no checkout                                       | 3.1 | No State change                           |           |          |           |           |
| T15 overDueLimit       | EP 2.2/BVA 1.3 | Book available (1+ copies), 3 Overdue books patron, no checkout                    | 4.0 | No State change                           |           |          |           |           |
| T16 tooHighOfTab       | EP 2.3/BVA 2.6 | Book available (1+ copies), $10 Tab patron, no checkout                            | 4.1 | No State change                           |           |          |           |           |
| T17 maxCheckoutFaculty | EP 2.2 | Book available (1+ copies), at 20 Checkout patron Faculty, no checkout                     | 3.2 | No State change                           |           |          |           |           |
| T18 maxCheckoutStudent | EP 2.2 | Book available (1+ copies), at 10 Checkout patron Student, no checkout                     | 3.2 | No State change                           |           |          |           |           |
| T19 maxCheckoutPublic  | EP 2.2 | Book available (1+ copies), at 5 Checkout patron Public, no checkout                       | 3.2 | No State change                           |           |          |           |           |
| T20 maxCheckoutChild   | EP 2.2 | Book available (1+ copies), at 3 Checkout patron Child, no checkout                        | 3.2 | No State change                           |           |          |           |           |
| T21 maxCheckoutStaff   | EP 2.2 | Book available (1+ copies), at 15 Checkout patron Staff, no checkout                       | 3.2 | No State change                           |           |          |           |           |
X| T22 maxCheckoutNull    | EP 2.2 | Book available (1+ copies), at 5 Checkout patron null type, no checkout                    | 3.2 | No State change                           |           |          |           |           |
| T23 waringHighOverdue  | BVA 1.2 | Book available (1+ copies), eligible patron overdueCount = 2, warnings normal checkout    | 1.0 | Patron map updated; copies of book change |           |          |           |           |
| T24 aboveOverdue       | BVA 1.4 | Book available (1+ copies), eligible patron overdueCount = 4                              | 4.0 | No State change                           |           |          |           |           |
| T25 tabAboveLimit      | BVA 2.7 | Book available (1+ copies), eligible patron  tab = 14.0                                   | 4.1 | No State change                           |           |          |           |           |

(Add rows until you have at least 20.)

---

## Part 4: Bug Analysis

### Easter Eggs Found
List any easter egg messages you observed:
- 
- 

### Implementation Results

| Implementation | Bugs Found (count) |
|----------------|---------------------|
| Checkout0      | |
| Checkout1      | |
| Checkout2      | |
| Checkout3      | |

### Bugs Discovered
List distinct bugs you identified for each implementation. Each bug must cite at least one test case that revealed it.

**Checkout0:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout1:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout2:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

**Checkout3:**
- Bug 1: [Brief description] — Revealed by: [Test ID]

### Comparative Analysis
Compare the four implementations:
- Which bugs are most critical (cause the worst failures)?
- Which implementation would you use if you had to choose?
- Why? Justify your choice considering bug severity and frequency.

---

## Part 5: Reflection

**Which testing technique was most effective for finding bugs?**

**What was the most challenging aspect of this assignment?**

**How did you decide on your EP and BVA?**
I read the comments above function checkoutBook.

**Describe one test where checking only the return value would NOT have been sufficient to detect a bug.**

