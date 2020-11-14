package com.construction.tender.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TENDER")
public class Tender {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "CREATED", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "UPDATED", nullable = false)
    private LocalDateTime updated;

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
        updated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updated = LocalDateTime.now();
    }
}
