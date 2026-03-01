package dev.matheuscruz.c4p;

import io.quarkiverse.flow.Flow;
import io.quarkus.logging.Log;
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
                        emitJson("dev.matheuscruz.proposal.submitted", ProposalDTO.class)
                )
                .build();
    }

    public ProposalDTO saveProposal(ProposalDTO input) {

        Speaker speaker = new Speaker(input.speaker().name(), input.speaker().email(),
                input.speaker().title());
        Proposal proposal = new Proposal(input.title(), input.subject(), input.description(), speaker);

        QuarkusTransaction.requiringNew().run(() -> {
            speaker.persist();
            proposal.persist();
        });

        Log.info("Proposal ID: " + proposal.id);

        return new ProposalDTO(proposal.getId(), proposal.getTitle(), proposal.getSubject(), proposal.getDescription(), proposal.getStatus(), new SpeakerDTO(
                speaker.getName(), speaker.getTitle(), speaker.getEmail()
        ));
    }
}
