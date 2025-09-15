package br.dev.diisk.presentation.finance.expense_transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.finance.expense_transaction.cases.AddExpenseTransactionCase;
import br.dev.diisk.application.finance.expense_transaction.cases.DeleteExpenseTransactionCase;
import br.dev.diisk.application.finance.expense_transaction.cases.ListPaidExpenseTransactionsCase;
import br.dev.diisk.application.finance.expense_transaction.cases.ListPendingExpenseTransactionsCase;
import br.dev.diisk.application.finance.expense_transaction.cases.UpdateExpenseTransactionCase;
import br.dev.diisk.application.finance.expense_transaction.dtos.AddExpenseTransactionParams;
import br.dev.diisk.application.finance.expense_transaction.dtos.PendingExpenseTransactionDTO;
import br.dev.diisk.application.finance.expense_transaction.dtos.UpdateExpenseTransactionParams;
import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.finance.expense_transaction.ListExpenseTransactionsFilter;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.finance.expense_transaction.dtos.CreateExpenseRequest;
import br.dev.diisk.presentation.finance.expense_transaction.dtos.ExpenseResponse;
import br.dev.diisk.presentation.finance.expense_transaction.dtos.UpdateExpenseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/transactions/expenses")
@RequiredArgsConstructor
public class ExpenseTransactionController {

    private final IResponseService responseService;
    private final AddExpenseTransactionCase addExpenseTransactionCase;
    private final ListPaidExpenseTransactionsCase listPaidExpenseTransactionsCase;
    private final UpdateExpenseTransactionCase updateExpenseTransactionCase;
    private final DeleteExpenseTransactionCase deleteExpenseTransactionCase;
    private final ListPendingExpenseTransactionsCase listPendingExpenseTransactionsCase;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<List<ExpenseResponse>>> listPendingExpenses(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String searchString,
            @AuthenticationPrincipal User user) {
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setSearchString(searchString);

        return responseService.ok(null);
    }

    @GetMapping("/paids")
    public ResponseEntity<SuccessResponse<PageResponse<ExpenseResponse>>> listPaidExpenses(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        ListExpenseTransactionsFilter filter = new ListExpenseTransactionsFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setSearchString(searchString);
        Page<ExpenseTransaction> expenses = listPaidExpenseTransactionsCase.execute(user, filter, pageable);
        PageResponse<ExpenseResponse> response = responseService.getPageResponse(user, expenses,
                ExpenseResponse::new);
        return responseService.ok(response);
    }

    @GetMapping("/pendings")
    public ResponseEntity<SuccessResponse<List<ExpenseResponse>>> listPendingExpenses(
            @AuthenticationPrincipal User user) {
        List<PendingExpenseTransactionDTO> pendingTransactions = listPendingExpenseTransactionsCase.execute(user);
        List<ExpenseResponse> response = pendingTransactions.stream().map(ExpenseResponse::new).toList();
        return responseService.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ExpenseResponse>> createExpense(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateExpenseRequest request) {
        AddExpenseTransactionParams params = new AddExpenseTransactionParams();
        params.setDescription(request.description());
        params.setValue(request.value());
        params.setPaymentDate(request.paymentDate());
        params.setDueDate(request.dueDate());
        params.setCategoryId(request.categoryId());
        params.setCreditCardId(request.creditCardId());
        ExpenseTransaction expenseTransaction = addExpenseTransactionCase.execute(user, params);
        ExpenseResponse response = new ExpenseResponse(expenseTransaction);
        return responseService.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal User user, @RequestBody @Valid UpdateExpenseRequest request) {
        UpdateExpenseTransactionParams params = new UpdateExpenseTransactionParams();
        params.setDescription(request.description());
        params.setValue(request.value());
        params.setPaymentDate(request.paymentDate());
        params.setDueDate(request.dueDate());
        ExpenseTransaction expenseTransaction = updateExpenseTransactionCase.execute(user, id, params);
        ExpenseResponse response = new ExpenseResponse(expenseTransaction);
        return responseService.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteExpense(@AuthenticationPrincipal User user,
            @PathVariable Long id) {
        deleteExpenseTransactionCase.execute(user, id, false);
        return responseService.ok(true);
    }
}
