package model;

import data.entity.Person;
import data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import service.DataManager;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class SearchHandler {

    private final DataManager dataManager;

    String firstName;
    String lastName;
    String email;
    String idNumber;
    String phoneNumber;
    String position;

    public HashMap<Person, String> getResultMap() {
        HashMap<Person, String> map = new HashMap<>();
        List<Person> personList = getPersonWithParameters();
        for (Person person : personList) {
            User user = dataManager.load(User.class)
                    .query("SELECT * FROM user u " +
                            "JOIN person p ON u.person_id = p.id " +
                            "WHERE p.id = :personId")
                    .parameter("personId", person.getId())
                    .getSingleResult();
            String email = user.getEmail();
            map.put(person, email);
        }

        return map;
    }
    private List<Person> getPersonWithParameters() {
        return dataManager.load(Person.class)
                .query("SELECT person.* FROM person " +
                        "INNER JOIN user ON person.id = user.person_id " +
                        "INNER JOIN post ON person.post_id = post.id "+
                        "WHERE person.first_name LIKE CONCAT('%', :firstName, '%') " +
                        "AND post.post_name LIKE CONCAT('%', :position, '%')" +
                        "AND person.last_name LIKE CONCAT('%', :lastName, '%') " +
                        "AND person.id_number LIKE CONCAT('%', :idNumber, '%') " +
                        "AND person.phone_number LIKE CONCAT('%', :phoneNumber, '%') " +
                        "AND user.email LIKE CONCAT('%', :email, '%')")
                .parameter("firstName", firstName)
                .parameter("lastName", lastName)
                .parameter("idNumber", idNumber)
                .parameter("position", position)
                .parameter("phoneNumber", phoneNumber)
                .parameter("email", email)
                .list();
    }
}