# ADR-011: Dynamic Difficulty Categorization

**Date:** 2026-07-19
**Author:** Apurva Jha
**Status:** Accepted

## Context
Code reviews vary wildly in complexity. Treating all generated scenarios identically doesn't reflect reality. To provide better context before the user begins a review, and to track performance more accurately in the analytics dashboard, the system needs to assess and expose the inherent difficulty of the generated pull request.

## Decision
1. **LLM-Driven Evaluation:** We will instruct the AI generator to explicitly grade its own generated PR as "Easy", "Medium", or "Hard".
2. **Categorization Heuristics:** The prompt will mandate that the AI weigh four specific factors: PR size, cognitive load of the context/README, total issue count, and the subtlety/type of the injected flaws.
3. **UI Badging:** We will introduce a color-coded badging system (Green, Yellow, Red) injected into both the main arena's "System Context" header and the Analytics Dashboard's session log.
4. **Graceful Degradation:** The backend and frontend will treat missing `difficulty` keys as empty/unrated to ensure backward compatibility with sessions saved prior to this feature.

## Consequences
*   **Pros:** Sets accurate user expectations before a review. Allows for richer future analytics (e.g., "Win rate on Hard concurrency bugs").
*   **Cons:** Increases the JSON schema complexity slightly. Relies on the LLM's subjective weighting of difficulty, which may occasionally misalign with human perception.