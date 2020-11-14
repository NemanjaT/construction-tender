package com.construction.tender.service;

import com.construction.tender.entity.Offer;
import com.construction.tender.entity.OfferStatus;
import com.construction.tender.entity.Tender;

import java.util.List;

public interface IssuerService {
    Tender createTender(Tender tender);

    Tender acceptOffer(Long tenderId, Long offerId);

    List<Offer> getOffers(Long tenderId);

    List<Tender> getTenders(String issuerName);
}
