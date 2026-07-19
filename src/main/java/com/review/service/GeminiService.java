package com.review.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private final Client client;
    private final String model;

    @Value("classpath:prompts/generate-prompt.txt")
    private Resource generatePromptResource;

    @Value("classpath:prompts/evaluate-prompt.txt")
    private Resource evaluatePromptResource;

    @Value("classpath:prompts/topics.txt")
    private Resource topicsResource;

    @Value("classpath:prompts/chat-prompt.txt")
    private Resource chatPromptResource;

    private List<String> allTopicsCache = null;

    public GeminiService(@Value("${gemini.api-key}") String apiKey,
                         @Value("${gemini.model:gemini-2.0-flash-lite}") String model) {
        this.client = Client.builder().apiKey(apiKey).build();
        this.model = model;
    }

    private String readResource(Resource resource) throws Exception {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private List<String> getAvailableTopics() throws Exception {
        if (allTopicsCache == null) {
            String rawTopics = readResource(topicsResource);
            allTopicsCache = Arrays.stream(rawTopics.split("\\r?\\n"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(allTopicsCache);
    }

    public String generateProblem(String language, String level, String topics) throws Exception {
        String promptTemplate = readResource(generatePromptResource);
        String finalTopics = topics;

        if (finalTopics == null || finalTopics.trim().isEmpty()) {
            List<String> mutableTopics = getAvailableTopics();
            Collections.shuffle(mutableTopics);
            finalTopics = mutableTopics.stream()
                    .limit(3)
                    .collect(Collectors.joining(", "));
        }

        String prompt = String.format(promptTemplate, level, language, finalTopics);
        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String evaluateReview(String language, String level, String targetCode, String candidateCommentsJson) throws Exception {
        String promptTemplate = readResource(evaluatePromptResource);
        String prompt = String.format(promptTemplate, level, language, targetCode, candidateCommentsJson);
        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String chatAboutFlaw(String scenario, String missedComment, String query, String history) throws Exception {
        String promptTemplate = readResource(chatPromptResource);
        String prompt = String.format(promptTemplate, scenario, missedComment, history, query);
        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    private String cleanRawResponse(String raw) {
        if (raw == null) return "{}";
        return raw.replaceAll("```json", "").replaceAll("```", "").trim();
    }
}