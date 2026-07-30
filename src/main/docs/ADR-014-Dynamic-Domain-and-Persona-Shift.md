# ADR 014: Dynamic Domain Context and Persona Shift for Product Thinking Module

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
The initial implementation of the Product Thinking module hardcoded the interviewer persona as an "Engineering Director" and did not allow the user to specify the industry context of the generated scenario. However, EM product rounds are frequently conducted by Product Managers, and a candidate's preparation is highly dependent on practicing within specific industry domains (e.g., e-commerce, enterprise cloud, food delivery).

## Decision
1. Update the LLM prompt templates (`generate-product-thinking.txt` and `evaluate-product-thinking.txt`) to adopt the persona of a "Senior Product Manager".
2. Introduce a `domain` parameter to the scenario generation pipeline.
3. Update the frontend UI to include a configuration panel where the user can specify the industry domain and target competency before generating a scenario.

## Consequences
*   **Positive:** The generated scenarios and subsequent evaluations will be more authentic to a real-world PM/EM dynamic. The tool becomes significantly more versatile for targeted practice.
*   **Negative:** Requires updating the existing method signatures in the Controller and Service layers, and shifting the frontend from auto-fetching on page load to a user-triggered fetch.c