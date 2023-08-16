package dto.request;
import data.entity.Post;
import data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class EditUserRequest {
    public String personId;
    public String salary;
    public String postName;
}
