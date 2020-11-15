package com.construction.tender.service.impl;

import com.construction.tender.entity.Offer;
import com.construction.tender.entity.Tender;
import com.construction.tender.entity.TenderStatus;
import com.construction.tender.exception.InvalidIdProvidedException;
import com.construction.tender.exception.InvalidOperationException;
import com.construction.tender.repository.BidderRepository;
import com.construction.tender.repository.OfferRepository;
import com.construction.tender.repository.TenderRepository;
import com.construction.tender.service.BidderService;
import com.construction.tender.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class BidderServiceImpl implements BidderService {
    @Autowired
    private LockService lockService;

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

        return lockService.lockTenderAndCall(tenderId, () -> {
            final var tender = tenderRepository.findById(tenderId)
                    .orElseThrow(() -> new InvalidIdProvidedException("Invalid tenderId=" + tenderId + " provided."));
            if (isClosed(tender)) {
                throw new InvalidOperationException("Can not create an offer for closed tender.");
            }
            offer.setTender(tender);
            bidderRepository.findByName(offer.getBidder().getName())
                    .ifPresent(offer::setBidder);
            return offerRepository.save(offer);
        });
    }

    private boolean isClosed(Tender tender) {
        return TenderStatus.CLOSED.equals(tender.getStatus());
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
