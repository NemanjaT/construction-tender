package com.construction.tender.dto.response;

import com.construction.tender.entity.Tender;
import com.construction.tender.entity.TenderStatus;
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
public class TenderResponse extends RepresentationModel<TenderResponse> {
    @JsonProperty("tenderId")
    private final Long id;

    @JsonProperty("constructionSite")
    private final String constructionSite;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("issuerName")
    private final String issuerName;

    @JsonProperty("tenderStatus")
    private final TenderStatus tenderStatus;

    public static TenderResponse fromEntity(Tender tender) {
        return TenderResponse.builder()
                .id(tender.getId())
                .constructionSite(tender.getConstructionSite())
                .description(tender.getDescription())
                .issuerName(tender.getIssuer().getName())
                .tenderStatus(tender.getStatus())
                .build();
    }
}
