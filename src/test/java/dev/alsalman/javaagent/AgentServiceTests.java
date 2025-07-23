package dev.alsalman.javaagent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgentServiceTests {

    @Mock
    private ChatClient writerChatClient;

    @Mock
    private ChatClient reviewerChatClient;

    @Mock
    private JobStore jobStore;

    private AgentService agentService;

    @BeforeEach
    void setUp() {
        agentService = new AgentService(writerChatClient, reviewerChatClient, jobStore);
    }

    @Test
    void submitStoryRequest_shouldReturnJobId() {
        when(jobStore.newJob()).thenReturn("job-123");

        Request request = new Request("test topic");
        String jobId = agentService.submitStoryRequest(request);

        assertEquals("job-123", jobId);
    }

    @Test
    void getStoryResult_shouldReturnJob() {
        String jobId = "job-123";
        JobStore.Job expectedJob = new JobStore.Job(jobId, JobStore.JobStatus.RUNNING, null, null);
        when(jobStore.getJob(jobId)).thenReturn(expectedJob);

        JobStore.Job actualJob = agentService.getStoryResult(jobId);

        assertEquals(expectedJob, actualJob);
    }
}
