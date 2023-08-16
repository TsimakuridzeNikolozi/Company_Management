package model;

import data.entity.Person;
import data.entity.Post;
import dto.request.EditUserRequest;
import dto.response.EditUserResponse;
import service.DataManager;

import java.math.BigDecimal;

public class EditUserHandler {
    private final DataManager dataManager;

    public EditUserHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public EditUserResponse edit(EditUserRequest request){
        String personId= request.getPersonId();
        String salary= request.getSalary();
        String postName= request.getPostName();

        Person person = dataManager.load(Person.class)
                .query("SELECT * FROM person WHERE id = :personId")
                .parameter("personId", personId)
                .getSingleResult();


        if(postName!=null) {
            Post post = dataManager.load(Post.class)
                    .query("SELECT * FROM post WHERE post_name = :postName")
                    .parameter("postName", postName)
                    .getSingleResult();
            person.setPost(post);
        }

        if(salary!=null) {
            person.setSalary(BigDecimal.valueOf(Integer.parseInt(salary)));
        }
        dataManager.save(person);

        EditUserResponse response = EditUserResponse.builder()
                .success(true)
                .error("Saved")
                .build();
        return response;
    }
}