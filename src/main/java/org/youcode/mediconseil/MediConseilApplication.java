package org.youcode.mediconseil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediConseilApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediConseilApplication.class, args);
    }

}
