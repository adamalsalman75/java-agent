package dev.alsalman.javaagent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(AgentController.class)
class AgentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgentService agentService;

    @Test
    void submit_shouldReturnJobId() throws Exception {
        Request request = new Request("test topic");
        when(agentService.submitStoryRequest(any(Request.class))).thenReturn("job-123");

        mockMvc.perform(post("/story")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"topic\": \"test topic\" }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'jobId':'job-123'}"));
    }

    @Test
    void getResult_shouldReturnJob() throws Exception {
        String jobId = "job-123";
        JobStore.Job job = new JobStore.Job(jobId, JobStore.JobStatus.COMPLETED, new Response("story", java.util.Collections.emptyList()), null);
        when(agentService.getStoryResult(jobId)).thenReturn(job);

        mockMvc.perform(get("/story/{jobId}", jobId))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':'job-123','status':'COMPLETED','result':{'story':'story','reviews':[]},'errorMessage':null}"));
    }
}
