# ADR-006: Local Workspace Session Storage

**Date:** 2026-07-19
**Author:** Apurva Jha
**Status:** Accepted

## Context
Currently, the application state is completely ephemeral. If the application restarts or the user generates a new scenario, all context regarding previous code reviews, candidate comments, and AI evaluations is permanently lost. To support continuous learning, we need a way to persist and review past performance.

## Decision
We will implement a local file-based storage mechanism.
1. **Workspace Directory:** A `workspace` folder will be dynamically created at the root of the project.
2. **JSON Bundling:** Upon completing an AI evaluation, the backend will bundle the generated scenario, the user's line-by-line comments, and the AI's grading rubric into a single timestamped JSON file.
3. **History UI:** The frontend will feature a "Past Sessions" modal that fetches a lightweight summary of all files in the workspace. Clicking a session will rehydrate the UI into a "read-only" historical mode, displaying the code, comments, and inline AI feedback exactly as it was during that session.

## Consequences
*   **Pros:** Requires no external database dependencies. Keeps the application highly portable. Allows users to easily backup or share their sessions by zipping the `workspace` folder.
*   **Cons:** File I/O operations block the main thread, though at this single-user scale, the latency impact is entirely negligible.