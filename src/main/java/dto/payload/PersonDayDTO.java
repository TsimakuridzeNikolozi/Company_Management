package dto.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class PersonDayDTO {
    private UUID personDayId;
    private Date accountingDate;
    private Date startTime;
    private Date endTime;
}
