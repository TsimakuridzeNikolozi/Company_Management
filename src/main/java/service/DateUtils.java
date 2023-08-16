package service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
//import java.util.GregorianCalendar;

public class DateUtils {
    private static final String[] HOLIDAYS = {"0101", "0102", "0107", "0119", "0303", "0308", "0409",
            "0509", "0512", "0526", "0828", "1014", "1123"};

    private DateUtils() {
    }

    public static long calculateCalendarDays(final Date startDate, final Date endDate) {
        if (startDate.compareTo(endDate) == 0 || startDate.after(endDate)) {
            return 0;
        }

        final LocalDate start = convertToLocalDate(startDate, false);
        final LocalDate end = convertToLocalDate(endDate, false);

        return ChronoUnit.DAYS.between(start, end);
    }

    public static long calculateWorkDays(final Date startDate, final Date endDate, boolean correctToUtcPlus4) {
        if (startDate.compareTo(endDate) == 0 || startDate.after(endDate)) {
            return 0;
        }

        final LocalDate start = convertToLocalDate(startDate, correctToUtcPlus4);
        final LocalDate end = convertToLocalDate(endDate, correctToUtcPlus4);

        return calculateWorkDays(start, end);
    }

    public static long calculateWorkDays(final LocalDate startDate, final LocalDate endDate) {
        if (startDate.isEqual(endDate) || startDate.isAfter(endDate)) {
            return 0;
        }

        LocalDate start = startDate;

        long result = isWorkday(start) ? 1 : 0;

        while ((start.plusDays(1).isBefore(endDate))) {
            start = start.plusDays(1);
            if (isWorkday(start)) {
                ++result;
            }
        }

        return result;
    }


    public static LocalDate convertToLocalDate(Date dateToConvert, boolean correctToUtcPlus4) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateToConvert);
        if (correctToUtcPlus4) {
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 20) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                dateToConvert = calendar.getTime();
            }
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate goodFriday(int year, boolean orthodox) {
        int easter = getEaster(year, orthodox);

        if (orthodox) {
            easter = easter + 13 - 2;
        } else {
            easter -= 2;
        }

        if (easter < 32) {
            //return new GregorianCalendar(year,2,easter);
            return LocalDate.of(year, 3, easter);
        }

        if (easter < 62) {
            //return new GregorianCalendar(year,3,easter-31);
            return LocalDate.of(year, 4, easter-31);
        }
        else {
            //return new GregorianCalendar(year,4,easter-61);
            return LocalDate.of(year, 5, easter-61);
        }
    }

    private static int getEaster(int year, boolean orthodox) {
        int K = year /100;
        int tmp1 = (3*K + 3)/4;
        int tmp2 = (8*K + 13)/25;
        int M , S = 0;

        if (!orthodox) {
            M = 15 + tmp1 - tmp2;
            S = 2 - tmp1;
        } else {
            M = 15;
        }

        int A = year % 19;
        int D = (19 * A + M) % 30;  //M=15
        tmp1 = D/29;
        tmp2 = D/28;
        int tmp3 = A/11;
        int R = tmp1 + (tmp2 - tmp1) * tmp3;
        int OG = 21 + D - R;
        tmp3 = year /4;
        int SZ = 7 - (year + tmp3 + S)%7;  //S=0
        int OE = 7 - (OG - SZ)%7;
        return OG + OE;
    }

    public static boolean isWeekend(final LocalDate date) {
        DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }

    public static boolean isHoliday(LocalDate date) {
        //String pattern = String.format("%02d%02d", date.get(Calendar.MONTH)+1,date.get(Calendar.DAY_OF_MONTH));

        String pattern = String.format("%02d%02d", date.getMonthValue(), date.getDayOfMonth());
        for (String holiday : HOLIDAYS) {
            if (pattern.equals(holiday)) {
                return true;
            }
        }

        //GregorianCalendar friday = DateUtils.goodFriday(date.get(Calendar.YEAR), true);
        LocalDate friday = DateUtils.goodFriday(date.getYear(), true);
        if (date.equals(friday)) {
            return true;
        }

        //friday.add(Calendar.DAY_OF_MONTH, 1);
        if (date.equals(friday.plusDays( 1))) {
            return true;
        }

        //friday.add(Calendar.DAY_OF_MONTH, 1);
        //Easter
        if (date.equals(friday.plusDays( 2))) {
            return true;
        }

        //friday.add(Calendar.DAY_OF_MONTH, 1);
        return date.equals(friday.plusDays( 3));
    }

    public static boolean isWorkday(LocalDate date) {
        return (!isHoliday(date) && !isWeekend(date));
    }

    public static Date adjustDate(Date date, Long minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Set the time of the day to 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long millisecondsToAdd = minutes * 60 * 1000; // Convert minutes to milliseconds
        long adjustedTimeInMillis = calendar.getTimeInMillis() + millisecondsToAdd;

        return new Date(adjustedTimeInMillis);
    }

    public static Long getMinutesFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return (long) (hours * 60 + minutes);
    }

    public static long calculateHoursBetweenDates(Date startDate, Date endDate) {
        long timeDifferenceInMillis = endDate.getTime() - startDate.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis);
        return hours;
    }

}