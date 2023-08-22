package controllers;

import data.entity.Message;
import data.entity.Person;
import data.entity.PersonDocument;
import data.entity.User;
import data.enums.DocumentType;
import dto.request.SaveFileRequest;
import dto.response.SaveFileResponse;
import model.FileUploadHandler;
import service.DataManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

@WebServlet("/fileUploadServlet")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User activeUser = (User) request.getSession().getAttribute("user");
        request.getSession().setAttribute("user", activeUser);
        if (request.getParameter("personIdForDocument") != null) {
            UUID personId = UUID.fromString(request.getParameter("personIdForDocument"));
            request.setAttribute("personIdForDocument", personId);
        }
        request.getRequestDispatcher("/WEB-INF/fileUpload.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User)request.getSession().getAttribute("user");
        UUID personId = user.getPerson().getId();
        if (request.getParameter("personIdForDocument") != null) {
            personId = UUID.fromString(request.getParameter("personIdForDocument"));
        }

        String documentNumber = request.getParameter("documentNumber");
        DocumentType documentType = DocumentType.parseString(request.getParameter("documentType"));

        Date issueDate = Date.valueOf(request.getParameter("issueDate"));
        Date expirationDate = Date.valueOf(request.getParameter("expirationDate"));

        Part filePart = request.getPart("file");
        String fileContentType = filePart.getContentType();
        InputStream fileContent = filePart.getInputStream();

        FileUploadHandler fileUploadServlet = new FileUploadHandler(new DataManager());
        SaveFileResponse saveFileResponse = fileUploadServlet.saveFile(
                SaveFileRequest.builder()
                        .personId(personId)
                        .documentNumber(documentNumber)
                        .documentType(documentType)
                        .issueDate(issueDate)
                        .expirationDate(expirationDate)
                        .fileContent(fileContent)
                        .fileContentType(fileContentType)
                        .build()
        );

        request.setAttribute("flag", saveFileResponse.getSuccess());
        request.setAttribute("error", saveFileResponse.getError());

        request.getRequestDispatcher("/WEB-INF/fileUpload.jsp").forward(request, response);
    }
}

