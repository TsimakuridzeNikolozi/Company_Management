package controllers;

import data.entity.Person;
import data.entity.PersonDay;
import data.entity.User;
import dto.request.EditProfileRequest;
import dto.response.EditProfileResponse;
import model.EditProfileHandler;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfilePageServlet extends HttpServlet {
    private final EditProfileHandler editProfileHandler;
    public ProfilePageServlet() {this.editProfileHandler = new EditProfileHandler(new DataManager()); }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User activeUser = (User) request.getSession().getAttribute("user");
        request.getSession().setAttribute("errorMessage", null);
        request.getSession().setAttribute("successMessage", null);
        if (activeUser.isSupervisor()) {
            List<PersonDay> personDays =null; //  PersonDay data from the database
            request.setAttribute("personDays", personDays);
            request.getRequestDispatcher("/WEB-INF/attendanceControl.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/profilePage.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Get edit parameters from request
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String idNumber = request.getParameter("idNumber");
        String birthDate = request.getParameter("birthDate");
        System.out.println(birthDate);
        String gender = request.getParameter("gender");
        String phoneNumber = request.getParameter("phoneNumber");
        User user = (User) request.getSession().getAttribute("user");

//        System.out.println(firstName);
//        System.out.println(lastName);
//        System.out.println(idNumber);
//        System.out.println(birthDate);
//        System.out.println(gender);
//        System.out.println(phoneNumber);

        EditProfileRequest editProfileRequest = EditProfileRequest.builder()
                .user(user)
                .firstName(firstName)
                .lastName(lastName)
                .idNumber(idNumber)
                .gender(gender)
                .date(birthDate)
                .phoneNumber(phoneNumber)
                .build();

        EditProfileResponse editProfileResponse = editProfileHandler.handleEdit(editProfileRequest);
        if (!editProfileResponse.getSuccess()) {
            // Used for debugging
            System.out.println(editProfileResponse.getError());
            request.getSession().setAttribute("errorMessage", editProfileResponse.getError());
            request.getSession().setAttribute("successMessage", null);
            System.out.println("error!");
        } else {
            request.getSession().setAttribute("successMessage","Saved successfully");
            request.getSession().setAttribute("errorMessage", null);
            System.out.println("Success!");
        }

        request.getRequestDispatcher("/WEB-INF/profilePage.jsp").forward(request, response);
//        request.getRequestDispatcher("/WEB-INF/profilePage.jsp").forward(request, response);
//        String[] workedHoursArray = request.getParameterValues("workedHours");
//        List<PersonDay> personDays = new ArrayList<>();
    }
}
