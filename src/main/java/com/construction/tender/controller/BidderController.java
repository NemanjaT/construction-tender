package com.construction.tender.controller;

import com.construction.tender.dto.request.OfferRequest;
import com.construction.tender.dto.response.OfferResponse;
import com.construction.tender.service.BidderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/bidder")
@Slf4j
public class BidderController {
    @Autowired
    private BidderService bidderService;

    @PutMapping("/tender/{tenderId}/create-offer")
    public ResponseEntity<OfferResponse> createResponse(@Valid @RequestBody OfferRequest request,
                                                        @PathVariable("tenderId") Long tenderId) {
        log.info("Received request for creating offer for tenderId={}", tenderId);
        final var createdOffer = bidderService.createOffer(tenderId, request.toEntity());
        return ResponseEntity.ok(OfferResponse.fromEntity(createdOffer)
                .add(linkTo(methodOn(BidderController.class).getOffers(createdOffer.getBidder().getName(), null)).withRel("allOffers"),
                        linkTo(methodOn(BidderController.class).getOffers(createdOffer.getBidder().getName(), tenderId)).withRel("allTenderOffers")));
    }

    @GetMapping("/{bidderName}/offers")
    public ResponseEntity<List<OfferResponse>> getOffers(@PathVariable("bidderName") String bidderName,
                                                         @RequestParam(required = false, value = "tenderId") Long tenderId) {
        log.info("Received request for getting offers for bidderName={} with tenderId={}", bidderName, tenderId);
        final var offers = Optional.ofNullable(tenderId)
                .map(tid -> bidderService.getOffers(bidderName, tid))
                .orElseGet(() -> bidderService.getOffers(bidderName));
        return okOrNoContent(offers.stream()
                .map(OfferResponse::fromEntity)
                .collect(Collectors.toList()));
    }

    private static <T> ResponseEntity<List<T>> okOrNoContent(List<T> list) {
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }
}
