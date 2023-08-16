package dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private UUID postId;
}
