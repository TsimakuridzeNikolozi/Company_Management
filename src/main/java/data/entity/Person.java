package data.entity;

import data.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@SuperBuilder
@Getter
@Setter
public class Person extends BaseEntity {
    private String firstName;
    private String lastName;
    private String idNumber;
    private Date birthDate;
    private Gender gender;
    private BigDecimal salary;
    private Date hireDate;
    private Post post;
    private String phoneNumber;
}
