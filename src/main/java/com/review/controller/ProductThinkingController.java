package com.review.controller;
import com.review.service.ProductThinkingGeminiService;
import com.review.service.WorkspaceService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/product-thinking")
public class ProductThinkingController {
    private final ProductThinkingGeminiService productThinkingService;
    private final WorkspaceService workspaceService;

    private String activeScenario = "";
    private String activeCompetency = "";
    private String activeInitialResponse = "";
    private String activeFollowUp = "";

    public ProductThinkingController(ProductThinkingGeminiService productThinkingService, WorkspaceService workspaceService) {
        this.productThinkingService = productThinkingService;
        this.workspaceService = workspaceService;
    }

    @GetMapping(value = "/generate", produces = "application/json")
    public String getNextScenario(
            @RequestParam(defaultValue = "E-commerce") String domain,
            @RequestParam(defaultValue = "Product Discussion & Customer Obsession") String competency) throws Exception {
        this.activeCompetency = competency;

        String pastTopics = workspaceService.getRecentProductKeywords();
        String scenario = productThinkingService.generateScenario(domain, competency, pastTopics);

        this.activeScenario = scenario;
        return scenario;
    }

    @PostMapping(value = "/evaluate", consumes = "application/json", produces = "application/json")
    public String processEvaluation(@RequestBody Map<String, Object> payload) throws Exception {
        String candidateResponse = (String) payload.getOrDefault("text", "");
        int hintsUsed = (Integer) payload.getOrDefault("hintsUsed", 0);

        this.activeInitialResponse = candidateResponse;
        String evaluation = productThinkingService.evaluateResponse(this.activeScenario, candidateResponse);

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(evaluation);
            this.activeFollowUp = root.path("followUpQuestion").asText("");
        } catch (Exception e) {
            this.activeFollowUp = "";
        }

        workspaceService.saveProductThinkingSession(
                "PRODUCT_THINKING",
                activeCompetency,
                activeScenario,
                candidateResponse,
                evaluation,
                hintsUsed
        );
        return evaluation;
    }

    @PostMapping(value = "/chat", consumes = "application/json", produces = "application/json")
    public String processChat(@RequestBody Map<String, String> payload) throws Exception {
        String query = payload.getOrDefault("query", "");
        String history = payload.getOrDefault("history", "");
        return productThinkingService.chatAboutScenario(
                this.activeScenario,
                this.activeInitialResponse,
                this.activeFollowUp,
                history,
                query
        );
    }
}