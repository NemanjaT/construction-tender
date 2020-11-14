package com.construction.tender.repository;

import com.construction.tender.entity.Tender;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

import static com.construction.tender.repository.IssuerRepositoryTest.sampleIssuer;
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

    public static Tender sampleTender() {
        final var tender = new Tender();
        tender.setDescription(RandomString.make(150));
        tender.setConstructionSite(RandomString.make(15));
        tender.setIssuer(sampleIssuer());
        return tender;
    }
}
