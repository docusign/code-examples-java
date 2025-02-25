package com.docusign.core.utils;

import org.springframework.util.StreamUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    /**
     * Loads a file content and copy it into a byte array.
     *
     * @param path the absolute path within the class path
     * @return the new byte array that has been loaded from the file
     * @throws IOException in case of I/O errors
     */
    public byte[] readFile(String path) throws IOException {
       InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + path);
        }

        return StreamUtils.copyToByteArray(inputStream);
    }
}
