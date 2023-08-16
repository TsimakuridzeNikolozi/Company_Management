package controllers;

import dto.request.LoginUserRequest;
import dto.response.LoginUserResponse;
import model.LoginHandler;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final LoginHandler loginHandler;

    public LoginServlet() {this.loginHandler = new LoginHandler(new DataManager()); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward the request to the loginPage.jsp view.
        req.getRequestDispatcher("/WEB-INF/loginPage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve the username and password from the request parameters.
        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        LoginUserRequest loginUserRequest = LoginUserRequest.builder()
                .username(userName)
                .password(password)
                .build();

        LoginUserResponse loginUserResponse = loginHandler.handleLogin(loginUserRequest);

        if (!loginUserResponse.getSuccess()) {
            request.setAttribute("error", loginUserResponse.getError());
            request.getRequestDispatcher("/WEB-INF/loginPage.jsp").forward(request, response);
        }

        if (loginUserResponse.getSuccess()) {
            HttpSession session = request.getSession();

            session.setAttribute("user", loginUserResponse.getUser());

            response.sendRedirect(request.getContextPath() + "/profilePage");
        }
    }
}
