package com.review.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;
    private final String MODEL = "gemini-3.5-flash";

    public GeminiService(@Value("${gemini.api-key}") String apiKey) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String generateProblem() throws Exception {
        String prompt = """
            Generate a unique, Staff/Principal-level Java architecture code review challenge.
            The system must be split across 2 to 4 distinct Java files (e.g., a Controller, a Service, and a Repository/Cache layer) to simulate a real multi-file PR.
            
            CRITICAL INSTRUCTION: 
            The very first file in the 'files' array MUST be named 'README.md'. 
            This README must act as the PR specification. It should briefly explain the intended business use-case, the expected behavior of the code, and what the system is *supposed* to do. 
            Do NOT reveal, hint at, or spoil any of the intentional bugs in the README. Write it exactly as an engineer would write a fresh PR description.
            
            The accompanying Java code must look professional and clean but contain 3-5 critical, structural system design flaws spread across the files (e.g., domain leaks between layers, distributed state bugs, lack of transaction boundaries, or cross-file race conditions).

            Return ONLY a raw JSON object. Do not wrap it in markdown.
            JSON Blueprint:
            {
              "description": "System architecture context, SLAs, scale requirements, and constraints.",
              "files": [
                {
                  "name": "README.md",
                  "content": "# System Overview\\n\\nThis PR introduces..."
                },
                {
                  "name": "OrderService.java",
                  "content": "package com.system;\\n\\npublic class OrderService {\\n..."
                }
              ]
            }
            """;

        GenerateContentResponse response = client.models.generateContent(MODEL, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String evaluateReview(String targetCode, String candidateCommentsJson) throws Exception {
        String prompt = String.format("""
            You are a Principal Software Engineer evaluating a candidate's high-level code review on a multi-file Pull Request for a Staff/Principal level role.
            
            Original System Code (JSON Format):
            %s
            
            Candidate's Submitted Line Comments (JSON Format mapped by File Name -> Line Number OR Line Range -> Comment):
            %s
            (Note: Keys in the comments JSON might be single lines like "12" or ranges representing a block of code like "15-22").
            
            Evaluate if they identified the severe cross-file system design flaws, or if they got distracted by local syntax mechanics. 
            Note: The candidate may have also left valid critiques on the README.md if they spotted inconsistencies between the stated spec and the actual code. Treat these as highly valuable Principal-level insights.
            
            Based on their performance, assign a numerical score out of 100, and a strict hiring signal.
            
            Return ONLY a raw JSON object. Do not wrap it in markdown.
            JSON Blueprint:
            {
              "score": 85,
              "hireSignal": "Strong Hire", 
              "overallVerdict": "A strict 3-sentence architectural review summary of their capability.",
              "missedFlaws": ["Systemic flaw 1 missed...", "Systemic flaw 2 missed..."],
              "superfluousComments": ["File X Line Y: Candidate focused on styling instead of the architectural flaw..."],
              "validCritiques": ["File Z Line Y: Candidate accurately identified..."]
            }
            // Note for hireSignal: It MUST be exactly one of: ["Strong Hire", "Hire", "Lean Hire", "No Hire"]
            """, targetCode, candidateCommentsJson);

        GenerateContentResponse response = client.models.generateContent(MODEL, prompt, null);
        return cleanRawResponse(response.text());
    }

    private String cleanRawResponse(String raw) {
        if (raw == null) return "{}";
        return raw.replaceAll("```json", "").replaceAll("```", "").trim();
    }
}