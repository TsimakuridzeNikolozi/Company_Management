package controllers;

import data.entity.Person;
import data.entity.User;
import dto.payload.PersonDTO;
import dto.payload.PersonDayDTO;
import dto.request.LogTimeRequest;
import dto.request.TimeRequest;
import dto.response.LogTimeResponse;
import dto.response.TimeResponse;
import lombok.SneakyThrows;
import model.AttendanceControlHandler;
import model.TimeHandler;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@WebServlet("/attendanceControl")
public class AttendanceControlServlet extends HttpServlet {
    private final DataManager dataManager = new DataManager();
    private static final int days = 7;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        Person person = user.getPerson();

        LocalDate currentDate = LocalDate.now();
        // Convert LocalDate to Date
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        TimeRequest timeRequest = TimeRequest.builder()
                .numDays(String.valueOf(days))
                .personId(person.getId())
                .date(date)
                .build();

        TimeHandler timeHandler = new TimeHandler(dataManager);
        TimeResponse timeResponse = timeHandler.handleTime(timeRequest);

        PersonDTO name = timeResponse.getPerson();
        List<PersonDayDTO> personDayList = timeResponse.getPersonDayList();
        Map<PersonDTO, List<PersonDayDTO>> map = timeResponse.getMap();
        request.setAttribute("name", name);
        request.setAttribute("dayList", personDayList);
        request.setAttribute("map", map);


        request.getRequestDispatcher("/WEB-INF/attendanceControl.jsp").forward(request, response);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String personId = request.getParameter("personId");
        String dayDate = request.getParameter("dayDate");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dayDate);

        LogTimeRequest logTimeRequest = LogTimeRequest.builder()
                .personId(UUID.fromString(personId))
                .accountingDate(date)
//                .startDate(startTime)
//                .endDate(endTime)
                .build();
        AttendanceControlHandler attendanceControlHandler = new AttendanceControlHandler(dataManager);
        LogTimeResponse logTimeResponse = attendanceControlHandler.logPersonTime(logTimeRequest);
        request.setAttribute("message", (String) logTimeResponse.getError());


        request.getRequestDispatcher("/WEB-INF/attendanceControl.jsp").forward(request, response);
    }

}