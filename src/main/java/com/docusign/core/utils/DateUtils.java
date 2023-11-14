package com.docusign.core.utils;

import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final DateTimeFormatter DATE_WITH_SLASH = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static final DateTimeFormatter DATE_WITH_LINES = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
