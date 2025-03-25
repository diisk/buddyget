package br.dev.diisk.presentation.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.dev.diisk.application.cases.credit_card.AddCreditCardCase;
import br.dev.diisk.application.cases.credit_card.DeleteCreditCardCase;
import br.dev.diisk.application.cases.credit_card.ListCreditCardsCase;
import br.dev.diisk.application.cases.credit_card.UpdateCreditCardCase;
import br.dev.diisk.application.dtos.credit_card.AddCreditCardDto;
import br.dev.diisk.application.dtos.credit_card.UpdateCreditCardDto;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.exceptions.EmptyListException;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.credit_card.CreditCard;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos.credit_card.AddCreditCardRequest;
import br.dev.diisk.presentation.dtos.credit_card.CreditCardResponse;
import br.dev.diisk.presentation.dtos.credit_card.UpdateCreditCardRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/credit-cards")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('DEFAULT')")
@SecurityRequirement(name = "bearer-key")
public class CreditCardController {

    private final AddCreditCardCase addCreditCardCase;
    private final DeleteCreditCardCase deleteCreditCardCase;
    private final UpdateCreditCardCase updateCreditCardCase;
    private final ModelMapper mapper;
    private final ListCreditCardsCase getCreditCardsCase;
    private final IResponseService responseService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CreditCardResponse>> addCreditCard(@RequestBody @Valid AddCreditCardRequest dto,
            @AuthenticationPrincipal User user) {
        CreditCard creditCard = addCreditCardCase.execute(user, mapper.map(dto, AddCreditCardDto.class));
        CreditCardResponse response = mapper.map(creditCard, CreditCardResponse.class);
        return responseService.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<SuccessResponse<CreditCardResponse>> updateCreditCard(@PathVariable Long id,
            @RequestBody @Valid UpdateCreditCardRequest dto,
            @AuthenticationPrincipal User user) {
        CreditCard creditCard = updateCreditCardCase.execute(user, id, mapper.map(dto, UpdateCreditCardDto.class));
        CreditCardResponse response = mapper.map(creditCard, CreditCardResponse.class);
        return responseService.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteCreditCard(@PathVariable Long id,
            @AuthenticationPrincipal User user) {
        deleteCreditCardCase.execute(user, id);
        return responseService.ok(true);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CreditCardResponse>>> getCreditCards(
            @AuthenticationPrincipal User user) {
        List<CreditCard> creditCards = getCreditCardsCase.execute(user);

        if (creditCards.size() == 0)
            throw new EmptyListException(getClass(), "credit-cards");

        List<CreditCardResponse> response = creditCards.stream()
                .map(creditCard -> mapper.map(creditCard, CreditCardResponse.class))
                .toList();

        return responseService.ok(response);
    }

}
