package dto.response;

import data.entity.Person;
import data.entity.PersonDay;
import dto.payload.PersonDTO;
import dto.payload.PersonDayDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class TimeResponse {
    PersonDTO person;
    List<PersonDayDTO> personDayList;
    Map<PersonDTO, List<PersonDayDTO>> map;
}
