package br.dev.diisk.infra.services;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.interfaces.locale.IMessageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private MessageSource messageSource;

    @Override
    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(key, locale);
    }

    @Override
    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    @Override
    public void setLocale(Locale locale) {
        LocaleContextHolder.setDefaultLocale(locale);
    }

}
