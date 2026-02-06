package dev.matheuscruz.api.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Entity
public class Proposal extends PanacheEntity {

    private String title;
    private String subject;
    @OneToOne
    private Speaker speaker;

    protected Proposal() {
    }

    public Proposal(String title, String subject, Speaker speaker) {
        this.title = title;
        this.subject = subject;
        this.speaker = Objects.requireNonNull(speaker, "speaker must not be null");
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public Speaker getSpeaker() {
        return speaker;
    }
}
