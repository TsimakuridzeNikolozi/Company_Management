package model;

import data.entity.Person;
import data.entity.Post;
import data.entity.TreeNode;
import data.entity.User;
import dto.request.EditUserRequest;
import dto.response.EditUserResponse;
import service.CycleChecker;
import service.DataManager;
import service.UserManagement;

import javax.ws.rs.sse.Sse;
import java.math.BigDecimal;
import java.util.UUID;

public class EditUserHandler {
    private final DataManager dataManager;

    public EditUserHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public EditUserResponse edit(EditUserRequest request){
        UUID personId = request.getPersonId();
        String salary= request.getSalary();
        String postName= request.getPostName();
        String newHeadUsername = request.getHeadUsername() == null ? "" : request.getHeadUsername();
        Person person = dataManager.load(Person.class).id(personId).getSingleResult();
        String oldHeadUsername = ReverseTreeNode.getPersonHeadUsername(person);
        oldHeadUsername = oldHeadUsername == null ? "" : oldHeadUsername;
        if (!request.getIsHr() && !newHeadUsername.equals(oldHeadUsername)) return EditUserResponse.builder()
                .success(false)
                .error("Only HR can edit head of person")
                .build();

        TreeNode personTreeNode = getPersonTreeNode(person);
        TreeNode headPersonTreeNode = getTreeNodeByUsername(newHeadUsername);
        if (headPersonTreeNode == null) return EditUserResponse.builder()
                .success(false)
                .error("Wrong username")
                .build();

        personTreeNode.setParentNode(headPersonTreeNode);
        CycleChecker cycleChecker = new CycleChecker();
        System.out.println("x");
        if (!cycleChecker.makesCycle(personTreeNode))
        dataManager.save(personTreeNode);
        else return EditUserResponse.builder()
                .success(false)
                .error("You cant set this user as head, because it makes cycle in tree")
                .build();
        System.out.println("x");
        if (postName != null) {
            Post post = dataManager.load(Post.class)
                    .query("SELECT * FROM post WHERE post_name = :postName")
                    .parameter("postName", postName)
                    .getSingleResult();
            person.setPost(post);
        }

        if (salary != null) {
            person.setSalary(BigDecimal.valueOf(Integer.parseInt(salary)));
        }
        dataManager.save(person);

        return EditUserResponse.builder()
                .success(true)
                .error("Saved")
                .build();
    }

    private TreeNode getPersonTreeNode(Person person) {
        TreeNode personTreeNode = dataManager.load(TreeNode.class)
                .query("SELECT * FROM tree_node" +
                        " WHERE person_id = :personId")
                .parameter("personId", person.getId())
                .getSingleResult();

        if (personTreeNode != null) return personTreeNode;
        personTreeNode = TreeNode.builder()
                .id(UUID.randomUUID())
                .person(person)
                .parentNode(null)
                .build();

        return personTreeNode;
    }

    private TreeNode getTreeNodeByUsername(String username) {
        User user = dataManager.load(User.class)
                .query("SELECT * FROM user" +
                        " WHERE username = :username")
                .parameter("username", username)
                .getSingleResult();

        if (user == null) return null;

        Person person = user.getPerson();

        return getPersonTreeNode(person);
    }
}