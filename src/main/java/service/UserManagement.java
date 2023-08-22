package service;

import data.entity.Person;
import data.entity.User;

import java.util.UUID;

public class UserManagement {

    private static final UUID HR_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static Boolean isHr(Person person) {
        return HR_ID.equals(person.getPost().getId());
    }
}
