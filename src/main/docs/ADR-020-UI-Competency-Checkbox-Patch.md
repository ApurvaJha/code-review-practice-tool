# ADR 020: UI Competency Checkbox Patch

**Date:** 2026-07-30
**Author:** Apurva Jha
**Status:** Accepted

## Context
Following ADR 018, the backend and JavaScript logic were updated to support multi-competency selection. However, the DOM structure in the configuration panel retained the legacy `<select>` dropdown, preventing users from selecting multiple options.

## Decision
Hard-replace the `<select id="config-competency">` element with a `<div id="config-competencies" class="checkbox-group">` containing standard HTML5 checkbox inputs. The CSS and JavaScript must explicitly target this new DOM structure.

## Consequences
*   **Positive:** Unblocks the multi-select feature, allowing the JavaScript `querySelectorAll` to correctly aggregate selected competencies.
*   **Negative:** None.