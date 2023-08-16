package dto.request;

import data.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class SaveFileRequest {
    private UUID personId;
    private String documentNumber;
    private Date issueDate;
    private Date expirationDate;
    private DocumentType documentType;
    private InputStream fileContent;
}
