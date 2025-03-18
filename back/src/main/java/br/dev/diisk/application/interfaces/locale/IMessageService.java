package br.dev.diisk.application.interfaces.locale;

import java.util.Locale;

public interface IMessageService {

    String getMessage(String key);

    String getMessage(String key, Locale locale);

    void setLocale(Locale locale);

}
