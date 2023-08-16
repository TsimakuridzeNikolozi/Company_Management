package dto.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PersonDTO {
    private UUID personId;
    private String personName;

}
