# ADR 019: Strict Evaluation Scoring Scale

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
During the evaluation phase of the Product Thinking module, the LLM graded a highly competent answer as an "8", intending an 8/10 scale. Because the frontend UI statically displays `[score]/100`, the candidate's result rendered as `8/100 (Hire)`. The prompt lacked explicit boundaries for the expected integer scale.

## Decision
Modify the JSON schema definition inside `evaluate-product-thinking.txt` to explicitly mandate an integer between 0 and 100, providing an inline comment in the schema example to reinforce this constraint.

## Consequences
*   **Positive:** Eliminates scoring scale mismatches between the LLM generation and frontend rendering. Scores will correctly reflect a percentage-based grading system (e.g., 85/100).
*   **Negative:** None.