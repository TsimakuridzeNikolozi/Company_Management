package model;

import dto.request.CreateUserRequest;
import dto.response.CreateUserResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.DataManager;

import java.util.UUID;

import static junit.framework.TestCase.*;

public class CreateUserTest {

    private static final String INVALID_EMAIL = "Provided email is not valid";
    private static final String USERNAME_AND_EMAIL_TAKEN = "User with the given username and email already exists";
    private static final String USERNAME_TAKEN = "User with the given username already exists";
    private static final String EMAIL_TAKEN = "User with the given email already exists";
    private static final String SUCCESS = "Successfully created a new user";



    private static CreateUser createUser;

    @BeforeAll
    public static void setUp() {
        createUser = new CreateUser(new DataManager());
    }

    @Test
    public void testEmailInvalid() throws Exception {
        String firstName = "Rolandi";
        String lastName = "Gagnidze";
        String userName = "Rolandi Gagnidze";
        String password = "examplePassword";
        String email = "RolandiGagnidze@oop-hr.com";
        UUID postId = UUID.fromString("20c33f12-a5fd-400b-8120-49cf0f89032f");

        CreateUserRequest request = new CreateUserRequest(firstName, lastName, userName, password, email, postId);
        CreateUserResponse response;

        // Email ending isn't correct, should return false and INVALID_EMAIL message
        response = createUser.tryCreatingUser(request);
        assertFalse(response.getFlag());
        assertEquals(INVALID_EMAIL, response.getMessage());
    }

    @Test
    public void testUserExists() throws Exception {
        String firstName = "Rolandi";
        String lastName = "Gagnidze";
        String userName = "Rolandi Gagnidze";
        String password = "examplePassword";
        String email = "Rolandi.Gagnidze@oop_hr.com";
        UUID postId = UUID.fromString("20c33f12-a5fd-400b-8120-49cf0f89032f");
        CreateUserRequest request = new CreateUserRequest(firstName, lastName, userName, password, email, postId);
        CreateUserResponse response;

        // When user with the given username and ID number already exists, corresponding exception should be thrown
        response = createUser.tryCreatingUser(request);
        assertFalse(response.getFlag());
        assertEquals(USERNAME_AND_EMAIL_TAKEN, response.getMessage());

        // When user with the given username already exists, corresponding exception should be thrown
        request.setEmail("RolandGagnidze@oop_hr.com");
        response = createUser.tryCreatingUser(request);
        assertFalse(response.getFlag());
        assertEquals(USERNAME_TAKEN, response.getMessage());

        // When user with the given id number already exists, corresponding exception should be thrown
        request.setUserName("Roland Gagnidze");
        request.setEmail("Rolandi.Gagnidze@oop_hr.com");
        response = createUser.tryCreatingUser(request);
        assertFalse(response.getFlag());
        assertEquals(EMAIL_TAKEN, response.getMessage());
    }


    @Test
    public void testCreateUser() throws Exception {
        String firstName = "Mock";
        String lastName = "User2";
        String userName = "Mock User2";
        String password = "examplePassword";
        String email = "MockUser2@oop_hr.com";
        UUID postId = UUID.fromString("20c33f12-a5fd-400b-8120-49cf0f89032f");
        CreateUserRequest request = new CreateUserRequest(firstName, lastName, userName, password, email, postId);
        CreateUserResponse response;

        // Everything is valid, user should be created
        response = createUser.tryCreatingUser(request);
        assertTrue(response.getFlag());
        assertEquals(SUCCESS, response.getMessage());
    }
}