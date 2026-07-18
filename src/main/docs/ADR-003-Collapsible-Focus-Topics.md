# ADR-003: Collapsible Focus Topics and Custom Tooltips

**Date:** 2026-07-18
**Author:** Apurva Jha
**Status:** Accepted

## Context
The dynamic focus areas were rendering immediately on load, cluttering the UI. Additionally, relying on the browser's native `title` attribute for tooltips resulted in delayed or unreliable display of the focus area descriptions. We need a cleaner, more deliberate UI pattern similar to LeetCode's "Related Topics" section.

## Decision
1. **Collapsible UI:** We will group the generated tags inside a dedicated, collapsible "Topics to Focus" accordion. By default, this section will be collapsed to keep the UI clean.
2. **Custom CSS Tooltips:** We will replace the native browser tooltip with a custom, instant-rendering CSS tooltip that appears immediately on hover and clearly displays the evaluation criteria.

## Consequences
*   **Pros:** Keeps the sidebar clean and uncluttered. Gives the candidate the option to challenge themselves "blind" without looking at the hints, or reveal them by clicking the accordion. Tooltips are now instantly readable.
*   **Cons:** Adds slight complexity to the DOM manipulation and CSS styling in the frontend.