package com.construction.tender.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TENDER")
public class Tender {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CONSTRUCTION_SITE", nullable = false)
    private String constructionSite;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ISSUER_ID", nullable = false)
    private Issuer issuer;

    @Embedded
    private Timestamps timestamps = new Timestamps();
}
