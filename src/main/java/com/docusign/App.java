package com.docusign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;


@SpringBootApplication
@EnableAutoConfiguration(exclude={JmxAutoConfiguration.class})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
