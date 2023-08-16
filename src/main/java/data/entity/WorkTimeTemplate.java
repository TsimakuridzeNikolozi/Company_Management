package data.entity;

import data.enums.Direction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class WorkTimeTemplate extends BaseEntity {
    private Direction direction;
    private Boolean isControlled;
    private Long eventMinutes;
    private String typeName;
    private Person person;
}
