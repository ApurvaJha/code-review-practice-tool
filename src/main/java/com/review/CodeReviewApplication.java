package com.review;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class CodeReviewApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(CodeReviewApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            String url = "http://localhost:8080";
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                Runtime runtime = Runtime.getRuntime();
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("mac")) {
                    runtime.exec("open " + url);
                } else if (os.contains("nix") || os.contains("nux")) {
                    runtime.exec("xdg-open " + url);
                }
            }
            System.out.println(">>> UI successfully launched on localhost:8080");
        } catch (Exception e) {
            System.out.println(">>> Server running, but failed to auto-launch browser. Open http://localhost:8080 manually.");
        }
    }
}