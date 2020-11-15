package com.construction.tender.service.unit;

import com.construction.tender.entity.Offer;
import com.construction.tender.exception.InvalidOperationException;
import com.construction.tender.repository.BidderRepository;
import com.construction.tender.repository.OfferRepository;
import com.construction.tender.repository.TenderRepository;
import com.construction.tender.service.BidderService;
import com.construction.tender.service.LockService;
import com.construction.tender.service.impl.BidderServiceImpl;
import com.construction.tender.service.impl.LockServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.construction.tender.helper.Sample.sampleBidder;
import static com.construction.tender.helper.Sample.sampleOffer;
import static com.construction.tender.helper.Sample.sampleTender;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidderServiceImplTest {
    @Spy
    private final LockService lockService = new LockServiceImpl();

    @Mock
    private BidderRepository bidderRepository;

    @Mock
    private TenderRepository tenderRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private final BidderService bidderService = new BidderServiceImpl();

    @Test
    public void createOfferNewBidder() {
        final var offer = sampleOffer();
        when(tenderRepository.findById(eq(1234L)))
                .thenReturn(Optional.of(sampleTender()));

        bidderService.createOffer(1234L, offer);

        verify(offerRepository).save(eq(offer));
    }

    @Test
    public void createOfferExistingBidder() {
        final var offer = sampleOffer();
        final var bidder = sampleBidder();
        when(tenderRepository.findById(eq(1234L)))
                .thenReturn(Optional.of(sampleTender()));
        when(bidderRepository.findByName(any(String.class)))
                .thenReturn(Optional.of(bidder));
        when(offerRepository.save(any(Offer.class)))
                .thenAnswer(a -> {
                    final var offerArg = (Offer) a.getArguments()[0];
                    offerArg.setId((long) (Math.random() * 10_000));
                    return offerArg;
                });

        final var result = bidderService.createOffer(1234L, offer);

        verify(offerRepository).save(eq(offer));
        assertThat(result.getBidder()).isEqualTo(bidder);
    }

    @Test
    public void createOfferInvalidTenderId() {
        assertThrows(InvalidOperationException.class, () -> {
            when(tenderRepository.findById(eq(1234L))).thenReturn(Optional.empty());
            bidderService.createOffer(1234L, sampleOffer());
        });
    }

    @Test
    public void createOfferNullTenderId() {
        assertThrows(IllegalArgumentException.class, () -> {
            bidderService.createOffer(null, sampleOffer());
        });
    }

    @Test
    public void createOfferNullOffer() {
        assertThrows(IllegalArgumentException.class, () -> {
            bidderService.createOffer(1234L, null);
        });
    }

    @Test
    public void createOfferNullBidder() {
        assertThrows(IllegalArgumentException.class, () -> {
            final var offer = sampleOffer();
            offer.setBidder(null);
            bidderService.createOffer(1234L, offer);
        });
    }

    @Test
    public void createOfferNullBid() {
        assertThrows(IllegalArgumentException.class, () -> {
            final var offer = sampleOffer();
            offer.setBid(null);
            bidderService.createOffer(1234L, offer);
        });
    }

    @Test
    public void getOffersWithBidderName() {
        bidderService.getOffers("bidder");
        verify(offerRepository).findByBidder_Name(eq("bidder"));
    }

    @Test
    public void getOffersWithBidderNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            bidderService.getOffers(null);
        });
    }

    @Test
    public void getOffersWithBidderNameAndTenderId() {
        bidderService.getOffers("bidder", 12345L);
        verify(offerRepository).findByBidder_NameAndTender_Id(eq("bidder"), eq(12345L));
    }

    @Test
    public void getOffersWithBidderNameAndTenderIdNullBidderName() {
        assertThrows(IllegalArgumentException.class, () -> {
            bidderService.getOffers(null, 12345L);
        });
    }

    @Test
    public void getOffersWithBidderNameAndTenderIdNullTenderId() {
        assertThrows(IllegalArgumentException.class, () -> {
            bidderService.getOffers("bidder", null);
        });
    }
}
