package parkmania.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.FRENCH);

    public static String format(LocalDateTime date) {
        return (date != null) ? date.format(FORMATTER) : "";
    }
}
