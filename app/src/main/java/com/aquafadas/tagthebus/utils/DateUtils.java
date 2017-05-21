package com.aquafadas.tagthebus.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Oussama on 21/05/2017.
 */

public class DateUtils {

    private static final String SIMPLE_SHORT_DATE_PATTERN = "dd/MM/yyyy HH:mm";

    public static String getDateFromTimeStamp(long aTimeStamp) {
        String result;
        DateFormat formatDate = new SimpleDateFormat(SIMPLE_SHORT_DATE_PATTERN);
        Date netDate = (new Date(aTimeStamp));
        result = formatDate.format(netDate);
        return result;
    }
}
