# ADR 015: Ideal Response Generation & Conversational Follow-ups

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
The Product Thinking module successfully grades candidate responses, but lacks two critical learning mechanisms. First, candidates need an actionable example of a "good" answer to understand how to better address stakeholder asks. Second, the evaluation generates a "follow-up question," but the UI lacks a mechanism to actually answer it, breaking the interactive loop expected in a real EM interview.

## Decision
1.  **Ideal Response:** Update the JSON schema in `evaluate-product-thinking.txt` to include an `idealResponse` field, which synthesizes a perfect answer balancing the specific scenario constraints.
2.  **Conversational Loop:** Introduce a new prompt (`chat-product-thinking.txt`), a new service method, and a `/api/product-thinking/chat` endpoint.
3.  **UI Updates:** Render the ideal response in the evaluation panel and attach a chat input specifically for continuing the conversation with the PM persona.

## Consequences
*   **Positive:** Creates a complete feedback loop. The candidate can read the ideal response for immediate course correction, and then dynamically defend their position via the chat interface.
*   **Negative:** Increased token usage per session due to sending the conversation history back to the LLM on each chat turn.