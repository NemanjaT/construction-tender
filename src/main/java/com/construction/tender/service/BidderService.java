package com.construction.tender.service;

import com.construction.tender.entity.Offer;

import java.util.List;

/**
 * Service with operations for the tender offer-ers, aka. bidders.
 */
public interface BidderService {
    /**
     * Creates an offer for a specified tender ID. If the tender is closed, method will throw
     * {@link com.construction.tender.exception.InvalidOperationException}.
     * While an offer is being accepted, it's not possible to create new offers for the same tender.
     * @param tenderId Tender ID for which an offer is created.
     * @param offer New offer
     * @return Offer created
     */
    Offer createOffer(Long tenderId, Offer offer);

    /**
     * Get all offers for a bidder
     * @param bidderName Bidder whose offers are required
     * @return All offers or empty list
     */
    List<Offer> getOffers(String bidderName);

    /**
     * Get all offers for bidder and specific tender ID
     * @param bidderName Bidder whose offers are required
     * @param tenderId Tender ID whose offers are required
     * @return All offers or empty list
     */
    List<Offer> getOffers(String bidderName, Long tenderId);
}
