package io.kenji.seckill.infrastructure.utils.date;

import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-04
 **/
public class JodaDateTimeUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date parseStringToDate(String currentTime, String format) {
        return DateTimeFormat.forPattern(format).parseDateTime(currentTime).toDate();
    }

}
