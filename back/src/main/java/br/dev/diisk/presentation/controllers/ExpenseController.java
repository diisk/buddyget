package br.dev.diisk.presentation.controllers;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.dev.diisk.application.cases.expense.AddExpenseCase;
import br.dev.diisk.application.cases.expense.DeleteExpenseCase;
import br.dev.diisk.application.cases.expense.ListExpenseCase;
import br.dev.diisk.application.cases.expense.UpdateExpenseCase;
import br.dev.diisk.application.dtos.expense.AddExpenseDto;
import br.dev.diisk.application.dtos.expense.UpdateExpenseDto;
import br.dev.diisk.application.dtos.response.PageResponse;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.mappers.expense.ExpenseToExpenseResponseMapper;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.expense.Expense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.expense.ListExpenseFilter;
import br.dev.diisk.presentation.dtos.expense.AddExpenseRequest;
import br.dev.diisk.presentation.dtos.expense.ExpenseResponse;
import br.dev.diisk.presentation.dtos.expense.UpdateExpenseRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/expenses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('DEFAULT')")
@SecurityRequirement(name = "bearer-key")
public class ExpenseController {

    private final AddExpenseCase addExpenseCase;
    private final ListExpenseCase listExpenseCase;
    private final DeleteExpenseCase deleteExpenseCase;
    private final UpdateExpenseCase updateExpenseCase;
    private final ModelMapper mapper;
    private final ExpenseToExpenseResponseMapper expenseToExpenseResponseMapper;
    private final IResponseService responseService;

    @PostMapping
    public ResponseEntity<SuccessResponse<ExpenseResponse>> addExpense(
            @RequestBody @Valid AddExpenseRequest dto,
            @AuthenticationPrincipal User user) {
        Expense expense = addExpenseCase.execute(user, mapper.map(dto, AddExpenseDto.class));
        ExpenseResponse response = expenseToExpenseResponseMapper.apply(user, expense);
        return responseService.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<SuccessResponse<ExpenseResponse>> updateExpense(@PathVariable Long id,
            @RequestBody @Valid UpdateExpenseRequest dto,
            @AuthenticationPrincipal User user) {
        Expense expense = updateExpenseCase.execute(user, id,
                mapper.map(dto, UpdateExpenseDto.class));
        ExpenseResponse response = expenseToExpenseResponseMapper.apply(user, expense);
        return responseService.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteExpense(@PathVariable Long id,
            @AuthenticationPrincipal User user) {
        deleteExpenseCase.execute(user, id);
        return responseService.ok(true);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<ExpenseResponse>>> listFixedExpenses(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long creditCardId,
            @RequestParam(required = false) Long fixedExpenseId,
            @RequestParam(required = false) LocalDateTime startDueDate,
            @RequestParam(required = false) LocalDateTime endDueDate,
            @RequestParam(required = false) LocalDateTime startPaymentDate,
            @RequestParam(required = false) LocalDateTime endPaymentDate,
            Pageable pageable,
            @AuthenticationPrincipal User user) {

        ListExpenseFilter filter = new ListExpenseFilter();
        filter.setCategoryId(categoryId);
        filter.setCreditCardId(creditCardId);
        filter.setFixedExpenseId(fixedExpenseId);
        filter.setStartDueDate(startDueDate);
        filter.setEndDueDate(endDueDate);
        filter.setStartPaymentDate(startPaymentDate);
        filter.setEndPaymentDate(endPaymentDate);

        Page<Expense> fixedExpenses = listExpenseCase.execute(user, filter, pageable);

        PageResponse<ExpenseResponse> response = responseService.getPageResponse(user, fixedExpenses,
                expenseToExpenseResponseMapper, "expenses");

        return responseService.ok(response);
    }

}
