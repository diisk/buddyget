package br.dev.diisk.infra.repositories.wish_list;

import org.springframework.stereotype.Repository;

import br.dev.diisk.domain.entities.wish_list.WishListItem;
import br.dev.diisk.domain.repositories.wish_list.IWishListItemRepository;
import br.dev.diisk.infra.jpas.wish_list.WishListItemJPA;
import br.dev.diisk.infra.repositories.BaseRepository;

@Repository
public class WishListItemRepository extends BaseRepository<WishListItemJPA, WishListItem> implements IWishListItemRepository {

    public WishListItemRepository(WishListItemJPA jpa) {
        super(jpa);
    }

    // Add any custom methods if needed
}
