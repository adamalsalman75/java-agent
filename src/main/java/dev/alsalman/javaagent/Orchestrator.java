package dev.alsalman.javaagent;

import dev.alsalman.javaagent.agents.MarketTrendsAgent;
import dev.alsalman.javaagent.agents.ReviewAgent;
import dev.alsalman.javaagent.agents.StoryWriterAgent;
import dev.alsalman.javaagent.agents.TargetAudienceAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@Service
public class Orchestrator {

    private static final Logger logger = LoggerFactory.getLogger(Orchestrator.class);

    private final TargetAudienceAgent targetAudienceAgent;
    private final MarketTrendsAgent marketTrendsAgent;
    private final StoryWriterAgent storyWriterAgent;
    private final ReviewAgent reviewAgent;
    private final JobStore jobStore;

    public Orchestrator(TargetAudienceAgent targetAudienceAgent, MarketTrendsAgent marketTrendsAgent, StoryWriterAgent storyWriterAgent, ReviewAgent reviewAgent, JobStore jobStore) {
        this.targetAudienceAgent = targetAudienceAgent;
        this.marketTrendsAgent = marketTrendsAgent;
        this.storyWriterAgent = storyWriterAgent;
        this.reviewAgent = reviewAgent;
        this.jobStore = jobStore;
    }

    public String submitStoryRequest(Request request) {
        String jobId = jobStore.newJob();
        logger.info("Starting new story request with topic: {} and job ID: {}", request.topic(), jobId);
        Thread.ofVirtual().start(() -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                var audienceFuture = scope.fork(() -> {
                    logger.info("Getting target audience for topic: {}", request.topic());
                    return targetAudienceAgent.getTargetAudience(request.topic());
                });
                var trendsFuture = scope.fork(() -> {
                    logger.info("Getting market trends for topic: {}", request.topic());
                    return marketTrendsAgent.getMarketTrends(request.topic());
                });

                scope.join();
                scope.throwIfFailed();

                String audience = audienceFuture.get();
                String trends = trendsFuture.get();
                logger.info("Audience: {}\n\nTrends: {} for topic: {}", audience, trends, request.topic());


                logger.info("Writing story for topic: {}", request.topic());
                String story = storyWriterAgent.writeStory(request.topic(), audience, trends);
                logger.info("Reviewing story for topic: {}", request.topic());
                String review = reviewAgent.reviewStory(story);

                List<String> reviews = new ArrayList<>();
                reviews.add(review);

                int iteration = 1;
                while (!review.contains("happy")) {
                    logger.info("Reviewer not happy. Iteration: {}. Rewriting story for topic: {}", iteration, request.topic());
                    story = storyWriterAgent.writeStory(request.topic(), audience, trends);
                    review = reviewAgent.reviewStory(story);
                    reviews.add(review);
                    iteration++;
                }

                logger.info("Reviewer is happy. Completing job ID: {}", jobId);
                jobStore.completeJob(jobId, new Response(story, reviews));
            } catch (Exception e) {
                logger.error("Job ID: {} failed with error: {}", jobId, e.getMessage());
                jobStore.failJob(jobId, e.getMessage());
            }
        });
        return jobId;
    }

    public JobStore.Job getStoryResult(String jobId) {
        return jobStore.getJob(jobId);
    }
}
