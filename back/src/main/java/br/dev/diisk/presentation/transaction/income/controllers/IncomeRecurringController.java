package br.dev.diisk.presentation.transaction.income.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.transaction.income.dtos.CreateIncomeRecurringRequest;
import br.dev.diisk.presentation.transaction.income.dtos.IncomeRecurringResponse;
import br.dev.diisk.presentation.transaction.income.dtos.UpdateIncomeRecurringRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recurring/incomes")
@RequiredArgsConstructor
public class IncomeRecurringController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<IncomeRecurringResponse>>> listRecurringIncomes(
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<IncomeRecurringResponse>> createRecurringIncome(
            @RequestBody @Valid CreateIncomeRecurringRequest request) {
        return responseService.ok();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<IncomeRecurringResponse>> updateRecurringIncome(
            @PathVariable Long id, @RequestBody @Valid UpdateIncomeRecurringRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteRecurringIncome(@PathVariable Long id) {
        return responseService.ok(true);
    }
}
