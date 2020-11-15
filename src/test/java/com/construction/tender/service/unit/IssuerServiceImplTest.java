package com.construction.tender.service.unit;

import com.construction.tender.entity.Offer;
import com.construction.tender.entity.OfferStatus;
import com.construction.tender.entity.Tender;
import com.construction.tender.entity.TenderStatus;
import com.construction.tender.exception.InvalidOperationException;
import com.construction.tender.repository.IssuerRepository;
import com.construction.tender.repository.TenderRepository;
import com.construction.tender.service.IssuerService;
import com.construction.tender.service.LockService;
import com.construction.tender.service.impl.IssuerServiceImpl;
import com.construction.tender.service.impl.LockServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.construction.tender.helper.Sample.sampleIssuer;
import static com.construction.tender.helper.Sample.sampleOffer;
import static com.construction.tender.helper.Sample.sampleTender;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssuerServiceImplTest {
    @Spy
    private final LockService lockService = new LockServiceImpl();

    @Mock
    private IssuerRepository issuerRepository;

    @Mock
    private TenderRepository tenderRepository;

    @InjectMocks
    private final IssuerService issuerService = new IssuerServiceImpl();

    @Test
    public void createTenderNewIssuer() {
        final var tender = sampleTender();
        issuerService.createTender(tender);

        verify(tenderRepository).save(eq(tender));
    }

    @Test
    public void createTenderExistingIssuer() {
        final var issuer = sampleIssuer();
        when(issuerRepository.findByName(any(String.class)))
                .thenReturn(Optional.of(issuer));
        when(tenderRepository.save(any(Tender.class)))
                .thenAnswer(a -> {
                    final var tenderArg = (Tender) a.getArguments()[0];
                    tenderArg.setId((long) (Math.random() * 10_000));
                    return tenderArg;
                });
        final var tender = sampleTender();
        final var result = issuerService.createTender(tender);

        verify(tenderRepository).save(eq(tender));
        assertThat(result.getIssuer()).as("Tender issuer").isEqualTo(issuer);
    }

    @Test
    public void createTenderNullIssuer() {
        assertThrows(IllegalArgumentException.class, () -> {
            final var tender = sampleTender();
            tender.setIssuer(null);
            issuerService.createTender(tender);
        });
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

    @Test
    public void acceptClosedOffer() {
        assertThrows(InvalidOperationException.class, () -> {
            final var tender = tenderWithOffers();
            tender.setStatus(TenderStatus.CLOSED);
            when(tenderRepository.findById(eq(1234L)))
                    .thenReturn(Optional.of(tender));

            issuerService.acceptOffer(1234L, 4321L);
        });
    }

    @Test
    public void acceptNonExistingTender() {
        assertThrows(InvalidOperationException.class, () -> {
            issuerService.acceptOffer(1234L, 4321L);
        });
    }

    @Test
    public void acceptNonExistingOffer() {
        assertThrows(InvalidOperationException.class, () -> {
            final var tender = tenderWithOffers();
            when(tenderRepository.findById(eq(1234L)))
                    .thenReturn(Optional.of(tender));

            issuerService.acceptOffer(1234L, 9999L);
        });
    }

    @Test
    public void acceptNullTender() {
        assertThrows(IllegalArgumentException.class, () -> {
            issuerService.acceptOffer(null, 9999L);
        });
    }

    @Test
    public void acceptNullOffer() {
        assertThrows(IllegalArgumentException.class, () -> {
            issuerService.acceptOffer(1234L, null);
        });
    }

    @Test
    public void getOffersScenarios() {
        when(tenderRepository.findById(any(Long.class))).thenReturn(Optional.of(tenderWithOffers()));
        assertThat(issuerService.getOffers(1234L)).isNotEmpty();

        when(tenderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThat(issuerService.getOffers(1234L)).isEmpty();
    }

    @Test
    public void getOffersForNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            issuerService.getOffers(null);
        });
    }

    @Test
    public void getTenders() {
        final var tenders = List.of(sampleTender());
        when(tenderRepository.findByIssuer_Name(any(String.class))).thenReturn(tenders);
        assertThat(issuerService.getTenders("issuer")).isEqualTo(tenders);
    }

    @Test
    public void getTendersForNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            issuerService.getTenders(null);
        });
    }

    @Test
    public void isTenderForIssuer() {
        final var sampleTender = sampleTender();
        when(tenderRepository.findById(anyLong())).thenReturn(Optional.of(sampleTender));
        assertThat(issuerService.isTenderFromIssuer(1234L, sampleTender.getIssuer().getName())).isTrue();

        when(tenderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThat(issuerService.isTenderFromIssuer(1234L, "issuer")).isFalse();
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
