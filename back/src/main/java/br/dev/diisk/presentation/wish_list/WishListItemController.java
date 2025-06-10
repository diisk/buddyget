package br.dev.diisk.presentation.wish_list;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.wish_list.dtos.CreateWishListItemRequest;
import br.dev.diisk.presentation.wish_list.dtos.UpdateWishListItemRequest;
import br.dev.diisk.presentation.wish_list.dtos.WishListItemPurchaseRequest;
import br.dev.diisk.presentation.wish_list.dtos.WishListItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/wishlist")
@RequiredArgsConstructor
public class WishListItemController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<WishListItemResponse>>> listWishListItems(
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Long categoryId,
            Pageable pageable
            ) {
        return responseService.ok(null);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<WishListItemResponse>> createWishListItem(
            @RequestBody @Valid CreateWishListItemRequest request) {
        return responseService.ok();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<WishListItemResponse>> updateWishListItem(
            @PathVariable Long id, @RequestBody @Valid UpdateWishListItemRequest request) {
        return responseService.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteWishListItem(@PathVariable Long id) {
        return responseService.ok(true);
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<SuccessResponse<WishListItemResponse>> purchaseWishListItem(
            @PathVariable Long id, @RequestBody @Valid WishListItemPurchaseRequest request) {
        return responseService.ok();
    }
}
