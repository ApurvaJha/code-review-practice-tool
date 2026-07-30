# ADR 021: UI Grid Layout Patch

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
The CSS grid layout applied to the `body` tag in `product-thinking.html` lacked explicit row sizing. This caused the browser's default grid alignment to distribute vertical space evenly, resulting in a massive, unintended gap at the top of the page above the navigation header.

## Decision
Update the `body` CSS rule to include `grid-template-rows: auto 1fr;`. This forces the first row (the header) to only consume as much height as its content needs, and allows the second row (the scenario panels) to consume the remaining fractional space (`1fr`).

## Consequences
*   **Positive:** The header is appropriately pinned to the top of the viewport, removing the dead space.
*   **Negative:** None.