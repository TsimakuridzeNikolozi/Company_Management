package controllers;

import data.entity.PersonDocument;
import data.entity.User;
import model.FileUploadHandler;
import service.DataManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;


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

        request.getRequestDispatcher("WEB-INF/userDocuments.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UUID documentId = UUID.fromString(request.getParameter("documentId"));
        String result = createUserDocumentFile(getDocumentWithId(documentId));

        boolean flag = result.startsWith("Success");
        String error = flag ? result.replaceFirst("Success", "Successfully saved file at: ") : result.replaceFirst("Fail", "");
        request.setAttribute("flag", flag);
        request.setAttribute("error", error);

        User activeUser = (User) request.getSession().getAttribute("user");
        List<PersonDocument> userDocuments = getUserDocuments(activeUser);
        request.setAttribute("userDocuments", userDocuments);
        request.getRequestDispatcher("WEB-INF/userDocuments.jsp").forward(request, response);
    }

    /**
     * Gets all the documents for the current user
     * @param user Current user
     * @return List of documents
     */
    private List<PersonDocument> getUserDocuments(User user) {
        return dataManager.load(PersonDocument.class)
                .query("SELECT * FROM person_document WHERE person_id = :userPersonId")
                .parameter("userPersonId", user.getPerson().getId())
                .list();
    }

    /**
     * Gets the document with the given document id
     * @param id ID of the document we want
     * @return Document
     */
    private PersonDocument getDocumentWithId(UUID id) {
        return dataManager.load(PersonDocument.class).id(id).getSingleResult();
    }

    /**
     * Creates a file in the Downloads directory based on the given PersonDocument entity
     * @param document PersonDocument entity for which the file should be created
     * @return String result with success or failure message in the beginning and the filepath or error message after
     */
    private String createUserDocumentFile(PersonDocument document) {
        String filePath = System.getProperty("user.home") + File.separator + "Downloads";
        String fileName = document.getDocumentNumber() + FileUploadHandler.contentTypeToExtension.get(document.getFileContentType());
        File file = new File(filePath, fileName);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(document.getFileContent());
            fileOutputStream.close();
        } catch (IOException e) {
            return "Fail" + e.getMessage();
        }

        return "Success" + filePath + "\\" + fileName;
    }
}
