package com.construction.tender.repository;

import org.junit.jupiter.api.Test;

import static com.construction.tender.helper.Sample.sampleBidder;
import static com.construction.tender.helper.Sample.sampleOffer;
import static org.assertj.core.api.Assertions.assertThat;

public class BidderRepositoryTest extends RepositoryTest {
    @Test
    public void createAndFindBidder() {
        final var bidder = sampleBidder();
        final var saveResult = bidderRepository.save(bidder);
        final var findResult = bidderRepository.findAll();

        assertThat(bidder.getName()).as("Bidder name").isEqualTo(saveResult.getName());
        assertThat(saveResult.getId()).isNotNull();
        assertThat(findResult).isNotNull();
        assertThat(findResult.size()).as("Bidder 'find' result").isEqualTo(1);
        assertThat(findResult.get(0)).as("First bidder result").hasToString(saveResult.toString());
        assertThat(saveResult.getTimestamps().getCreated()).as("Bidder created datetime").isNotNull();
        assertThat(saveResult.getTimestamps().getUpdated()).as("Bidder updated datetime").isNotNull();
    }

    @Test
    public void deletingBidderDeletesOffer() {
        final var offer = sampleOffer();
        offerRepository.save(offer);

        assertThat(bidderRepository.findAll()).hasSize(1);
        assertThat(offerRepository.findAll()).hasSize(1);

        bidderRepository.delete(bidderRepository.findAll().get(0));

        assertThat(bidderRepository.findAll()).isEmpty();
        assertThat(offerRepository.findAll()).isEmpty();
    }
}
