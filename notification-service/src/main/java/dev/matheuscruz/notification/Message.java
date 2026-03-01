package dev.matheuscruz.notification;

public record Message(String to, boolean accepted, String content) {
}
