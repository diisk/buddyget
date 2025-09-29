package br.dev.diisk.presentation.finance.income_transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.finance.income_transaction.cases.AddIncomeTransactionCase;
import br.dev.diisk.application.finance.income_transaction.cases.DeleteIncomeTransactionCase;
import br.dev.diisk.application.finance.income_transaction.cases.ListPaidIncomeTransactionsCase;
import br.dev.diisk.application.finance.income_transaction.cases.ListPendingIncomeTransactionsCase;
import br.dev.diisk.application.finance.income_transaction.cases.UpdateIncomeTransactionCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.finance.income_transaction.dtos.PendingIncomeTransactionDTO;
import br.dev.diisk.application.finance.income_transaction.dtos.UpdateIncomeTransactionParams;
import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.ListPaidIncomeTransactionsFilter;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.finance.income_transaction.dtos.CreateIncomeRequest;
import br.dev.diisk.presentation.finance.income_transaction.dtos.IncomeResponse;
import br.dev.diisk.presentation.finance.income_transaction.dtos.UpdateIncomeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/transactions/incomes")
@RequiredArgsConstructor
public class IncomeTransactionController {

    private final IResponseService responseService;
    private final AddIncomeTransactionCase addIncomeTransactionCase;
    private final ListPaidIncomeTransactionsCase listPaidIncomeTransactionsCase;
    private final ListPendingIncomeTransactionsCase listPendingIncomeTransactionsCase;
    private final UpdateIncomeTransactionCase updateIncomeTransactionCase;
    private final DeleteIncomeTransactionCase deleteIncomeTransactionCase;

    @GetMapping("/paids")
    public ResponseEntity<SuccessResponse<PageResponse<IncomeResponse>>> listPaidIncomes(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        ListPaidIncomeTransactionsFilter filter = new ListPaidIncomeTransactionsFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setSearchString(searchString);
        Page<IncomeTransaction> incomeTransactions = listPaidIncomeTransactionsCase.execute(user, filter, pageable);
        PageResponse<IncomeResponse> pageResponse = responseService.getPageResponse(user, incomeTransactions,
                IncomeResponse::new);
        return responseService.ok(pageResponse);
    }

    @GetMapping("/pendings")
    public ResponseEntity<SuccessResponse<List<IncomeResponse>>> listPedingIncomes(
            @AuthenticationPrincipal User user) {
        List<PendingIncomeTransactionDTO> pendings = listPendingIncomeTransactionsCase.execute(user);
        List<IncomeResponse> response = pendings.stream().map(IncomeResponse::new).toList();
        return responseService.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<IncomeResponse>> createIncome(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateIncomeRequest request) {
        AddIncomeTransactionParams params = new AddIncomeTransactionParams();
        params.setDescription(request.description());
        params.setCategoryId(request.categoryId());
        params.setValue(request.value());
        params.setPaymentDate(request.paymentDate());

        IncomeTransaction incomeTransaction = addIncomeTransactionCase.execute(user, params);
        IncomeResponse response = new IncomeResponse(incomeTransaction);

        return responseService.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<IncomeResponse>> updateIncome(
            @AuthenticationPrincipal User user,
            @PathVariable Long id, @RequestBody @Valid UpdateIncomeRequest request) {
        UpdateIncomeTransactionParams params = new UpdateIncomeTransactionParams();
        params.setDescription(request.description());
        params.setValue(request.value());
        params.setPaymentDate(request.paymentDate());

        IncomeTransaction incomeTransaction = updateIncomeTransactionCase.execute(user, id, params);
        IncomeResponse response = new IncomeResponse(incomeTransaction);
        return responseService.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteIncome(@AuthenticationPrincipal User user,
            @PathVariable Long id) {
        deleteIncomeTransactionCase.execute(user, id, false);
        return responseService.ok(true);
    }
}
