# ADR 013: Domain Isolation for Product Thinking Module

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
The Architectural Assessment Engine is evolving from a strict code-review simulator into a comprehensive interview practice suite. The new requirement introduces a "Product Discussion & Customer Obsession" round. Unlike the existing module, which evaluates line-by-line code annotations, this new round requires generating text-based business scenarios and evaluating unstructured, conversational responses.

Initially, extending the existing `ReviewController` and `GeminiService` was considered, but doing so would create a monolithic service handling multiple distinct domains, violating the Single Responsibility Principle.

## Decision
We will isolate the new interview module into its own distinct domain namespace:
1. Create `ProductThinkingController` mapped to `/api/product-thinking`.
2. Create `ProductThinkingGeminiService` to handle prompts specific to business, product, and stakeholder management competencies.
3. Modify `WorkspaceService` to accept a `moduleType` discriminator (e.g., `CODE_REVIEW` vs. `PRODUCT_THINKING`) to allow a unified analytics dashboard without breaking historical session deserialization.

## Consequences
*   **Positive:** Domain-Driven Design is maintained. The core code-review tool remains untouched and robust. Future modules (e.g., System Design, Behavioral) can replicate this isolated pattern.
*   **Negative:** Minor duplication of the Gemini client instantiation and resource reading logic across services, which may necessitate a shared `BaseGeminiClient` in future refactoring.