package dev.alsalman.javaagent;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/story")
    public Map<String, String> submitStoryRequest(@RequestBody Request request) {
        String jobId = agentService.submitStoryRequest(request);
        return Map.of("jobId", jobId);
    }

    @GetMapping("/story/{jobId}")
    public JobStore.Job getStoryResult(@PathVariable String jobId) {
        return agentService.getStoryResult(jobId);
    }
}