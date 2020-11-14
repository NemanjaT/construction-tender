package com.construction.tender.repository;

import com.construction.tender.entity.Issuer;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

import static com.construction.tender.repository.TenderRepositoryTest.sampleTender;
import static org.assertj.core.api.Assertions.assertThat;

public class IssuerRepositoryTest extends RepositoryTest {
    @Test
    public void createAndFindIssuer() {
        final var issuer = sampleIssuer();
        final var saveResult = issuerRepository.save(issuer);
        final var findResult = issuerRepository.findAll();

        assertThat(issuer.getName()).as("Issuer name").isEqualTo(saveResult.getName());
        assertThat(saveResult.getId()).isNotNull();
        assertThat(findResult).isNotNull();
        assertThat(findResult.size()).as("Issuer 'find' result").isEqualTo(1);
        assertThat(findResult.get(0)).as("First issuer result").hasToString(saveResult.toString());
        assertThat(saveResult.getTimestamps().getCreated()).as("Issuer created datetime").isNotNull();
        assertThat(saveResult.getTimestamps().getUpdated()).as("Issuer updated datetime").isNotNull();
    }

    @Test
    public void deletingIssuerDeletesTender() {
        final var tender = sampleTender();
        tenderRepository.save(tender);

        assertThat(issuerRepository.findAll()).hasSize(1);
        assertThat(tenderRepository.findAll()).hasSize(1);

        issuerRepository.delete(issuerRepository.findAll().get(0));

        assertThat(issuerRepository.findAll()).isEmpty();
        assertThat(tenderRepository.findAll()).isEmpty();
    }

    public static Issuer sampleIssuer() {
        final var issuer = new Issuer();
        issuer.setName(RandomString.make(10));
        return issuer;
    }
}
