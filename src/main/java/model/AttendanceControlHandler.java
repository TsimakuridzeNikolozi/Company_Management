package model;

import data.entity.Person;
import data.entity.PersonDay;
import dto.request.LogTimeRequest;
import dto.response.LogTimeResponse;
import service.DataManager;
import service.DateUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class AttendanceControlHandler {

    private final DataManager dataManager;
    public AttendanceControlHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public LogTimeResponse logPersonTime(LogTimeRequest logTimeRequest) {
        try {
            Date accountingDate = logTimeRequest.getAccountingDate();
            LocalDate accountingDateAsLocalDate = DateUtils.convertToLocalDate(accountingDate, false);
            Date startDate = logTimeRequest.getStartDate();
            Date endDate = logTimeRequest.getEndDate();

            if (startDate.after(endDate)) return formResponse(false, "Start date cant be before end date");
            if (DateUtils.calculateHoursBetweenDates(startDate, endDate) > 24) return formResponse(false, "Its impossible to work more than 24 hours");

            logTime(logTimeRequest.getPersonId(),
                    accountingDate,
                    DateUtils.getMinutesFromDate(startDate),
                    DateUtils.getMinutesFromDate(endDate),
                    DateUtils.isHoliday(accountingDateAsLocalDate),
                    DateUtils.isWeekend(accountingDateAsLocalDate));

            return formResponse(true, null);
        } catch (Exception e) {
            return formResponse(false, e.getLocalizedMessage());
        }
    }

    private void logTime(UUID personId, Date accountingDate, Long startMinutes, Long endMinutes, Boolean holiday, Boolean weekend) {
        PersonDay existingPersonDay = getPersonDayForPerson(personId, accountingDate);
        PersonDay personDay = PersonDay.builder()
                .id(existingPersonDay == null ? UUID.randomUUID() : existingPersonDay.getId())
                .person(dataManager.load(Person.class).id(personId).getSingleResult())
                .accountingDate(accountingDate)
                .startMinutes(startMinutes)
                .endMinutes(endMinutes)
                .holiday(holiday)
                .weekend(weekend)
                .build();

        dataManager.save(personDay);
    }

    private LogTimeResponse formResponse(Boolean success, String error) {
        return LogTimeResponse.builder()
                .success(success)
                .error(error)
                .build();
    }

    private PersonDay getPersonDayForPerson(UUID personId, Date date) {
        try {
            return dataManager.load(PersonDay.class)
                    .query("SELECT * FROM person_day" +
                            " WHERE person_id = :personId" +
                            " AND accounting_date = :date")
                    .parameter("personId", personId)
                    .parameter("date", date)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
