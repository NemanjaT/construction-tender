package com.construction.tender;

import com.construction.tender.repository.BidderRepository;
import com.construction.tender.repository.IssuerRepository;
import com.construction.tender.repository.OfferRepository;
import com.construction.tender.repository.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles="test")
public abstract class ApplicationTest {
    @Autowired
    protected IssuerRepository issuerRepository;

    @Autowired
    protected BidderRepository bidderRepository;

    @Autowired
    protected OfferRepository offerRepository;

    @Autowired
    protected TenderRepository tenderRepository;

    protected void clearDatabase() {
        tenderRepository.deleteAll();
        offerRepository.deleteAll();
        bidderRepository.deleteAll();
        issuerRepository.deleteAll();
    }
}
