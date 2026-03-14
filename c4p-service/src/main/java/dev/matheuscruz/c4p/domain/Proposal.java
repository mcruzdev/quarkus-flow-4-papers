package dev.matheuscruz.c4p.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(value = EnumType.STRING)
    private ProposalStatus status;

    protected Proposal() {
    }

    public Proposal(String title, String subject, String description, Speaker speaker) {
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.speaker = Objects.requireNonNull(speaker, "speaker must not be null");
        this.status = ProposalStatus.PENDING;
    }

    public Long getId() {
        return id;
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

    public ProposalStatus getStatus() {
        return status;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void accepted(boolean accepted) {
        if (status != ProposalStatus.PENDING) {
            return;
        }

        this.status = accepted ? ProposalStatus.ACCEPTED : ProposalStatus.REJECTED;
    }
}
