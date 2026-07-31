# ADR 028: Obsidian-Optimized Markdown Export

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
Candidates hit a plateau if they do not actively review their past mistakes. While the JSON files are saved locally, they are not easily readable or searchable as study materials. The candidate needs a seamless way to port scenarios, evaluations, and chat histories into their personal knowledge management system.

## Decision
1. Implement a client-side Markdown generator in `product-thinking.html`.
2. Format the output with YAML frontmatter (for metadata like score, date, and competency) and hashtag structures optimized for Obsidian knowledge vaults.
3. Trigger a browser-level Blob download to save the `.md` file to the user's local machine instantly.

## Consequences
*   **Positive:** Creates actionable, highly searchable study notes with zero backend API overhead. Allows the candidate to easily track their "Missed Opportunities" across multiple sessions to fix recurring anti-patterns.
*   **Negative:** None.