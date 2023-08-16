package model;
import data.entity.Person;
import data.entity.PersonDay;
import dto.payload.PersonDTO;
import dto.payload.PersonDayDTO;
import dto.request.TimeRequest;
import dto.response.TimeResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.DataManager;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
public class TimeHandlerTest {
    private static DataManager dataManager;
    private static TimeHandler timeHandler;

    private static final int TEST_NUMBER_DAYS = 7;
    private static final int TEST_CALENDAR_YEAR = 2023;
    private static final int TEST_CALENDAR_DAY_OF_MONTH = 13;
    private static final int TEST_CALENDAR_MONTH = 7;

    @BeforeAll
    public static void setUp() {
        dataManager = new DataManager();
        timeHandler = new TimeHandler(dataManager);
    }

    @Test
    public void testHandleTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, TEST_CALENDAR_YEAR);
        calendar.set(Calendar.MONTH, TEST_CALENDAR_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, TEST_CALENDAR_DAY_OF_MONTH);

        java.sql.Date sqlDate = new java.sql.Date(calendar.getTimeInMillis());

        Person person = dataManager.load(Person.class)
                .query("SELECT * FROM person " +
                        "WHERE id = '060e3f96-302f-4f81-aff7-0fa1437dfb81'")
                .getSingleResult();

        TimeRequest request = TimeRequest.builder()
                .date(sqlDate)
                .numDays(String.valueOf(TEST_NUMBER_DAYS))
                .personId(person.getId())
                .build();

        TimeResponse response = timeHandler.handleTime(request);

        List<PersonDayDTO> currPersonTime = response.getPersonDayList();

        PersonDay firstDay = dataManager.load(PersonDay.class)
                .query("SELECT * FROM person_day " +
                        "WHERE id = '447a7242-393b-11ee-a3b3-325096b39f47'")
                .getSingleResult();

        assertEquals(firstDay,currPersonTime.get(0));

        PersonDay secondDay = dataManager.load(PersonDay.class)
                .query("SELECT * FROM person_day " +
                        "WHERE id = '447a7288-393b-11ee-b33d-325096b39f47'")
                .getSingleResult();

        assertEquals(secondDay,currPersonTime.get(1));

        PersonDay seventhDay = dataManager.load(PersonDay.class)
                .query("SELECT * FROM person_day " +
                        "WHERE id = '447a73c8-393b-11ee-94e3-325096b39f47'")
                .getSingleResult();

        assertEquals(seventhDay,currPersonTime.get(6));

        Map<PersonDTO,List<PersonDayDTO>> actualMap = response.getMap();

        Person firstChild = dataManager.load(Person.class)
                .query("SELECT * FROM person " +
                        "WHERE id = '3052652c-3846-4642-b037-435357d8d2ce'")
                .getSingleResult();

        assertEquals(true,actualMap.containsKey(firstChild));
        assertEquals(2,actualMap.size());

        List<PersonDayDTO> firstChildList = actualMap.get(firstChild);

        PersonDay firstChildTimeDay1 = dataManager.load(PersonDay.class)
                .query("SELECT * FROM person_day " +
                        "WHERE id = '447a706c-393b-11ee-abe8-325096b39f47'")
                .getSingleResult();

        assertEquals(firstChildTimeDay1,firstChildList.get(0));

        PersonDay firstChildTimeDay7 = dataManager.load(PersonDay.class)
                .query("SELECT * FROM person_day " +
                        "WHERE id = '447a7206-393b-11ee-ae58-325096b39f47'")
                .getSingleResult();

        assertEquals(firstChildTimeDay7,firstChildList.get(6));

        Person secondChild = dataManager.load(Person.class)
                .query("SELECT * FROM person " +
                        "WHERE id = '0aadb2b9-6837-421a-8e75-f88d04978945'")
                .getSingleResult();

        List<PersonDayDTO> secondChildList = actualMap.get(secondChild);

        PersonDay secondChildTimeDay1 = dataManager.load(PersonDay.class)
                .query("SELECT * FROM person_day " +
                        "WHERE id = '447a7418-393b-11ee-8fbb-325096b39f47'")
                .getSingleResult();

        assertEquals(secondChildTimeDay1,secondChildList.get(0));

        PersonDay secondChildTimeDay7 = dataManager.load(PersonDay.class)
                .query("SELECT * FROM person_day " +
                        "WHERE id = '447a7648-393b-11ee-8720-325096b39f47'")
                .getSingleResult();

        assertEquals(secondChildTimeDay7,secondChildList.get(6));
    }
}
