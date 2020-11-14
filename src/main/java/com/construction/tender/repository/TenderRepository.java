package com.construction.tender.repository;

import com.construction.tender.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderRepository extends JpaRepository<Tender, Long> {
}
