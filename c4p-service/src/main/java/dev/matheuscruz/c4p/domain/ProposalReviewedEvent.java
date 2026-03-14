package dev.matheuscruz.c4p.domain;

public record ProposalReviewedEvent(Long proposalId, boolean accepted) {
}
