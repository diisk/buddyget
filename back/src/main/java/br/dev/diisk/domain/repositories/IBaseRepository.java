package br.dev.diisk.domain.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IBaseRepository<E,T> {
    E save(E entity);

    List<E> save(Collection<E> entities);

    Optional<E> findById(T id);

    void delete(E entity);

    void delete(Collection<E> entities);

}
