package dev.matheuscruz.c4p;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ProposalReviewedListener {


    final ObjectMapper mapper;
    final WaitReviewWorkflow workflow;

    public ProposalReviewedListener(ObjectMapper mapper, WaitReviewWorkflow workflow) {
        this.mapper = mapper;
        this.workflow = workflow;
    }

    @Incoming("proposal.reviewed")
    @Blocking
    public Uni<Void> handleProposalReviewed(String payload) {
        Log.info("Proposal reviewed event: " + payload);
        return workflow.startInstance(readSubmissionReviewedEvent(payload))
                .onItem()
                .transformToUni(model -> Uni.createFrom().voidItem());
    }

    private ProposalReviewedEvent readSubmissionReviewedEvent(String proposal) {
        try {
            JsonNode root = mapper.readTree(proposal);
            return mapper.treeToValue(root.get("data"), ProposalReviewedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while reading ProposalReviewedEvent from Kafka", e);
        }
    }
}
