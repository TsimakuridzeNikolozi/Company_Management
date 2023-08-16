package data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@SuperBuilder
@Getter
@Setter
public class PersonDay extends BaseEntity {
    private Person person;
    private Date accountingDate;
    private Long startMinutes;
    private Long endMinutes;
    private Boolean holiday;
    private Boolean weekend;
    private Double workedHours;
}
