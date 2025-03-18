package br.dev.diisk.domain;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {
    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    public static String getMessage(String key) {
        return messageSource.getMessage(key, new Object[0], LocaleContextHolder.getLocale());
    }

    public static String getMessage(String key, String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag);
        return messageSource.getMessage(key, new Object[0], locale);
    }
}
