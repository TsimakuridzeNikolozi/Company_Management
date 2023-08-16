package service;

import junit.framework.TestCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest extends TestCase {

    @SneakyThrows
    @Test
    public void testAdjustDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = sdf.parse("2023-08-12 10:00");
        Long minutes = 660L;

        Date adjustedDate = DateUtils.adjustDate(date, minutes);

        Calendar cal = Calendar.getInstance();
        cal.setTime(adjustedDate);

        Assertions.assertEquals(11, cal.get(Calendar.HOUR_OF_DAY));
        Assertions.assertEquals(0, cal.get(Calendar.MINUTE));
        Assertions.assertEquals(2023, cal.get(Calendar.YEAR));
        Assertions.assertEquals(7, cal.get(Calendar.MONTH));
        Assertions.assertEquals(12, cal.get(Calendar.DAY_OF_MONTH));
    }

    @SneakyThrows
    @Test
    public void testHoursBetween() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = sdf.parse("2023-08-12 10:00");
        Date endDate = sdf.parse("2023-08-12 17:00");
        Date endDate1 = sdf.parse("2023-08-12 19:00");

        Assertions.assertEquals(DateUtils.calculateHoursBetweenDates(startDate, endDate), 7);
        Assertions.assertEquals(DateUtils.calculateHoursBetweenDates(startDate, endDate1), 9);
    }
}