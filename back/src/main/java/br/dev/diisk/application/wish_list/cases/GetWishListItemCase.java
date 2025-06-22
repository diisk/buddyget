package br.dev.diisk.application.wish_list.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.wish_list.IWishListItemRepository;
import br.dev.diisk.domain.wish_list.WishListItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetWishListItemCase {

    private final IWishListItemRepository wishListItemRepository;

    @Transactional
    public WishListItem execute(User user, Long wishItemId) {
        WishListItem wishItem = wishListItemRepository.findById(wishItemId).orElse(null);
        if (wishItem == null || !wishItem.getUserId().equals(user.getId())) {
            throw new DatabaseValueNotFoundException(getClass(), wishItemId.toString());
        }
        return wishItem;
    }
}
