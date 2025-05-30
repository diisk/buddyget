package br.dev.diisk.presentation.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.dtos.response.PageResponse;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos.transaction.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

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
        return responseService.created(null, null);
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

    @GetMapping("/recurring")
    public ResponseEntity<SuccessResponse<PageResponse<ExpenseRecurringResponse>>> listExpensesRecurrings(
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return responseService.ok(null);
    }

    @PostMapping("/recurring")
    public ResponseEntity<SuccessResponse<ExpenseRecurringResponse>> createExpenseRecurring(
            @RequestBody @Valid CreateExpenseRecurringRequest request) {
        return responseService.created(null, null);
    }

    @PatchMapping("/recurring/{id}")
    public ResponseEntity<SuccessResponse<ExpenseRecurringResponse>> updateExpenseRecurring(
            @PathVariable Long id, @RequestBody @Valid UpdateExpenseRecurringRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/recurring/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteExpenseRecurring(@PathVariable Long id) {
        return responseService.ok(true);
    }
}
