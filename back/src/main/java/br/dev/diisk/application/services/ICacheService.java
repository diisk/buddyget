package br.dev.diisk.application.services;

public interface ICacheService {

    void evictCache(String value, String startsWith);

}
