package br.dev.diisk.infra.services;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.services.IMessageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final MessageSource messageSource;

    @Override
    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

    @Override
    public String getMessage(String key, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return messageSource.getMessage(key, null, locale);
    }

}
