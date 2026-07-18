# ADR-004: User-Selected Focus Areas and Strict README Contracts

**Date:** 2026-07-18
**Author:** Apurva Jha
**Status:** Accepted

## Context
1. The AI occasionally hallucinates or omits the public interfaces in the `README.md`, making it difficult for the candidate to understand the system's entry points and intended contracts before diving into the code.
2. The AI tends to bias toward a repetitive subset of the 11 focus areas when allowed to choose randomly. Users need the ability to manually override this and force the system to generate challenges testing specific weaknesses (e.g., forcing a concurrency and resilience test).

## Decision
1. **Strict Prompting for README:** We will explicitly instruct the AI that the `README.md` must contain the public interfaces (API endpoints, public methods, or message contracts) and their expected use cases.
2. **User-Driven Generation:** We will introduce a multi-select tag system in the frontend.
    * If the user selects tags, the Java backend will dynamically rewrite the prompt's instruction to strictly force those specific topics.
    * If the user leaves them unselected, the Java backend will instruct the AI to select 2-3 randomly.

## Consequences
*   **Pros:** Guarantees a clear specification in the README, accurately reflecting real-world PRs. Empowers the user to customize their training regimen.
*   **Cons:** Requires passing query parameters from the frontend through the Controller and modifying the `GeminiService` to dynamically compile the prompt template.