package br.dev.diisk.domain.shared.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import br.dev.diisk.domain.shared.entities.RastreableEntity;

public interface IBaseRepository<E extends RastreableEntity> {
    E save(E entity);

    List<E> save(Collection<E> entities);

    Optional<E> findById(Long id);

    void delete(E entity);

    void delete(Collection<E> entities);

}
