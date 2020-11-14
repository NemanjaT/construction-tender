package com.construction.tender.service.unit;

import com.construction.tender.entity.Offer;
import com.construction.tender.entity.OfferStatus;
import com.construction.tender.entity.Tender;
import com.construction.tender.entity.TenderStatus;
import com.construction.tender.exception.InvalidOperationException;
import com.construction.tender.repository.IssuerRepository;
import com.construction.tender.repository.TenderRepository;
import com.construction.tender.service.IssuerService;
import com.construction.tender.service.impl.IssuerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.construction.tender.helper.Sample.sampleIssuer;
import static com.construction.tender.helper.Sample.sampleOffer;
import static com.construction.tender.helper.Sample.sampleTender;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IssuerServiceImplTest {
    @Mock
    private IssuerRepository issuerRepository;

    @Mock
    private TenderRepository tenderRepository;

    @InjectMocks
    private final IssuerService issuerService = new IssuerServiceImpl();

    @Before
    public void setup() {
        when(tenderRepository.save(any(Tender.class)))
                .thenAnswer(a -> {
                    final var tenderArg = (Tender) a.getArguments()[0];
                    tenderArg.setId((long) (Math.random() * 10_000));
                    return tenderArg;
                });
        when(issuerRepository.findByNameEquals(any(String.class)))
                .thenReturn(Optional.empty());
    }

    @Test
    public void createTenderNewIssuer() {
        final var tender = sampleTender();
        issuerService.createTender(tender);

        verify(tenderRepository).save(eq(tender));
    }

    @Test
    public void createTenderExistingIssuer() {
        final var issuer = sampleIssuer();
        when(issuerRepository.findByNameEquals(any(String.class)))
                .thenReturn(Optional.of(issuer));
        final var tender = sampleTender();
        final var result = issuerService.createTender(tender);

        verify(tenderRepository).save(eq(tender));
        assertThat(result.getIssuer()).as("Tender issuer").isEqualTo(issuer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTenderNullIssuer() {
        final var tender = sampleTender();
        tender.setIssuer(null);
        issuerService.createTender(tender);
    }

    @Test
    public void acceptOffer() {
        final var tender = tenderWithOffers();
        when(tenderRepository.findById(eq(1234L)))
                .thenReturn(Optional.of(tender));

        issuerService.acceptOffer(1234L, 4321L);

        verify(tenderRepository).save(eq(tender));
        assertThat(tender.getOffers().stream().filter(o -> o.getStatus().equals(OfferStatus.ACCEPTED)).count())
                .as("ACCEPTED offer count").isEqualTo(1);
        assertThat(tender.getOffers().stream().filter(o -> o.getStatus().equals(OfferStatus.REJECTED)).count())
                .as("REJECTED offer count").isEqualTo(tender.getOffers().size() - 1);
        assertThat(tender.getStatus()).as("Tender status").isEqualTo(TenderStatus.CLOSED);
    }

    @Test(expected = InvalidOperationException.class)
    public void acceptClosedOffer() {
        final var tender = tenderWithOffers();
        tender.setStatus(TenderStatus.CLOSED);
        when(tenderRepository.findById(eq(1234L)))
                .thenReturn(Optional.of(tender));

        issuerService.acceptOffer(1234L, 4321L);
    }

    @Test(expected = InvalidOperationException.class)
    public void acceptNonExistingTender() {
        issuerService.acceptOffer(1234L, 4321L);
    }

    @Test(expected = InvalidOperationException.class)
    public void acceptNonExistingOffer() {
        final var tender = tenderWithOffers();
        when(tenderRepository.findById(eq(1234L)))
                .thenReturn(Optional.of(tender));

        issuerService.acceptOffer(1234L, 9999L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void acceptNullTender() {
        issuerService.acceptOffer(null, 9999L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void acceptNullOffer() {
        issuerService.acceptOffer(1234L, null);
    }

    @Test
    public void getOffersScenarios() {
        when(tenderRepository.findById(any(Long.class))).thenReturn(Optional.of(tenderWithOffers()));
        assertThat(issuerService.getOffers(1234L)).isNotEmpty();

        when(tenderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThat(issuerService.getOffers(1234L)).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOffersForNull() {
        issuerService.getOffers(null);
    }

    @Test
    public void getTenders() {
        final var tenders = List.of(sampleTender());
        when(tenderRepository.findByIssuer_Name(any(String.class))).thenReturn(tenders);
        assertThat(issuerService.getTenders("issuer")).isEqualTo(tenders);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTendersForNull() {
        issuerService.getTenders(null);
    }

    private Tender tenderWithOffers() {
        final var tender = sampleTender();
        List<Offer> offers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final var offer = sampleOffer();
            offer.setTender(tender);
            offer.setId(4321L + i);
            offers.add(offer);
        }
        tender.setOffers(offers);
        return tender;
    }

}
