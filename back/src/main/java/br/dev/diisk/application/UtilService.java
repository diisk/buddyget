package br.dev.diisk.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public String nameToKey(String name) {
        return removeAccents(name).toLowerCase().trim()
                .replaceAll("\\s+", " ")
                .replaceAll(" ", "-")
                .replaceAll("-[a-z]{1,2}-", "-");
    }

    public LocalDateTime toReference(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), 1, 0, 0);
    }

    public String getMonthName(LocalDateTime dateTime) {
        String name = dateTime.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        return name.toUpperCase().charAt(0) + name.toLowerCase().substring(1);
    }

    public String onlyNumbers(String input) {
        String result = input.replaceAll("[\\D,\\.]", "");
        return result;
    }

    public BigDecimal divide(BigDecimal bigDecimal1, BigDecimal bigdeDecimal2){
        return bigDecimal1.divide(bigdeDecimal2,2,RoundingMode.HALF_EVEN);
    }

    public String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("");
        return result;
    }

    public Boolean equalsIgnoreCaseAndAccents(String str1, String str2) {
        return removeAccents(str1).equalsIgnoreCase(removeAccents(str2));
    }

}
