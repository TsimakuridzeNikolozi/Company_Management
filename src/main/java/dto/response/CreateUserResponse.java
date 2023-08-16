package dto.response;

import data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateUserResponse {
    private Boolean flag;
    private String message;
    private User user;
}
