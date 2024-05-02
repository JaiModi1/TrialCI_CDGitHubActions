package com.digitallending.userservice.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Integer getMonthDifference(Date date1, Date date2) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(date1);

        Calendar previousCalendar = Calendar.getInstance();
        previousCalendar.setTime(date2);

        return (currentCalendar.get(Calendar.YEAR) - previousCalendar.get(Calendar.YEAR)) * 12
                + Math.abs(currentCalendar.get(Calendar.MONTH) - previousCalendar.get(Calendar.MONTH));

    }
}
