# ADR-002: Dynamic Code Review Focus Areas

**Date:** 2026-07-18
**Author:** Apurva Jha
**Status:** Accepted

## Context
Code reviews often lack clear guardrails, leading candidates to focus on superficial syntax issues rather than deep architectural flaws. To properly simulate a directed Staff/Principal engineering review environment, the generated challenges need specific constraints.

## Decision
We will inject a predefined rubric of 11 core review focus areas into the generation prompt. The AI will randomly select 2-3 of these areas, design specific, targeted flaws around them, and return the selected areas in the JSON response payload.
The UI will parse these and display them as interactive, hoverable tags within the System Context sidebar to guide the candidate's review strategy.

## Consequences
*   **Pros:** Creates highly focused, realistic review scenarios. Prevents the AI from hallucinating random syntax errors, forcing it to stick to the requested constraints (e.g., "Concurrency" and "Resource Management").
*   **Cons:** Increases the length of the generation prompt, consuming slightly more input tokens per request.# ADR-002: Dynamic Code Review Focus Areas

**Date:** 2026-07-18
**Author:** Apurva Jha
**Status:** Accepted

## Context
Code reviews often lack clear guardrails, leading candidates to focus on superficial syntax issues rather than deep architectural flaws. To properly simulate a directed Staff/Principal engineering review environment, the generated challenges need specific constraints.

## Decision
We will inject a predefined rubric of 11 core review focus areas into the generation prompt. The AI will randomly select 2-3 of these areas, design specific, targeted flaws around them, and return the selected areas in the JSON response payload.
The UI will parse these and display them as interactive, hoverable tags within the System Context sidebar to guide the candidate's review strategy.

## Consequences
*   **Pros:** Creates highly focused, realistic review scenarios. Prevents the AI from hallucinating random syntax errors, forcing it to stick to the requested constraints (e.g., "Concurrency" and "Resource Management").
*   **Cons:** Increases the length of the generation prompt, consuming slightly more input tokens per request.