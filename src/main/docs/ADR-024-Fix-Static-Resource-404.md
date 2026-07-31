# ADR 024: Fix Static Resource 404 for Product Analytics

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
When navigating to `http://localhost:8080/history-pt.html`, the application returned a 404 Not Found error. In Spring Boot, static HTML files must be placed exactly inside the `src/main/resources/static/` directory to be served automatically at the root URL. If the file is added while the server is running, the compiled `target/` directory may not sync, resulting in a 404.

## Decision
1. Explicitly verify the creation of `history-pt.html` inside `src/main/resources/static/`.
2. Require a server restart/rebuild to ensure the static assets are copied to the classpath.
3. Confirm that the persistence layer (`WorkspaceService`) is correctly writing to the absolute path `~/.architectural-arena/workspace` so data survives JVM restarts.

## Consequences
*   **Positive:** Restores access to the Product Leadership Analytics dashboard. Confirms data durability.
*   **Negative:** None.