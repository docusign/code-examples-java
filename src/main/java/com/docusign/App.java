package com.docusign;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.util.Collections;

import java.awt.Desktop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude={JmxAutoConfiguration.class})
public class App {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class, args);
        openHomePage();
        initFileSystem();
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
