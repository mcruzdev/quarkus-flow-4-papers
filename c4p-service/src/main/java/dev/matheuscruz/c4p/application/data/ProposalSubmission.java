package dev.matheuscruz.c4p.application.data;

import dev.matheuscruz.c4p.domain.ProposalStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProposalSubmission(Long id, @NotBlank String title, @NotBlank String subject,
        @Size(min = 100) String description, ProposalStatus status, @Valid ProposalSubmission.Speaker speaker) {

    public record Speaker(@NotBlank String name, @NotBlank String title, @Email String email) {
    }
}
