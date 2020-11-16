package com.construction.tender.controller;

import com.construction.tender.dto.request.OfferRequest;
import com.construction.tender.dto.response.OfferResponse;
import com.construction.tender.service.BidderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@Api("Bidder controller")
public class BidderController {
    private static final String ALL_OFFERS = "allOffers";
    private static final String ALL_TENDER_OFFERS = "allTenderOffers";

    @Autowired
    private BidderService bidderService;

    @PutMapping("/tender/{tenderId}/create-offer")
    @ApiOperation("Creates an offer for a specified tender")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created an offer for a tender"),
            @ApiResponse(code = 400, message = "Either the request for creating a tender is invalid (missing information), " +
                    "or requested tender does not exist, or is closed. Exact information will be provided in response object.")
    })
    public ResponseEntity<OfferResponse> createResponse(@Valid @RequestBody OfferRequest request,
                                                        @PathVariable("tenderId") Long tenderId,
                                                        @RequestHeader("bidder-name") String bidderName) {
        log.info("Received request for creating offer for tenderId={}", tenderId);
        final var createdOffer = bidderService.createOffer(tenderId, request.toEntity(bidderName));
        return ResponseEntity.status(HttpStatus.CREATED).body(OfferResponse.fromEntity(createdOffer)
                .add(linkTo(methodOn(BidderController.class).getOffers(createdOffer.getBidder().getName(), null)).withRel(ALL_OFFERS),
                        linkTo(methodOn(BidderController.class).getOffers(createdOffer.getBidder().getName(), tenderId)).withRel(ALL_TENDER_OFFERS)));
    }

    @GetMapping("/{bidderName}/offers")
    @ApiOperation("Gets all offers for a specified bidder and (optionally) tender")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved offers for bidder/tender"),
            @ApiResponse(code = 204, message = "Retrieved an empty list from the database")
    })
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
        return list.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(list) : ResponseEntity.ok(list);
    }
}
