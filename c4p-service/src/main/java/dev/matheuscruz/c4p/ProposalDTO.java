package dev.matheuscruz.c4p;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProposalDTO(Long id, @NotBlank String title, @NotBlank String subject, @Size(min = 100) String description,
                          @Valid SpeakerDTO speaker) {
}
