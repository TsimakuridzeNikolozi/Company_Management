package model;

import data.entity.Message;
import data.entity.Person;
import data.entity.ReverseTreeNode;
import data.entity.User;
import data.enums.MessageType;
import dto.request.SendMessageRequest;
import dto.response.SendMessageResponse;
import service.DataManager;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendMessage {
    private static final String COMPANY_EMAIL_ENDING = "oop_hr.com";
    private static final String RECEIVER_NOT_FOUND = "Receiver could not be found";
    private static final String SUCCESS = "Successfully sent the message";
    private static final String INVALID_EMAIL = "Provided email is not valid";
    private static final String WARNING_RECEIVER_INCORRECT = "Warning can only be sent to employees who work directly under you";

    private final DataManager dataManager;

    public SendMessage(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Attempts sending a message, checks if the parameters are correct, and builds and
     * saves the message object
     * @param request SendMessageRequest object containing all the info about the message
     * @return SendMessageResponse object describing the result
     */
    public SendMessageResponse trySendingMessage(SendMessageRequest request) {
        String subject = request.getSubject();
        String text = request.getText();
        User sender = request.getSender();
        String receiverEmail = request.getReceiverEmail();
        MessageType messageType = request.getMessageType();
        Timestamp timestamp = request.getTimestamp();

        if (!isValidEmail(receiverEmail))
            return buildSendMessageResponse(false, INVALID_EMAIL, null);

        if (!isValidMessage(receiverEmail, messageType, sender))
            return buildSendMessageResponse(false, WARNING_RECEIVER_INCORRECT, null);

        User receiver = getUserByEmail(request.getReceiverEmail());
        if (receiver == null)
            return buildSendMessageResponse(false, RECEIVER_NOT_FOUND, null);

        Message message = buildMessage(subject, text, sender,
                receiver, messageType, timestamp);

        dataManager.save(message);
        return buildSendMessageResponse(true, SUCCESS, message);
    }


    /**
     * @param user Current user
     * @return Mails of the children of the current user
     */
    public List<String> getChildrenEmails(User user) {
        List<Person> children = ReverseTreeNode.getChildren(user.getPerson());
        List<String> childrenEmails = new ArrayList<>();
        for(Person child: children)
            childrenEmails.add(getUserFromPersonId(child.getId()).getEmail());

        return childrenEmails;
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
     * Checks if the message is valid, Warning can only be sent to users branch
     * @param receiverEmail
     * @param messageType
     * @param sender
     * @return
     */
    private boolean isValidMessage(String receiverEmail, MessageType messageType, User sender) {
        if (messageType != MessageType.WARNING) return true;
        List<String> childrenEmails = getChildrenEmails(sender);
        return childrenEmails.contains(receiverEmail);
    }

    /**
     * Checks if a provided email is valid (Belongs to the company)
     * @param email Given Email for a new user
     * @return boolean value representing if the email is valid
     */
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        String[] splitEmail = email.split("@");

        if (splitEmail.length != 2) return false;
        return splitEmail[1].equals(COMPANY_EMAIL_ENDING);
    }

    /**
     * Searches and returns a User with the provided email (Emails are unique)
     * @param email Provided email string
     * @return User entity
     */
    private User getUserByEmail(String email) {
        return dataManager.load(User.class)
                .query("SELECT * FROM user " +
                        "WHERE email = :email")
                .parameter("email", email)
                .getSingleResult();
    }


    /**
     * Builds a message with the provided parameters
     * @param subject String
     * @param text String
     * @param sender User entity
     * @param receiver User entity
     * @param messageType MessageType enum
     * @return Message entity
     */
    private Message buildMessage(String subject, String text, User sender, User receiver,
                                 MessageType messageType, Timestamp timestamp) {
        return Message.builder()
                .id(UUID.randomUUID())
                .subject(subject)
                .text(text)
                .sender(sender)
                .receiver(receiver)
                .messageType(messageType)
                .timestamp(timestamp)
                .build();
    }

    /**
     * Builds a SendMessageResponse object with the provided parameters
     * @param flag boolean
     * @param error String
     * @param message Message entity
     * @return SendMessageResponse object
     */
    private SendMessageResponse buildSendMessageResponse(boolean flag, String error, Message message) {
        return SendMessageResponse.builder()
                .flag(flag)
                .error(error)
                .message(message)
                .build();
    }
}
