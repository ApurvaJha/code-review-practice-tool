# ADR 022: Anti-Repetition Guardrails and Split Dashboards

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
The LLM frequently defaults to generating "15-minute delivery" scenarios when domain constraints are loose. Additionally, the analytics dashboard currently mixes Code Review and Product Thinking data, and the Product Thinking tool lacks a mechanism to hydrate and review historical sessions.

## Decision
1.  **Anti-Repetition:** Require the LLM to generate a `keywords` array in its JSON output. `WorkspaceService` will parse recent sessions, extract these keywords, and inject them into the generation prompt as topics to strictly avoid.
2.  **Split Analytics:** `history.html` will be dedicated solely to Code Review. A new `history-pt.html` will be created for Product Thinking.
3.  **Session Hydration:** Add URL parameter parsing (`?session={id}`) to `product-thinking.html` to fetch and render past sessions in a read-only state.

## Consequences
*   **Positive:** Forces the LLM to traverse a wider breadth of business constraints (e.g., payment gateways, fraud detection, supply chain). Dashboards become focused and actionable.
*   **Negative:** Minor latency increase on generation due to the backend reading recent files from disk to extract past keywords.