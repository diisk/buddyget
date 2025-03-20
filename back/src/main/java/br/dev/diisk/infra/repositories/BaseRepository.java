package br.dev.diisk.infra.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.diisk.domain.entities.RastreableEntity;
import br.dev.diisk.domain.repositories.IBaseRepository;

public abstract class BaseRepository<J extends JpaRepository<E, Long>, E extends RastreableEntity>
        implements IBaseRepository<E> {

    protected final J jpa;

    public BaseRepository(J jpa) {
        this.jpa = jpa;
    }

    @Override
    public void delete(Collection<E> entities) {
        List<E> entitiesList = entities.stream().map(e -> {
            e.delete();
            return e;
        }).toList();
        save(entitiesList);
    }

    @Override
    public void delete(E entity) {
        entity.delete();
        save(entity);
    }

    @Override
    public Optional<E> findById(Long id) {
        Optional<E> entity = jpa.findById(id);
        if (entity.isPresent() && entity.get().isDeleted())
            return Optional.empty();

        return entity;
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
