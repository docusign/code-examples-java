package com.docusign.common;

import com.docusign.monitor.client.ApiException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SelectedApiTypes {
    @Value("${DS_API_NAME.ESIGNATURE}")
    private Boolean ESIGNATURE;
    @Value("${DS_API_NAME.ROOMS}")
    private Boolean ROOMS;
    @Value("${DS_API_NAME.CLICK}")
    private Boolean CLICK;
    @Value("${DS_API_NAME.MONITOR}")
    private Boolean MONITOR;
    @Value("${DS_API_NAME.ADMIN}")
    private Boolean ADMIN;
    private final String SELECTED_API_NOT_SUPPORTED = "Currently selected api is not supported by launcher. Please, check appsettings.json file";

    public String getFirstSelectedApi() {
        return (ESIGNATURE)? ApiIndex.ESIGNATURE.name()
                : (ROOMS)? ApiIndex.ROOMS.name()
                : (CLICK)? ApiIndex.CLICK.name()
                : (MONITOR)? ApiIndex.MONITOR.name()
                : (ADMIN)? ApiIndex.ADMIN.name()
                : SELECTED_API_NOT_SUPPORTED;
    }
}
