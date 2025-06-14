package br.dev.diisk.presentation.transaction.expense.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.transaction.expense.dtos.CreateExpenseRequest;
import br.dev.diisk.presentation.transaction.expense.dtos.ExpenseResponse;
import br.dev.diisk.presentation.transaction.expense.dtos.UpdateExpenseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/transactions/expenses")
@RequiredArgsConstructor
public class ExpenseTransactionController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<ExpenseResponse>>> listExpenses(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return responseService.ok(null);
    }

    @GetMapping("/{id}") // VERIFICAR SE É NECESSÁRIO ESSE ENDPOINT
    public ResponseEntity<SuccessResponse<ExpenseResponse>> getExpense(@PathVariable Long id) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ExpenseResponse>> createExpense(
            @RequestBody @Valid CreateExpenseRequest request) {
        return responseService.ok();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id, @RequestBody @Valid UpdateExpenseRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteExpense(@PathVariable Long id) {
        return responseService.ok(true);
    }
}
