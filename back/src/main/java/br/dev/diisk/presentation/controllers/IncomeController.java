package br.dev.diisk.presentation.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.dtos.response.PageResponse;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos.transaction.CreateIncomeRequest;
import br.dev.diisk.presentation.dtos.transaction.CreateIncomeRecurringRequest;
import br.dev.diisk.presentation.dtos.transaction.IncomeResponse;
import br.dev.diisk.presentation.dtos.transaction.IncomeRecurringResponse;
import br.dev.diisk.presentation.dtos.transaction.UpdateIncomeRequest;
import br.dev.diisk.presentation.dtos.transaction.UpdateIncomeRecurringRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<IncomeResponse>>> listIncomes(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return responseService.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<IncomeResponse>> getIncome(@PathVariable Long id) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<IncomeResponse>> createIncome(
            @RequestBody @Valid CreateIncomeRequest request) {
        return responseService.created(null, null);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<IncomeResponse>> updateIncome(
            @PathVariable Long id, @RequestBody @Valid UpdateIncomeRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteIncome(@PathVariable Long id) {
        return responseService.ok(true);
    }

    @GetMapping("/recurring")
    public ResponseEntity<SuccessResponse<PageResponse<IncomeRecurringResponse>>> listRecurringIncomes(
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return responseService.ok(null);
    }

    @PostMapping("/recurring")
    public ResponseEntity<SuccessResponse<IncomeRecurringResponse>> createRecurringIncome(
            @RequestBody @Valid CreateIncomeRecurringRequest request) {
        return responseService.created(null, null);
    }

    @PatchMapping("/recurring/{id}")
    public ResponseEntity<SuccessResponse<IncomeRecurringResponse>> updateRecurringIncome(
            @PathVariable Long id, @RequestBody @Valid UpdateIncomeRecurringRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/recurring/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteRecurringIncome(@PathVariable Long id) {
        return responseService.ok(true);
    }
}
