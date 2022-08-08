package com.docusign;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import java.awt.Desktop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude={JmxAutoConfiguration.class})
public class App {

    public static void main(String[] args) throws IOException, URISyntaxException {
        initFileSystem();

        ClassLoader classLoader = App.class.getClassLoader();
        URL applicationJsonURL = classLoader.getResource("application.json");

        if (applicationJsonURL != null){
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
                } catch (Exception e) {
                    System.out.println("File applicationJsonMissingError.html could not be accessed.");
                }

                Desktop.getDesktop().browse(path.toUri());
            }

            System.out.println("");
            System.out.println("Please, add the application.json file to the resources folder.");
            System.out.println("");
        }
    }

    private static void openHomePage() throws IOException {
        System.setProperty("java.awt.headless", "false");
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } catch (URISyntaxException use) {
            System.err.println("Invalid URI in App.openHomePage()");
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
