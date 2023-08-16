package dto.request;

import data.entity.User;
import data.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class SendMessageRequest {
    private String subject;
    private String text;
    private User sender;
    private String receiverEmail;
    private MessageType messageType;
    private Boolean permission;
    private Timestamp timestamp;
}
