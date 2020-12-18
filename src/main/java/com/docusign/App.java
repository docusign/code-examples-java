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
        // Each operating system opens web browsers differently so we need to determine the user's
        // OS and open a browser to the example homepage accordingly.
        Runtime rt = Runtime.getRuntime();
        if (OSDetector.isMac()){
            String[] arguments = { "osascript", "-e", "open location \"" + "http://localhost:8080" + "\"" };
            rt.exec(arguments);
        }
        else if (OSDetector.isWindows()){
           rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080");
        }
        else if (OSDetector.isLinux()){
            String [] browsers = {"firefox", "safari", "chrome", "mozilla"};
            StringBuffer cmd = new StringBuffer();
            for (int i=0; i<browsers.length; i++)
                cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + "http://localhost:8080" + "\" ");
            rt.exec(new String[] { "sh", "-c", cmd.toString() });
        }

    }
}
