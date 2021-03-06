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
@Table(name = "OFFER")
public class Offer {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "BIDDER_ID", nullable = false)
    private Bidder bidder;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "TENDER_ID", nullable = false)
    private Tender tender;

    @Column(name = "description")
    private String description;

    @Embedded
    private Money bid;

    @Column(name = "STATUS")
    private OfferStatus status = OfferStatus.PENDING;

    @Embedded
    private Timestamps timestamps = new Timestamps();
}
