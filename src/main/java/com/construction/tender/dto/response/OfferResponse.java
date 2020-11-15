package com.construction.tender.dto.response;

import com.construction.tender.dto.MoneyDto;
import com.construction.tender.entity.Offer;
import com.construction.tender.entity.OfferStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferResponse extends RepresentationModel<OfferResponse> {
    @JsonProperty("offerId")
    private final Long id;

    @JsonProperty("status")
    private final OfferStatus status;

    @JsonProperty("offerBid")
    private final MoneyDto bid;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("tenderId")
    private final Long tenderId;

    @JsonProperty("bidderName")
    private final String bidderName;

    public static OfferResponse fromEntity(Offer offer) {
        return OfferResponse.builder()
                .id(offer.getId())
                .status(offer.getStatus())
                .bid(MoneyDto.builder()
                        .amount(offer.getBid().getAmount())
                        .currency(offer.getBid().getCurrency())
                        .build())
                .description(offer.getDescription())
                .tenderId(offer.getTender().getId())
                .bidderName(offer.getBidder().getName())
                .build();
    }
}
