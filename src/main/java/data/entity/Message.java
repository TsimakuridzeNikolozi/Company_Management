package data.entity;

import data.enums.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
public class Message extends BaseEntity {
    private String subject;
    private String text;
    private User sender;
    private User receiver;
    private MessageType messageType;
    private Timestamp timestamp;
}
