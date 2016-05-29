package app.com.ttins.gettogether.common.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeFormat {

    private static final String TIME_DELIMITER = "[:]";
    private static final String DATE_DELIMITER = "[/]";

    private static final int HOURS_TIME_POSITION = 0;
    private static final int MINUTE_TIME_POSITION = 1;

    private static final int YEAR_DATE_POSITION = 0;
    private static final int MONTH_DATE_POSITION = 1;
    private static final int DAY_DATE_POSITION = 2;

    private static final int ERROR = -1;

    public static String convertTime(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public static String convertTime(String hours, String minutes) {

        if(hours == null || hours.isEmpty() || minutes == null || minutes.isEmpty()) {
            return "";
        }

        return String.format(Locale.getDefault(), "%02d:%02d", Integer.parseInt(hours), Integer.parseInt(minutes));
    }

    public static int getHoursFromTime(String time) {
        int hours;

        if (time != null && !time.isEmpty()) {
            String[] tokens = time.split(TIME_DELIMITER);
            hours = Integer.getInteger(tokens[HOURS_TIME_POSITION]);
        } else {
            hours = ERROR;
        }

        return hours;
    }

    public static String getStringHoursFromTime(String time) {
        String hours = "";

        if (time != null && !time.isEmpty() && time.compareTo(":") != 0) {
            String[] tokens = time.split(TIME_DELIMITER);
            hours = tokens[HOURS_TIME_POSITION];
        }

        return hours;
    }

    public static int getMinutesFromTime(String time) {
        int hours;

        if (time != null && !time.isEmpty()) {
            String[] tokens = time.split(TIME_DELIMITER);
            hours = Integer.getInteger(tokens[MINUTE_TIME_POSITION]);
        } else {
            hours = ERROR;
        }

        return hours;
    }

    public static String getStringMinutesFromTime(String time) {
        String minutes = "";

        if (time != null && !time.isEmpty() && time.compareTo(":") != 0) {
            String[] tokens = time.split(TIME_DELIMITER);
            minutes = tokens[MINUTE_TIME_POSITION];
        }

        return minutes;
    }

    public static String convertDate(int day, int month, int year) {
        String date;
        date = String.format(Locale.getDefault(), "%d/%02d/%02d", year, month, day);
        return date;
    }

    public static String convertDate(String day, String month, String year) {

        if (day == null || day.isEmpty() || month == null || month.isEmpty() ||
                year == null || year.isEmpty()) {
            return null;
        }

        return String.format(Locale.getDefault(), "%s/%02d/%02d",
                year, Integer.parseInt(month), Integer.parseInt(day));
    }

    public static int getDayFromDate(String date) {
        int day;

        if (date != null && !date.isEmpty()) {
            String[] tokens = date.split(DATE_DELIMITER);
            day = Integer.getInteger(tokens[DAY_DATE_POSITION]);
        } else {
            day = ERROR;
        }

        return day;
    }

    public static String getStringDayFromDate(String date) {
        String day = "";

        if (date != null && !date.isEmpty()) {
            String[] tokens = date.split(DATE_DELIMITER);
            day = tokens[DAY_DATE_POSITION];
        }

        return day;
    }

    public static int getMonthFromDate(String date) {
        int month;

        if (date != null && !date.isEmpty()) {
            String[] tokens = date.split(DATE_DELIMITER);
            month = Integer.getInteger(tokens[MONTH_DATE_POSITION]);
        } else {
            month = ERROR;
        }

        return month;
    }

    public static String getStringMonthFromDate(String date) {
        String month = "";

        if (date != null && !date.isEmpty()) {
            String[] tokens = date.split(DATE_DELIMITER);
            month = tokens[MONTH_DATE_POSITION];
        }

        return month;
    }

    public static int getYearFromDate(String date) {
        int year;
        if (date != null && !date.isEmpty()) {
            String[] tokens = date.split(DATE_DELIMITER);
            year = Integer.getInteger(tokens[YEAR_DATE_POSITION]);
        } else {
            year = ERROR;
        }

        return year;
    }

    public static String getStringYearFromDate(String date) {
        String year = "";

        if (date != null && !date.isEmpty()) {
            String[] tokens = date.split(DATE_DELIMITER);
            year = tokens[YEAR_DATE_POSITION];
        }

        return year;
    }

    public static String getCurrentTime(String timeformat){
        Format formatter = new SimpleDateFormat(timeformat);
        return formatter.format(new Date());
    }
}
