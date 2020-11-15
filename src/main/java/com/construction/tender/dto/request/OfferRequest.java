package com.construction.tender.dto.request;

import com.construction.tender.dto.MoneyDto;
import com.construction.tender.entity.Bidder;
import com.construction.tender.entity.Money;
import com.construction.tender.entity.Offer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferRequest {
    @JsonProperty("bidderName")
    @JsonPropertyDescription("Bidder name")
    @NotEmpty(message = "Bidder name has to be set.")
    private String bidderName;

    @JsonProperty("bid")
    @JsonPropertyDescription("Bid offer for the tender")
    @Valid
    private MoneyDto bidOffer;

    public OfferRequest(@JsonProperty("bidderName") String bidderName,
                        @JsonProperty("bid") MoneyDto bidOffer) {
        this.bidderName = bidderName;
        this.bidOffer = Optional.ofNullable(bidOffer).orElse(new MoneyDto(.0D, null));
    }

    public Offer toEntity() {
        final var bidder = new Bidder();
        bidder.setName(bidderName);

        final var offer = new Offer();
        offer.setBid(Money.of(bidOffer.getAmount(), bidOffer.getCurrency()));
        offer.setBidder(bidder);
        return offer;
    }
}
