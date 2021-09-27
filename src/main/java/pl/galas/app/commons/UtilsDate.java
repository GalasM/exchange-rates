package pl.galas.app.commons;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UtilsDate {

    /* The documentation specifies that the maximum time span is 93 days,
     but in response from the API, the maximum number of days is 367 */
    private static final int MAX_NUMBER_OF_DAYS = 367;

    public static String getDateYYYYMMDD(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(date);
    }

    public static boolean itExceedsNumberOfDays(Date startDate, Date endDate) {
        long numberOfDays = calculateDaysBetweenDates(startDate, endDate);
        return numberOfDays >= MAX_NUMBER_OF_DAYS;
    }

    public static Map<Date, Date> choppedDate(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            throw new IllegalArgumentException();
        Map<Date, Date> mapOfDates = new HashMap<>();
        if (itExceedsNumberOfDays(startDate, endDate)) {
            Date lastDate = startDate;
            Date nextDate = moveDate(Calendar.DAY_OF_YEAR, MAX_NUMBER_OF_DAYS, lastDate);
            if (nextDate != null && nextDate.before(endDate)) {
                mapOfDates.put(lastDate, nextDate);
            }
            while (nextDate != null && nextDate.before(endDate)) {
                lastDate = moveDate(Calendar.DAY_OF_YEAR, 1, nextDate);
                nextDate = moveDate(Calendar.DAY_OF_YEAR, MAX_NUMBER_OF_DAYS, lastDate);
                if (nextDate != null && nextDate.before(endDate)) {
                    mapOfDates.put(lastDate, nextDate);
                }
            }
            mapOfDates.put(lastDate, endDate);
        } else
            mapOfDates.put(startDate, endDate);

        return mapOfDates;
    }

    public static Date moveDate(int type, int value, Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(type, value);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date stringToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InvalidParameterException("Invalid date format. Should be yyyy/MM/dd");
        }
    }

    private static long calculateDaysBetweenDates(Date d1, Date d2) {
        long diffInMillies = Math.abs(d2.getTime() - d1.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
