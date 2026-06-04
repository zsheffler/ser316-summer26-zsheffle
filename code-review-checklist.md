# Code Review Checklist

**Reviewer Name:** [Your Name]
**Date:** [Date]
**Branch:** Review

## Instructions
Review ALL source files (in main not test) in the project and identify defects using the categories below. Log at least 5 defects total:
- At least 1 from CS (Coding Standards)
- At least 1 from CG (Code Quality/General)
- At least 1 from FD (Functional Defects)
- Remaining can be from any category

## Review Categories

- **CS**: Coding Standards (naming conventions, formatting, style violations)
- **CG**: Code Quality/General (design issues, code smells, maintainability)
- **FD**: Functional Defects (logic errors, incorrect behavior, bugs)
- **MD**: Miscellaneous (documentation, comments, other issues)

## Defect Log

| Defect ID | File | Line(s) | Category | Description | Severity |
|-----------|------|---------|----------|-------------|----------|
| 1 | calculateFine.java | 169     | CG | Use of magic numbers                        | Low      |
| 2 | calculateFine.java | 231     | CG | Use of magic numbers                        | Low      |
| 3 | calculateFine.java | 259&264 | CG | Returns -1 with no documention              | Medium   |
| 4 | Book.java          | 46      | FD | zero copies, yet avaible                    | Critical |
| 5 | Book.java          | 138     | CG | Magic number                                | Medium   |
| 6 | Partron.java       | 118     | CS | Should be switch                            | Medium   |
| 7 | Checkout.java      | 17      | CS | Variable bookList misleading - Map not List | Medium   |
| 8 | Book.java          | 107     | FD | Magic number 100 should be totalCopies      | High     |
| 9 | | | | | |
| 10 | | | | | |

**Severity Levels:**
- **Critical**: Causes system failure, data corruption, or security issues
- **High**: Major functional defect or significant quality issue
- **Medium**: Moderate issue affecting maintainability or minor functional problem
- **Low**: Minor style issue or cosmetic problem

## Example Entry

| Defect ID | File          | Line(s) | Category | Description                                | Severity |
|-----------|---------------|---------|----------|--------------------------------------------|----------|
| 1 | Checkout.java | 17      | CS       | Variable bookList misleading - Map not List | Medium |
| 2 | Book.java     | 107     | FD       | Magic number 100 should be totalCopies      | High |

## Notes
- Be specific with line numbers
- Provide clear, actionable descriptions
- Consider: readability, maintainability, correctness, performance, security
- Focus on issues that impact code quality or functionality
