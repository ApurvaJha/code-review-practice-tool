# ADR-005: Strict Anti-Spoiler Constraints in Code Generation

**Date:** 2026-07-19
**Author:** Apurva Jha
**Status:** Accepted

## Context
The AI generation engine was leaving inline code comments (e.g., `// FLAW: ...`) directly above the intentional architectural bugs. This acts as a spoiler, entirely defeating the purpose of a blind code review assessment.

## Decision
We will add a strict negative constraint to the generation prompt, explicitly forbidding the AI from documenting, hinting at, or explaining the flaws in the code comments. We will instruct the AI to adopt the persona of an engineer who genuinely believes their code is ready for production.

## Consequences
*   **Pros:** Restores the integrity of the review challenge. Forces the candidate to read the code structurally rather than scanning for comments.
*   **Cons:** None.