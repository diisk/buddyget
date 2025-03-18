package br.dev.diisk.infra.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.repositories.IBaseRepository;

public abstract class BaseRepository<J extends JpaRepository<E,T>,E,T> implements IBaseRepository<E,T> {

    protected final J jpa;

    public BaseRepository(J jpa) {
        this.jpa = jpa;
    }

    @Override
    public void delete(Collection<E> entities) {
        jpa.deleteAll(entities);
    }

    @Override
    public void delete(E entity) {
        jpa.delete(entity);
    }

    @Override
    public Optional<E> findById(T id) {
        return jpa.findById(id);
    }

    @Override
    public List<E> save(Collection<E> entities) {
        return jpa.saveAll(entities);
    }

    @Override
    public E save(E entity) {
        return jpa.save(entity);
    }
}
