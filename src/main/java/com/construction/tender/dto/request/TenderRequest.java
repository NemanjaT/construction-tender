package com.construction.tender.dto.request;

import com.construction.tender.entity.Issuer;
import com.construction.tender.entity.Tender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenderRequest {
    @JsonProperty("constructionSite")
    @JsonPropertyDescription("Construction site code which is referenced by the tender.")
    @NotEmpty(message = "Construction site must not be empty.")
    private final String constructionSite;

    @JsonProperty("description")
    @JsonPropertyDescription("Detailed tender description.")
    @NotEmpty(message = "Description must not be empty.")
    private final String description;

    @JsonProperty("issuerName")
    @JsonPropertyDescription("Name of the issuer in charge of the tender.")
    @NotEmpty(message = "Issuer name must not be empty.")
    private final String issuerName;

    public TenderRequest(@JsonProperty("constructionSite") String constructionSite,
                         @JsonProperty("description") String description,
                         @JsonProperty("issuerName") String issuerName) {
        this.constructionSite = constructionSite;
        this.description = description;
        this.issuerName = issuerName;
    }

    public Tender toEntity() {
        final var issuer = new Issuer();
        issuer.setName(issuerName);

        final var tender = new Tender();
        tender.setConstructionSite(constructionSite);
        tender.setIssuer(issuer);
        tender.setDescription(description);

        return tender;
    }
}
