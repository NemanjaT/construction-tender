package com.construction.tender.service;

import com.construction.tender.entity.Offer;

import java.util.List;

public interface BidderService {
    Offer createOffer(Long tenderId, Offer offer);

    List<Offer> getOffers(String bidderName);

    List<Offer> getOffers(String bidderName, Long tenderId);
}
