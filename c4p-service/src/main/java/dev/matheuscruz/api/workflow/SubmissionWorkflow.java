package dev.matheuscruz.api.workflow;

import dev.matheuscruz.api.data.ProposalDTO;
import dev.matheuscruz.api.data.SpeakerDTO;
import dev.matheuscruz.api.model.Proposal;
import dev.matheuscruz.api.model.Speaker;
import io.quarkiverse.flow.Flow;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.serverlessworkflow.api.types.Workflow;
import jakarta.enterprise.context.ApplicationScoped;

import static io.serverlessworkflow.fluent.func.FuncWorkflowBuilder.workflow;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.emit;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.emitJson;
import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.function;

@ApplicationScoped
public class SubmissionWorkflow extends Flow {

    @Override
    public Workflow descriptor() {
        return workflow("submissionWorkflow").tasks(function("saveProposal", this::saveProposal, ProposalDTO.class),
                emitJson("dev.matheuscruz.proposal.submitted", ProposalDTO.class)).build();
    }

    public ProposalDTO saveProposal(ProposalDTO proposalDTO) {

        Speaker speaker = new Speaker(proposalDTO.speaker().name(), proposalDTO.speaker().email(),
                proposalDTO.speaker().title());
        Proposal proposal = new Proposal(proposalDTO.title(), proposalDTO.subject(), speaker);

        // https://github.com/quarkiverse/quarkus-flow/issues/192
        QuarkusTransaction.requiringNew().run(() -> {
            speaker.persist();
            proposal.persist();
        });

        return new ProposalDTO(proposal.id, proposal.getTitle(), proposal.getSubject(), new SpeakerDTO(
                proposal.getSpeaker().getName(), proposal.getSpeaker().getTitle(), proposal.getSpeaker().getEmail()));
    }
}
