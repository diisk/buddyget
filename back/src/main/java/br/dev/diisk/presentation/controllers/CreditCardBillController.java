package br.dev.diisk.presentation.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.dtos.response.PageResponse;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.presentation.dtos.creditcard.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/credit-cards-bills")
@RequiredArgsConstructor
public class CreditCardBillController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<CreditCardBillResponse>>> listCreditCardsBills(
        @RequestParam(required = false) String searchString,
        Pageable pageable
    ) {
        return responseService.ok(null);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<SuccessResponse<CreditCardBillPaymentResponse>> payCreditCardBill(
            @PathVariable Long id, @RequestBody @Valid PayCreditCardBillRequest request) {
        return responseService.ok(null);
    }

}
