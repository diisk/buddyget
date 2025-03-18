package br.dev.diisk.application.interfaces.cache;

public interface ICacheService {

    void evictCache(String value, String startsWith);

}
