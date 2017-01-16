package com.twsela.driver.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 2/22/2015.
 */
public class DateUtils {

    public static Calendar convertToCalendar(String strDate, String strFormat) {
        try {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            final DateFormat df = new SimpleDateFormat(strFormat);
            calendar.setTime(df.parse(strDate));

            return calendar;
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertToString(Calendar calendar, String strFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat);
            String strDate = format.format(calendar.getTime());
            return strDate;
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDate(String strDate, String originalFormat, String desiredFormat) {
        return convertToString(convertToCalendar(strDate, originalFormat), desiredFormat);
    }

    public static String getDayName(String date, String dateFormat) {
        Calendar calendar = convertToCalendar(date, dateFormat);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayName = sdf.format(calendar.getTime());

        return dayName;
    }

    public static String getDayName(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayName = sdf.format(date.getTime());

        return dayName;
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        return convertToString(calendar, "hh:mm:ss");
    }

    public static boolean isCurrentDate(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        return (calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)
                && calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH))
                && calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR);
    }

    public static boolean isPastDate(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        return calendar.getTimeInMillis() < currentCalendar.getTimeInMillis();
    }

    public static boolean isToday(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        return currentCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getNewStringDate(String strDate, String strFormat, int daysToAdd) {
        Calendar calendar = convertToCalendar(strDate, strFormat);
        if (calendar != null) {
            calendar.add(Calendar.DATE, daysToAdd);
            return convertToString(calendar, strFormat);
        } else {
            return null;
        }
    }

    public static Calendar addDays(String strDate, String strFormat, int daysToAdd) {
        Calendar calendar = convertToCalendar(strDate, strFormat);
        if (calendar != null) {
            calendar.add(Calendar.DATE, daysToAdd);
            return calendar;
        } else {
            return null;
        }
    }

    public static Calendar addDays(int daysToAdd) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.DATE, daysToAdd);
        return calendar;
    }

    public static Calendar addHours(String strDate, String strFormat, int hoursToAdd) {
        Calendar calendar = convertToCalendar(strDate, strFormat);
        if (calendar != null) {
            calendar.add(Calendar.HOUR, hoursToAdd);
            return calendar;
        } else {
            return null;
        }
    }

    /**
     * method, used to compare two dates
     *
     * @param strDate1
     * @param strDate2
     * @param dateFormat
     * @return 1 if date1 is after date2, -1 if date1 is before date2 and 0 if the the dates are the same
     */
    public static int compare(String strDate1, String strDate2, String dateFormat) {
        Calendar calendar1 = convertToCalendar(strDate1, dateFormat);
        Calendar calendar2 = convertToCalendar(strDate2, dateFormat);

        return calendar1.compareTo(calendar2);
    }

    /**
     * method, used to get difference between two string dates in milliseconds
     *
     * @param strDate1
     * @param strDate2
     * @param dateFormat
     * @return
     */
    public static long getDifferenceInMillies(String strDate1, String strDate2, String dateFormat) {
        long calMillis1 = convertToCalendar(strDate1, dateFormat).getTimeInMillis();
        long calMillis2 = convertToCalendar(strDate2, dateFormat).getTimeInMillis();

        return calMillis2 - calMillis1;
    }

    /**
     * method, used to get difference between two string dates as values array
     *
     * @param strDate1
     * @param strDate2
     * @param dateFormat
     * @return
     */
    public static long[] getDifferenceArray(String strDate1, String strDate2, String dateFormat) {
        long differenceInMillies = getDifferenceInMillies(strDate1, strDate2, dateFormat);

        // prepare values
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        // days
        long elapsedDays = differenceInMillies / daysInMilli;
        differenceInMillies = differenceInMillies % daysInMilli;

        // hours
        long elapsedHours = differenceInMillies / hoursInMilli;
        differenceInMillies = differenceInMillies % hoursInMilli;

        // minutes
        long elapsedMinutes = differenceInMillies / minutesInMilli;
        differenceInMillies = differenceInMillies % minutesInMilli;

        // seconds
        long elapsedSeconds = differenceInMillies / secondsInMilli;

        // create the array
        long[] differenceArr = new long[]{elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
        return differenceArr;
    }
}
