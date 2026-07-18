package com.review.controller;

import com.review.service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final GeminiService geminiService;
    private String activeCodeState = "";
    private String activeLanguage = "Java";
    private String activeLevel = "Principal";

    public ReviewController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping(value = "/generate", produces = "application/json")
    public String getNextProblem(
            @RequestParam(defaultValue = "Java") String language,
            @RequestParam(defaultValue = "Principal") String level) throws Exception {

        this.activeLanguage = language;
        this.activeLevel = level;

        String problem = geminiService.generateProblem(language, level);
        this.activeCodeState = problem;
        return problem;
    }

    @PostMapping(value = "/evaluate", consumes = "application/json", produces = "application/json")
    public String processEvaluation(@RequestBody String candidateComments) throws Exception {
        return geminiService.evaluateReview(this.activeLanguage, this.activeLevel, this.activeCodeState, candidateComments);
    }
}