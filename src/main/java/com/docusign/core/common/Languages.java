package com.docusign.core.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Value;


/**
 * Supported languages.
 */
public final class Languages {

    private static final List<Lang> SUPPORTED_LANGUAGES = createList();

    private Languages() {}


    public static List<Lang> getSupportedLanguages() {
        return Collections.unmodifiableList(SUPPORTED_LANGUAGES);
    }

    private static List<Lang> createList() {
        List<Lang> languages = new ArrayList<>();
        languages.add(new Lang("ar", "Arabic"));
        languages.add(new Lang("hy", "Armenian"));
        languages.add(new Lang("id", "Bahasa Indonesia"));
        languages.add(new Lang("ms", "Bahasa Malay"));
        languages.add(new Lang("bg", "Bulgarian"));
        languages.add(new Lang("zh_CN", "Chinese Simplified"));
        languages.add(new Lang("zh_TW", "Chinese Traditional"));
        languages.add(new Lang("hr", "Croatian"));
        languages.add(new Lang("cs", "Czech"));
        languages.add(new Lang("da", "Danish"));
        languages.add(new Lang("nl", "Dutch"));
        languages.add(new Lang("en_GB", "English UK"));
        languages.add(new Lang("en", "English US"));
        languages.add(new Lang("et", "Estonian"));
        languages.add(new Lang("fa", "Farsi"));
        languages.add(new Lang("fi", "Finnish"));
        languages.add(new Lang("fr", "French"));
        languages.add(new Lang("fr_CA", "French Canada"));
        languages.add(new Lang("de", "German"));
        languages.add(new Lang("el", "Greek"));
        languages.add(new Lang("he", "Hebrew"));
        languages.add(new Lang("hi", "Hindi"));
        languages.add(new Lang("hu", "Hungarian"));
        languages.add(new Lang("it", "Italian"));
        languages.add(new Lang("ja", "Japanese"));
        languages.add(new Lang("ko", "Korean"));
        languages.add(new Lang("lv", "Latvian"));
        languages.add(new Lang("lt", "Lithuanian"));
        languages.add(new Lang("no", "Norwegian"));
        languages.add(new Lang("pl", "Polish"));
        languages.add(new Lang("pt", "Portuguese"));
        languages.add(new Lang("pt_BR", "Portuguese Brasil"));
        languages.add(new Lang("ro", "Romanian"));
        languages.add(new Lang("ru", "Russian"));
        languages.add(new Lang("sr", "Serbian"));
        languages.add(new Lang("sk", "Slovak"));
        languages.add(new Lang("sl", "Slovenian"));
        languages.add(new Lang("es", "Spanish"));
        languages.add(new Lang("es_MX", "Spanish Latin America"));
        languages.add(new Lang("sv", "Swedish"));
        languages.add(new Lang("th", "Thai"));
        languages.add(new Lang("tr", "Turkish"));
        languages.add(new Lang("uk", "Ukranian"));
        languages.add(new Lang("vi", "Vietnamese"));
        return languages;
    }

    @Value
    public static class Lang {
		private String code;
        private String name;

    }
}
