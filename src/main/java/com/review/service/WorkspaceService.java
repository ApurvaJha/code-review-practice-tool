package com.review.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class WorkspaceService {
    private final Path WORKSPACE_DIR = Paths.get(System.getProperty("user.home"), ".architectural-arena", "workspace");
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(WORKSPACE_DIR)) { Files.createDirectories(WORKSPACE_DIR); }
        } catch (Exception e) { System.err.println("❌ Failed to create workspace directory: " + e.getMessage()); }
    }

    public void saveSession(String language, String level, String scenario, String comments, String evaluation) {
        try {
            String id = String.valueOf(System.currentTimeMillis());
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ObjectNode sessionNode = mapper.createObjectNode();
            sessionNode.put("id", id);
            sessionNode.put("timestamp", timestamp);
            sessionNode.put("moduleType", "CODE_REVIEW");
            sessionNode.put("language", language);
            sessionNode.put("level", level);

            try { sessionNode.set("scenario", mapper.readTree(scenario)); } catch (Exception e) { sessionNode.set("scenario", mapper.createObjectNode()); }
            try { sessionNode.set("comments", mapper.readTree(comments)); } catch (Exception e) { sessionNode.set("comments", mapper.createObjectNode()); }
            try { sessionNode.set("evaluation", mapper.readTree(evaluation)); } catch (Exception e) { sessionNode.set("evaluation", mapper.createObjectNode()); }

            Path filePath = WORKSPACE_DIR.resolve(id + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), sessionNode);
        } catch (Exception e) { System.err.println("❌ Critical error saving session to disk: " + e.getMessage()); }
    }

    public void saveProductThinkingSession(String moduleType, String competency, String scenario, String candidateResponse, String evaluation, int hintsUsed) {
        try {
            String id = String.valueOf(System.currentTimeMillis());
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ObjectNode sessionNode = mapper.createObjectNode();

            sessionNode.put("id", id);
            sessionNode.put("timestamp", timestamp);
            sessionNode.put("moduleType", moduleType);
            sessionNode.put("competency", competency);
            sessionNode.put("hintsUsed", hintsUsed);

            try { sessionNode.set("scenario", mapper.readTree(scenario)); } catch (Exception e) { sessionNode.put("scenario", scenario); }
            sessionNode.put("candidateResponse", candidateResponse);
            try { sessionNode.set("evaluation", mapper.readTree(evaluation)); } catch (Exception e) { sessionNode.put("evaluation", evaluation); }

            Path filePath = WORKSPACE_DIR.resolve(id + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), sessionNode);
        } catch (Exception e) {
            System.err.println("❌ Critical error saving Product Thinking session to disk: " + e.getMessage());
        }
    }

    public String getRecentProductKeywords() {
        File[] files = WORKSPACE_DIR.toFile().listFiles((d, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) return "None";

        List<String> tags = new ArrayList<>();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File f : files) {
            try {
                JsonNode root = mapper.readTree(f);
                if ("PRODUCT_THINKING".equals(root.path("moduleType").asText())) {
                    JsonNode keywords = root.path("scenario").path("keywords");
                    if (keywords.isArray()) {
                        for (JsonNode kw : keywords) { tags.add(kw.asText()); }
                    }
                    if (tags.size() >= 15) break;
                }
            } catch (Exception e) { }
        }
        return tags.isEmpty() ? "None" : String.join(", ", tags);
    }

    // --- NEW: Nemesis Extraction Logic ---
    public String getRecentMissedOpportunities() {
        File[] files = WORKSPACE_DIR.toFile().listFiles((d, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) return "None";

        List<String> missed = new ArrayList<>();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        int count = 0;
        for (File f : files) {
            try {
                JsonNode root = mapper.readTree(f);
                if ("PRODUCT_THINKING".equals(root.path("moduleType").asText())) {
                    JsonNode missedNode = root.path("evaluation").path("missedOpportunities");
                    if (missedNode.isArray()) {
                        for (JsonNode m : missedNode) { missed.add(m.asText()); }
                    }
                    count++;
                    if (count >= 5) break; // Look at last 5 sessions
                }
            } catch (Exception e) { }
        }
        return missed.isEmpty() ? "None" : String.join("; ", missed);
    }

    public List<Map<String, Object>> listSessions() {
        File[] files = WORKSPACE_DIR.toFile().listFiles((d, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) return Collections.emptyList();
        List<Map<String, Object>> sessions = new ArrayList<>();
        for (File f : files) {
            try {
                JsonNode root = mapper.readTree(f);
                Map<String, Object> summary = new HashMap<>();
                summary.put("id", root.path("id").asText(f.getName()));
                summary.put("timestamp", root.path("timestamp").asText("Unknown Time"));

                String moduleType = root.path("moduleType").asText("CODE_REVIEW");
                summary.put("moduleType", moduleType);

                if ("PRODUCT_THINKING".equals(moduleType)) {
                    summary.put("competency", root.path("competency").asText("Unknown"));
                    summary.put("hintsUsed", root.path("hintsUsed").asInt(0));
                    JsonNode scenarioNode = root.path("scenario");
                    summary.put("title", scenarioNode.path("title").asText("Manager Scenario"));

                    JsonNode eval = root.path("evaluation");
                    if (!eval.isMissingNode()) {
                        summary.put("score", eval.path("score").asInt(0));
                        summary.put("hireSignal", eval.path("hireSignal").asText("Pending"));
                    }
                } else {
                    summary.put("language", root.path("language").asText("Unknown"));
                    summary.put("level", root.path("level").asText("Unknown"));
                    JsonNode scenarioNode = root.path("scenario");
                    summary.put("difficulty", scenarioNode.path("difficulty").asText(""));
                    List<String> focusAreas = new ArrayList<>();
                    JsonNode focusNode = scenarioNode.path("focusAreas");
                    if (focusNode.isArray()) { for (JsonNode area : focusNode) { focusAreas.add(area.path("tag").asText()); } }
                    summary.put("focusAreas", focusAreas);

                    JsonNode eval = root.path("evaluation");
                    int accurate = 0; int missed = 0; int superfluous = 0;
                    if (!eval.isMissingNode()) {
                        summary.put("score", eval.path("score").asInt(0));
                        summary.put("hireSignal", eval.path("hireSignal").asText("Pending"));
                        JsonNode feedback = eval.path("inlineFeedback");
                        if (feedback.isArray()) {
                            for (JsonNode fb : feedback) {
                                String type = fb.path("type").asText("").toLowerCase();
                                if (type.equals("accurate")) accurate++;
                                else if (type.equals("missed")) missed++;
                                else if (type.equals("superfluous")) superfluous++;
                            }
                        }
                    } else { summary.put("score", 0); summary.put("hireSignal", "Pending"); }
                    summary.put("accurateCount", accurate);
                    summary.put("missedCount", missed);
                    summary.put("superfluousCount", superfluous);
                }
                sessions.add(summary);
            } catch (Exception e) {}
        }
        sessions.sort((a, b) -> ((String)b.get("id")).compareTo((String)a.get("id")));
        return sessions;
    }

    public String getSession(String id) throws Exception {
        return Files.readString(WORKSPACE_DIR.resolve(id + ".json"));
    }
}