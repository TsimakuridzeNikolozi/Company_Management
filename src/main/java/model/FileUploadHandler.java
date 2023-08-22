package model;

import data.entity.Person;
import data.entity.PersonDocument;
import data.enums.DocumentType;
import dto.request.SaveFileRequest;
import dto.response.SaveFileResponse;
import service.DataManager;
import service.UserManagement;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUploadHandler {
    private static final String ILLEGAL_CONTENT_TYPE = "This content type is not supported";
    private static final String INCORRECT_DATES = "Issue and expiration dates are not correct";
    private static final String CONTRACT_ERROR = "Contract cannot be uploaded by the user";
    private final DataManager dataManager;
    public static Map<String, String> contentTypeToExtension;

    static {
        FileUploadHandler.contentTypeToExtension = new HashMap<>() {{
            put("image/png", ".png");
            put("image/jpeg", ".jpg");
            put("image/gif", ".gif");
            put("text/plain", ".txt");
            put("text/html", ".html");
            put("video/mp4", ".mp4");
            put("audio/mp3", ".mp3");
            put("application/pdf", ".pdf");
            put("application/msword", ".docx");
            put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
            put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
            put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
        }};
    }

    public FileUploadHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public SaveFileResponse saveFile(SaveFileRequest saveFileRequest) {
        try {
            if (!contentTypeToExtension.containsKey(saveFileRequest.getFileContentType()))
                return formResponse(false, ILLEGAL_CONTENT_TYPE);

            if (saveFileRequest.getIssueDate().after(saveFileRequest.getExpirationDate()))
                return formResponse(false, INCORRECT_DATES);

            if (saveFileRequest.getDocumentType().equals(DocumentType.CONTRACT)
                    && !UserManagement.isHr(dataManager.load(Person.class).id(saveFileRequest.getPersonId()).getSingleResult()))
                return formResponse(false, CONTRACT_ERROR);

            PersonDocument personDocument = PersonDocument.builder()
                    .id(UUID.randomUUID())
                    .person(dataManager.load(Person.class).id(saveFileRequest.getPersonId()).getSingleResult())
                    .documentType(saveFileRequest.getDocumentType())
                    .documentNumber(saveFileRequest.getDocumentNumber())
                    .expirationDate(saveFileRequest.getExpirationDate())
                    .issueDate(saveFileRequest.getIssueDate())
                    .fileContent(saveFileRequest.getFileContent().readAllBytes())
                    .fileContentType(saveFileRequest.getFileContentType())
                    .build();

            dataManager.save(personDocument);
            return formResponse(true, "Success");
        } catch (Exception e) {
            return formResponse(false, e.getLocalizedMessage());
        }

    }

    private SaveFileResponse formResponse(Boolean success, String error) {
        return SaveFileResponse.builder()
                .success(success)
                .error(error)
                .build();
    }
}
