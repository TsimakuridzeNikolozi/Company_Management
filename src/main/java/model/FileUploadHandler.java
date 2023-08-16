package model;

import data.entity.Person;
import data.entity.PersonDocument;
import dto.request.SaveFileRequest;
import dto.response.SaveFileResponse;
import service.DataManager;

import java.util.UUID;

public class FileUploadHandler {

    private final DataManager dataManager;

    public FileUploadHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public SaveFileResponse saveFile(SaveFileRequest saveFileRequest) {
        try {
            PersonDocument personDocument = PersonDocument.builder()
                    .id(UUID.randomUUID())
                    .person(dataManager.load(Person.class).id(saveFileRequest.getPersonId()).getSingleResult())
                    .documentType(saveFileRequest.getDocumentType())
                    .documentNumber(saveFileRequest.getDocumentNumber())
                    .expirationDate(saveFileRequest.getExpirationDate())
                    .issueDate(saveFileRequest.getIssueDate())
                    .fileContent(saveFileRequest.getFileContent().readAllBytes())
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
