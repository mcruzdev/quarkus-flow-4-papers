package dev.matheuscruz.review;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ReviewProposalWorkflow extends Flow {

    static final int MIN_SCORE = 7;

    @Inject
    ObjectMapper mapper;

    @Inject
    ReviewerAi reviewer;

    @Override
    public Workflow descriptor() {
        return workflow("reviewSubmission")
                .tasks(
                        agent("scoreSubmission",
                                (uniqueId, proposal) -> reviewer.scoreSubmission(proposal), Proposal.class)
                                .outputAs(buildSubmissionOutput(), Long.class),
                        emit("emitProposalReviewed", "dev.matheuscruz.proposal.reviewed", input -> JsonCloudEventData.wrap(mapper.valueToTree(input)))
                                .inputFrom(buildEmitReviewInput(), ScoreSubmissionOutput.class)
                )
                .build();
    }

    private static JavaContextFunction<ScoreSubmissionOutput, EmitReviewInput> buildEmitReviewInput() {
        return (lastState, workflowContext) -> new EmitReviewInput(lastState.score() >= MIN_SCORE, lastState.proposal().id());
    }

    private static JavaContextFunction<Long, ScoreSubmissionOutput> buildSubmissionOutput() {
        return (score, workflowContext) ->
                new ScoreSubmissionOutput(score, workflowContext.instanceData().input().as(Proposal.class).orElseThrow());
    }

    public record ScoreSubmissionOutput(Long score, Proposal proposal) {
    }

    public record EmitReviewInput(Boolean accepted, Long proposalId) {
    }
}
