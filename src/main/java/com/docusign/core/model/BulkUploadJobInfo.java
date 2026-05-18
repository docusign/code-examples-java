package com.docusign.core.model;

import java.util.Objects;
import java.util.List;

public final class BulkUploadJobInfo {
    private final String jobId;
    private final List<String> uploadUrls;

    public BulkUploadJobInfo(String jobId, List<String> uploadUrls) {
        this.jobId = Objects.requireNonNull(jobId, "jobId must not be null");
        this.uploadUrls = Objects.requireNonNull(uploadUrls, "uploadUrls must not be null");
    }

    public String getJobId() {
        return jobId;
    }

    public List<String> getUploadUrls() {
        return uploadUrls;
    }
}
