package com.example.springbootmusictgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBootMusicTgBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMusicTgBotApplication.class, args);
    }

}
