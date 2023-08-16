package dto.response;

import data.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class SendMessageResponse {
    Boolean flag;
    String error;
    Message message;
}
