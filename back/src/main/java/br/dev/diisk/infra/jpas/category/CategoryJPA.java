package br.dev.diisk.infra.jpas.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import br.dev.diisk.domain.entities.category.Category;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;

public interface CategoryJPA extends JpaRepository<Category, Long> {

    Optional<Category> findByUser_IdAndTypeAndNameAndDeletedFalse(Long userId, CategoryTypeEnum type,
            String description);

    @Query("""
                SELECT c FROM Category c
                WHERE c.user.id = :userId
                  AND (:type IS NULL OR c.type = :type)
                  AND (
                    COALESCE(:searchString, '') = ''
                    OR c.name LIKE %:searchString%
                    OR c.description LIKE %:searchString%
                  )
                  AND c.deleted = false
            """)
    Page<Category> findAllByUser(Long userId, CategoryTypeEnum type, String searchString, Pageable pageable);

}
