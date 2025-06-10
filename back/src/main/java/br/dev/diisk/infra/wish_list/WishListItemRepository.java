package br.dev.diisk.infra.wish_list;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.wish_list.WishListItem;
import br.dev.diisk.domain.repositories.wish_list.IWishListItemRepository;
import br.dev.diisk.infra.shared.BaseRepository;

@Repository
public class WishListItemRepository extends BaseRepository<WishListItemJPA, WishListItem> implements IWishListItemRepository {

    public WishListItemRepository(WishListItemJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
