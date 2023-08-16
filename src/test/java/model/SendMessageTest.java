package model;

import data.entity.Message;
import data.entity.User;
import data.enums.MessageType;
import dto.request.SendMessageRequest;
import dto.response.SendMessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.DataManager;

import java.util.List;

import static junit.framework.Assert.*;

public class SendMessageTest {
    private static final String RECEIVER_NOT_FOUND = "Receiver could not be found";
    private static final String SUCCESS = "Successfully sent the message";

    private static SendMessage sendMessage;
    private static DataManager dataManager;

    @BeforeAll
    public static void setUp() {
        dataManager = new DataManager();
        sendMessage = new SendMessage(dataManager);
    }

    @Test
    public void testReceiverDoesntExist() {
        String subject = "Test Subject";
        String text = "Test Text";
        User sender = dataManager.load(User.class)
                .query("SELECT * FROM user " +
                        "WHERE id = '2324347e-2f9c-4634-b588-bfde4ef1b5b9'")
                .getSingleResult();
        String receiverEmail = "test.test@oop_hr.com";
        MessageType messageType = MessageType.PERMIT;
        Boolean permission = true;

        SendMessageRequest request = SendMessageRequest.builder()
                .subject(subject)
                .text(text)
                .sender(sender)
                .receiverEmail(receiverEmail)
                .messageType(messageType)
                .permission(permission)
                .build();

        SendMessageResponse response = sendMessage.trySendingMessage(request);
        assertFalse(response.getFlag());
        assertEquals(RECEIVER_NOT_FOUND, response.getError());
        assertNull(response.getMessage());
    }

    @Test
    public void testSendMessage() {
        String subject = "Test Subject";
        String text = "Test Text";
        User sender = dataManager.load(User.class)
                .query("SELECT * FROM user " +
                        "WHERE id = '2324347e-2f9c-4634-b588-bfde4ef1b5b9'")
                .getSingleResult();
        String receiverEmail = "Giorgi.Tsereteli@oop_hr.com";
        MessageType messageType = MessageType.PERMIT;
        Boolean permission = true;

        SendMessageRequest request = SendMessageRequest.builder()
                .subject(subject)
                .text(text)
                .sender(sender)
                .receiverEmail(receiverEmail)
                .messageType(messageType)
                .permission(permission)
                .build();

        SendMessageResponse response = sendMessage.trySendingMessage(request);
        assertTrue(response.getFlag());
        assertEquals(SUCCESS, response.getError());
        assertNotNull(response.getMessage());

        Message message = response.getMessage();
        List<Message> allMessages = dataManager.load(Message.class)
                .query("SELECT * FROM message")
                .list();
        assertTrue(allMessages.contains(message));
    }
}
