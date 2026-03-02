package dev.matheuscruz.c4p;

import io.quarkiverse.flow.Flow;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.serverlessworkflow.api.types.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.function.Function;

import static io.serverlessworkflow.fluent.func.FuncWorkflowBuilder.workflow;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.function;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.http;

@ApplicationScoped
public class WaitReviewWorkflow extends Flow {

    @ConfigProperty(name = "notification.service.url", defaultValue = "http://localhost:8082")
    String notificationUrl;

    @Override
    public Workflow descriptor() {
        return workflow("waitReviewStatusForCommunication")
                .tasks(
                        function("updateProposalStatus", updateProposal(), ProposalReviewedEvent.class),
                        http().POST().uri(URI.create(notificationUrl + "/api/notifications"))
                                .header("Content-Type", "application/json")
                                .body("${.}")
                                .inputFrom(Function.identity(), MessageDTO.class))
                .build();
    }

    private static Function<ProposalReviewedEvent, MessageDTO> updateProposal() {
        return event -> {
            Proposal updatedProposal = QuarkusTransaction.requiringNew()
                    .call(() -> {
                        Proposal proposal = Proposal.findById(event.proposalId());
                        return proposal.accepted(event.accepted());
                    });

            return new MessageDTO(
                    updatedProposal.getSpeaker().getEmail(),
                    updatedProposal.getStatus() == ProposalStatus.ACCEPTED,
                    updatedProposal.getDescription());
        };
    }
}
