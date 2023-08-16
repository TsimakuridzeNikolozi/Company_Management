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

public class OutboxServlet extends HttpServlet {

    private final DataManager dataManager;

    public OutboxServlet() {
        this.dataManager = new DataManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User activeUser = (User) request.getSession().getAttribute("user");
        List<Message> sentMessages = getSentMessages(activeUser);
        request.setAttribute("sentMessages", sentMessages);
        request.getRequestDispatcher("/WEB-INF/outbox.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/outbox.jsp").forward(request, response);
    }

    private List<Message> getSentMessages(User user) {
        return dataManager.load(Message.class)
                .query("SELECT * FROM message WHERE sender_id = :userId ORDER BY timestamp DESC")
                .parameter("userId", user.getId())
                .list();
    }
}
