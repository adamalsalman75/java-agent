package dev.alsalman.javaagent;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AgentController {

    private final Orchestrator orchestrator;

    public AgentController(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/story")
    public Map<String, String> submitStoryRequest(@RequestBody Request request) {
        String jobId = orchestrator.submitStoryRequest(request);
        return Map.of("jobId", jobId);
    }

    @GetMapping("/story/{jobId}")
    public JobStore.Job getStoryResult(@PathVariable String jobId) {
        return orchestrator.getStoryResult(jobId);
    }
}