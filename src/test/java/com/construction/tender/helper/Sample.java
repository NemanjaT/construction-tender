package com.construction.tender.helper;

import com.construction.tender.entity.Bidder;
import com.construction.tender.entity.Issuer;
import com.construction.tender.entity.Money;
import com.construction.tender.entity.Offer;
import com.construction.tender.entity.Tender;
import org.assertj.core.internal.bytebuddy.utility.RandomString;

public class Sample {
    public static Bidder sampleBidder() {
        final var bidder = new Bidder();
        bidder.setName(RandomString.make(11));
        return bidder;
    }

    public static Issuer sampleIssuer() {
        final var issuer = new Issuer();
        issuer.setName(RandomString.make(10));
        return issuer;
    }

    public static Offer sampleOffer() {
        final var offer = new Offer();
        offer.setBidder(sampleBidder());
        offer.setTender(sampleTender());
        offer.setBid(Money.of(50_000D, "CHF"));
        return offer;
    }

    public static Tender sampleTender() {
        final var tender = new Tender();
        tender.setDescription(RandomString.make(150));
        tender.setConstructionSite(RandomString.make(15));
        tender.setIssuer(sampleIssuer());
        return tender;
    }
}
