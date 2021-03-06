package com.construction.tender.repository;

import org.junit.jupiter.api.Test;

import static com.construction.tender.helper.Sample.sampleOffer;
import static com.construction.tender.helper.Sample.sampleTender;
import static org.assertj.core.api.Assertions.assertThat;

public class TenderRepositoryTest extends RepositoryTest {
    @Test
    public void createAndFindTender() {
        final var tender = sampleTender();
        final var saveResult = tenderRepository.save(tender);
        final var findResult = tenderRepository.findAll();

        assertThat(tender.getDescription()).as("Tender description").isEqualTo(saveResult.getDescription());
        assertThat(tender.getConstructionSite()).as("Tender construction site code")
                .isEqualTo(saveResult.getConstructionSite());
        assertThat(tender.getIssuer()).as("Tender issuer").hasToString(saveResult.getIssuer().toString());
        assertThat(saveResult.getId()).isNotNull();
        assertThat(findResult).isNotNull();
        assertThat(findResult.size()).as("Tender 'find' result").isEqualTo(1);
        assertThat(findResult.get(0)).as("First tender result").hasToString(saveResult.toString());
        assertThat(saveResult.getTimestamps().getCreated()).as("Tender created datetime").isNotNull();
        assertThat(saveResult.getTimestamps().getUpdated()).as("Tender updated datetime").isNotNull();
    }

    @Test
    public void deletingTenderDeletesOffer() {
        final var offer = sampleOffer();
        offerRepository.save(offer);

        assertThat(tenderRepository.findAll()).hasSize(1);
        assertThat(offerRepository.findAll()).hasSize(1);

        tenderRepository.delete(tenderRepository.findAll().get(0));

        assertThat(tenderRepository.findAll()).isEmpty();
        assertThat(offerRepository.findAll()).isEmpty();
    }
}
