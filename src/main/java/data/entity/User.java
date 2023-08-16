package data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class User extends BaseEntity {
    private String userName;
    private String password;
    private String email;
    private Person person;

    public boolean isSupervisor() {
        return false;
    }
}
