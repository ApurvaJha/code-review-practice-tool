# ADR 027: Product Ideation & Feature Brainstorming Competency

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
Engineering Managers, especially at staff/principal-equivalent leadership levels, are often evaluated on their ability to operate autonomously as a "Technical Product Manager." The current competencies cover prioritization and stakeholder alignment, but lack a category dedicated to zero-to-one product ideation, where the EM must proactively brainstorm features based on technical capabilities and user pain points.

## Decision
1.  **UI Updates:** Add a new checkbox in `product-thinking.html` for "Product Ideation & Feature Brainstorming".
2.  **Framework Updates:** Introduce a new actionable blueprint in the Frameworks modal: *The Technical Leverage Brainstorm*. This provides a structured way to answer ideation questions by mapping existing backend capabilities to new frontend user value.
3.  **Prompt Updates:** Relax the hardcoded constraints in `generate-product-thinking.txt` so the LLM dynamically adapts the scenario requirements directly to the selected competencies, rather than forcing stakeholder management into every single prompt.

## Consequences
*   **Positive:** Candidates can now practice open-ended brainstorming and product vision questions, rounding out the suite for full EM loop preparation.
*   **Negative:** None.