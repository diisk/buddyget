package br.dev.diisk.domain.budget;

import java.util.Optional;

import br.dev.diisk.domain.shared.interfaces.IBaseRepository;

public interface IBudgetRepository extends IBaseRepository<Budget> {
    Optional<Budget> findByCategory(Long userId, Long categoryId);
}
