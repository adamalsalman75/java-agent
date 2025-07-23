package dev.alsalman.javaagent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobStoreTests {

    private JobStore jobStore;

    @BeforeEach
    void setUp() {
        jobStore = new JobStore();
    }

    @Test
    void newJob_shouldCreateJobWithPendingStatus() {
        String jobId = jobStore.newJob();
        assertNotNull(jobId);

        JobStore.Job job = jobStore.getJob(jobId);
        assertNotNull(job);
        assertEquals(jobId, job.id());
        assertEquals(JobStore.JobStatus.RUNNING, job.status());
        assertNull(job.result());
        assertNull(job.errorMessage());
    }

    @Test
    void getJob_shouldReturnNullForUnknownId() {
        assertNull(jobStore.getJob("unknown-id"));
    }

    @Test
    void completeJob_shouldUpdateJobToCompleted() {
        String jobId = jobStore.newJob();
        Response response = new Response("story", java.util.Collections.emptyList());

        jobStore.completeJob(jobId, response);

        JobStore.Job job = jobStore.getJob(jobId);
        assertEquals(JobStore.JobStatus.COMPLETED, job.status());
        assertEquals(response, job.result());
        assertNull(job.errorMessage());
    }

    @Test
    void failJob_shouldUpdateJobToFailed() {
        String jobId = jobStore.newJob();
        String errorMessage = "Something went wrong";

        jobStore.failJob(jobId, errorMessage);

        JobStore.Job job = jobStore.getJob(jobId);
        assertEquals(JobStore.JobStatus.FAILED, job.status());
        assertEquals(errorMessage, job.errorMessage());
        assertNull(job.result());
    }
}
