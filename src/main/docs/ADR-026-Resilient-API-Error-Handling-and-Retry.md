# ADR 026: Resilient API Error Handling & Retry Mechanism

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
As seen in the stack trace and `image_846cf5.png`, the application is susceptible to `503 Service Unavailable` errors when the Gemini API experiences sudden traffic spikes. Previously, the frontend optimistically rendered the user's chat message and cleared the input box *before* the backend confirmed success. When the API failed, it returned a Spring Boot HTML error page (shown in `image_9374f7.png`), causing the frontend JSON parser to crash, rendering `undefined` in the UI, and permanently losing the user's typed response.

## Decision
1.  **Pessimistic UI Updates:** Modify the `submitChat` and `submitResponse` JavaScript functions in `product-thinking.html` to hold the user's text in the input field until a `200 OK` HTTP status is explicitly returned by the backend.
2.  **Graceful Failures:** Introduce strict `!res.ok` checks before attempting to parse the response as JSON. If an error occurs, surface a transient alert to the user indicating a network spike, leaving their typed text completely intact so they can simply click "Reply" to retry.

## Consequences
*   **Positive:** Candidates no longer lose their carefully crafted, multi-paragraph responses during temporary API outages. The UI gracefully supports immediate retries.
*   **Negative:** The user's chat message doesn't instantly appear in the history log upon clicking "Reply", adding a slight perceptual delay, but this ensures state safety.