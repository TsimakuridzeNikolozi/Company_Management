package dto.request;
import data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class EditProfileRequest {
    public User user;
    public String firstName;
    public String lastName;
    public String idNumber;
    public String date;
    public String gender;
    public String phoneNumber;
}
