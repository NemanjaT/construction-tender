package com.construction.tender.service;

import com.construction.tender.entity.Offer;
import com.construction.tender.entity.Tender;

import java.util.List;

/**
 * Service with operations for the tender issuer.
 */
public interface IssuerService {
    /**
     * Creates and returns a new tender for specified issuer. If issuer does not already exist, creates one.
     * @param tender New tender
     * @return Created tender
     */
    Tender createTender(Tender tender);

    /**
     * Accepts an offer with given offerId. If tender with given ID does not exists, or offer with given ID is not
     * within the requested tender, throws {@link com.construction.tender.exception.InvalidIdProvidedException}.
     * All offers for tender, except the accepted one will change status to rejected and tender will close.
     * While an offer is being accepted, it's not possible to create new offers for the same tender.
     * @param tenderId Tender to close
     * @param offerId Offer to accept
     * @return Closed tender
     */
    Tender acceptOffer(Long tenderId, Long offerId);

    /**
     * Gets all offers for a tender with given ID.
     * @param tenderId Tender for which offers are requested.
     * @return All offers or empty list.
     */
    List<Offer> getOffers(Long tenderId);

    /**
     * Get all tenders for an issuer.
     * @param issuerName Issuer for which tenders are requested.
     * @return All tenders or empty list.
     */
    List<Tender> getTenders(String issuerName);

    /**
     * Check whether a tender with given ID belongs to given issuer.
     * @param tenderId Tender ID to compare
     * @param issuerName Issuer name to compare
     * @return whether a tender with tenderId has the issuer issuerName
     */
    boolean isTenderFromIssuer(Long tenderId, String issuerName);
}
