package dev.matheuscruz.c4p;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SpeakerDTO(@NotBlank String name, @NotBlank String title, @Email String email) {
}
