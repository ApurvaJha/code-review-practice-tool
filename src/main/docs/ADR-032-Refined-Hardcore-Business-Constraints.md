# ADR 032: Refined Hardcore Business Constraints

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
The initial implementation of "Hardcore Mode" (ADR 031) instructed the PM persona to interrupt the candidate with highly specific backend technical failures (e.g., Kafka split-brain). This broke the persona's realism, as Product Managers typically communicate in business impacts and symptoms, not infrastructure root causes.

## Decision
Update the `isHardcore` prompt injection string in `ProductThinkingGeminiService.java`. The PM will now interrupt with severe business, operational, or resourcing crises (e.g., halved timelines, competitor launches, or a sudden loss of engineering capacity due to an opaque production outage).

## Consequences
*   **Positive:** Restores the realism of the PM persona. Forces the candidate to handle executive-level business curveballs and prioritize resources on the fly, which is highly relevant for top-tier engineering leadership loops.
*   **Negative:** None.