package dev.matheuscruz.review;

import jakarta.validation.constraints.NotBlank;

public record Submission(Long proposalId, @NotBlank String title, @NotBlank String subject, String description) {
}
