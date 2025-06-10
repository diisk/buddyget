package br.dev.diisk.presentation.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.category.cases.AddCategoryCase;
import br.dev.diisk.application.category.cases.DeleteCategoryCase;
import br.dev.diisk.application.category.cases.ListCategoriesCase;
import br.dev.diisk.application.category.cases.UpdateCategoryCase;
import br.dev.diisk.application.category.dtos.AddCategoryParams;
import br.dev.diisk.application.category.dtos.UpdateCategoryParams;
import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.category.ListCategoriesFilter;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.category.dtos.CategoryResponse;
import br.dev.diisk.presentation.category.dtos.CreateCategoryRequest;
import br.dev.diisk.presentation.category.dtos.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/categories")
@PreAuthorize("hasAuthority('DEFAULT')")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class CategoryController {

    private final IResponseService responseService;
    private final AddCategoryCase addCategoryCase;
    private final ListCategoriesCase listCategoriesCase;
    private final UpdateCategoryCase updateCategoryCase;
    private final DeleteCategoryCase deleteCategoryCase;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<CategoryResponse>>> listCategories(
            @RequestParam(required = false) CategoryTypeEnum type,
            @RequestParam(required = false) String searchString,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        ListCategoriesFilter filter = new ListCategoriesFilter();
        filter.setType(type);
        filter.setSearchString(searchString);
        Page<Category> categories = listCategoriesCase.execute(user, filter, pageable);

        PageResponse<CategoryResponse> response = responseService.getPageResponse(user, categories,
                CategoryResponse::new);
        return responseService.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<CategoryResponse>> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateCategoryRequest request) {
        AddCategoryParams params = new AddCategoryParams();
        params.setDescription(request.description());
        params.setName(request.name());
        params.setType(request.type());
        params.setIconName(request.iconName());
        params.setColor(request.color());
        Category category = addCategoryCase.execute(user, params);
        CategoryResponse response = new CategoryResponse(category);
        return responseService.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<CategoryResponse>> updateCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest request) {
        UpdateCategoryParams params = new UpdateCategoryParams();
        params.setDescription(request.description());
        params.setName(request.name());
        params.setIconName(request.iconName());
        params.setColor(request.color());
        Category category = updateCategoryCase.execute(user, id, params);
        CategoryResponse response = new CategoryResponse(category);
        return responseService.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        deleteCategoryCase.execute(user, id);
        return responseService.ok(true);
    }
}