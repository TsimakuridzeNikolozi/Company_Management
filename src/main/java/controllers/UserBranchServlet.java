package controllers;

import data.entity.Person;
import data.entity.ReverseTreeNode;
import data.entity.User;
import dto.request.EditUserRequest;
import dto.response.EditUserResponse;
import dto.response.GetPostResponse;
import model.EditUserHandler;
import model.GetPost;
import service.DataManager;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println(activeUser.getUserName());
        System.out.println(activeUser.getPerson().getFirstName());
        List<Person> userBranch = ReverseTreeNode.getChildren(activeUser.getPerson());
        request.setAttribute("userBranch", userBranch);

        List<String> options = retrieveOptionsFromDatabase();
        request.setAttribute("options", options);

        request.getRequestDispatcher("/WEB-INF/userBranch.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String salary = request.getParameter("salary");
        String position = request.getParameter("position");
        String id = request.getParameter("id");

        EditUserRequest editUserRequest = EditUserRequest.builder()
                .personId(id)
                .salary(salary)
                .postName(position)
                .build();
        EditUserHandler editUserHandler = new EditUserHandler(dataManager);
        EditUserResponse editUserResponse = editUserHandler.edit(editUserRequest);
        System.out.println(editUserResponse.getError());
        request.setAttribute("message", (String)editUserResponse.getError());

        request.getRequestDispatcher("/WEB-INF/userBranch.jsp").forward(request, response);

    }

    private List<String> retrieveOptionsFromDatabase() {
        List<GetPostResponse> posts = getPost.getAllPosts();
        List<String> options = new ArrayList<>();
        for (GetPostResponse p : posts) {
            options.add(p.getPostName());
        }
        return options;
    }
}
