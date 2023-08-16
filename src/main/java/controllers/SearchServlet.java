package controllers;

import data.entity.Person;
import data.entity.User;
import dto.response.GetPostResponse;
import model.GetPost;
import model.SearchHandler;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchServlet extends HttpServlet {
    private final DataManager dataManager;
    private final GetPost getPost;
    public SearchServlet() {
        this.dataManager = new DataManager();
        this.getPost = new GetPost(dataManager);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> options = retrieveOptionsFromDatabase();
        request.setAttribute("options", options);
        request.getRequestDispatcher("/WEB-INF/search.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> options = retrieveOptionsFromDatabase();
        request.setAttribute("options", options);

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String idNumber = request.getParameter("idNumber");
        String position = request.getParameter("post_name");
        String phoneNumber = request.getParameter("phoneNumber");

        if (position==null || position.equals("No Position")) position = "";
        if (firstName==null) firstName = "";
        if (lastName==null) lastName = "";
        if (email==null) email = "";
        if (idNumber==null) idNumber = "";
        if (phoneNumber==null) phoneNumber = "";

        System.out.println("Searching for firstname: "+firstName+",lastname: "+lastName+",email: "+email+",idNumber: "+idNumber+",phoneNumber: "+phoneNumber+",position: "+position);

        SearchHandler searchHandler = SearchHandler.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .idNumber(idNumber)
                .position(position)
                .dataManager(dataManager)
                .build();

        request.setAttribute("searchResult",searchHandler.getResultMap());

        request.getRequestDispatcher("/WEB-INF/search.jsp").forward(request, response);
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
