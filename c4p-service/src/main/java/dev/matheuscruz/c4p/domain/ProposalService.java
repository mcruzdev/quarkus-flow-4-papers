package dev.matheuscruz.c4p.domain;

import io.quarkus.narayana.jta.QuarkusTransaction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ProposalService {

    public static Function<ProposalReviewedEvent, Proposal> updateProposal = event -> QuarkusTransaction.requiringNew()
            .call(() -> {
                Proposal proposal = Proposal.findById(event.proposalId());
                if (proposal == null) {
                    throw new IllegalStateException("Proposal with ID " + event.proposalId() + " not found.");
                }

                proposal.accepted(event.accepted());

                return proposal;
            });

    public static BiFunction<Proposal, Speaker, Proposal> saveProposal = (proposal, speaker) -> {
        QuarkusTransaction.requiringNew().run(() -> {
            speaker.persist();
            proposal.persist();
        });
        return proposal;
    };
}
