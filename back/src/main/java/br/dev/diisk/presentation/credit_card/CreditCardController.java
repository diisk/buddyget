package br.dev.diisk.presentation.credit_card;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.credit_card.dtos.CreateCreditCardRequest;
import br.dev.diisk.presentation.credit_card.dtos.CreditCardResponse;
import br.dev.diisk.presentation.credit_card.dtos.UpdateCreditCardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/credit-cards")
@RequiredArgsConstructor
public class CreditCardController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<CreditCardResponse>>> listCreditCards(
            @RequestParam(required = false) String searchString,
            Pageable pageable) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<CreditCardResponse>> createCreditCard(
            @RequestBody @Valid CreateCreditCardRequest request) {
        return responseService.ok();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<CreditCardResponse>> updateCreditCard(
            @PathVariable Long id, @RequestBody @Valid UpdateCreditCardRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteCreditCard(@PathVariable Long id) {
        return responseService.ok(true);
    }
}
