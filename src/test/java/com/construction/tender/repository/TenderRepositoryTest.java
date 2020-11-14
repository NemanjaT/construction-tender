package com.construction.tender.repository;

import com.construction.tender.entity.Tender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles="test")
public class TenderRepositoryTest {
    @Autowired
    private TenderRepository tenderRepository;

    @BeforeEach
    public void setup() {
        tenderRepository.deleteAll();
    }

    @Test
    public void createAndFindTender() {
        final var tender = sampleTender();
        final var saveResult = tenderRepository.save(tender);
        final var findResult = tenderRepository.findAll();

        assertThat(tender.getDescription()).as("Tender description").isEqualTo(saveResult.getDescription());
        assertThat(tender.getConstructionSite()).as("Tender construction site code")
                .isEqualTo(saveResult.getConstructionSite());
        assertThat(tender.getIssuer()).as("Tender issuer").isEqualTo(saveResult.getIssuer());
        assertThat(saveResult.getId()).isNotNull();
        assertThat(findResult).isNotNull();
        assertThat(findResult.size()).as("Tender 'find' result").isEqualTo(1);
        assertThat(findResult.get(0)).as("First tender result").isEqualTo(saveResult);
        assertThat(saveResult.getCreated()).as("Tender created datetime").isNotNull();
        assertThat(saveResult.getUpdated()).as("Tender updated datetime").isNotNull();
    }

    private Tender sampleTender() {
        final var tender = new Tender();
        tender.setDescription("Very long description");
        tender.setConstructionSite("A-1234-BB-CC");
        tender.setIssuer("GDS");
        return tender;
    }
}
