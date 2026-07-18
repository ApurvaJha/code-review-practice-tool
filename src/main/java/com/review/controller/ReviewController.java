package com.review.controller;

import com.review.service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final GeminiService geminiService;
    private String ActiveCodeState = "";

    public ReviewController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping(value = "/generate", produces = "application/json")
    public String getNextProblem() throws Exception {
        String problem = geminiService.generateProblem();
        this.ActiveCodeState = problem;
        return problem;
    }

    @PostMapping(value = "/evaluate", consumes = "application/json", produces = "application/json")
    public String processEvaluation(@RequestBody String candidateComments) throws Exception {
        return geminiService.evaluateReview(this.ActiveCodeState, candidateComments);
    }
}