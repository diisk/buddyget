package br.dev.diisk.presentation.controllers;

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

import br.dev.diisk.application.cases.expense.AddFixedExpenseCase;
import br.dev.diisk.application.cases.expense.DeleteFixedExpenseCase;
import br.dev.diisk.application.cases.expense.ListFixedExpenseCase;
import br.dev.diisk.application.cases.expense.UpdateFixedExpenseCase;
import br.dev.diisk.application.dtos.expense.AddFixedExpenseDto;
import br.dev.diisk.application.dtos.expense.UpdateFixedExpenseDto;
import br.dev.diisk.application.dtos.response.PageResponse;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.mappers.expense.FixedExpenseToFixedExpenseResponseMapper;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.filters.expense.ListFixedExpenseFilter;
import br.dev.diisk.presentation.dtos.expense.AddFixedExpenseRequest;
import br.dev.diisk.presentation.dtos.expense.FixedExpenseResponse;
import br.dev.diisk.presentation.dtos.expense.UpdateFixedExpenseRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/fixed-expenses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('DEFAULT')")
@SecurityRequirement(name = "bearer-key")
public class FixedExpenseController {

    private final AddFixedExpenseCase addFixedExpenseCase;
    private final ListFixedExpenseCase listFixedExpenseCase;
    private final DeleteFixedExpenseCase deleteFixedExpenseCase;
    private final UpdateFixedExpenseCase updateFixedExpenseCase;
    private final ModelMapper mapper;
    private final FixedExpenseToFixedExpenseResponseMapper fixedExpenseToFixedExpenseResponseMapper;
    private final IResponseService responseService;

    @PostMapping
    public ResponseEntity<SuccessResponse<FixedExpenseResponse>> addFixedExpense(
            @RequestBody @Valid AddFixedExpenseRequest dto,
            @AuthenticationPrincipal User user) {
        FixedExpense fixedExpense = addFixedExpenseCase.execute(user, mapper.map(dto, AddFixedExpenseDto.class));
        FixedExpenseResponse response = fixedExpenseToFixedExpenseResponseMapper.apply(user, fixedExpense);
        return responseService.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<SuccessResponse<FixedExpenseResponse>> updateFixedExpense(@PathVariable Long id,
            @RequestBody @Valid UpdateFixedExpenseRequest dto,
            @AuthenticationPrincipal User user) {
        FixedExpense fixedExpense = updateFixedExpenseCase.execute(user, id,
                mapper.map(dto, UpdateFixedExpenseDto.class));
        FixedExpenseResponse response = fixedExpenseToFixedExpenseResponseMapper.apply(user, fixedExpense);
        return responseService.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteFixedExpense(@PathVariable Long id,
            @AuthenticationPrincipal User user) {
        deleteFixedExpenseCase.execute(user, id);
        return responseService.ok(true);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<FixedExpenseResponse>>> listFixedExpenses(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable,
            @AuthenticationPrincipal User user) {

        ListFixedExpenseFilter filter = new ListFixedExpenseFilter();
        filter.setCategoryId(categoryId);
        filter.setActive(active);

        Page<FixedExpense> fixedExpenses = listFixedExpenseCase.execute(user, filter, pageable);

        PageResponse<FixedExpenseResponse> response = responseService.getPageResponse(user, fixedExpenses,
                fixedExpenseToFixedExpenseResponseMapper, "fixedExpenses");

        return responseService.ok(response);
    }

}
