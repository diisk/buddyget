package br.dev.diisk.presentation.finance.expense_recurring;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.finance.expense_recurring.cases.AddExpenseRecurringCase;
import br.dev.diisk.application.finance.expense_recurring.cases.DeleteExpenseRecurringCase;
import br.dev.diisk.application.finance.expense_recurring.cases.EndExpenseRecurringCase;
import br.dev.diisk.application.finance.expense_recurring.cases.ListExpenseRecurringsCase;
import br.dev.diisk.application.finance.expense_recurring.cases.PayExpenseRecurringCase;
import br.dev.diisk.application.finance.expense_recurring.dtos.AddExpenseRecurringParams;
import br.dev.diisk.application.finance.expense_recurring.dtos.EndExpenseRecurringParams;
import br.dev.diisk.application.finance.expense_recurring.dtos.PayExpenseRecurringParams;
import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.finance.expense_recurring.ExpenseRecurring;
import br.dev.diisk.domain.finance.expense_recurring.ListExpenseRecurringsFilter;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.finance.expense_recurring.dtos.CreateExpenseRecurringRequest;
import br.dev.diisk.presentation.finance.expense_recurring.dtos.EndExpenseRecurringRequest;
import br.dev.diisk.presentation.finance.expense_recurring.dtos.ExpenseRecurringResponse;
import br.dev.diisk.presentation.finance.expense_recurring.dtos.PayExpenseRecurringRequest;
import br.dev.diisk.presentation.finance.expense_transaction.dtos.ExpenseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recurring/expenses")
@RequiredArgsConstructor
public class ExpenseRecurringController {

    private final IResponseService responseService;
    private final ListExpenseRecurringsCase listExpenseRecurringsCase;
    private final AddExpenseRecurringCase addExpenseRecurringCase;
    private final PayExpenseRecurringCase payExpenseRecurringCase;
    private final EndExpenseRecurringCase endExpenseRecurringCase;
    private final DeleteExpenseRecurringCase deleteExpenseRecurringCase;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<ExpenseRecurringResponse>>> listExpensesRecurrings(
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        ListExpenseRecurringsFilter filter = new ListExpenseRecurringsFilter();
        filter.setSearchString(searchString);
        Page<ExpenseRecurring> expenseRecurrings = listExpenseRecurringsCase.execute(user, filter, pageable);
        PageResponse<ExpenseRecurringResponse> responsePage = responseService.getPageResponse(user, expenseRecurrings,
                ExpenseRecurringResponse::new);
        return responseService.ok(responsePage);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<SuccessResponse<ExpenseResponse>> payExpenseRecurring(
            @RequestBody @Valid PayExpenseRecurringRequest request, @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        PayExpenseRecurringParams params = new PayExpenseRecurringParams();
        params.setReferenceDate(request.referenceDate());
        params.setPaymentDate(request.paymentDate());

        ExpenseTransaction expenseTransaction = payExpenseRecurringCase.execute(user, id, params);
        ExpenseResponse response = new ExpenseResponse(expenseTransaction);

        return responseService.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ExpenseRecurringResponse>> createExpenseRecurring(
            @RequestBody @Valid CreateExpenseRecurringRequest request, @AuthenticationPrincipal User user) {
        AddExpenseRecurringParams params = new AddExpenseRecurringParams();
        params.setCategoryId(request.categoryId());
        params.setCreditCardId(request.creditCardId());
        params.setDescription(request.description());
        params.setStartDate(request.startDate());
        params.setValue(request.value());
        params.setEndDate(request.endDate());
        params.setWishItemId(request.wishItemId());
        params.setDueDay(request.dueDay());
        ExpenseRecurring expenseRecurring = addExpenseRecurringCase.execute(
                user, params);
        ExpenseRecurringResponse response = new ExpenseRecurringResponse(expenseRecurring);
        return responseService.ok(response);
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<SuccessResponse<ExpenseRecurringResponse>> endExpenseRecurring(
            @PathVariable Long id, @RequestBody @Valid EndExpenseRecurringRequest request,
            @AuthenticationPrincipal User user) {
        EndExpenseRecurringParams params = new EndExpenseRecurringParams();
        params.setEndDate(request.endDate());
        ExpenseRecurring expenseRecurring = endExpenseRecurringCase.execute(user, params, id);
        ExpenseRecurringResponse response = new ExpenseRecurringResponse(expenseRecurring);
        return responseService.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteExpenseRecurring(@PathVariable Long id,
            @AuthenticationPrincipal User user) {
        deleteExpenseRecurringCase.execute(user, id);
        return responseService.ok(true);
    }
}
