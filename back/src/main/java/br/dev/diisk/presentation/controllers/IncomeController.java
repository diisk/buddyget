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

import br.dev.diisk.application.UtilService;
import br.dev.diisk.application.cases.income.AddIncomeCase;
import br.dev.diisk.application.cases.income.DeleteIncomeCase;
import br.dev.diisk.application.cases.income.ListIncomesCase;
import br.dev.diisk.application.cases.income.UpdateIncomeCase;
import br.dev.diisk.application.dtos.income.AddIncomeDto;
import br.dev.diisk.application.dtos.income.UpdateIncomeDto;
import br.dev.diisk.application.dtos.response.PageResponse;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.mappers.income.IncomeToIncomeResponseMapper;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.income.Income;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.income.ListIncomesFilter;
import br.dev.diisk.presentation.dtos.income.AddIncomeRequest;
import br.dev.diisk.presentation.dtos.income.IncomeResponse;
import br.dev.diisk.presentation.dtos.income.UpdateIncomeRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/incomes")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('DEFAULT')")
@SecurityRequirement(name = "bearer-key")
public class IncomeController {

    private final AddIncomeCase addIncomeCase;
    private final ListIncomesCase listIncomesCase;
    private final UtilService utilService;
    private final DeleteIncomeCase deleteIncomeCase;
    private final UpdateIncomeCase updateIncomeCase;
    private final ModelMapper mapper;
    private final IncomeToIncomeResponseMapper incomeToIncomeResponseMapper;
    private final IResponseService responseService;

    @PostMapping
    public ResponseEntity<SuccessResponse<IncomeResponse>> addIncome(@RequestBody @Valid AddIncomeRequest dto,
            @AuthenticationPrincipal User user) {
        Income income = addIncomeCase.execute(user, mapper.map(dto, AddIncomeDto.class));
        IncomeResponse response = incomeToIncomeResponseMapper.apply(user, income);
        return responseService.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<SuccessResponse<IncomeResponse>> updateIncome(@PathVariable Long id,
            @RequestBody @Valid UpdateIncomeRequest dto,
            @AuthenticationPrincipal User user) {
        Income income = updateIncomeCase.execute(user, id, mapper.map(dto, UpdateIncomeDto.class));
        IncomeResponse response = incomeToIncomeResponseMapper.apply(user, income);
        return responseService.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteIncome(@PathVariable Long id,
            @AuthenticationPrincipal User user) {
        deleteIncomeCase.execute(user, id);
        return responseService.ok(true);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<IncomeResponse>>> listIncomes(
            @RequestParam(required = false) LocalDateTime startReferenceDate,
            @RequestParam(required = false) LocalDateTime endReferenceDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean onlyPending,
            Pageable pageable,
            @AuthenticationPrincipal User user) {

        ListIncomesFilter filter = new ListIncomesFilter();
        filter.setStartReferenceDate(utilService.toReference(startReferenceDate));
        filter.setEndReferenceDate(utilService.toReference(endReferenceDate, true));
        filter.setCategoryId(categoryId);
        filter.setOnlyPending(onlyPending);

        Page<Income> incomes = listIncomesCase.execute(user, filter, pageable);

        PageResponse<IncomeResponse> response = responseService.getPageResponse(
                user, incomes, incomeToIncomeResponseMapper, "incomes");

        return responseService.ok(response);
    }

}
