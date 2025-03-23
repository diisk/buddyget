package br.dev.diisk.infra.jpas.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;

public interface CategoryJPA extends JpaRepository<Category, Long> {

    Optional<Category> findByUser_IdAndTypeAndDescriptionAndDeletedFalse(Long userId, CategoryTypeEnum type,
            String description);

    List<Category> findByUser_IdAndTypeAndDeletedFalse(Long userId, CategoryTypeEnum type);

}
