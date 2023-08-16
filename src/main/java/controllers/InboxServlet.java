package controllers;

import data.entity.Message;
import data.entity.User;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class InboxServlet extends HttpServlet {

    private final DataManager dataManager;

    public InboxServlet() {
        this.dataManager = new DataManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User activeUser = (User) request.getSession().getAttribute("user");
        List<Message> myMessages = getMyMessages(activeUser);
        request.setAttribute("myMessages", myMessages);
        request.getRequestDispatcher("/WEB-INF/inbox.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/inbox.jsp").forward(request, response);
    }

    private List<Message> getMyMessages(User user) {
        return dataManager.load(Message.class)
                .query("SELECT * FROM message WHERE receiver_id = :userId ORDER BY timestamp DESC")
                .parameter("userId", user.getId())
                .list();
    }
}
