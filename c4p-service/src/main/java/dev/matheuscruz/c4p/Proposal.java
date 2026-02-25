package dev.matheuscruz.c4p;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Entity
public class Proposal extends PanacheEntity {

    private String title;
    private String subject;
    @Column(length = 300)
    private String description;
    @OneToOne
    private Speaker speaker;

    protected Proposal() {
    }

    public Proposal(String title, String subject, String description, Speaker speaker) {
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.speaker = Objects.requireNonNull(speaker, "speaker must not be null");
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public Speaker getSpeaker() {
        return speaker;
    }
}
