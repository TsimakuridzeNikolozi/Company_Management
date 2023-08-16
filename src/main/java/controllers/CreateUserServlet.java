package controllers;

import dto.request.CreateUserRequest;
import dto.response.CreateUserResponse;
import dto.response.GetPostResponse;
import model.CreateUser;
import model.GetPost;
import service.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUserServlet extends HttpServlet {
    private final GetPost getPost;

    private final CreateUser createUser;

    public CreateUserServlet() {
        DataManager dataManager = new DataManager();
        this.createUser = new CreateUser(dataManager);
        this.getPost = new GetPost(dataManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> options = retrieveOptionsFromDatabase();
        request.setAttribute("options", options);
        request.getRequestDispatcher("/WEB-INF/createUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String postName = request.getParameter("post_name");
        UUID postId = getPostIdFromName(postName);

        CreateUserResponse createUserResponse = sendCreateUserRequest(username, password, email, firstName, lastName, postId);

        request.setAttribute("response", createUserResponse.getMessage());
        List<String> options = retrieveOptionsFromDatabase();
        request.setAttribute("options", options);

        if (createUserResponse.getFlag()) response.sendRedirect(request.getContextPath() + "/login");
        else request.getRequestDispatcher("/WEB-INF/createUser.jsp").forward(request, response);
    }

    private List<String> retrieveOptionsFromDatabase() {
        List<GetPostResponse> posts = getPost.getAllPosts();
        List<String> options = new ArrayList<>();
        for (GetPostResponse p : posts) {
            options.add(p.getPostName());
        }
        return options;
    }

    /**
     * Returns an ID of a post with the corresponding name
     * @param postName String, provided by the user
     * @return UUID
     */
    private UUID getPostIdFromName(String postName) {
        List<GetPostResponse> posts = getPost.getAllPosts();
        for (GetPostResponse p : posts) {
            if (p.getPostName().equals(postName)) return p.getPostId();
        }
        return null;
    }

    private CreateUserResponse sendCreateUserRequest(String username,
                                                        String password,
                                                        String email,
                                                        String firstName,
                                                        String lastName, UUID postId)
    {
        try {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .userName(username)
                    .password(password)
                    .email(email)
                    .postId(postId)
                    .build();

            return createUser.tryCreatingUser(createUserRequest);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


}
