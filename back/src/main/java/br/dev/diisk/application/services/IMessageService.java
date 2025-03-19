package br.dev.diisk.application.services;

public interface IMessageService {

    String getMessage(String key);

    String getMessage(String key, String languageTag);

}
