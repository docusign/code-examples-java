package com.docusign.services.eSignature.examples;

import com.docusign.controller.eSignature.examples.ExampleException;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

public final class EnvelopeGetDocService {

    public static byte[] envelopeGetDoc(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            String documentId
    ) throws ApiException {
        return envelopesApi.getDocument(accountId, envelopeId, documentId);
    }

    public static String addExtension(String fileName, String extension) {
        if (FilenameUtils.isExtension(fileName, extension)) {
            return fileName;
        }
        return String.join(".", fileName, extension);
    }
}
