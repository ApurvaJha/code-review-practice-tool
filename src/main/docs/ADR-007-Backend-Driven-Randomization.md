# ADR-007: Backend-Driven Random Topic Selection

**Date:** 2026-07-19
**Author:** Apurva Jha
**Status:** Accepted

## Context
When instructed to "randomly select" topics from a list, Large Language Models exhibit strong token bias, frequently anchoring to the same subset of concepts (e.g., Concurrency, Resource Management, Resilience) as seen in recent generations. This defeats the purpose of providing a diverse, unpredictable training environment.

## Decision
We will remove the randomization responsibility from the AI. Instead, the Java backend will handle the true random selection. If a user submits an empty topic selection, the `GeminiService` will shuffle a hardcoded list of the 11 focus areas, select 3, and inject them into the prompt as a strict, mandatory directive.

## Consequences
*   **Pros:** Ensures a mathematically uniform distribution of challenge topics. Completely eliminates LLM anchoring bias.
*   **Cons:** Requires maintaining the list of available topics in both the frontend HTML and the backend Java service to ensure they stay perfectly synchronized.