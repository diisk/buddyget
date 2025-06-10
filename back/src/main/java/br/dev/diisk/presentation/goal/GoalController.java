package br.dev.diisk.presentation.goal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.goal.dtos.CreateGoalRequest;
import br.dev.diisk.presentation.goal.dtos.GoalCompleteRequest;
import br.dev.diisk.presentation.goal.dtos.GoalDepositResponse;
import br.dev.diisk.presentation.goal.dtos.GoalDetailedResponse;
import br.dev.diisk.presentation.goal.dtos.GoalResponse;
import br.dev.diisk.presentation.goal.dtos.GoalTransactionRequest;
import br.dev.diisk.presentation.goal.dtos.GoalWithdrawResponse;
import br.dev.diisk.presentation.goal.dtos.UpdateGoalRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<GoalResponse>>> listGoals(
            @RequestParam(required = false) String status) {
        return responseService.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<GoalDetailedResponse>> getGoal(@PathVariable Long id) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<GoalResponse>> createGoal(
            @RequestBody @Valid CreateGoalRequest request) {
        return responseService.ok();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<GoalResponse>> updateGoal(
            @PathVariable Long id, @RequestBody @Valid UpdateGoalRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteGoal(@PathVariable Long id) {
        return responseService.ok(true);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<SuccessResponse<GoalDepositResponse>> depositToGoal(
            @PathVariable Long id, @RequestBody @Valid GoalTransactionRequest request) {
        return responseService.ok();
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<SuccessResponse<GoalWithdrawResponse>> withdrawFromGoal(
            @PathVariable Long id, @RequestBody @Valid GoalTransactionRequest request) {
        return responseService.ok();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<SuccessResponse<GoalDetailedResponse>> complete(
            @PathVariable Long id, @RequestBody @Valid GoalCompleteRequest request) {
        return responseService.ok();
    }
}
