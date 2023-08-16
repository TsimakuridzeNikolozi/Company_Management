package model;

import data.entity.Person;
import data.entity.Post;
import data.entity.User;
import data.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class ActiveUser {
    User user;
    Person person;
    public ActiveUser(HttpServletRequest request) {
        user = (User)request.getSession().getAttribute("user");
        person = user.getPerson();
    }

    public String getFirstName() {
        return person.getFirstName();
    }

    public String getLastName() {
        return person.getLastName();
    }

    public String getIdNumber() {
        String idNumber = person.getIdNumber();
        if(idNumber==null) {
            return "ID number not set";
        } else return person.getIdNumber();
    }

    public String getBirthDate() {
        Date birthDate = person.getBirthDate();
        if (birthDate==null) {
            return "Birth date not set";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(birthDate);
        }
    }

    public String getGender() {
        Gender gender = person.getGender();
        if(gender==null) {
            return "Gender not set";
        } else return gender.toString();
    }

    public String getSalary() {
        BigDecimal salary = person.getSalary();
        if(salary==null) {
            return "Salary not set";
        } else return "$"+salary;
    }

    public String getHireDate() {
        Date hireDate = person.getHireDate();
        if (hireDate==null) {
            return "Hire date not set";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(hireDate);
        }
    }

    public String getPost() {
        Post post = person.getPost();
        return post.getPostName();
    }

    public String getPhoneNumber() {
        String phoneNumber = person.getPhoneNumber();
        if(phoneNumber==null) {
            return "Phone number not set";
        } else return phoneNumber;
    }
}
