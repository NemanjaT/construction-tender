package com.construction.tender.repository;

import com.construction.tender.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;

public abstract class RepositoryTest extends ApplicationTest {
    @BeforeEach
    public void setup() {
        clearDatabase();
    }
}
