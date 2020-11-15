package com.construction.tender.controller;

import com.construction.tender.dto.MoneyDto;
import com.construction.tender.dto.request.OfferRequest;
import com.construction.tender.entity.Offer;
import com.construction.tender.service.BidderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.construction.tender.helper.Sample.sampleOffer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BidderController.class)
public class BidderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BidderService bidderService;

    @Test
    public void createOffer_Ok() throws Exception {
        when(bidderService.createOffer(anyLong(),any(Offer.class)))
                .thenReturn(sampleOffer());

        mockMvc.perform(put("/bidder/tender/12345/create-offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new OfferRequest("bidder name", MoneyDto.builder()
                                .amount(10_000D).currency("EUR").build()))))
                .andExpect(status().isOk());
    }

    @Test
    public void createOffer_BadRequest() throws Exception {
        when(bidderService.createOffer(anyLong(),any(Offer.class)))
                .thenReturn(sampleOffer());

        mockMvc.perform(put("/bidder/tender/12345/create-offer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new OfferRequest("", MoneyDto.builder()
                        .amount(10_000D).currency("EUR").build()))))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/bidder/tender/12345/create-offer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new OfferRequest("", MoneyDto.builder()
                        .amount(null).currency("EUR").build()))))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/bidder/tender/12345/create-offer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new OfferRequest("bidder name", MoneyDto.builder()
                        .amount(10_000D).currency("").build()))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOffers_Ok() throws Exception {
        when(bidderService.getOffers(anyString()))
                .thenReturn(List.of(sampleOffer()));
        mockMvc.perform(get("/bidder/bidder-name/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(bidderService.getOffers(anyString()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/bidder/bidder-name/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        when(bidderService.getOffers(anyString(), anyLong()))
                .thenReturn(List.of(sampleOffer()));
        mockMvc.perform(get("/bidder/bidder-name/offers?tenderId=1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(bidderService.getOffers(anyString(), anyLong()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/bidder/bidder-name/offers?tenderId=1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }
}
