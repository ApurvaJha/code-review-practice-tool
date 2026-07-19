package com.review.controller;

import com.review.service.GeminiService;
import com.review.service.WorkspaceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final GeminiService geminiService;
    private final WorkspaceService workspaceService;

    private String activeCodeState = "";
    private String activeLanguage = "Java";
    private String activeLevel = "Principal";

    public ReviewController(GeminiService geminiService, WorkspaceService workspaceService) {
        this.geminiService = geminiService;
        this.workspaceService = workspaceService;
    }

    @GetMapping(value = "/generate", produces = "application/json")
    public String getNextProblem(
            @RequestParam(defaultValue = "Java") String language,
            @RequestParam(defaultValue = "Principal") String level,
            @RequestParam(required = false) String topics) throws Exception {

        this.activeLanguage = language;
        this.activeLevel = level;

        String problem = geminiService.generateProblem(language, level, topics);
        this.activeCodeState = problem;
        return problem;
    }

    @PostMapping(value = "/evaluate", consumes = "application/json", produces = "application/json")
    public String processEvaluation(@RequestBody String candidateComments) throws Exception {
        String evaluation = geminiService.evaluateReview(this.activeLanguage, this.activeLevel, this.activeCodeState, candidateComments);

        // Save the entire session state to the local workspace disk
        workspaceService.saveSession(activeLanguage, activeLevel, activeCodeState, candidateComments, evaluation);

        return evaluation;
    }

    @GetMapping(value = "/sessions", produces = "application/json")
    public List<Map<String, Object>> getSessions() {
        return workspaceService.listSessions();
    }

    @GetMapping(value = "/sessions/{id}", produces = "application/json")
    public String getSession(@PathVariable String id) throws Exception {
        return workspaceService.getSession(id);
    }
}