# ADR 029: Verbal Mode (Web Speech API Integration)

**Date:** 2026-07-31
**Author:** Apurva Jha
**Status:** Accepted

## Context
Written practice creates a false sense of security. Candidates often pause, backspace, and restructure their thoughts while typing. Real engineering leadership loops require on-the-fly verbal synthesis, structured communication, and the ability to articulate complex trade-offs without rambling.

## Decision
1. Implement a "Hold to Speak" feature in `product-thinking.html` utilizing the browser's native `SpeechRecognition` interface (Web Speech API).
2. The transcribed speech will be dynamically appended to the candidate response `<textarea>` in real-time, capturing both interim and final results.
3. Apply visual feedback (CSS pulsing animation) to clearly indicate when the microphone is hot.

## Consequences
*   **Positive:** Simulates high-pressure, real-time verbal communication. Forces the candidate to confront their use of filler words and rambling. Requires zero backend changes.
*   **Negative:** The Web Speech API is not fully supported in all browsers (primarily optimized for Chrome, Edge, and Safari). Since this is a personal, locally-hosted tool, browser standardization is not a blocker.