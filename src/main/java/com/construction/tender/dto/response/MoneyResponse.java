package com.construction.tender.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoneyResponse {
    @JsonProperty("amount")
    private final Double amount;

    @JsonProperty("currency")
    private final String currency;
}
