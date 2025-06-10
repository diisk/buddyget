package br.dev.diisk.infra.wish_list;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.wish_list.WishListItem;

public interface WishListItemJPA extends JpaRepository<WishListItem, Long> {
    // Add any custom methods if needed
}
