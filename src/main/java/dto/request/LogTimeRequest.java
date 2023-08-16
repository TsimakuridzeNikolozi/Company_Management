package dto.request;

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
public class LogTimeRequest {
    private UUID personDayId;
    private UUID personId;
    private Date accountingDate;
    private Date startDate;
    private Date endDate;
}
