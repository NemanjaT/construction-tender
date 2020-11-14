package com.construction.tender.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "BIDDER")
@ToString(exclude = "offers")
public class Bidder {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bidder", cascade = CascadeType.ALL)
    private List<Offer> offers;

    @Embedded
    Timestamps timestamps = new Timestamps();
}
