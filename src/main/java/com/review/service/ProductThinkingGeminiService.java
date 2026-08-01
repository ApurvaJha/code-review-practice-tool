package com.review.service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class ProductThinkingGeminiService {
    private final Client client;
    private final String model;
    @Value("classpath:prompts/generate-product-thinking.txt") private Resource generatePromptResource;
    @Value("classpath:prompts/evaluate-product-thinking.txt") private Resource evaluatePromptResource;
    @Value("classpath:prompts/chat-product-thinking.txt") private Resource chatPromptResource;

    public ProductThinkingGeminiService(@Value("${gemini.api-key}") String apiKey, @Value("${gemini.model:gemini-2.0-flash-lite}") String model) {
        this.client = Client.builder().apiKey(apiKey).build();
        this.model = model;
    }

    private String readResource(Resource resource) throws Exception {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String generateScenario(String domain, String competency, String pastTopics, String recentWeaknesses) throws Exception {
        String promptTemplate = readResource(generatePromptResource);
        String prompt = String.format(promptTemplate, domain, competency, pastTopics, recentWeaknesses);
        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String evaluateResponse(String scenario, String candidateResponse) throws Exception {
        String promptTemplate = readResource(evaluatePromptResource);
        String prompt = String.format(promptTemplate, scenario, candidateResponse);
        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    public String chatAboutScenario(String scenario, String initialResponse, String followUp, String history, String newReply, boolean isHardcore) throws Exception {
        String promptTemplate = readResource(chatPromptResource);

        // --- UPDATED: Hardcore Business Wrench Injection ---
        String hardcoreInstruction = isHardcore
                ? "HARDCORE MODE: You MUST suddenly interrupt the candidate's strategy with a severe, unexpected business or operational crisis. Examples: The CEO just mandated this feature launch in half the original time, a major competitor just launched a clone of this today so we are losing market share, or a massive P0 production outage just drained 50% of the engineering team's capacity for the next month. Force the candidate to pivot their product and resourcing strategy instantly to handle this new business reality."
                : "";

        String prompt = String.format(promptTemplate, scenario, initialResponse, followUp, history, newReply, hardcoreInstruction);
        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return cleanRawResponse(response.text());
    }

    private String cleanRawResponse(String raw) {
        if (raw == null) return "{}";
        return raw.replaceAll("```json", "").replaceAll("```", "").trim();
    }
}