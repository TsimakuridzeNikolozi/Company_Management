package dto.request;

import data.entity.Person;
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
public class TimeRequest {
    String numDays;
    UUID personId;
    Date date;
}
