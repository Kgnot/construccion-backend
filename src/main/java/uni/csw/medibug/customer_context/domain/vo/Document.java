package uni.csw.medibug.customer_context.domain.vo;

import lombok.Getter;
import uni.csw.medibug.customer_context.domain.error.DocumentValidateError;

@Getter
public class Document {

    private final DocumentType documentType;
    private final String value;

    private Document(DocumentType documentType, String value) {
        this.documentType = documentType;
        this.value = value;
    }

    public static Document create(DocumentType documentType, String value) {
        if (value == null || value.isEmpty()) {
            throw new DocumentValidateError("Document value cannot be null or empty");
        }
        if (value.length() > 12) {
            throw new DocumentValidateError("Document value cannot exceed 20 characters");
        }
        return new Document(documentType, value);
    }


    public enum DocumentType {
        CC, TI
    }
}
