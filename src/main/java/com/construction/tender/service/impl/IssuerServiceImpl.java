package com.construction.tender.service.impl;

import com.construction.tender.entity.Offer;
import com.construction.tender.entity.OfferStatus;
import com.construction.tender.entity.Tender;
import com.construction.tender.entity.TenderStatus;
import com.construction.tender.exception.InvalidIdProvidedException;
import com.construction.tender.exception.InvalidOperationException;
import com.construction.tender.repository.IssuerRepository;
import com.construction.tender.repository.TenderRepository;
import com.construction.tender.service.IssuerService;
import com.construction.tender.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class IssuerServiceImpl implements IssuerService {
    @Autowired
    private LockService lockService;

    @Autowired
    private IssuerRepository issuerRepository;

    @Autowired
    private TenderRepository tenderRepository;

    @Override
    public Tender createTender(Tender tender) {
        Assert.notNull(tender.getIssuer(), "Tender issuer is required!");
        log.info("Creating new tender for constructionSite={}", tender.getConstructionSite());
        issuerRepository.findByName(tender.getIssuer().getName())
                .ifPresent(tender::setIssuer);
        return tenderRepository.save(tender);
    }

    @Override
    @Transactional
    public Tender acceptOffer(Long tenderId, Long offerId) {
        Assert.notNull(tenderId, "Tender ID is required!");
        Assert.notNull(offerId, "Offer ID is required!");

        return lockService.lockTenderAndCall(tenderId, () -> {
            log.info("Searching and attempting to accept tenderId={} offerId={}", tenderId, offerId);
            final var tender = tenderRepository.findById(tenderId)
                    .orElseThrow(() -> new InvalidIdProvidedException("Invalid tenderId=" + tenderId + " provided."));

            if (isClosed(tender)) {
                throw new InvalidOperationException("Tender already accepted one of the offers.");
            }

            final var offer = tender.getOffers().stream().filter(o -> o.getId().equals(offerId)).findFirst()
                    .orElseThrow(() -> new InvalidIdProvidedException("Invalid offerId=" + offerId + " provided."));

            log.info("Setting offerId={} to status={} and closing tenderId={}", offerId, OfferStatus.ACCEPTED, tenderId);
            acceptOfferAndCloseTender(tender, offer);
            return tenderRepository.save(tender);
        });
    }

    private boolean isClosed(Tender tender) {
        return TenderStatus.CLOSED.equals(tender.getStatus());
    }

    private void acceptOfferAndCloseTender(Tender tender, Offer offer) {
        offer.setStatus(OfferStatus.ACCEPTED);
        tender.getOffers().stream().filter(o -> !o.getId().equals(offer.getId()))
                .forEach(o -> o.setStatus(OfferStatus.REJECTED));
        tender.setStatus(TenderStatus.CLOSED);
    }

    @Override
    @Transactional
    public List<Offer> getOffers(Long tenderId) {
        Assert.notNull(tenderId, "Tender ID is required!");
        return tenderRepository.findById(tenderId)
                .map(tender -> {
                    final var offers = tender.getOffers();
                    Hibernate.initialize(offers);
                    return offers;
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Tender> getTenders(String issuerName) {
        Assert.notNull(issuerName, "Issuer name is required!");
        return tenderRepository.findByIssuer_Name(issuerName);
    }

    @Override
    public boolean isTenderFromIssuer(Long tenderId, String issuerName) {
        return tenderRepository.findById(tenderId)
                .map(tender -> tender.getIssuer().getName().equals(issuerName))
                .orElse(false);
    }
}
