package dev.matheuscruz.c4p;

import io.quarkiverse.flow.Flow;
import io.quarkus.narayana.jta.QuarkusTransaction;
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
                .tasks(
                        function("saveProposal", this::saveProposal, ProposalDTO.class),
                        emitJson("dev.matheuscruz.proposal.submitted", SubmissionDTO.class)
                )
                .build();
    }

    public SubmissionDTO saveProposal(ProposalDTO input) {

        Speaker speaker = new Speaker(input.speaker().name(), input.speaker().email(),
                input.speaker().title());
        Proposal proposal = new Proposal(input.title(), input.subject(), input.description(), speaker);

        QuarkusTransaction.requiringNew().run(() -> {
            speaker.persist();
            proposal.persist();
        });

        return new SubmissionDTO(proposal.id, proposal.getTitle(), proposal.getSubject(), proposal.getDescription());
    }
}
