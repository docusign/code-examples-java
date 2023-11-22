package com.docusign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@Slf4j
@SpringBootApplication(exclude = {JmxAutoConfiguration.class})
@ComponentScan(basePackages = "com.docusign")
@Import(WebSecurityConfig.class)

public class App {

    public static void main(String[] args) throws IOException, URISyntaxException {
        initFileSystem();

        ClassLoader classLoader = App.class.getClassLoader();
        URL applicationJsonURL = classLoader.getResource("application.json");

        if (applicationJsonURL != null) {
            SpringApplication.run(App.class, args);
            openHomePage();
        } else {
            InputStream inputStream = classLoader.getResourceAsStream("applicationJsonMissingError.html");

            if (inputStream.available() > 0) {
                Path path = Files.createTempFile("applicationJsonMissingError", ".html");
                try (FileOutputStream out = new FileOutputStream(path.toFile())) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    log.error("File applicationJsonMissingError.html could not be accessed.");
                }

                Desktop.getDesktop().browse(path.toUri());
            }

            log.info("");
            log.info("Please, add the application.json file to the resources folder.");
            log.info("");
        }
    }

    private static void openHomePage() throws IOException {
        System.setProperty("java.awt.headless", "false");
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } catch (URISyntaxException use) {
            log.error("Invalid URI in App.openHomePage()");
        }
    }

    private static void initFileSystem() {
        try {
            FileSystems.newFileSystem(new URI(""), Collections.emptyMap());
            log.info("FileSystem initialized successfully");
        } catch (Exception e) {
            if (e instanceof FileSystemAlreadyExistsException) {
                log.info("FileSystem is already initialized");
            } else {
                log.info("Retrieving the default initialize context");
                FileSystems.getDefault();
            }
        }
    }
}
