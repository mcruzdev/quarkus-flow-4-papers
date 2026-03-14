package dev.matheuscruz.c4p.domain;

public record ProposalSubmittedEvent(Long id, String title, String subject, String description) {
}
