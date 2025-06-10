package br.dev.diisk.presentation.budget;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.budget.dtos.BudgetPerformanceResponse;
import br.dev.diisk.presentation.budget.dtos.BudgetResponse;
import br.dev.diisk.presentation.budget.dtos.CreateBudgetRequest;
import br.dev.diisk.presentation.budget.dtos.DetailedBudgetResponse;
import br.dev.diisk.presentation.budget.dtos.UpdateBudgetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<DetailedBudgetResponse>>> listBudgets(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) String searchString,
            Pageable pageable) {

        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<BudgetResponse>> createBudget(
            @RequestBody @Valid CreateBudgetRequest request) {
        return responseService.ok();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<BudgetResponse>> updateBudget(
            @PathVariable Long id, @RequestBody @Valid UpdateBudgetRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteBudget(@PathVariable Long id) {
        return responseService.ok(true);
    }

    @GetMapping("/performance") // VERIFICAR SE JÁ EXISTE NO DASHBOARD
    public ResponseEntity<SuccessResponse<BudgetPerformanceResponse>> getBudgetPerformance(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        return responseService.ok(null);
    }
}
