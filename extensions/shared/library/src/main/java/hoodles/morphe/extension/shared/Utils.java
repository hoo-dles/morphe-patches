package hoodles.morphe.extension.shared;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("NewApi")
public class Utils {
    public static String smaliClassToJava(String clazz) {
        return clazz.substring(1, clazz.length() - 1).replace('/', '.');
    }

    private static final TimeZone utcTZ = TimeZone.getTimeZone("UTC");
    private static final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);
    public static String toIso8601(Date date) {
        iso8601Format.setTimeZone(utcTZ);
        return iso8601Format.format(date);
    }
}
