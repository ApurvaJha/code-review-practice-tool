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

    public String generateProblem(String language, String level) throws Exception {
        String promptTemplate = readResource(generatePromptResource);
        // Inject level and language into the prompt
        String prompt = String.format(promptTemplate, level, language);

        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String evaluateReview(String language, String level, String targetCode, String candidateCommentsJson) throws Exception {
        String promptTemplate = readResource(evaluatePromptResource);
        // Inject level, language, code, and comments into the prompt
        String prompt = String.format(promptTemplate, level, language, targetCode, candidateCommentsJson);

        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    private String cleanRawResponse(String raw) {
        if (raw == null) return "{}";
        return raw.replaceAll("```json", "").replaceAll("```", "").trim();
    }
}