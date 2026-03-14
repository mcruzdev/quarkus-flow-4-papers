package dev.matheuscruz.c4p.application.workflow;

import dev.matheuscruz.c4p.application.data.SendEmailPayload;
import dev.matheuscruz.c4p.domain.Proposal;
import dev.matheuscruz.c4p.domain.ProposalReviewedEvent;
import dev.matheuscruz.c4p.domain.ProposalService;
import dev.matheuscruz.c4p.domain.ProposalStatus;
import io.quarkiverse.flow.Flow;
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
        return workflow("waitReviewStatusForCommunication").tasks(function("updateProposalStatus", event -> {
            Proposal updatedProposal = ProposalService.updateProposal.apply(event);
            return new SendEmailPayload(updatedProposal.getSpeaker().getEmail(),
                    updatedProposal.getStatus() == ProposalStatus.ACCEPTED, updatedProposal.getDescription());
        }, ProposalReviewedEvent.class),
                http().POST().uri(URI.create(notificationUrl + "/api/notifications"))
                        .header("Content-Type", "application/json").body("${.}")
                        .inputFrom(Function.identity(), SendEmailPayload.class))
                .build();
    }

}
