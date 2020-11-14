package com.construction.tender.service.func;

import com.construction.tender.ApplicationTest;
import com.construction.tender.entity.OfferStatus;
import com.construction.tender.entity.TenderStatus;
import com.construction.tender.service.IssuerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static com.construction.tender.helper.Sample.sampleOffer;
import static com.construction.tender.helper.Sample.sampleTender;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class IssuerServiceTest extends ApplicationTest {
    @Autowired
    private IssuerService issuerService;

    @Before
    public void setup() {
        clearDatabase();
        final var tender = sampleTender();
        for (int i = 0; i < 10; i++) {
            final var offer = sampleOffer();
            offer.setTender(tender);
            tender.getOffers().add(offer);
        }
        tenderRepository.save(tender);
    }

    @Test
    public void createTender() {
        final var tender = sampleTender();

        issuerService.createTender(tender);

        assertThat(tenderRepository.findAll()).as("Tenders").isNotEmpty();
        assertThat(tenderRepository.findAll().stream().anyMatch(t -> t.getConstructionSite().equals(tender.getConstructionSite())))
                .as("Identical tender construction site exists").isTrue();
    }

    @Test
    public void acceptOffer() {
        final var tender = tenderRepository.findAll().get(0);
        final var offer = offerRepository.findAll().get(0);

        final var result = issuerService.acceptOffer(tender.getId(), offer.getId());

        assertThat(result.getStatus()).isEqualTo(TenderStatus.CLOSED);
        assertThat(result.getOffers().stream().filter(o -> o.getId().equals(offer.getId())).findFirst().get().getStatus())
                .isEqualTo(OfferStatus.ACCEPTED);
        assertThat(result.getOffers().stream().filter(o -> !o.getId().equals(offer.getId()))
                .allMatch(o -> o.getStatus().equals(OfferStatus.REJECTED))).isTrue();
        assertThat(tenderRepository.findAll().get(0).getStatus()).isEqualTo(TenderStatus.CLOSED);
    }

    @Test
    public void getOffersWithResults() {
        final var result = issuerService.getOffers(tenderRepository.findAll().get(0).getId());

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    public void getOffersNoResults() {
        final var result = issuerService.getOffers(tenderRepository.findAll().get(0).getId() + 1);

        assertThat(result).isEmpty();
    }

    @Test
    public void getTenders() {
        final var result = issuerService.getTenders(tenderRepository.findAll().get(0).getIssuer().getName());

        assertThat(result).isNotEmpty();
    }
}
