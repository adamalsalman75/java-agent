package dev.alsalman.javaagent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@Service
public class AgentService {

    private final ChatClient writer;
    private final ChatClient reviewer;
    private final JobStore jobStore;

    public AgentService(@Qualifier("writerChatClient") ChatClient writer, @Qualifier("reviewerChatClient") ChatClient reviewer, JobStore jobStore) {
        this.writer = writer;
        this.reviewer = reviewer;
        this.jobStore = jobStore;
    }

    public String submitStoryRequest(Request request) {
        String jobId = jobStore.newJob();
        Thread.ofVirtual().start(() -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                String story = "";
                List<String> reviews = new ArrayList<>();

                for (int i = 0; i < 5; i++) {
                    final String currentStory = story;
                    
                    var storyFuture = scope.fork(() -> writer.prompt()
                            .user(u -> u.text("""
                                    Based on the topic: {topic}
                                    Previous story: {story}
                                    Reviewer feedback: {reviews}
                                    Please write a new version of the story.
                                    """)
                                    .param("topic", request.topic())
                                    .param("story", currentStory)
                                    .param("reviews", String.join("\n", reviews)))
                            .call()
                            .content());

                    // Join and get the story result
                    scope.join();
                    scope.throwIfFailed();
                    final String nextStory = storyFuture.get();

                    var reviewFuture = scope.fork(() -> reviewer.prompt()
                            .user(u -> u.text("""
                                    Here is a story about {topic}.
                                    What do you think?
                                    If you like it, say "looks good".
                                    Otherwise, provide feedback.
                                    Story: {story}
                                    """)
                                    .param("topic", request.topic())
                                    .param("story", nextStory))
                            .call()
                            .content());

                    // Join and get the review result
                    scope.join();
                    scope.throwIfFailed();
                    String review = reviewFuture.get();
                    
                    reviews.add(review);
                    story = nextStory;

                    if (review.toLowerCase().contains("looks good")) {
                        break;
                    }
                }

                jobStore.completeJob(jobId, new Response(story, reviews));
            } catch (Exception e) {
                jobStore.failJob(jobId, e.getMessage());
            }
        });
        return jobId;
    }

    public JobStore.Job getStoryResult(String jobId) {
        return jobStore.getJob(jobId);
    }
}