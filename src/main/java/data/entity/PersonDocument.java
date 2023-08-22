package data.entity;

import data.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Lob;
import java.util.Date;

@SuperBuilder
@Getter
@Setter
public class PersonDocument extends BaseEntity {
    private Person person;
    private String documentNumber;
    private Date issueDate;
    private Date expirationDate;
    private DocumentType documentType;

    @Lob
    private byte[] fileContent;
    private String fileContentType;
}
