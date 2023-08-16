package model;

import data.PasswordEncryptor;
import data.entity.User;
import dto.request.LoginUserRequest;
import dto.response.LoginUserResponse;
import service.DataManager;

public class LoginHandler {

    private final DataManager dataManager;

    public LoginHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public LoginUserResponse handleLogin(LoginUserRequest loginUserRequest) {
        User user = dataManager.load(User.class)
                .query("SELECT * FROM user WHERE username = :username")
                .parameter("username", loginUserRequest.getUsername())
                .getSingleResult();

        if (user == null) return userDoesNotExist();
        String inputPassword = PasswordEncryptor.hashPassword(loginUserRequest.getPassword());
        assert inputPassword != null;
        String realPassword = user.getPassword();

        if (!inputPassword.equals(realPassword)) return incorrectPassword();

        return success(user);
    }

    private LoginUserResponse userDoesNotExist() {
        return LoginUserResponse.builder()
                .success(false)
                .error("User with that username does not exist!")
                .build();
    }

    private LoginUserResponse incorrectPassword() {
        return LoginUserResponse.builder()
                .success(false)
                .error("Incorrect password!")
                .build();
    }

    private LoginUserResponse success(User user) {
        return LoginUserResponse.builder()
                .success(true)
                .user(user)
                .build();
    }

}
