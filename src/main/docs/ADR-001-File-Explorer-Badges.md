# ADR-001: File Explorer Feedback Badges

**Date:** 2026-07-18
**Author:** Apurva Jha
**Status:** Accepted

## Context
Following the implementation of inline AI evaluation, users lose visibility of their performance distribution once the evaluation completes unless they manually click through every file. We need a way to summarize the density of accurate, missed, and superfluous comments at a glance in the file tree.

## Decision
We will introduce color-coded, circular indicator badges to the left-hand file explorer.
*   **Blue Circle:** Count of candidate's submitted comments.
*   **Green Circle:** Count of "Accurate" architectural insights.
*   **Yellow Circle:** Count of "Superfluous" comments.
*   **Red Circle:** Count of critical flaws "Missed" by the candidate.

## Consequences
*   **Pros:** Immediate visual feedback loop. The candidate can instantly see which files contain major missed flaws (red indicators) and navigate directly to them without guessing.
*   **Cons:** Requires recalculating the badge state dynamically whenever the AI evaluation JSON is returned, necessitating an update to the `renderFileExplorer` DOM manipulation logic.