package model;

import data.entity.Person;
import data.entity.User;
import data.enums.Gender;
import dto.request.EditProfileRequest;
import dto.response.EditProfileResponse;
import service.DataManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileHandler {

    private static final String FIRST_NAME_ERROR = "Invalid first name. Enter a non empty, alphabetical word";
    private static final String LAST_NAME_ERROR = "Invalid last name. Enter a non empty, alphabetical word";
    private static final String ID_NUMBER_ERROR = "Invalid ID number. Enter a non empty number";
    private static final String PHONE_NUMBER_ERROR = "Invalid phone number. Enter a non empty number";

    private static final String BIRTH_DATE_ERROR = "Invalid birth date";
    private final DataManager dataManager;

    public EditProfileHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public EditProfileResponse handleEdit(EditProfileRequest request) {
        if(!isAlphabeticalString(request.getFirstName())) {
            return EditProfileResponse.builder().success(false).error(FIRST_NAME_ERROR).build();
        }
        if(!isAlphabeticalString(request.getLastName())) {
            return EditProfileResponse.builder().success(false).error(LAST_NAME_ERROR).build();
        }
        if(!isNumericString(request.getIdNumber())) {
            return EditProfileResponse.builder().success(false).error(ID_NUMBER_ERROR).build();
        }
        if(!isNumericString(request.getPhoneNumber())) {
            return EditProfileResponse.builder().success(false).error(PHONE_NUMBER_ERROR).build();
        }
        if(!isValidYearDate(request.getDate())) {
            return EditProfileResponse.builder().success(false).error(BIRTH_DATE_ERROR).build();
        }

        // Get user and person that need to be updated
        User user = request.getUser();
        Person person = user.getPerson();

        // Update each attribute
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setIdNumber(request.getIdNumber());
        person.setPhoneNumber(request.getPhoneNumber());
        person.setBirthDate(formatDate(request.getDate()));

        if(request.getGender().equals("male")) {
            person.setGender(Gender.MALE);
        } else if(request.getGender().equals("female")) {
            person.setGender(Gender.FEMALE);
        }
        dataManager.save(person);
        dataManager.save(user);
        return EditProfileResponse.builder().success(true).build();
    }

    private java.sql.Date formatDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDayUtil;
        java.sql.Date birthDate = null;
        try {
            birthDayUtil = dateFormat.parse(dateString);
            birthDate = new java.sql.Date(birthDayUtil.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse exception as needed
        }
        return birthDate;
    }
    private boolean isValidYearDate(String dateStr) {
        try {
            // Split the date string into parts
            String[] parts = dateStr.split("-");

            // Ensure the correct number of parts
            if (parts.length != 3) {
                return false;
            }

            // Parse the year part and check the range
            int year = Integer.parseInt(parts[0]);
            if (year < 1900 || year > 2100) {
                return false;
            }

            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Handle parsing errors or incorrect format
            return false;
        }
    }
    private boolean isNumericString(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }
    private boolean isAlphabeticalString(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        for (char c : input.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
}
