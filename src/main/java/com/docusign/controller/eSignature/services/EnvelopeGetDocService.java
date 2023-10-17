package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import org.apache.commons.io.FilenameUtils;

public final class EnvelopeGetDocService {

    public static byte[] envelopeGetDoc(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            String documentId
    ) throws ApiException {
        //ds-snippet-start:eSign7Step3
        return envelopesApi.getDocument(accountId, envelopeId, documentId);
        //ds-snippet-end:eSign7Step3
    }

    public static String addExtension(String fileName, String extension) {
        if (FilenameUtils.isExtension(fileName, extension)) {
            return fileName;
        }
        return String.join(".", fileName, extension);
    }
}
