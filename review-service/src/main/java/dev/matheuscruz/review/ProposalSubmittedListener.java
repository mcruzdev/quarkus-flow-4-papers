package dev.matheuscruz.review;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ProposalSubmittedListener {

    static final String DATA_KEY = "data";

    @Inject
    ObjectMapper mapper;

    @Inject
    ReviewProposalWorkflow workflow;

    @Incoming("proposal.submitted")
    public void consumeProposal(String proposal) {
        Log.info("Proposal submitted event: " + proposal);
        try {
            Proposal submission = readProposal(proposal);
            workflow.startInstance(submission)
                    .subscribe()
                    .with(m -> {
                        Log.info("ReviewProposalWorkflow finished with the following output: " + m);
                    }, Log::error);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Proposal readProposal(String proposal) throws JsonProcessingException {
        JsonNode root = mapper.readTree(proposal);
        return mapper.treeToValue(root.get(DATA_KEY), Proposal.class);
    }
}
