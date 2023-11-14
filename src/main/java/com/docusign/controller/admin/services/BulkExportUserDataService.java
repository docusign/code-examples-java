package com.docusign.controller.admin.services;

import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.OrganizationExportRequest;
import com.docusign.admin.model.OrganizationExportResponse;
import com.docusign.admin.model.OrganizationExportsResponse;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class BulkExportUserDataService {
    public static OrganizationExportsResponse bulkExportsUserData(
            BulkExportsApi bulkExportsApi,
            UUID organizationId) throws ApiException {
        return bulkExportsApi.getUserListExports(organizationId);
    }

    public static OrganizationExportResponse bulkExportUserData(
            BulkExportsApi bulkExportsApi,
            UUID organizationId,
            UUID exportId) throws ApiException {
        return bulkExportsApi.getUserListExport(organizationId, exportId);
    }

    public static OrganizationExportResponse createUserListExport(
            BulkExportsApi bulkExportsApi,
            UUID organizationId) throws ApiException {
        OrganizationExportRequest request = new OrganizationExportRequest();
        request.setType("organization_memberships_export");
        return bulkExportsApi.createUserListExport(organizationId, request);
    }

    public static String moveUserListExportToFile(
            String csvUri,
            String bearerAuthentication,
            String accessToken,
            Integer bufferSize) throws IOException, URISyntaxException {
        //ds-snippet-start:Admin3Step5
        URL request_url = new URL(csvUri);

        // Send Web request to download and save the exported CSV data
        HttpsURLConnection.setFollowRedirects(true); // Defaults to true
        HttpsURLConnection http_conn = (HttpsURLConnection) request_url.openConnection();
        http_conn.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        http_conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, "mutlipart/form-data");
        http_conn.setRequestProperty(HttpHeaders.AUTHORIZATION, bearerAuthentication + accessToken);
        http_conn.setInstanceFollowRedirects(true);

        ClassLoader rawPath = Thread.currentThread().getContextClassLoader();
        String rootDir = Paths.get(Objects.requireNonNull(rawPath.getResource("")).toURI()).toString();
        // Set the save file path in the project root instead of the compiled folder and
        // force forward slash scheme
        rootDir = StringUtils.remove(rootDir, "target\\classes");
        rootDir = StringUtils.remove(rootDir, "file:/");
        rootDir = StringUtils.replace(rootDir, "\\", "/");

        // opens input stream from the HTTP connection
        InputStream inputStream = http_conn.getInputStream();
        String saveFilePath = rootDir + "src/main/resources/ExportedUserData.csv";

        // opens an output stream to save into file
        FileOutputStream outputStream = FileUtils.openOutputStream(new File(saveFilePath));

        int bytesRead;
        byte[] buffer = new byte[bufferSize];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();
        http_conn.disconnect();
        //ds-snippet-end:Admin3Step5

        return saveFilePath;
    }
}
