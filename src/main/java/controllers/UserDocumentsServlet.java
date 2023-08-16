package controllers;

import data.entity.Message;
import data.entity.PersonDocument;
import data.entity.User;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/userDocumentsServlet")
public class UserDocumentsServlet extends HttpServlet {
    private final DataManager dataManager;

    public UserDocumentsServlet() {
        this.dataManager = new DataManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User activeUser = (User) request.getSession().getAttribute("user");
        List<PersonDocument> userDocuments = getUserDocuments(activeUser);
        request.setAttribute("userDocuments", userDocuments);
        request.getRequestDispatcher("/WEB-INF/userDocuments.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/userDocuments.jsp").forward(request, response);
    }

    private List<PersonDocument> getUserDocuments(User user) {
        return dataManager.load(PersonDocument.class)
                .query("SELECT * FROM person_document WHERE person_id = :userPersonId")
                .parameter("userPersonId", user.getPerson().getId())
                .list();
    }

    private List<String> createUserDocumentFiles(List<PersonDocument> userDocuments) {
        for(PersonDocument document: userDocuments) {
            String filePath = "./WEB-INF/resources/" + document.getDocumentNumber();
        }

        return null;
    }
}
