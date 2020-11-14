package com.construction.tender.repository;

import com.construction.tender.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByBidder_Name(String bidderName);

    List<Offer> findByBidder_NameAndTender_Id(String bidderName, Long tenderId);
}
