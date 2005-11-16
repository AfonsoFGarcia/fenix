package net.sourceforge.fenixedu.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateFormatUtil {

    private static final Map<String, DateFormat> dateFormatMap = new HashMap<String, DateFormat>();

    public static DateFormat dateFormat(final String format) {
        final DateFormat dateFormat;
        if (!dateFormatMap.containsKey(format)) {
            dateFormat = new SimpleDateFormat(format);
            dateFormatMap.put(format, dateFormat);
        } else {
            dateFormat = dateFormatMap.get(format);
        }
        return dateFormat;
    }

    public static Date parse(final String format, final String dateString) throws ParseException {
        return dateFormat(format).parse(dateString);
    }

    public static String format(final String format, final Date date) {
        return dateFormat(format).format(date);
    }

    public static boolean equalDates(final String format, final Date date1, final Date date2) {
        final DateFormat dateFormat = dateFormat(format);
        return dateFormat.format(date1).equals(dateFormat.format(date2));
    }

}
