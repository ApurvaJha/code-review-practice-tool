# ADR 025: Conversational Follow-up Scoring

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
The Product Thinking module provides a rich, scored evaluation for the initial candidate response, followed by an interactive chat with the PM persona. However, the subsequent follow-up replies from the candidate were only met with conversational text, lacking the objective scoring, missed opportunities, and "ideal answer" feedback present in the first stage.

## Decision
1. Update `chat-product-thinking.txt` to return a structured JSON response instead of raw text. The JSON will include the PM's conversational reply, a score for the candidate's latest argument, missed opportunities, and an ideal response snippet.
2. Update `ProductThinkingController.java` to explicitly produce `application/json` for the `/chat` endpoint.
3. Update the frontend `product-thinking.html` `submitChat()` function to parse this JSON and render an inline "mini-evaluation" box directly inside the chat history container.

## Consequences
*   **Positive:** Candidates receive continuous, structured feedback on their ability to defend trade-offs dynamically, not just on their initial essay.
*   **Negative:** Marginally increases the token output size and latency for the chat endpoint.