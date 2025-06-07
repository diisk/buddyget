package br.dev.diisk.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.interfaces.IBaseEnum;

@Service
public class UtilService {

    public String nameToKey(String name) {
        return removeAccents(name).toLowerCase().trim()
                .replaceAll("\\s+", " ")
                .replaceAll(" ", "-")
                .replaceAll("-[a-z]{1,2}-", "-");
    }

    public LocalDateTime toReference(LocalDateTime dateTime) {
        return toReference(dateTime, false);
    }

    public LocalDateTime toReference(LocalDateTime dateTime, Boolean endOfMonth) {
        if (dateTime == null)
            return null;

        LocalDateTime startOfMonth = LocalDateTime
                .of(
                        dateTime.getYear(),
                        dateTime.getMonthValue(),
                        1,
                        0,
                        0,
                        0,
                        0);

        if (endOfMonth)
            return startOfMonth.plusMonths(1).minusNanos(1);

        return startOfMonth;
    }

    public String getMonthName(LocalDateTime dateTime) {
        String name = dateTime.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        return name.toUpperCase().charAt(0) + name.toLowerCase().substring(1);
    }

    public String onlyNumbers(String input) {
        String result = input.replaceAll("[\\D,\\.]", "");
        return result;
    }

    public BigDecimal divide(BigDecimal bigDecimal1, BigDecimal bigdeDecimal2) {
        return bigDecimal1.divide(bigdeDecimal2, 2, RoundingMode.HALF_EVEN);
    }

    public String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("");
        return result;
    }

    public Integer getMonthsBetweenReferences(LocalDateTime startReference, LocalDateTime endReference) {
        return (endReference.getYear() - startReference.getYear()) * 12 + endReference.getMonthValue()
                - startReference.getMonthValue();
    }

    public Boolean equalsIgnoreCaseAndAccents(String str1, String str2) {
        return removeAccents(str1).equalsIgnoreCase(removeAccents(str2));
    }

    public <E extends Enum<E> & IBaseEnum> Optional<E> getEnumByName(Class<E> enumClass, String value) {
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }
}
