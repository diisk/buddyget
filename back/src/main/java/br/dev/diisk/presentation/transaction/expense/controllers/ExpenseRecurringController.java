package br.dev.diisk.presentation.transaction.expense.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.transaction.expense.dtos.CreateExpenseRecurringRequest;
import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseRecurringResponse;
import br.dev.diisk.presentation.transaction.expense.dtos.UpdateExpenseRecurringRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recurring/expenses")
@RequiredArgsConstructor
public class ExpenseRecurringController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<ExpenseRecurringResponse>>> listExpensesRecurrings(
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ExpenseRecurringResponse>> createExpenseRecurring(
            @RequestBody @Valid CreateExpenseRecurringRequest request) {
        return responseService.ok();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<ExpenseRecurringResponse>> updateExpenseRecurring(
            @PathVariable Long id, @RequestBody @Valid UpdateExpenseRecurringRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteExpenseRecurring(@PathVariable Long id) {
        return responseService.ok(true);
    }
}
