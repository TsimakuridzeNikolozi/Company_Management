package controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Perform logout logic (e.g., invalidate session, clear authentication data)
        HttpSession session = request.getSession();
        session.invalidate();
        // Redirect to loginPage.jsp
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/loginPage.jsp");
//        dispatcher.forward(request, response);
        response.sendRedirect(request.getContextPath() + "/loginPage");
    }
}
