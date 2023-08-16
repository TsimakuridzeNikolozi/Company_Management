package controllers;

import data.entity.*;
import dto.request.SendMessageRequest;
import dto.response.SendMessageResponse;
import data.enums.MessageType;
import model.SendMessage;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/composeMailServlet")
public class ComposeMailServlet extends HttpServlet {
    private final static String APPROVED_RESPONSE = "Permission Granted";
    private final static String DENIED_RESPONSE = "Permission denied";
    private final static String UNDERSTOOD_RESPONSE = "Understood the warning";

    private final SendMessage sendMessage;
    private final DataManager dataManager;

    public ComposeMailServlet() {
        this.dataManager = new DataManager();
        this.sendMessage = new SendMessage(dataManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User)request.getSession().getAttribute("user");
        String parentEmail = getParentEmail(user);
        request.setAttribute("user", user);
        request.setAttribute("parentEmail", parentEmail);
        request.getRequestDispatcher("/WEB-INF/composeMail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subject, text, receiverEmail, messageTypeString;
        MessageType messageType;
        Timestamp timestamp;

        // When response is not null, it means request to send mail was sent to respond to permit or warning
        // When response is null, it means that it's a regular request from composeMail jsp
        if (request.getParameter("response") != null) {
            String messageId = request.getParameter("formMessageId");
            deleteMessage(UUID.fromString(messageId));
            subject = "Response to: (" + request.getParameter("formSubject") + ")";

            String button = request.getParameter("response");
            if (button.equals("Approve"))
                text = APPROVED_RESPONSE;
            else if (button.equals("Deny"))
                text = DENIED_RESPONSE;
            else
                text = UNDERSTOOD_RESPONSE;

            receiverEmail = request.getParameter("formSenderEmail");
            messageType = MessageType.NOTIFICATION;
        } else {
            subject = request.getParameter("subject");
            text = request.getParameter("text");
            receiverEmail = request.getParameter("receiverEmail");
            messageTypeString = request.getParameter("email-type").toUpperCase();
            if (messageTypeString.equals("PERMISSION")) messageType = MessageType.PERMIT;
            else messageType = MessageType.parseString(messageTypeString);
        }

        timestamp = Timestamp.valueOf(LocalDateTime.now());
        User sender = (User) request.getSession().getAttribute("user");

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .subject(subject)
                .text(text)
                .sender(sender)
                .receiverEmail(receiverEmail)
                .messageType(messageType)
                .timestamp(timestamp)
                .build();

        SendMessageResponse sendMessageResponse = sendMessage.trySendingMessage(sendMessageRequest);

        // forward the response object to the correct view (result.jsp)
        request.setAttribute("error", sendMessageResponse.getError());
        request.setAttribute("flag", sendMessageResponse.getFlag());
        User user = (User)request.getSession().getAttribute("user");
        String parentEmail = getParentEmail(user);
        request.setAttribute("user", user);
        request.setAttribute("parentEmail", parentEmail);

        if (request.getParameter("response") != null)
            response.sendRedirect(request.getContextPath() + "/inbox");
        else
            request.getRequestDispatcher("/WEB-INF/composeMail.jsp").forward(request, response);
    }


    /**
     * Searches for the parent of the current user and returns his/her email
     * @param user Current user
     * @return String email, null if user doesn't have a parent
     */
    public String getParentEmail(User user) {
        UUID personId = user.getPerson().getId();
        TreeNode personNode = dataManager.load(TreeNode.class)
                .query("SELECT * FROM tree_node " +
                        "WHERE person_id = :personId")
                .parameter("personId", personId)
                .getSingleResult();

        if (personNode == null) return null;
        if (personNode.getParentNode() == null) return null;

        UUID parentId = personNode.getParentNode().getPerson().getId();
        User parentUser = getUserFromPersonId(parentId);
        if (parentUser == null) return null;
        return parentUser.getEmail();
    }


    /**
     * @param personId ID of the person whose user entity we are searching for
     * @return User object of the corresponding person
     */
    public User getUserFromPersonId(UUID personId) {
        return dataManager.load(User.class)
                .query("SELECT * FROM user " +
                        " WHERE person_id = :personId")
                .parameter("personId", personId)
                .getSingleResult();
    }

    /**
     * Deletes the message after it has been responded to
     * @param messageId ID of the message which was responded to
     */
    public void deleteMessage(UUID messageId) {
        dataManager.load(Message.class)
                .query("DELETE FROM message " +
                        "WHERE id = :messageId")
                .parameter("messageId", messageId)
                .execute();
    }
}
