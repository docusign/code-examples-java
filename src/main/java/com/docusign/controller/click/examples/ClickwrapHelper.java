package com.docusign.controller.click.examples;

import com.docusign.click.model.Document;
import com.docusign.core.utils.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.Base64;

public class ClickwrapHelper {

    public static final String STATUS_ACTIVE = "active";

    /**
     * Loads document from a file and creates a document object that represents
     * loaded document.
     * @param fileName name of the file to load document; the extension of the
     * loading file determines an extension of the created document
     * @param docName the name of the document; it may be differ from the file
     * @param order order of the created document
     * @return the {@link Document} object
     * @throws IOException if document cannot be loaded due to some reason
     */
    public static Document createDocumentFromFile(String fileName, String docName, Integer order) throws IOException {
        byte[] buffer = FileUtils.readFile(fileName);
        String extension = FilenameUtils.getExtension(fileName);
        return createDocument(buffer, docName, extension, order);
    }

    /**
     * Creates a document object from the raw data.
     * @param data the raw data
     * @param documentName the name of the document; it may be differ from the file
     * @param fileExtension the extension of the creating file
     * @param order order of the created document
     * @return the {@link Document} object
     */
    static Document createDocument(byte[] data, String documentName, String fileExtension, Integer order) {
        Document document = new Document();
        document.setDocumentBase64(Base64.getEncoder().encodeToString(data));
        document.setDocumentName(documentName);
        document.setFileExtension(fileExtension);
        document.setOrder(order);
        return document;
    }
}
