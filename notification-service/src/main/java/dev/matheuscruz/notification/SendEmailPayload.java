package dev.matheuscruz.notification;

public record SendEmailPayload(String to, boolean accepted, String content) {
}
