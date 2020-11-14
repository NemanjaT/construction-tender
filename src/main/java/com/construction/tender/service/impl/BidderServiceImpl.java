package com.construction.tender.service.impl;

import com.construction.tender.entity.Offer;
import com.construction.tender.exception.InvalidIdProvidedException;
import com.construction.tender.repository.BidderRepository;
import com.construction.tender.repository.OfferRepository;
import com.construction.tender.repository.TenderRepository;
import com.construction.tender.service.BidderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class BidderServiceImpl implements BidderService {
    @Autowired
    private BidderRepository bidderRepository;

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Override
    @Transactional
    public Offer createOffer(Long tenderId, Offer offer) {
        Assert.notNull(tenderId, "Tender ID is required!");
        Assert.notNull(offer, "Offer object is required!");
        Assert.notNull(offer.getBidder(), "Offer bidder is required!");
        Assert.notNull(offer.getBid(), "Offer bid is required!");

        final var tender = tenderRepository.findById(tenderId)
                .orElseThrow(() -> new InvalidIdProvidedException("Invalid tenderId=" + tenderId + " provided."));
        offer.setTender(tender);
        offer.setBidder(bidderRepository.findByNameEquals(offer.getBidder().getName())
                .orElse(offer.getBidder()));
        return offerRepository.save(offer);
    }

    @Override
    public List<Offer> getOffers(String bidderName) {
        Assert.notNull(bidderName, "Bidder name is required!");
        return offerRepository.findByBidder_Name(bidderName);
    }

    @Override
    public List<Offer> getOffers(String bidderName, Long tenderId) {
        Assert.notNull(bidderName, "Bidder name is required!");
        Assert.notNull(tenderId, "Tender ID is required!");
        return offerRepository.findByBidder_NameAndTender_Id(bidderName, tenderId);
    }
}
