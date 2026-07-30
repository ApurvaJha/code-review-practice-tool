# ADR 016: Dynamic Hints and Competency Frameworks

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
To improve the utility of the Product Thinking module as a learning tool, users need scaffolding when tackling complex business scenarios. Without guidance, users may struggle to structure their answers or miss the core trade-offs expected of an Engineering Manager.

## Decision
1.  **Dynamic Hints:** Update the `generate-product-thinking.txt` prompt to instruct the LLM to return an array of three progressive hints in the JSON response. Render these hints using HTML5 `<details>` tags on the scenario canvas, mimicking the progressive reveal pattern seen on platforms like LeetCode.
2.  **Framework Modal:** Implement a static UI modal accessible via a top-right navigation button. This modal will provide targeted mental models and structural frameworks tailored for the specific competencies (Product Discussion, Stakeholder Management, Business Prioritization).

## Consequences
*   **Positive:** Provides a safety net for users who get stuck, improving the overall practice experience. The frameworks serve as a baseline calibration for what a "good" answer looks like before the user even begins typing.
*   **Negative:** Marginally increases the token output size for the generation prompt. Requires adding modal state management to the vanilla JS frontend.