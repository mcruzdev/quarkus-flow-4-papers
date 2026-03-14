package dev.matheuscruz.c4p.application.data;

public record SendEmailPayload(String to, boolean accepted, String content) {
}
