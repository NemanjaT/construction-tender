package com.construction.tender.repository;

import com.construction.tender.entity.Bidder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidderRepository extends JpaRepository<Bidder, Long> {
    Optional<Bidder> findByName(String name);
}
