package com.construction.tender.repository;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static com.construction.tender.helper.Sample.sampleOffer;
import static org.assertj.core.api.Assertions.assertThat;

public class OfferRepositoryTest extends RepositoryTest {
    @Test
    @Transactional
    public void createAndFindOffer() {
        final var offer = sampleOffer();
        final var saveResult = offerRepository.save(offer);
        final var findResult = offerRepository.findAll();

        assertThat(offer.getStatus()).as("Offer status").isEqualTo(saveResult.getStatus());
        assertThat(offer.getBid()).as("Offer bid").isEqualTo(saveResult.getBid());
        assertThat(saveResult.getId()).isNotNull();
        assertThat(findResult).isNotNull();
        assertThat(findResult.size()).as("Bidder 'find' result").isEqualTo(1);
        assertThat(findResult.get(0)).as("First bidder result").hasToString(saveResult.toString());
        assertThat(saveResult.getTimestamps().getCreated()).as("Bidder created datetime").isNotNull();
        assertThat(saveResult.getTimestamps().getUpdated()).as("Bidder updated datetime").isNotNull();
    }
}
