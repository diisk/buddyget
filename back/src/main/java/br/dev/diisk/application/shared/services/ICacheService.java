package br.dev.diisk.application.shared.services;

public interface ICacheService {

    void evictCache(String value, String startsWith);

}
