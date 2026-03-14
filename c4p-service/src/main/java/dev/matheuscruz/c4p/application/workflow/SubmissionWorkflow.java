package dev.matheuscruz.c4p.application.workflow;

import dev.matheuscruz.c4p.domain.Proposal;
import dev.matheuscruz.c4p.application.data.ProposalSubmission;
import dev.matheuscruz.c4p.domain.ProposalService;
import dev.matheuscruz.c4p.domain.ProposalSubmittedEvent;
import dev.matheuscruz.c4p.domain.Speaker;
import io.quarkiverse.flow.Flow;
import io.quarkus.logging.Log;
import io.serverlessworkflow.api.types.Workflow;
import jakarta.enterprise.context.ApplicationScoped;

import static io.serverlessworkflow.fluent.func.FuncWorkflowBuilder.workflow;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.emitJson;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.function;

@ApplicationScoped
public class SubmissionWorkflow extends Flow {

    @Override
    public Workflow descriptor() {
        return workflow("submissionWorkflow")
                .tasks(function("saveProposal", this::saveProposal, ProposalSubmission.class),
                        emitJson("dev.matheuscruz.proposal.submitted", ProposalSubmittedEvent.class))
                .build();
    }

    public ProposalSubmittedEvent saveProposal(ProposalSubmission input) {
        // input -> domain
        Speaker speaker = new Speaker(input.speaker().name(), input.speaker().email(), input.speaker().title());

        Proposal proposal = new Proposal(input.title(), input.subject(), input.description(), speaker);

        Proposal savedProposal = ProposalService.saveProposal.apply(proposal, speaker);

        Log.info("Proposal ID: " + proposal.id);

        // domain entity -> domain event
        return new ProposalSubmittedEvent(savedProposal.getId(), savedProposal.getTitle(), savedProposal.getSubject(),
                savedProposal.getDescription());
    }
}
