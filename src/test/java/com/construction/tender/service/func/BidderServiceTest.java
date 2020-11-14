package com.construction.tender.service.func;

import com.construction.tender.ApplicationTest;
import com.construction.tender.service.BidderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.construction.tender.helper.Sample.sampleOffer;
import static com.construction.tender.helper.Sample.sampleTender;
import static org.assertj.core.api.Assertions.assertThat;

public class BidderServiceTest extends ApplicationTest {
    @Autowired
    private BidderService bidderService;

    @BeforeEach
    public void setup() {
        clearDatabase();
        for (int i = 0; i < 5; i++) {
            final var tender = sampleTender();
            tenderRepository.save(tender);
        }
    }

    @Test
    public void createOffer() {
        final var tender = tenderRepository.findAll().get(0);
        final var offer = sampleOffer();
        offer.setTender(null);

        bidderService.createOffer(tender.getId(), offer);

        assertThat(offerRepository.findAll()).as("Offers").isNotEmpty();
        assertThat(offerRepository.findAll().stream().anyMatch(o -> o.getBidder().getName().equals(offer.getBidder().getName())))
                .as("Identical bidder").isTrue();
    }

    @Test
    public void getOffersByBidderName() {
        fillData();
        final var result = bidderService.getOffers("bidder-name");

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(15);
    }

    @Test
    public void getOffersByBidderNameAndTenderId() {
        fillData();
        final var firstTender = tenderRepository.findAll().get(0);
        final var result = bidderService.getOffers("bidder-name", firstTender.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(3);
    }

    private void fillData() {
        final var tender = tenderRepository.findAll();
        tender.forEach(t -> {
            for (int i = 0; i < 3; i++) {
                final var offer = sampleOffer();
                offer.getBidder().setName("bidder-name");
                bidderService.createOffer(t.getId(), offer);
            }
        });
    }
}
