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

    public GeminiService(@Value("${gemini.api-key}") String apiKey,
                         @Value("${gemini.model:gemini-2.0-flash-lite}") String model) {
        this.client = Client.builder().apiKey(apiKey).build();
        this.model = model;
    }

    private String readResource(Resource resource) throws Exception {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String generateProblem(String language, String level, String topics) throws Exception {
        String promptTemplate = readResource(generatePromptResource);

        String topicsInstruction;
        if (topics == null || topics.trim().isEmpty()) {
            topicsInstruction = "Select exactly 2 to 3 Focus Areas randomly from the list below.";
        } else {
            topicsInstruction = "You MUST strictly focus on the following user-selected areas: " + topics + ". Do NOT select any other areas outside of these.";
        }

        // Inject level (%1$s), language (%2$s), and the dynamic topics instruction (%3$s)
        String prompt = String.format(promptTemplate, level, language, topicsInstruction);

        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String evaluateReview(String language, String level, String targetCode, String candidateCommentsJson) throws Exception {
        String promptTemplate = readResource(evaluatePromptResource);
        String prompt = String.format(promptTemplate, level, language, targetCode, candidateCommentsJson);

        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    private String cleanRawResponse(String raw) {
        if (raw == null) return "{}";
        return raw.replaceAll("```json", "").replaceAll("```", "").trim();
    }
}