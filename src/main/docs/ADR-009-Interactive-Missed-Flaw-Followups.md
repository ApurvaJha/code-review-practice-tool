# ADR-009: Interactive Missed Flaw Follow-ups & Guardrails

**Date:** 2026-07-19
**Author:** Apurva Jha
**Status:** Accepted

## Context
Candidates often need clarification on *why* they missed a specific architectural flaw. Relying on a single static string from the AI is insufficient for complex topics like concurrency or distributed consensus. However, opening a free-form chat window exposes the application to prompt injection, context drifting (e.g., asking the LLM to solve unrelated LeetCode problems), and quota abuse.

## Decision
1. **Targeted Follow-up:** We will inject an "Ask Follow-up" button strictly onto feedback tagged as `Missed`.
2. **Context-Aware Chat:** Clicking this button opens a modal that feeds the LLM the original codebase scenario, the specific missed flaw, and the ongoing conversation history.
3. **Three-Strike Guardrail System:** The backend will enforce strict domain guardrails. The LLM will evaluate if the user's query is off-topic. If it is, the LLM will return a polite refusal and an `isOffTopic: true` flag. The frontend tracks these violations per flaw. Upon accumulating 3 strikes for a specific flaw, the chat window closes permanently for that item, simulating a mentor walking away from an unprofessional interaction.

## Consequences
*   **Pros:** Radically improves the educational value of the tool while protecting the system from prompt abuse. Maintains state locally per flaw so one locked-out comment doesn't ruin the whole review.
*   **Cons:** Requires maintaining local conversation state arrays in the frontend and introduces a new endpoint dependency on the backend.