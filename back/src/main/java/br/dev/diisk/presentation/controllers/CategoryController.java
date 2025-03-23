package br.dev.diisk.presentation.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
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
import br.dev.diisk.application.cases.category.AddCategoryCase;
import br.dev.diisk.application.cases.category.DeleteCategoryCase;
import br.dev.diisk.application.cases.category.GetCategoriesCase;
import br.dev.diisk.application.cases.category.UpdateCategoryCase;
import br.dev.diisk.application.dtos.category.UpdateCategoryDTO;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.exceptions.EmptyListException;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.presentation.dtos.category.AddCategoryRequest;
import br.dev.diisk.presentation.dtos.category.CategoryResponse;
import br.dev.diisk.presentation.dtos.category.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('DEFAULT')")
@SecurityRequirement(name = "bearer-key")
public class CategoryController {

    private final AddCategoryCase addCategoryCase;
    private final DeleteCategoryCase deleteCategoryCase;
    private final UpdateCategoryCase updateCategoryCase;
    private final ModelMapper mapper;
    private final GetCategoriesCase getCategoriesCase;
    private final IResponseService responseService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CategoryResponse>> addCategory(@RequestBody @Valid AddCategoryRequest dto,
            @AuthenticationPrincipal User user) {
        Category category = addCategoryCase.execute(user, dto.getDescription(), dto.getType());
        CategoryResponse response = mapper.map(category, CategoryResponse.class);
        return responseService.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<SuccessResponse<CategoryResponse>> updateCategory(@PathVariable Long id,
            @RequestBody @Valid UpdateCategoryRequest dto,
            @AuthenticationPrincipal User user) {
        Category category = updateCategoryCase.execute(user.getId(), id, mapper.map(dto, UpdateCategoryDTO.class));
        CategoryResponse response = mapper.map(category, CategoryResponse.class);
        return responseService.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteCategory(@PathVariable Long id,
            @AuthenticationPrincipal User user) {
        deleteCategoryCase.execute(user.getId(), id);
        return responseService.ok(true);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CategoryResponse>>> getCategories(
            @RequestParam CategoryTypeEnum categoryType,
            @AuthenticationPrincipal User user) {
        List<Category> categories = getCategoriesCase.execute(user.getId(), categoryType);

        if (categories.size() == 0)
            throw new EmptyListException(getClass(), "Categories");

        List<CategoryResponse> response = categories.stream()
                .map(category -> mapper.map(category, CategoryResponse.class))
                .toList();

        return responseService.ok(response);
    }

}
