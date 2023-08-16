package data.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DocumentType {
    ID,
    PASSPORT,
    LICENSE,
    CONTRACT;



    /**
     * Parses a string and returns a corresponding Document type, or throws an exception if the
     * provided string is not a valid Document type
     * @param name String
     * @return DocumentType enum
     */
    public static DocumentType parseString(String name) {
        if (name == null) return null;
        if (name.equals("ID") || name.equals("PASSPORT") || name.equals("LICENSE") || name.equals("CONTRACT"))
            return DocumentType.valueOf(name);
        throw new IllegalArgumentException("Invalid Document Type, it can only be ID, Passport, License, or Contract");
    }

    /**
     * @return All the document types
     */
    public static List<DocumentType> getAllDocumentTypes() {
        return new ArrayList<>(Arrays.asList(DocumentType.ID, DocumentType.PASSPORT, DocumentType.LICENSE, DocumentType.CONTRACT));
    }
}
