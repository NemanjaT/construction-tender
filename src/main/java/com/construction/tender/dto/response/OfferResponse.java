package com.construction.tender.dto.response;

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
public class OfferResponse extends RepresentationModel<TenderResponse> {
    @JsonProperty("offerId")
    private final Long id;

    @JsonProperty("status")
    private final OfferStatus status;

    @JsonProperty("offerBid")
    private final MoneyResponse bid;

    @JsonProperty("tenderId")
    private final Long tenderId;

    @JsonProperty("bidderName")
    private final String bidderName;

    public static OfferResponse fromEntity(Offer offer) {
        return OfferResponse.builder()
                .id(offer.getId())
                .status(offer.getStatus())
                .bid(MoneyResponse.builder()
                        .amount(offer.getBid().getAmount())
                        .currency(offer.getBid().getCurrency())
                        .build())
                .tenderId(offer.getTender().getId())
                .bidderName(offer.getBidder().getName())
                .build();
    }
}
