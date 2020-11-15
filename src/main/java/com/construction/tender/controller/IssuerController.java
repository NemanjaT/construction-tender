package com.construction.tender.controller;

import com.construction.tender.dto.request.TenderRequest;
import com.construction.tender.dto.response.OfferResponse;
import com.construction.tender.dto.response.TenderResponse;
import com.construction.tender.exception.IdNotForCallerException;
import com.construction.tender.service.IssuerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/issuer")
@Slf4j
public class IssuerController {
    private static final String ALL_OFFERS = "allOffers";
    private static final String ALL_TENDERS = "allTenders";

    @Autowired
    private IssuerService issuerService;

    @PutMapping("/create-tender")
    public ResponseEntity<TenderResponse> createTender(@Valid @RequestBody TenderRequest request,
                                                       @RequestHeader("issuer-name") String issuerName) {
        log.info("Received request for creating tender for issuer={}", issuerName);
        final var createdTender = issuerService.createTender(request.toEntity(issuerName));
        return ResponseEntity.status(HttpStatus.CREATED).body(TenderResponse.fromEntity(createdTender)
            .add(linkTo(methodOn(IssuerController.class).getOffersForTender(createdTender.getId())).withRel(ALL_OFFERS),
                    linkTo(methodOn(IssuerController.class).getTendersForIssuer(createdTender.getIssuer().getName())).withRel(ALL_TENDERS)));
    }

    @PostMapping("/tender/{tenderId}/accept-offer/{offerId}")
    public ResponseEntity<TenderResponse> acceptOffer(@PathVariable("tenderId") Long tenderId,
                                                      @PathVariable("offerId") Long offerId,
                                                      @RequestHeader("issuer-name") String issuerName) {
        log.info("Received request for approving offer tenderId={} offerId={}", tenderId, offerId);
        if (!issuerService.isTenderFromIssuer(tenderId, issuerName)) {
            throw new IdNotForCallerException("Tender with tenderId=" + tenderId + " does not belong to issuerName=" + issuerName);
        }
        final var closedTender = issuerService.acceptOffer(tenderId, offerId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(TenderResponse.fromEntity(closedTender)
                .add(linkTo(methodOn(IssuerController.class).getOffersForTender(closedTender.getId())).withRel(ALL_OFFERS),
                        linkTo(methodOn(IssuerController.class).getTendersForIssuer(closedTender.getIssuer().getName())).withRel(ALL_TENDERS)));
    }

    @GetMapping("/tender/{tenderId}/offers")
    public ResponseEntity<List<OfferResponse>> getOffersForTender(@PathVariable("tenderId") Long tenderId) {
        log.info("Received request for getting offers for tenderId={}", tenderId);
        final var result = issuerService.getOffers(tenderId).stream()
                .map(offer -> OfferResponse.fromEntity(offer)
                        .add(linkTo(methodOn(IssuerController.class)
                                .acceptOffer(offer.getTender().getId(), offer.getId(), offer.getTender().getIssuer().getName())).withRel(ALL_OFFERS)))
                .collect(Collectors.toList());
        return okOrNoContent(result);
    }

    @GetMapping("/{issuerName}/tenders")
    public ResponseEntity<List<TenderResponse>> getTendersForIssuer(@PathVariable("issuerName") String issuerName) {
        log.info("Received request for getting tenders for issuerName={}", issuerName);
        final var result = issuerService.getTenders(issuerName).stream()
                .map(TenderResponse::fromEntity)
                .map(tr -> tr.add(linkTo(methodOn(IssuerController.class).getOffersForTender(tr.getId())).withRel(ALL_OFFERS)))
                .collect(Collectors.toList());
        return okOrNoContent(result);
    }

    private static <T> ResponseEntity<List<T>> okOrNoContent(List<T> list) {
        return list.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(list) : ResponseEntity.ok(list);
    }
}
