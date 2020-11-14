package com.construction.tender.repository;

import com.construction.tender.entity.Issuer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssuerRepository extends JpaRepository<Issuer, Long> {

    Optional<Issuer> findByNameEquals(String name);

}
