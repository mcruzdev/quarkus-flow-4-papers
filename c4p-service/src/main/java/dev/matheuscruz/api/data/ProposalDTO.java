package dev.matheuscruz.api.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ProposalDTO(Long id, @NotBlank String title, @NotBlank String subject, @Valid SpeakerDTO speaker) {
}
