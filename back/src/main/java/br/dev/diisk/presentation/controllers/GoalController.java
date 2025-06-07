package br.dev.diisk.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.presentation.dtos.goal.*;
import br.dev.diisk.presentation.dtos.response.SuccessResponse;
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
        return responseService.created(null, null);
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
        return responseService.created(null, null);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<SuccessResponse<GoalWithdrawResponse>> withdrawFromGoal(
            @PathVariable Long id, @RequestBody @Valid GoalTransactionRequest request) {
        return responseService.created(null, null);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<SuccessResponse<GoalDetailedResponse>> complete(
            @PathVariable Long id, @RequestBody @Valid GoalCompleteRequest request) {
        return responseService.created(null, null);
    }
}
