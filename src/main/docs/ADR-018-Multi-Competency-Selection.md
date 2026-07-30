# ADR 018: Multi-Competency Selection

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
Engineering Manager interviews rarely test a single competency in isolation. A scenario testing "Stakeholder Management" often inherently requires "Business Prioritization." The current implementation restricts the user to selecting only one target competency per generated scenario, limiting the realism and difficulty of the practice sessions.

## Decision
1.  **UI Update:** Replace the single-select `<select>` dropdown in `product-thinking.html` with a group of checkboxes, allowing the user to select one, two, or all three competencies simultaneously.
2.  **State Management:** The frontend JavaScript will aggregate the selected checkboxes into a comma-separated string before sending the request to the backend.
3.  **Prompt Update:** Modify `generate-product-thinking.txt` to grammatically handle a list of competencies rather than a singular item.

## Consequences
*   **Positive:** Candidates can generate highly complex, multi-faceted scenarios that closer mimic real-world EM loop interviews.
*   **Negative:** The LLM may struggle to heavily emphasize *all* selected competencies if too many are chosen, potentially leading to overly broad scenarios. We mitigate this by keeping the core list limited to three distinct options.
*   **Note on Backend:** No Java backend code changes are required. The existing `@RequestParam String competency` in the controller naturally accepts comma-separated values, and the `WorkspaceService` will save the compound string exactly as it does a single string.