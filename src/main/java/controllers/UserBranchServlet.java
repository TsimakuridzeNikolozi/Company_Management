package controllers;

import data.entity.Person;
import data.entity.TreeNode;
import model.ReverseTreeNode;
import data.entity.User;
import dto.request.EditUserRequest;
import dto.response.EditUserResponse;
import dto.response.GetPostResponse;
import model.EditUserHandler;
import model.GetPost;
import service.DataManager;
import service.UserManagement;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.util.*;

@WebServlet("/userBranchServlet")
public class UserBranchServlet extends HttpServlet {
    private final GetPost getPost;
    private final DataManager dataManager = new DataManager();
    public UserBranchServlet() {

        this.getPost = new GetPost(dataManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User activeUser = (User) request.getSession().getAttribute("user");
        Map<Person, String> userBranch = new HashMap<>();
        Person person = activeUser.getPerson();
        if (UserManagement.isHr(person)) {
            userBranch = getAllPeople(person);
        } else {
            List<Person> personList = ReverseTreeNode.getChildren(person);
            for (Person p : personList) {
                userBranch.put(p, ReverseTreeNode.getPersonHeadUsername(p));
            }
        }

        request.setAttribute("userBranch", userBranch);

        List<String> options = retrieveOptionsFromDatabase();
        request.setAttribute("options", options);
        String personHeadUsername = ReverseTreeNode.getPersonHeadUsername(person);
        request.setAttribute("head", personHeadUsername);

        request.getRequestDispatcher("/WEB-INF/userBranch.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User activeUser = (User) request.getSession().getAttribute("user");
        String salary = request.getParameter("salary");
        String position = request.getParameter("position");
        UUID id = UUID.fromString(request.getParameter("id"));
        System.out.println(id);
        String headUsername = request.getParameter("personHead");
        System.out.println(headUsername);

        EditUserRequest editUserRequest = EditUserRequest.builder()
                .personId(id)
                .salary(salary)
                .postName(position)
                .headUsername(headUsername)
                .isHr(UserManagement.isHr(activeUser.getPerson()))
                .build();
        EditUserHandler editUserHandler = new EditUserHandler(dataManager);
        EditUserResponse editUserResponse = editUserHandler.edit(editUserRequest);
        request.setAttribute("message", editUserResponse.getError());
        request.setAttribute("flag", editUserResponse.isSuccess());
        System.out.println(editUserResponse.getError());
        this.doGet(request, response);
    }

    private List<String> retrieveOptionsFromDatabase() {
        List<GetPostResponse> posts = getPost.getAllPosts();
        List<String> options = new ArrayList<>();
        for (GetPostResponse p : posts) {
            options.add(p.getPostName());
        }
        return options;
    }

    private Map<Person, String> getAllPeople(Person person) {
        List<Person> persons = dataManager.load(Person.class)
                .query("SELECT * FROM person " +
                        "WHERE id <> :personId")
                .parameter("personId", person.getId())
                .list();
        Map<Person, String> personsAndTheirHeads = new HashMap<>();
        for (Person p : persons) {
            personsAndTheirHeads.put(p, ReverseTreeNode.getPersonHeadUsername(p));
        }
        return personsAndTheirHeads;
    }
}
