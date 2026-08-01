# ADR 031: Nemesis Prompting and Hardcore Mode

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
To prevent a plateau in interview performance, the practice suite needs to shift from static scenario generation to an adaptive coaching model. Candidates must be forced to confront their repeated behavioral blind spots and handle sudden technical crises (incident command) while navigating product discussions.

## Decision
1.  **Historical Weakness Overloading (Nemesis Prompt):** Introduce `getRecentMissedOpportunities()` in `WorkspaceService` to extract the candidate's missed opportunities from the last 5 sessions. Inject these into `generate-product-thinking.txt` to force the LLM to design scenarios that target these exact weaknesses.
2.  **Hardcore Mode (Backend Wrench):** Add a UI toggle to the scenario configuration panel. When enabled, it passes a boolean to the `/chat` API, which injects a strict directive into `chat-product-thinking.txt`. This directive commands the PM persona to interrupt the product strategy with a catastrophic backend infrastructure failure, forcing an immediate pivot to technical incident response.

## Consequences
*   **Positive:** Creates a highly personalized, adaptive practice loop that actively patches behavioral anti-patterns and simulates the unpredictable pressure of high-level engineering leadership interviews.
*   **Negative:** None.