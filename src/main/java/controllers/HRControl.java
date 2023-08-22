package controllers;

import data.entity.Person;
import data.entity.PersonDocument;
import data.entity.User;
import data.enums.DocumentType;
import service.DataManager;
import service.UserManagement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Document;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@WebServlet("/HRControl")
public class HRControl extends HttpServlet {
    private static final int MAX_DAYS_BEFORE_NOTICE = 10;
    private final DataManager dataManager = new DataManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        if (!UserManagement.isHr(currentUser.getPerson())) {
            response.sendRedirect(request.getContextPath() + "/loginPage");
        } else {
            Map<Person, Integer> peopleWhoHaveBirthdays = getPeopleWhoHaveBirthdays();
            Map<Person, Integer> peopleWhoseContractsAreAboutToExpire = getPeopleWhoseContractsAreAboutToExpire();
            List<Person> peopleWhoDontHaveContracts = getPeopleWhoDontHaveContracts();

            request.setAttribute("birthdayPeople", peopleWhoHaveBirthdays);
            request.setAttribute("contractExpirePeople", peopleWhoseContractsAreAboutToExpire);
            request.setAttribute("noContractPeople", peopleWhoDontHaveContracts);
            request.getRequestDispatcher("/WEB-INF/hrControl.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/hrControl.jsp").forward(request, response);
    }


    /**
     * Method to get all people who have birthdays
     * @return Map of person and integer, person who has a birthday and his/her age
     */
    private Map<Person, Integer> getPeopleWhoHaveBirthdays() {
        Calendar calendar = Calendar.getInstance();
        List<Person> birthdayPeople =  dataManager.load(Person.class)
                .query("SELECT * FROM person " +
                        "WHERE MONTH(birth_date) = :currentDateMonth AND DAY(birth_date) = :currentDateDay")
                .parameter("currentDateMonth", calendar.get(Calendar.MONTH) + 1 + "")
                .parameter("currentDateDay", calendar.get(Calendar.DAY_OF_MONTH) + "")
                .list();

        Map<Person, Integer> peopleWhoHaveBirthdays = new HashMap<>();
        for(Person person: birthdayPeople) {
            String birthDateString = person.getBirthDate().toString();
            int age = (int)ChronoUnit.YEARS.between(LocalDate.parse(birthDateString), LocalDate.now());
            peopleWhoHaveBirthdays.put(person, age);
        }

        return peopleWhoHaveBirthdays;
    }

    /**
     * Method to get all people whose contracts are about to expire
     * @return Map of person and integer, person whose contract is about to expire and the number of days left
     *         before it expires
     */
    private Map<Person, Integer> getPeopleWhoseContractsAreAboutToExpire() {
        java.sql.Date currentDate = java.sql.Date.valueOf(LocalDate.now());
        List<PersonDocument> contractsAboutToExpire = dataManager.load(PersonDocument.class)
                .query("SELECT * FROM person_document " +
                        "WHERE document_type = :contract AND " +
                        "DATEDIFF(expiration_date, :currentDate) < :maxDaysBeforeNotice")
                .parameter("contract", DocumentType.CONTRACT.toString())
                .parameter("currentDate", currentDate)
                .parameter("maxDaysBeforeNotice", "" + MAX_DAYS_BEFORE_NOTICE)
                .list();

        Map<Person, Integer> peopleWhoseContractsAreAboutToExpire = new HashMap<>();
        for(PersonDocument contract: contractsAboutToExpire) {
            String expirationDateString = contract.getExpirationDate().toString();
            int daysBeforeExpiration = (int)ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(expirationDateString));
            peopleWhoseContractsAreAboutToExpire.put(contract.getPerson(), daysBeforeExpiration);
        }
        return peopleWhoseContractsAreAboutToExpire;
    }


    /**
     * Method to get all the people who don't have contracts
     * @return List
     */
    private List<Person> getPeopleWhoDontHaveContracts() {
        List<PersonDocument> allContracts = dataManager.load(PersonDocument.class)
                .query("SELECT * FROM person_document " +
                        "WHERE document_type = :contract")
                .parameter("contract", DocumentType.CONTRACT.toString())
                .list();

        List<Person> peopleWithContracts = new ArrayList<>();
        for(PersonDocument contract: allContracts)
            peopleWithContracts.add(contract.getPerson());

        List<Person> allPeople =  dataManager.load(Person.class)
                .query("SELECT * FROM person")
                .list();

        List<Person> peopleWhoDontHaveContracts = new ArrayList<>();
        for(Person person: allPeople) {
            if (!peopleWithContracts.contains(person))
                peopleWhoDontHaveContracts.add(person);
        }

        return peopleWhoDontHaveContracts;
    }
}
