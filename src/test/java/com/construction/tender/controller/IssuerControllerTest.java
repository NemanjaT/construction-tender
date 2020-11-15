package com.construction.tender.controller;

import com.construction.tender.dto.request.TenderRequest;
import com.construction.tender.entity.Tender;
import com.construction.tender.service.IssuerService;
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
import static com.construction.tender.helper.Sample.sampleTender;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = IssuerController.class)
public class IssuerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssuerService issuerService;

    @Test
    public void createTender_Ok() throws Exception {
        when(issuerService.createTender(any(Tender.class)))
                .thenReturn(sampleTender());

        mockMvc.perform(put("/issuer/create-tender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TenderRequest("construction-A", "description", "GDS"))))
                .andExpect(status().isCreated());
    }

    @Test
    public void createTender_BadRequest() throws Exception {
        mockMvc.perform(put("/issuer/create-tender")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TenderRequest("", "description", "GDS"))))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/issuer/create-tender")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TenderRequest("construction-A", "", "GDS"))))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/issuer/create-tender")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TenderRequest("construction-A", "description", ""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void acceptOffer_Ok() throws Exception {
        when(issuerService.acceptOffer(anyLong(), anyLong()))
                .thenReturn(sampleTender());

        mockMvc.perform(post("/issuer/tender/1234/accept-offer/4321")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void getOffers_Ok() throws Exception {
        when(issuerService.getOffers(anyLong()))
                .thenReturn(List.of(sampleOffer()));
        mockMvc.perform(get("/issuer/tender/1234/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(issuerService.getOffers(anyLong()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/issuer/tender/1234/offers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    public void getTenders_Ok() throws Exception {
        when(issuerService.getTenders(anyString()))
                .thenReturn(List.of(sampleTender()));
        mockMvc.perform(get("/issuer/issuername/tenders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(issuerService.getTenders(anyString()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/issuer/issuername/tenders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }
}
