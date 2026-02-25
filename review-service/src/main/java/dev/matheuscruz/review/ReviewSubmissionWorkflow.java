package dev.matheuscruz.review;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.cloudevents.jackson.JsonCloudEventData;
import io.quarkiverse.flow.Flow;
import io.serverlessworkflow.api.types.Workflow;
import io.serverlessworkflow.api.types.func.JavaContextFunction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static io.serverlessworkflow.fluent.func.FuncWorkflowBuilder.workflow;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.agent;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.emit;

@ApplicationScoped
public class ReviewSubmissionWorkflow extends Flow {

    static final int MIN_SCORE = 7;

    @Inject
    ReviewerAi reviewer;

    @Override
    public Workflow descriptor() {
        return workflow("reviewSubmission")
                .tasks(
                        agent("scoreSubmission",
                                (uniqueId, submission) -> reviewer.scoreSubmission(submission), Submission.class)
                                .outputAs(buildSubmissionOutput(), Long.class),
                        emit("emitReview", emitBuilder -> {
                            emitBuilder.event(eventBuilder -> eventBuilder.data(taskInput -> JsonCloudEventData.wrap(
                                            JsonNodeFactory.instance.objectNode())).type("dev.matheuscruz.submission.reviewed").build()).build();
                        }).inputFrom(buildEmitReviewInput(), ScoreSubmissionOutput.class)
                )
                .build();
    }

    private static JavaContextFunction<ScoreSubmissionOutput, EmitReviewInput> buildEmitReviewInput() {
        return (lastState, workflowContext) -> new EmitReviewInput(lastState.score() >= MIN_SCORE, lastState.submission().proposalId());
    }

    private static JavaContextFunction<Long, ScoreSubmissionOutput> buildSubmissionOutput() {
        return (score, workflowContext) -> new ScoreSubmissionOutput(score, workflowContext.instanceData().input().as(Submission.class).orElseThrow());
    }

    public record ScoreSubmissionOutput(Long score, Submission submission) {}

    public record EmitReviewInput(Boolean accepted, Long proposalId) {}
}
