package com.construction.tender.repository;

import com.construction.tender.entity.Offer;

import static com.construction.tender.repository.BidderRepositoryTest.sampleBidder;

public class OfferRepositoryTest {
    public static Offer sampleOffer() {
        final var offer = new Offer();
        offer.setBidder(sampleBidder());
        return offer;
    }
}
