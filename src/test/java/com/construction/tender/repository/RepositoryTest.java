package com.construction.tender.repository;

import com.construction.tender.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RepositoryTest extends ApplicationTest {
    @Autowired
    protected IssuerRepository issuerRepository;

    @Autowired
    protected BidderRepository bidderRepository;

    @Autowired
    protected TenderRepository tenderRepository;

    @BeforeEach
    public void setup() {
        tenderRepository.deleteAll();
        bidderRepository.deleteAll();
        issuerRepository.deleteAll();
    }
}
