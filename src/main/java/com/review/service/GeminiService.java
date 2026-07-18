package com.review.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class GeminiService {

    private final Client client;
    private final String model;

    @Value("classpath:prompts/generate-prompt.txt")
    private Resource generatePromptResource;

    @Value("classpath:prompts/evaluate-prompt.txt")
    private Resource evaluatePromptResource;

    // Spring injects both values from application.properties
    public GeminiService(@Value("${gemini.api-key}") String apiKey,
                         @Value("${gemini.model:gemini-2.0-flash-lite}") String model) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
        this.model = model;
    }

    // Helper method to read the text files into Java strings
    private String readResource(Resource resource) throws Exception {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String generateProblem() throws Exception {
        String prompt = readResource(generatePromptResource);

        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String evaluateReview(String targetCode, String candidateCommentsJson) throws Exception {
        // Read the prompt template and inject the variables using String.format
        String promptTemplate = readResource(evaluatePromptResource);
        String prompt = String.format(promptTemplate, targetCode, candidateCommentsJson);

        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    private String cleanRawResponse(String raw) {
        if (raw == null) return "{}";
        return raw.replaceAll("```json", "").replaceAll("```", "").trim();
    }
}