package dev.alsalman.javaagent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobStore {

    public enum JobStatus {
        RUNNING,
        COMPLETED,
        FAILED
    }

    public record Job(String id, JobStatus status, Response result, String errorMessage) {
    }

    private final Map<String, Job> jobs = new ConcurrentHashMap<>();

    public String newJob() {
        String jobId = java.util.UUID.randomUUID().toString();
        jobs.put(jobId, new Job(jobId, JobStatus.RUNNING, null, null));
        return jobId;
    }

    public void completeJob(String jobId, Response result) {
        jobs.put(jobId, new Job(jobId, JobStatus.COMPLETED, result, null));
    }

    public void failJob(String jobId, String errorMessage) {
        jobs.put(jobId, new Job(jobId, JobStatus.FAILED, null, errorMessage));
    }

    public Job getJob(String jobId) {
        return jobs.get(jobId);
    }
}