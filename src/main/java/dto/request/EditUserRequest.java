package dto.request;
import data.entity.Post;
import data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class EditUserRequest {
    private UUID personId;
    private String salary;
    private String postName;
    private String headUsername;
    private Boolean isHr;
}
