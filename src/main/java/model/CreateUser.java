package model;

import data.PasswordEncryptor;
import data.entity.Person;
import data.entity.Post;
import data.entity.TreeNode;
import data.entity.User;
import dto.request.CreateUserRequest;
import dto.response.CreateUserResponse;
import service.DataManager;

import java.util.UUID;

public class CreateUser {
    private static final String COMPANY_EMAIL_ENDING = "oop_hr.com";
    private static final String INVALID_EMAIL = "Provided email is not valid";
    private static final String USERNAME_AND_EMAIL_TAKEN = "User with the given username and email already exists";
    private static final String USERNAME_TAKEN = "User with the given username already exists";
    private static final String EMAIL_TAKEN = "User with the given email already exists";
    private static final String SUCCESS = "Successfully created a new user";

    private final DataManager dataManager;

    public CreateUser(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Facilitates the process of creating a new user
     * @param createUserRequest request parameter dto
     * @return always returns User object, if any problems arise exception is thrown
     */
    public CreateUserResponse tryCreatingUser(CreateUserRequest createUserRequest) throws Exception {
        String userName = createUserRequest.getUserName();
        String email = createUserRequest.getEmail();

        // Checking email
        if (!isValidEmail(email))
            return buildCreateUserResponse(false, INVALID_EMAIL, null);

        // Checking if the user with the given parameters already exists
        int userExistsCheck = userExists(userName, email);
        switch (userExistsCheck) {
            case 2: return buildCreateUserResponse(false, USERNAME_AND_EMAIL_TAKEN, null);
            case 1: return buildCreateUserResponse(false, USERNAME_TAKEN, null);
            case 0: return buildCreateUserResponse(false, EMAIL_TAKEN, null);
        }


        // Everything is valid
        // To create a user we need to have a person, and to have a person we need a post
        Post post = getPostWithId(createUserRequest.getPostId());
        Person person = createPerson(createUserRequest.getFirstName(), createUserRequest.getLastName(), post);
        String password = createUserRequest.getPassword();
        User user = createUser(userName, password, email, person);
        TreeNode treeNode = createTreeNode(person);

        // Storing a new person and a new user
        dataManager.save(person);
        dataManager.save(user);
        dataManager.save(treeNode);

        return buildCreateUserResponse(true, SUCCESS, user);
    }

    /**
     * Creates a new user based on the given parameters
     * @param userName username for a new user
     * @param password password for a new user
     * @param email email for a new user
     * @param person person object for a new user, user can not exist without a corresponding person
     * @return New User
     */
    private User createUser(String userName, String password, String email, Person person) {
        UUID uuid = UUID.randomUUID();
        String hashedPassword = PasswordEncryptor.hashPassword(password);

        return User.builder()
                .id(uuid)
                .userName(userName)
                .password(hashedPassword)
                .person(person)
                .email(email)
                .build();
    }


    /**
     * Checks if a user with either of the given parameters exists
     * @param userName Given username for a new user
     * @return Integer:  2: if both parameters are used; 1: if only username is used;
     *                   0: if only email is used; -1: if parameters haven't been used
     */
    private int userExists(String userName, String email) {
        User userWithUserName = dataManager.load(User.class)
                .query("SELECT * FROM user u" +
                        "    WHERE u.username = :userName")
                .parameter("userName", userName)
                .getSingleResult();

        User userWithEmail = dataManager.load(User.class)
                .query("SELECT * FROM user u" +
                        "    WHERE u.email = :email")
                .parameter("email", email)
                .getSingleResult();

        if (userWithUserName != null && userWithEmail != null) return 2;
        if (userWithUserName != null) return 1;
        if (userWithEmail != null) return 0;
        return -1;
    }


    /**
     * Searches for a post with the provided ID in a database and
     * creates and returns an instance of the post
     * @param postId ID of the post we are trying to get
     * @return Post object
     * @throws Exception Occurs when a post couldn't be found in the database
     */
    private Post getPostWithId(UUID postId) throws Exception {
        Post post = dataManager.load(Post.class)
                .query("SELECT * FROM post p " +
                        "WHERE p.id = :postId")
                .parameter("postId", postId)
                .getSingleResult();

        if (post != null) return post;
        throw new Exception("Could not find a post with the given post ID");
    }

    /**
     * Creates a person with the given parameters, used for creating a user
     * @param firstName Firstname of the person
     * @param lastName Lastname of the person
     * @param post Post which is occupied by the person
     * @return New Person instance
     */
    private Person createPerson(String firstName, String lastName, Post post) {
        UUID uuid = UUID.randomUUID();

        return Person.builder()
                .id(uuid)
                .firstName(firstName)
                .lastName(lastName)
                .post(post)
                .build();
    }

    /**
     * Checks if a provided email is valid (Belongs to the company)
     * @param email Given Email for a new user
     * @return boolean value representing if the email is valid
     */
    private boolean isValidEmail(String email) {
        String[] splitEmail = email.split("@");

        if (splitEmail.length != 2) return false;
        return splitEmail[1].equals(COMPANY_EMAIL_ENDING);
    }


    /**
     * Builds a CreateUserResponse instance with the given parameters
     * @param flag boolean representing the user creation result
     * @param message string describing the user creation result
     * @param user User entity, null if the creation failed
     * @return CreateUserResponse instance
     */
    private CreateUserResponse buildCreateUserResponse(boolean flag, String message, User user) {
        return CreateUserResponse.builder()
                .flag(flag)
                .message(message)
                .user(user)
                .build();
    }

    private TreeNode createTreeNode(Person person) {
        return TreeNode.builder()
                .id(UUID.randomUUID())
                .person(person)
                .build();
    }
}
