package com.construction.tender.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyDto {
    @JsonProperty("amount")
    @NotNull(message = "Amount has to be set.")
    @DecimalMin(value = "0.0", message = "Amount can not be negative.")
    private final Double amount;

    @JsonProperty("currency")
    @NotEmpty(message = "Currency must not be empty.")
    @Length(min = 3, max = 3, message = "Currency must have exactly 3 characters.")
    private final String currency;

    public MoneyDto(@JsonProperty("amount") Double amount,
                    @JsonProperty("currency") String currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
