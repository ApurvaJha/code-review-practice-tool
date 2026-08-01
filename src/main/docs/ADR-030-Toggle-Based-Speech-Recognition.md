# ADR 030: Toggle-Based Speech Recognition

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
The initial implementation of the Verbal Mode utilized a "Hold to Speak" interaction model. However, Engineering Manager interview responses (especially structural frameworks like the 70/20/10 rule or the Problem-to-MVP pipeline) often take 2-3 minutes to articulate. Requiring the candidate to hold down a mouse button or trackpad for this duration is ergonomically cumbersome and distracting.

## Decision
Refactor the Web Speech API integration in `product-thinking.html` to use a toggle mechanism. A single click will initiate the recording state, transforming the button into a "Stop Recording" action. A subsequent click will terminate the recording session.

## Consequences
*   **Positive:** Significantly improves user ergonomics and mimics the hands-free nature of a real video interview.
*   **Negative:** None.