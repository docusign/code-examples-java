package com.docusign;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;


@SpringBootApplication(exclude={JmxAutoConfiguration.class})
public class App {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class, args);
        openHomePage();
    }

    private static void openHomePage() throws IOException {
        if (OSDetector.isMac()){
            Runtime rt = Runtime.getRuntime();
            String[] arguments = { "osascript", "-e", "open location \"" + "http://localhost:8080" + "\"" };
            rt.exec(arguments);
        }
        else {
           Runtime rt = Runtime.getRuntime();
           rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080");
        }

    }

}