package model;


import static junit.framework.TestCase.*;

import data.entity.PersonDay;
import dto.request.LogTimeRequest;
import dto.response.LogTimeResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.DataManager;
import service.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AttendanceControlHandlerTest {

    private static AttendanceControlHandler attendanceControlHandler;

    private static DataManager dataManager;
    @BeforeAll
    public static void setUp() {
        dataManager = new DataManager();
        attendanceControlHandler = new AttendanceControlHandler(dataManager);
    }

    @SneakyThrows
    @Test
    public void testLogPersonTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = sdf.parse("2023-08-05 10:00");
        Date endDate = sdf.parse("2023-08-05 18:00");
        UUID personDayId = UUID.randomUUID();
        attendanceControlHandler.logPersonTime(LogTimeRequest.builder()
                        .personDayId(personDayId)
                        .personId(UUID.fromString("060e3f96-302f-4f81-aff7-0fa1437dfb81"))
                        .accountingDate(startDate)
                        .startDate(startDate)
                        .endDate(endDate)
                .build());

        Assertions.assertEquals(dataManager.load(PersonDay.class).id(personDayId).getSingleResult().getStartMinutes(), DateUtils.getMinutesFromDate(startDate));
    }

    @SneakyThrows
    @Test
    public void testLogPersonTime1() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = sdf.parse("2023-08-05 11:00");
        Date endDate = sdf.parse("2023-08-05 18:00");
        UUID personDayId = UUID.fromString("262df93d-11ba-49c0-9529-9e5d86f069bb");
        attendanceControlHandler.logPersonTime(LogTimeRequest.builder()
                .personDayId(personDayId)
                .personId(UUID.fromString("060e3f96-302f-4f81-aff7-0fa1437dfb81"))
                .accountingDate(startDate)
                .startDate(startDate)
                .endDate(endDate)
                .build());

        PersonDay singleResult = dataManager.load(PersonDay.class).id(personDayId).getSingleResult();
        Assertions.assertEquals(singleResult.getStartMinutes(), DateUtils.getMinutesFromDate(startDate));
        Assertions.assertEquals(singleResult.getWeekend(), true);
    }

    @SneakyThrows
    @Test
    public void testLogPersonTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = sdf.parse("2023-05-26 10:00");
        Date endDate = sdf.parse("2023-05-26 18:00");
        UUID personDayId = UUID.randomUUID();
        attendanceControlHandler.logPersonTime(LogTimeRequest.builder()
                .personDayId(personDayId)
                .personId(UUID.fromString("060e3f96-302f-4f81-aff7-0fa1437dfb81"))
                .accountingDate(startDate)
                .startDate(startDate)
                .endDate(endDate)
                .build());

        PersonDay singleResult = dataManager.load(PersonDay.class).id(personDayId).getSingleResult();
        Assertions.assertEquals(singleResult.getStartMinutes(), DateUtils.getMinutesFromDate(startDate));
        Assertions.assertEquals(singleResult.getHoliday(), true);
    }

    @SneakyThrows
    @Test
    public void testLogPersonTime3() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = sdf.parse("2023-08-04 10:00");
        Date endDate = sdf.parse("2023-08-05 18:00");
        UUID personDayId = UUID.randomUUID();
        LogTimeResponse logTimeResponse = attendanceControlHandler.logPersonTime(LogTimeRequest.builder()
                .personDayId(personDayId)
                .personId(UUID.fromString("060e3f96-302f-4f81-aff7-0fa1437dfb81"))
                .accountingDate(startDate)
                .startDate(startDate)
                .endDate(endDate)
                .build());

        Assertions.assertEquals(logTimeResponse.getSuccess(), false);
        Assertions.assertEquals(logTimeResponse.getError(), "Its impossible to work more than 24 hours");
    }
}