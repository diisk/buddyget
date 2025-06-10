package br.dev.diisk.application.shared;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import br.dev.diisk.domain.user.User;
import jakarta.annotation.PostConstruct;

public abstract class BaseMapper<S, T>{

    private Class<T> targetType;
    private Class<S> sourceType;

    private final ModelMapper mapper;

    @SuppressWarnings("unchecked")
    public BaseMapper(ModelMapper mapper) {
        this.sourceType = (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        this.targetType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        this.mapper = mapper;
    }

    @PostConstruct
    private void init() {
        TypeMap<S, T> typeMap = mapper.createTypeMap(sourceType, targetType);
        configureMapping(typeMap);
    }

    protected void configureMapping(TypeMap<S, T> typeMap) {

    }

    protected void doComplexMap(User user, S source, T target) {

    }

    protected void doComplexUpdate(User user, S source, T target) {

    }

    public T apply(User user, S source) {
        T target = mapper.map(source, targetType);
        doComplexMap(user, source, target);
        return target;
    }

    public void update(User user, S source, T target) {
        mapper.map(source, target);
        doComplexUpdate(user, source, target);
    }

    public List<T> mapList(User user, Collection<S> source) {
        return source.stream().map(obj->apply(user, obj)).collect(Collectors.toList());
    }

    public Set<T> mapSet(User user, Collection<S> source) {
        return source.stream().map(obj->apply(user, obj)).collect(Collectors.toSet());
    }
}
