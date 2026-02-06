package dev.matheuscruz.api.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Speaker extends PanacheEntity {

    private String name;
    private String email;
    private String title;

    protected Speaker() {
    }

    public Speaker(String name, String email, String title) {
        this.name = name;
        this.email = email;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }
}
