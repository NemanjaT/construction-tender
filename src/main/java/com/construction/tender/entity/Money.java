package com.construction.tender.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Money {
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    public static Money of(Double amount, String currency) {
        return new Money(amount, currency);
    }
}
