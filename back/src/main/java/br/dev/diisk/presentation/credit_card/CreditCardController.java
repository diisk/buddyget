package br.dev.diisk.presentation.credit_card;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.credit_card.cases.AddCreditCardCase;
import br.dev.diisk.application.credit_card.dtos.AddCreditCardParams;
import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.credit_card.CreditCard;
import br.dev.diisk.domain.user.User;
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
    private final AddCreditCardCase addCreditCardCase;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<CreditCardResponse>>> listCreditCards(
            @RequestParam(required = false) String searchString,
            Pageable pageable) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<CreditCardResponse>> createCreditCard(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateCreditCardRequest request) {
        AddCreditCardParams params = new AddCreditCardParams();
        params.setBillClosingDay(request.billClosingDay());
        params.setBillDueDay(request.billDueDay());
        params.setCardLimit(request.cardLimit());
        params.setColor(request.color());
        params.setName(request.name());
        CreditCard creditCard = addCreditCardCase.execute(user, params);
        CreditCardResponse response = new CreditCardResponse(creditCard);
        return responseService.ok(response);
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

    @GetMapping("/{id}/bills")
    public ResponseEntity<SuccessResponse<PageResponse<CreditCardResponse>>> listCreditCardBills(
            @RequestParam(required = false) String searchString,
            Pageable pageable) {
        return responseService.ok(null);
    }

    @GetMapping("/{id}/bills/{billId}")
    public ResponseEntity<SuccessResponse<PageResponse<CreditCardResponse>>> getCreditCardBill(
            @RequestParam(required = false) String searchString,
            Pageable pageable) {
        return responseService.ok(null);
    }
}
