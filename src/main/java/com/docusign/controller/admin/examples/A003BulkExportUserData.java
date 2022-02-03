package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.model.OrganizationExportResponse;
import com.docusign.admin.model.OrganizationExportRequest;
import com.docusign.admin.model.OrganizationExportsResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpHeaders;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

/**
 * How to bulk-export user data This example demonstrates how to bulk-export
 * user data from Admin API.
 */
@Controller
@RequestMapping("/a003")
public class A003BulkExportUserData extends AbstractAdminController {

    private static final int BUFFER_SIZE = 4096;
    private final Session session;
    private final User user;

    @Autowired
    public A003BulkExportUserData(DSConfiguration config, Session session, User user) {
        super(config, "a003", "Bulk export user data");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        // Step 3 start
        BulkExportsApi bulkExportsApi = createBulkExportsApi(this.user.getAccessToken(), this.session.getBasePath());
        OrganizationExportRequest request = new OrganizationExportRequest();
        request.setType("organization_memberships_export");
        OrganizationExportResponse bulkList = bulkExportsApi.createUserListExport(
                this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath()), request);
        // Step 3 end

        TimeUnit.SECONDS.sleep(30);

        // Step 4 start
        OrganizationExportResponse data = bulkExportsApi.getUserListExport(
                this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath()), bulkList.getId());
        // Step 4 end

        // Step 5 start
String csvUri = data.getResults().get(0).getUrl();
URL request_url = new URL(csvUri);

// Send Web request to download and save the exported CSV data
HttpsURLConnection.setFollowRedirects(true); // Defaults to true
HttpsURLConnection http_conn = (HttpsURLConnection) request_url.openConnection();
http_conn.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
http_conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, "mutlipart/form-data");
http_conn.setRequestProperty(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + user.getAccessToken());
http_conn.setInstanceFollowRedirects(true);

ClassLoader rawPath = Thread.currentThread().getContextClassLoader();
String rootDir = Paths.get(rawPath.getResource("").toURI()).toString();
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

int bytesRead = -1;
byte[] buffer = new byte[BUFFER_SIZE];
while ((bytesRead = inputStream.read(buffer)) != -1) {
    outputStream.write(buffer, 0, bytesRead);
}

outputStream.close();
inputStream.close();
http_conn.disconnect();
        // Step 5 end

        OrganizationExportsResponse results = bulkExportsApi
                .getUserListExports(this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath()));
        // Process results
        DoneExample.createDefault(title)
                .withMessage("User data exported to " + saveFilePath + "<br>from UserExport:getUserListExport method:")
                .withJsonObject(results).addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

}
