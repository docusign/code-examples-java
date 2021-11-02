package com.docusign.common;

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

    public String getFirstSelectedApi() {
        return (ESIGNATURE)? ApiIndex.ESIGNATURE.name()
                : (ROOMS)? ApiIndex.ROOMS.name()
                : (CLICK)? ApiIndex.CLICK.name()
                : (MONITOR)? ApiIndex.MONITOR.name()
                : (ADMIN)? ApiIndex.ADMIN.name()
                : null;
    }
}
